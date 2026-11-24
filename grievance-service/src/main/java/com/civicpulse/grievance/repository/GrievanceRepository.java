package com.civicpulse.grievance.repository;
import com.civicpulse.grievance.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
public interface GrievanceRepository extends JpaRepository<Grievance,Long> {
 java.util.Optional<Grievance> findByGrievanceNumber(String number);
 List<Grievance> findByStatusInAndSlaDueAtBefore(List<GrievanceStatus> statuses,LocalDateTime before);
 long countByStatus(GrievanceStatus status);
 long countByStatusIn(List<GrievanceStatus> statuses);
 long countByStatusInAndSlaBreachedFalse(List<GrievanceStatus> statuses);
 long countByCreatedAtAfter(LocalDateTime date);
}
