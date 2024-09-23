package com.congdinh.vivustore.dtos.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "First name is required")
    @Length(min = 3, max = 255, message = "First name must be between 3 and 255 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Length(min = 3, max = 255, message = "Last name must be between 3 and 255 characters")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Length(min = 6, max = 255, message = "Username must be between 6 and 255 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Length(min = 6, max = 255, message = "Email must be between 6 and 255 characters")
    private String email;
}
