package com.example.demo.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class HomeResource {

    @GetMapping("/")
    public String home(){
        return "you're in";
    }

    @GetMapping("/user")
    public String user() {
        return ("<h1>Welcome User</h1>");
    }

    @GetMapping("/admin")
    public String admin() {
        return ("<h1>Welcome Admin</h1>");
    }

}
