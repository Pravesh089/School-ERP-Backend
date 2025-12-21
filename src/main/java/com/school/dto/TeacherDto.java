package com.school.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Data
public class TeacherDto {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private String qualification;
    
    @Email(message = "Invalid email format")
    private String email;
}
