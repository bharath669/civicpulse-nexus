package com.civicpulse.grievance.service;
import com.civicpulse.grievance.entity.*;
import java.time.*;
public final class SlaPolicy {
 public enum SlaStatus { WITHIN_SLA,DUE_SOON,BREACHED,MET,MISSED }
 private SlaPolicy(){}
 public static int slaHours(GrievanceCategory category,Severity severity){
  return Math.max((int)Math.round(category.getBaseSlaHours()*severity.getSlaMultiplier()),4);
 }
 public static LocalDateTime dueAt(LocalDateTime from,int hours){return from.plusHours(hours);}
 public static SlaStatus slaStatus(Grievance g,LocalDateTime now){
  if(!g.getStatus().isOpen())return g.isSlaBreached()?SlaStatus.MISSED:SlaStatus.MET;
  if(g.isSlaBreached()||now.isAfter(g.getSlaDueAt()))return SlaStatus.BREACHED;
  long left=Duration.between(now,g.getSlaDueAt()).toMinutes();
  long warn=(long)(g.getSlaHours()*60*0.2);
  return left<=warn?SlaStatus.DUE_SOON:SlaStatus.WITHIN_SLA;
 }
 public static long hoursRemaining(Grievance g,LocalDateTime now){
  return g.getStatus().isOpen()?Duration.between(now,g.getSlaDueAt()).toHours():0;
 }
 public static String levelName(int level){return switch(level){
  case 0->"Not escalated";case 1->"Level 1 - Department Head";
  case 2->"Level 2 - Municipal Commissioner";default->"Level 3 - District Collector";};}
}
