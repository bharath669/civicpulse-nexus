package com.civicpulse.citizen.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter @Setter
@Entity
@Table(name="citizens")
public class Citizen {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="citizen_code", nullable=false, unique=true, length=20) private String citizenCode;
 @Column(name="full_name", nullable=false, length=100) private String fullName;
 @Column(nullable=false, unique=true, length=120) private String email;
 @Column(nullable=false, unique=true, length=10) private String phone;
 @Column(name="aadhaar_number", nullable=false, unique=true, length=12) private String aadhaarNumber;
 @Enumerated(EnumType.STRING) @Column(nullable=false, length=10) private Gender gender;
 @Column(name="date_of_birth", nullable=false) private LocalDate dateOfBirth;
 @Column(name="ward_number", nullable=false) private Integer wardNumber;
 @Column(nullable=false, length=255) private String address;
 @Column(name="created_at", nullable=false, updatable=false) private LocalDateTime createdAt;
 @PrePersist void onCreate() { createdAt=LocalDateTime.now(); }
}
