package com.civicpulse.citizen.repository;
import com.civicpulse.citizen.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface CitizenRepository extends JpaRepository<Citizen,Long> {
 boolean existsByEmail(String email);
 boolean existsByPhone(String phone);
 boolean existsByAadhaarNumber(String aadhaarNumber);
 Optional<Citizen> findByCitizenCode(String citizenCode);
 List<Citizen> findByFullNameContainingIgnoreCase(String name);
}
