package com.learning.restapi.service;

import lombok.Data;

@Data
public class RegistrationForm {
    private String username;
    private String password;
    private String repeatPassword;
    private String role;

    public boolean isValid() {
        return password.equals(repeatPassword);
    }
    
}
