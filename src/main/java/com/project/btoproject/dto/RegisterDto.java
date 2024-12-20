package com.project.btoproject.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RegisterDto {
    private String username;
    private String password;
    private String role;
}
