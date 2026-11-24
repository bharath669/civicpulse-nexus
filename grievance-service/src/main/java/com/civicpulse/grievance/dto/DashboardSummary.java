package com.civicpulse.grievance.dto;
import java.util.Map;
public record DashboardSummary(long registeredCitizens,long grievancesThisMonth,long totalGrievances,
 long openGrievances,long resolvedGrievances,double resolutionRate,long slaBreachedOpen,
 double slaCompliance,Map<String,Long> byStatus) {}
