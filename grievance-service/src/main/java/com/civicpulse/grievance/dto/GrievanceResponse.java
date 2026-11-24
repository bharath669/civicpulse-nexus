package com.civicpulse.grievance.dto;
import com.civicpulse.grievance.entity.*;
import com.civicpulse.grievance.service.SlaPolicy;
import java.time.*;
public record GrievanceResponse(Long id,String grievanceNumber,Long citizenId,String citizenCode,String citizenName,
 Integer wardNumber,String category,String severity,String title,String description,String location,String department,
 String departmentName,String assignedOfficer,String status,int escalationLevel,int slaHours,String slaStatus,
 LocalDateTime slaDueAt,long hoursRemaining,boolean slaBreached,String resolutionNotes,LocalDateTime createdAt) {
 public static GrievanceResponse from(Grievance g){
  LocalDateTime now=LocalDateTime.now();
  return new GrievanceResponse(g.getId(),g.getGrievanceNumber(),g.getCitizenId(),g.getCitizenCode(),g.getCitizenName(),
   g.getWardNumber(),g.getCategory().name(),g.getSeverity().name(),g.getTitle(),g.getDescription(),g.getLocation(),
   g.getDepartment().name(),g.getDepartment().getDisplayName(),g.getAssignedOfficer(),g.getStatus().name(),
   g.getEscalationLevel(),g.getSlaHours(),SlaPolicy.slaStatus(g,now).name(),g.getSlaDueAt(),
   Duration.between(now,g.getSlaDueAt()).toHours(),g.isSlaBreached(),g.getResolutionNotes(),g.getCreatedAt());
 }
}
