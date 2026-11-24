package com.civicpulse.grievance.controller;
import com.civicpulse.grievance.dto.DashboardSummary;
import com.civicpulse.grievance.service.GrievanceService;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
 private final GrievanceService service;
 public DashboardController(GrievanceService s){service=s;}
 @GetMapping("/summary") public DashboardSummary summary(){return service.dashboard();}
}
