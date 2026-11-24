package com.civicpulse.grievance.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter @Setter
@Entity @Table(name="grievances")
public class Grievance {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="grievance_number",unique=true,nullable=false,length=30) private String grievanceNumber;
 @Column(name="citizen_id",nullable=false) private Long citizenId;
 @Column(name="citizen_code",nullable=false,length=30) private String citizenCode;
 @Column(name="citizen_name",nullable=false,length=100) private String citizenName;
 @Column(name="ward_number",nullable=false) private Integer wardNumber;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private GrievanceCategory category;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private Severity severity;
 @Column(nullable=false,length=150) private String title;
 @Column(nullable=false,columnDefinition="TEXT") private String description;
 @Column(length=255) private String location;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private Department department;
 @Column(name="assigned_officer",length=100) private String assignedOfficer;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private GrievanceStatus status;
 @Column(name="escalation_level",nullable=false) private int escalationLevel=0;
 @Column(name="sla_hours",nullable=false) private int slaHours;
 @Column(name="sla_due_at",nullable=false) private LocalDateTime slaDueAt;
 @Column(name="sla_breached",nullable=false) private boolean slaBreached=false;
 @Column(name="resolution_notes",columnDefinition="TEXT") private String resolutionNotes;
 @Column(name="resolved_at") private LocalDateTime resolvedAt;
 @Column(name="closed_at") private LocalDateTime closedAt;
 @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
 @PrePersist void created(){createdAt=LocalDateTime.now();}
}
