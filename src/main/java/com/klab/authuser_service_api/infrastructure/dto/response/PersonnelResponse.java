package com.klab.authuser_service_api.infrastructure.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class PersonnelResponse {
    @Schema(description = "Personnel ID", example = "1")
    private Integer personnelId;

    @Schema(description = "Document type ID", example = "1")
    private Integer documentTypeId;

    @Schema(description = "Document number", example = "12345678")
    private String documentNumber;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "Date of birth", example = "1990-01-15")
    private String dateOfBirth;

    @Schema(description = "Gender", example = "M")
    private String gender;

    @Schema(description = "Address", example = "123 Main St")
    private String address;

    @Schema(description = "City", example = "New York")
    private String city;

    @Schema(description = "State/Province", example = "NY")
    private String stateProvince;

    @Schema(description = "Country", example = "USA")
    private String country;

    @Schema(description = "Postal code", example = "10001")
    private String postalCode;

    @Schema(description = "Emergency contact name", example = "Jane Doe")
    private String emergencyContactName;

    @Schema(description = "Emergency contact phone", example = "+1234567891")
    private String emergencyContactPhone;

    @Schema(description = "Hire date", example = "2023-01-01")
    private String hireDate;

    @Schema(description = "Job position", example = "Software Developer")
    private String position;

    @Schema(description = "Department", example = "IT")
    private String department;

    @Schema(description = "Active status", example = "1")
    private Short isActive;

    @Schema(description = "Creation timestamp")
    private String createdAt;

    @Schema(description = "Last update timestamp")
    private String updatedAt;


}
