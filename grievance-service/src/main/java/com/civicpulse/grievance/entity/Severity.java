package com.civicpulse.grievance.entity;
public enum Severity {
 LOW(2.0), MEDIUM(1.5), HIGH(1.0), CRITICAL(0.5);
 private final double slaMultiplier;
 Severity(double multiplier){this.slaMultiplier=multiplier;}
 public double getSlaMultiplier(){return slaMultiplier;}
}
