package com.civicpulse.grievance.service;
import com.civicpulse.grievance.client.CitizenClient;
import com.civicpulse.grievance.dto.*;
import com.civicpulse.grievance.entity.*;
import com.civicpulse.grievance.exception.*;
import com.civicpulse.grievance.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;
@Service
public class GrievanceService {
 private static final List<GrievanceStatus> OPEN=List.of(GrievanceStatus.REGISTERED,GrievanceStatus.ASSIGNED,GrievanceStatus.IN_PROGRESS);
 private static final List<GrievanceStatus> DONE=List.of(GrievanceStatus.RESOLVED,GrievanceStatus.CLOSED);
 private final GrievanceRepository repo; private final GrievanceHistoryRepository historyRepo; private final CitizenClient citizenClient;
 private final int extensionHours,maxLevel;
 public GrievanceService(GrievanceRepository r,GrievanceHistoryRepository h,CitizenClient c,
  @Value("${civic.escalation.extension-hours:24}") int extensionHours,@Value("${civic.escalation.max-level:3}") int maxLevel){
  repo=r;historyRepo=h;citizenClient=c;this.extensionHours=extensionHours;this.maxLevel=maxLevel;
 }
 @Transactional public GrievanceResponse register(GrievanceRequest req){
  CitizenInfo citizen=citizenClient.getCitizen(req.citizenId());
  Grievance g=new Grievance();g.setCitizenId(citizen.id());g.setCitizenCode(citizen.citizenCode());g.setCitizenName(citizen.fullName());
  g.setWardNumber(citizen.wardNumber());g.setCategory(req.category());g.setSeverity(req.severity());
  g.setTitle(req.title().trim());g.setDescription(req.description().trim());g.setLocation(req.location());
  g.setDepartment(req.category().getDepartment());g.setStatus(GrievanceStatus.REGISTERED);
  int hours=SlaPolicy.slaHours(req.category(),req.severity());g.setSlaHours(hours);g.setSlaDueAt(SlaPolicy.dueAt(LocalDateTime.now(),hours));
  g.setGrievanceNumber("TMP-"+UUID.randomUUID());g=repo.save(g);
  g.setGrievanceNumber(String.format("GRV-%d-%06d",Year.now().getValue(),g.getId()));
  addHistory(g,"REGISTERED",null,GrievanceStatus.REGISTERED,"Auto-routed to "+g.getDepartment().getDisplayName()+", SLA "+hours+"h","citizen:"+citizen.citizenCode());
  return GrievanceResponse.from(g);
 }
 @Transactional(readOnly=true) public GrievanceResponse get(Long id){return GrievanceResponse.from(find(id));}
 @Transactional(readOnly=true) public GrievanceResponse track(String number){return repo.findByGrievanceNumber(number).map(GrievanceResponse::from).orElseThrow(()->new ResourceNotFoundException("Grievance not found: "+number));}
 @Transactional(readOnly=true) public List<GrievanceResponse> list(GrievanceStatus status,Department dept,Integer ward,Long citizenId){
  return repo.findAll(Sort.by(Sort.Direction.DESC,"createdAt")).stream()
   .filter(g->status==null||g.getStatus()==status).filter(g->dept==null||g.getDepartment()==dept)
   .filter(g->ward==null||ward.equals(g.getWardNumber())).filter(g->citizenId==null||citizenId.equals(g.getCitizenId()))
   .map(GrievanceResponse::from).toList();
 }
 @Transactional(readOnly=true) public List<HistoryResponse> history(Long id){find(id);return historyRepo.findByGrievanceIdOrderByCreatedAtAsc(id).stream().map(HistoryResponse::from).toList();}
 @Transactional public GrievanceResponse assign(Long id,AssignRequest req){
  Grievance g=find(id);
  if(g.getStatus()!=GrievanceStatus.REGISTERED&&g.getStatus()!=GrievanceStatus.ASSIGNED)throw new BusinessRuleException("Only REGISTERED or ASSIGNED grievances can be (re)assigned. Current: "+g.getStatus());
  GrievanceStatus from=g.getStatus();if(req.department()!=null)g.setDepartment(req.department());
  g.setAssignedOfficer(req.officerName().trim());g.setStatus(GrievanceStatus.ASSIGNED);
  addHistory(g,"ASSIGNED",from,GrievanceStatus.ASSIGNED,"Assigned to "+g.getAssignedOfficer()+" ("+g.getDepartment().getDisplayName()+")"+(req.remarks()==null?"":" - "+req.remarks()),"admin");
  return GrievanceResponse.from(g);
 }
 @Transactional public GrievanceResponse updateStatus(Long id,StatusUpdateRequest req){
  Grievance g=find(id);GrievanceStatus from=g.getStatus(),to=req.status();
  if(!from.canMoveTo(to))throw new BusinessRuleException("Invalid transition "+from+" -> "+to+". Allowed: "+from.allowedNext());
  if(to==GrievanceStatus.RESOLVED){
   if(req.resolutionNotes()==null||req.resolutionNotes().trim().length()<10)throw new BusinessRuleException("Resolution notes (min 10 characters) are required to resolve");
   g.setResolutionNotes(req.resolutionNotes().trim());g.setResolvedAt(LocalDateTime.now());
   if(LocalDateTime.now().isAfter(g.getSlaDueAt()))g.setSlaBreached(true);
  }
  if(to==GrievanceStatus.REJECTED&&(req.remarks()==null||req.remarks().isBlank()))throw new BusinessRuleException("Remarks (reason) are required to reject a grievance");
  if(to==GrievanceStatus.CLOSED)g.setClosedAt(LocalDateTime.now());
  if(from==GrievanceStatus.RESOLVED&&to==GrievanceStatus.IN_PROGRESS)g.setResolvedAt(null);
  g.setStatus(to);addHistory(g,"STATUS_CHANGED",from,to,req.remarks(),req.performedBy()==null?"officer":req.performedBy());
  return GrievanceResponse.from(g);
 }
 @Transactional public GrievanceResponse escalate(Long id,String reason){Grievance g=find(id);doEscalate(g,reason==null||reason.isBlank()?"Manual escalation":reason,"admin");return GrievanceResponse.from(g);}
 @Transactional public int escalateOverdue(){
  List<Grievance> overdue=repo.findByStatusInAndSlaDueAtBefore(OPEN,LocalDateTime.now());int count=0;
  for(Grievance g:overdue){if(g.getEscalationLevel()<maxLevel){doEscalate(g,"SLA breached - auto escalation","system:sla-monitor");count++;}else g.setSlaBreached(true);}
  return count;
 }
 private void doEscalate(Grievance g,String reason,String by){
  if(!g.getStatus().isOpen())throw new BusinessRuleException("Only open grievances can be escalated");
  if(g.getEscalationLevel()>=maxLevel)throw new BusinessRuleException("Already at highest escalation level ("+maxLevel+")");
  int old=g.getEscalationLevel();g.setEscalationLevel(old+1);g.setSlaBreached(true);g.setSlaDueAt(LocalDateTime.now().plusHours(extensionHours));
  addHistory(g,"ESCALATED",g.getStatus(),g.getStatus(),reason+": "+SlaPolicy.levelName(old)+" -> "+SlaPolicy.levelName(old+1),by);
 }
 @Transactional public GrievanceResponse simulateBreach(Long id){Grievance g=find(id);if(!g.getStatus().isOpen())throw new BusinessRuleException("Only open grievances can be simulated");g.setSlaDueAt(LocalDateTime.now().minusMinutes(1));return GrievanceResponse.from(g);}
 @Transactional(readOnly=true) public DashboardSummary dashboard(){
  long total=repo.count(),rejected=repo.countByStatus(GrievanceStatus.REJECTED),resolved=repo.countByStatusIn(DONE),open=repo.countByStatusIn(OPEN);
  long breached=repo.findAll().stream().filter(g->g.getStatus().isOpen()).filter(g->g.isSlaBreached()||LocalDateTime.now().isAfter(g.getSlaDueAt())).count();
  long inSla=repo.countByStatusInAndSlaBreachedFalse(DONE);
  double rate=(total-rejected)==0?0:round(resolved*100.0/(total-rejected));double compliance=resolved==0?100:round(inSla*100.0/resolved);
  Map<String,Long> byStatus=new LinkedHashMap<>();for(GrievanceStatus s:GrievanceStatus.values())byStatus.put(s.name(),repo.countByStatus(s));
  LocalDateTime month=LocalDate.now().withDayOfMonth(1).atStartOfDay();
  return new DashboardSummary(citizenClient.countCitizens(),repo.countByCreatedAtAfter(month),total,open,resolved,rate,breached,compliance,byStatus);
 }
 private Grievance find(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Grievance not found with id "+id));}
 private void addHistory(Grievance g,String action,GrievanceStatus from,GrievanceStatus to,String remarks,String by){
  GrievanceHistory h=new GrievanceHistory(g.getId(),action,from,to,remarks,by);historyRepo.save(h);
 }
 private static double round(double v){return Math.round(v*10)/10.0;}
}
