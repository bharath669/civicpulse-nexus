package com.civicpulse.grievance.entity;
public enum Department {
 WATER("Water Dept"), ELECTRICITY("Electricity Dept"), ROADS("Roads & PWD"),
 SANITATION("Sanitation Dept"), HEALTH("Public Health Dept"), GENERAL("General Administration");
 private final String displayName;
 Department(String displayName){this.displayName=displayName;}
 public String getDisplayName(){return displayName;}
}
