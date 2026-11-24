package com.civicpulse.grievance.controller;
import com.civicpulse.grievance.dto.*;
import com.civicpulse.grievance.entity.*;
import com.civicpulse.grievance.service.GrievanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/grievances")
public class GrievanceController {
 private final GrievanceService service;
 public GrievanceController(GrievanceService s){service=s;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public GrievanceResponse register(@Valid @RequestBody GrievanceRequest r){return service.register(r);}
 @GetMapping public List<GrievanceResponse> list(@RequestParam(required=false) GrievanceStatus status,@RequestParam(required=false) Department department,
  @RequestParam(required=false) Integer ward,@RequestParam(required=false) Long citizenId){return service.list(status,department,ward,citizenId);}
 @GetMapping("/{id}") public GrievanceResponse get(@PathVariable Long id){return service.get(id);}
 @GetMapping("/track/{number}") public GrievanceResponse track(@PathVariable String number){return service.track(number);}
 @GetMapping("/{id}/history") public List<HistoryResponse> history(@PathVariable Long id){return service.history(id);}
 @PostMapping("/{id}/assign") public GrievanceResponse assign(@PathVariable Long id,@Valid @RequestBody AssignRequest r){return service.assign(id,r);}
 @PatchMapping("/{id}/status") public GrievanceResponse status(@PathVariable Long id,@Valid @RequestBody StatusUpdateRequest r){return service.updateStatus(id,r);}
 @PostMapping("/{id}/escalate") public GrievanceResponse escalate(@PathVariable Long id,@RequestBody(required=false) Map<String,String> b){return service.escalate(id,b==null?null:b.get("reason"));}
 @PostMapping("/{id}/simulate-breach") public GrievanceResponse simulate(@PathVariable Long id){return service.simulateBreach(id);}
 @GetMapping("/meta") public Map<String,Object> meta(){return Map.of("categories",GrievanceCategory.values(),"severities",Severity.values(),"departments",Department.values(),"statuses",GrievanceStatus.values());}
}
