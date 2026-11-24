package com.civicpulse.grievance.client;
import com.civicpulse.grievance.dto.CitizenInfo;
import com.civicpulse.grievance.exception.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
@Component
public class CitizenClient {
 private final RestClient client;
 public CitizenClient(@Value("${civic.citizen-service.base-url}") String baseUrl){client=RestClient.builder().baseUrl(baseUrl).build();}
 public CitizenInfo getCitizen(Long id){
  try{return client.get().uri("/api/citizens/{id}",id).retrieve().body(CitizenInfo.class);}
  catch(HttpClientErrorException.NotFound e){throw new ResourceNotFoundException("Citizen "+id+" is not registered. Register the citizen first.");}
  catch(ResourceAccessException e){throw new ServiceUnavailableException("Citizen Service is not reachable. Is it running on port 8081?");}
 }
 public long countCitizens(){
  try{return client.get().uri("/api/citizens/count").retrieve().body(Long.class);}
  catch(Exception e){return -1L;}
 }
}
