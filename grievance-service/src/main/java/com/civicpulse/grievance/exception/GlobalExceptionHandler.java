package com.civicpulse.grievance.exception;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex){
  Map<String,String> f=new LinkedHashMap<>();ex.getBindingResult().getFieldErrors().forEach(e->f.putIfAbsent(e.getField(),e.getDefaultMessage()));
  return response(HttpStatus.BAD_REQUEST,"Validation failed",f);
 }
 @ExceptionHandler(ResourceNotFoundException.class) ResponseEntity<ApiError> missing(ResourceNotFoundException ex){return response(HttpStatus.NOT_FOUND,ex.getMessage(),Map.of());}
 @ExceptionHandler(BusinessRuleException.class) ResponseEntity<ApiError> rule(BusinessRuleException ex){return response(HttpStatus.UNPROCESSABLE_ENTITY,ex.getMessage(),Map.of());}
 @ExceptionHandler(ServiceUnavailableException.class) ResponseEntity<ApiError> unavailable(ServiceUnavailableException ex){return response(HttpStatus.SERVICE_UNAVAILABLE,ex.getMessage(),Map.of());}
 @ExceptionHandler(Exception.class) ResponseEntity<ApiError> other(Exception ex){return response(HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected server error",Map.of());}
 private ResponseEntity<ApiError> response(HttpStatus s,String m,Map<String,String> f){return ResponseEntity.status(s).body(new ApiError(LocalDateTime.now(),s.value(),s.getReasonPhrase(),m,f));}
}
