package com.civicpulse.citizen.controller;
import com.civicpulse.citizen.dto.*;
import com.civicpulse.citizen.service.CitizenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/citizens")
public class CitizenController {
 private final CitizenService service;
 public CitizenController(CitizenService service){this.service=service;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public CitizenResponse register(@Valid @RequestBody CitizenRequest r){return service.register(r);}
 @GetMapping public List<CitizenResponse> list(@RequestParam(required=false) String name){return service.list(name);}
 @GetMapping("/{id}") public CitizenResponse get(@PathVariable Long id){return service.get(id);}
 @GetMapping("/count") public long count(){return service.count();}
}
