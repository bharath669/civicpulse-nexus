package com.civicpulse.grievance.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter @Setter
@Entity @Table(name="grievance_history")
public class GrievanceHistory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="grievance_id",nullable=false) private Long grievanceId;
 @Column(nullable=false,length=40) private String action;
 @Enumerated(EnumType.STRING) @Column(name="from_status",length=20) private GrievanceStatus fromStatus;
 @Enumerated(EnumType.STRING) @Column(name="to_status",length=20) private GrievanceStatus toStatus;
 @Column(columnDefinition="TEXT") private String remarks;
 @Column(name="performed_by",length=100) private String performedBy;
 @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
 protected GrievanceHistory(){}
 public GrievanceHistory(Long id,String action,GrievanceStatus from,GrievanceStatus to,String remarks,String by){
  this.grievanceId=id;this.action=action;this.fromStatus=from;this.toStatus=to;this.remarks=remarks;this.performedBy=by;
 }
 @PrePersist void created(){createdAt=LocalDateTime.now();}
}
