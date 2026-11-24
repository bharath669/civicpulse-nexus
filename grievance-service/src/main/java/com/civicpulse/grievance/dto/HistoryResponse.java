package com.civicpulse.grievance.dto;
import com.civicpulse.grievance.entity.*;
import java.time.LocalDateTime;
public record HistoryResponse(Long id,String action,String fromStatus,String toStatus,String remarks,String performedBy,LocalDateTime at){
 public static HistoryResponse from(GrievanceHistory h){return new HistoryResponse(h.getId(),h.getAction(),
  h.getFromStatus()==null?null:h.getFromStatus().name(),h.getToStatus()==null?null:h.getToStatus().name(),
  h.getRemarks(),h.getPerformedBy(),h.getCreatedAt());}
}
