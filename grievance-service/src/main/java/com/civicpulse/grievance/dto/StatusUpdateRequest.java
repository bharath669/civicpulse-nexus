package com.civicpulse.grievance.dto;
import com.civicpulse.grievance.entity.GrievanceStatus;
import jakarta.validation.constraints.NotNull;
public record StatusUpdateRequest(@NotNull(message="Status is required") GrievanceStatus status,
 String resolutionNotes,String remarks,String performedBy) {}
