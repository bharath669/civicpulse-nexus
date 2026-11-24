package com.civicpulse.citizen.dto;
import com.civicpulse.citizen.entity.Gender;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record CitizenRequest(
 @NotBlank(message="Full name is required") @Size(min=3,max=100,message="Full name must be 3-100 characters") String fullName,
 @NotBlank(message="Email is required") @Email(message="Email format is invalid") String email,
 @NotBlank(message="Phone is required") @Pattern(regexp="^[6-9]\\d{9}$",message="Phone must be a valid 10-digit mobile number") String phone,
 @NotBlank(message="Aadhaar number is required") @Pattern(regexp="^\\d{12}$",message="Aadhaar must be exactly 12 digits") String aadhaarNumber,
 @NotNull(message="Gender is required") Gender gender,
 @NotNull(message="Date of birth is required") @Past(message="Date of birth must be in the past") LocalDate dateOfBirth,
 @NotNull(message="Ward number is required") @Min(value=1,message="Ward number must be between 1 and 200") @Max(value=200,message="Ward number must be between 1 and 200") Integer wardNumber,
 @NotBlank(message="Address is required") @Size(max=255,message="Address can be at most 255 characters") String address
) {}
