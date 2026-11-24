package com.civicpulse.grievance.dto;
import com.civicpulse.grievance.entity.Department;
import jakarta.validation.constraints.*;
public record AssignRequest(@NotBlank(message="Officer name is required") @Size(max=100) String officerName,
 Department department,@Size(max=500) String remarks) {}
