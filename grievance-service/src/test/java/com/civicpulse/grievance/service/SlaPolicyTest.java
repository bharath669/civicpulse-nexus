package com.civicpulse.grievance.service;
import com.civicpulse.grievance.entity.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
class SlaPolicyTest {
 @Test void categoryAndSeverityDetermineSla(){assertThat(SlaPolicy.slaHours(GrievanceCategory.WATER_SUPPLY,Severity.MEDIUM)).isEqualTo(72);}
 @Test void criticalHasShorterSlaThanMedium(){assertThat(SlaPolicy.slaHours(GrievanceCategory.WATER_SUPPLY,Severity.CRITICAL)).isLessThan(SlaPolicy.slaHours(GrievanceCategory.WATER_SUPPLY,Severity.MEDIUM));}
 @Test void stateMachineRejectsInvalidTransition(){assertThat(GrievanceStatus.ASSIGNED.canMoveTo(GrievanceStatus.CLOSED)).isFalse();}
}
