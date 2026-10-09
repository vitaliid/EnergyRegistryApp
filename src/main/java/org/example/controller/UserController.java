package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.UsersApi;
import org.example.model.User;
import org.example.model.UserCreate;
import org.example.model.UserUpdate;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserService service;

    @Override
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(service.list());
    }

    @Override
    public ResponseEntity<User> createUser(UserCreate userCreate) {
        User created = service.create(userCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public ResponseEntity<User> getUserById(UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Override
    public ResponseEntity<User> patchUser(UUID id, UserUpdate userUpdate) {
        return ResponseEntity.ok(service.patch(id, userUpdate));
    }

    @Override
    public ResponseEntity<User> deactivateUser(UUID id, Boolean confirmed) {
        return ResponseEntity.ok(service.deactivate(id, Boolean.TRUE.equals(confirmed)));
    }
}
