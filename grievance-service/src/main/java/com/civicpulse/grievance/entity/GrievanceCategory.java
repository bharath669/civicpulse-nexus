package com.civicpulse.grievance.entity;
/** Category controls routing and the base SLA in hours. */
public enum GrievanceCategory {
 WATER_SUPPLY(Department.WATER,48), DRAINAGE(Department.SANITATION,48),
 ELECTRICITY(Department.ELECTRICITY,24), STREET_LIGHT(Department.ELECTRICITY,72),
 ROADS(Department.ROADS,120), SANITATION(Department.SANITATION,72),
 PUBLIC_HEALTH(Department.HEALTH,24), OTHER(Department.GENERAL,168);
 private final Department department; private final int baseSlaHours;
 GrievanceCategory(Department department,int hours){this.department=department;this.baseSlaHours=hours;}
 public Department getDepartment(){return department;}
 public int getBaseSlaHours(){return baseSlaHours;}
}
