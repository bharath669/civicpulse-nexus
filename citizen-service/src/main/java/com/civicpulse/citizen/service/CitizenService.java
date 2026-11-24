package com.civicpulse.citizen.service;
import com.civicpulse.citizen.dto.*;
import com.civicpulse.citizen.entity.Citizen;
import com.civicpulse.citizen.exception.*;
import com.civicpulse.citizen.repository.CitizenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Year;
import java.util.List;
@Service
public class CitizenService {
 private final CitizenRepository repo;
 public CitizenService(CitizenRepository repo){this.repo=repo;}
 @Transactional
 public CitizenResponse register(CitizenRequest r){
  String email=r.email().trim().toLowerCase();
  if(repo.existsByEmail(email)) throw new DuplicateResourceException("Email already registered");
  if(repo.existsByPhone(r.phone())) throw new DuplicateResourceException("Phone already registered");
  if(repo.existsByAadhaarNumber(r.aadhaarNumber())) throw new DuplicateResourceException("Aadhaar already registered");
  Citizen c=new Citizen(); c.setFullName(r.fullName().trim()); c.setEmail(email); c.setPhone(r.phone());
  c.setAadhaarNumber(r.aadhaarNumber()); c.setGender(r.gender()); c.setDateOfBirth(r.dateOfBirth());
  c.setWardNumber(r.wardNumber()); c.setAddress(r.address().trim());
  c.setCitizenCode("TMP-"+System.nanoTime());
  c=repo.save(c); c.setCitizenCode(String.format("CTZ-%d-%06d", Year.now().getValue(),c.getId()));
  return CitizenResponse.from(c);
 }
 @Transactional(readOnly=true) public List<CitizenResponse> list(String name){
  List<Citizen> rows=(name==null||name.isBlank())?repo.findAll():repo.findByFullNameContainingIgnoreCase(name.trim());
  return rows.stream().map(CitizenResponse::from).toList();
 }
 @Transactional(readOnly=true) public CitizenResponse get(Long id){return CitizenResponse.from(repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Citizen "+id+" is not registered")));}
 @Transactional(readOnly=true) public long count(){return repo.count();}
}
