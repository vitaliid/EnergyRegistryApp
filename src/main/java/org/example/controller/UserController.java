package org.example.controller;

import org.example.api.UsersApi;
import org.example.model.User;
import org.example.model.UserCreate;
import org.example.model.UserUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Implements the generated {@link UsersApi} (tag "Users").
 * All operations are stubs until the service layer is wired in.
 */
@RestController
public class UserController implements UsersApi {

    @Override
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<User> createUser(UserCreate userCreate) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<User> getUserById(UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<User> patchUser(UUID id, UserUpdate userUpdate) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<User> deactivateUser(UUID id, Boolean confirmed) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
