package com.learning.restapi.business;

import lombok.Data;

@Data
public class LoginBody {
    private String username;
    private String password; 
}
