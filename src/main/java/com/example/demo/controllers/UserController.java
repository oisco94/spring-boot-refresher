package com.example.demo.controllers;

import com.example.demo.data.AuthenticationRequest;
import com.example.demo.data.AuthenticationResponse;
import com.example.demo.data.entity.User;
import com.example.demo.services.UserService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.errors.UsernameTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtTokenUtil;

    @Autowired
    UserService userService;


    @PostMapping("/user")
    public ResponseEntity<String> createUser (@Valid @RequestBody User user) throws UsernameTakenException {
        userService.createUser(user);
        return ResponseEntity.ok("User is valid!!");
    }


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }catch (BadCredentialsException s){
            throw new Exception("bad username/password", s);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return  ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
