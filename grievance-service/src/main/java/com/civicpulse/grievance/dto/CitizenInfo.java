package com.civicpulse.grievance.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public record CitizenInfo(Long id,String citizenCode,String fullName,Integer wardNumber) {}
