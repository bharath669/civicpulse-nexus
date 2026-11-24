package com.civicpulse.grievance.entity;
import java.util.List;
public enum GrievanceStatus {
 REGISTERED, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED, REJECTED;
 public boolean isOpen(){return this==REGISTERED||this==ASSIGNED||this==IN_PROGRESS;}
 public List<GrievanceStatus> allowedNext(){
  return switch(this){
   case REGISTERED -> List.of(ASSIGNED,REJECTED);
   case ASSIGNED -> List.of(IN_PROGRESS,REJECTED);
   case IN_PROGRESS -> List.of(RESOLVED,REJECTED);
   case RESOLVED -> List.of(CLOSED,IN_PROGRESS);
   case CLOSED,REJECTED -> List.of();
  };
 }
 public boolean canMoveTo(GrievanceStatus next){return allowedNext().contains(next);}
}
