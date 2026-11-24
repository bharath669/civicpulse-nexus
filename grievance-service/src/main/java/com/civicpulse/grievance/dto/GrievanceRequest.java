package com.civicpulse.grievance.dto;
import com.civicpulse.grievance.entity.*;
import jakarta.validation.constraints.*;
public record GrievanceRequest(
 @NotNull(message="Citizen id is required") @Positive(message="Citizen id must be positive") Long citizenId,
 @NotNull(message="Category is required") GrievanceCategory category,
 @NotNull(message="Severity is required") Severity severity,
 @NotBlank(message="Title is required") @Size(min=5,max=150,message="Title must be 5-150 characters") String title,
 @NotBlank(message="Description is required") @Size(min=15,max=1000,message="Description must be 15-1000 characters") String description,
 @Size(max=255,message="Location can be at most 255 characters") String location
) {}
