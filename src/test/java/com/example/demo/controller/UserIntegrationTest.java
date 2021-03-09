package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.example.demo.controllers.UserController;
import com.example.demo.domain.AuthenticationRequest;
import com.example.demo.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@AutoConfigureMockMvc //need this in Spring Boot test
public class UserIntegrationTest {
    @Autowired
    UserController userController;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    //integration test to create a user -
    @Test
    public void registerUserTest() throws Exception{
        User user = new User();
        user.setUsername("something");
        user.setPassword("something");
        user.setRoles("ADMIN");

        mockMvc
                .perform(post("/user").content(mapper.writeValueAsString(user)).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    // create a user with bad credentials
    @Test
    public void registerUserNoUsernameTest() throws Exception{
        User user = new User();
        user.setPassword("something");
        user.setRoles("ADMIN");

        mockMvc
                .perform(post("/user").content(mapper.writeValueAsString(user)).contentType(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }


    //create 2 users with same username
    @Test
    public void loginTest() throws Exception {
        // create dummy user
        User user = new User();
        user.setUsername("loginTest");
        user.setPassword("something");
        user.setRoles("ADMIN");

        userController.createUser(user);

        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("loginTest");
        request.setPassword("something");

        //check valid user can login
        mockMvc
                .perform(post("/authenticate").content(mapper.writeValueAsString(request)).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        //check invalid use cannot log in
        request.setUsername("afasfa");
        mockMvc
                .perform(post("/authenticate").content(mapper.writeValueAsString(request)).contentType(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }


}
