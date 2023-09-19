package com.learning.restapi.controller;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import com.learning.restapi.business.LoginBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
public class APIAuthController {

    @Autowired
    JwtEncoder jwtEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    LoginBody currentLoginBody;


    @PostMapping("/token")
    public String token(@RequestBody LoginBody loginBody) {
        Instant now = Instant.now();
        System.out.println("inside api/auth/token");
        long expiry = 3600L;
        System.out.println(loginBody.getUsername());
        System.out.println(loginBody.getPassword());
        var username = loginBody.getUsername();
        var password = loginBody.getPassword();
        currentLoginBody = loginBody;
        
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        System.out.println("authentication: "+ authentication);
        String scope = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(expiry))
				.subject(authentication.getName())
				.claim("scope", scope)
				.build();
        System.out.println(this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
}
