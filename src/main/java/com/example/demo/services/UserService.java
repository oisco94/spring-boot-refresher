package com.example.demo.services;

import com.example.demo.data.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.errors.UsernameTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void createUser(User newUser) throws UsernameTakenException {

        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            throw new UsernameTakenException("username already taken", null);
        }

        userRepository.save(newUser);
    }
}
