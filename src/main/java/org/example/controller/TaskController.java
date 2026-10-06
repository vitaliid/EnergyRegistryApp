package org.example.controller;

import org.example.api.TasksApi;
import org.example.model.Task;
import org.example.model.TaskStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Implements the generated {@link TasksApi} (tag "Tasks").
 * All operations are stubs until the service layer is wired in.
 */
@RestController
public class TaskController implements TasksApi {

    @Override
    public ResponseEntity<List<Task>> listTasks() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<TaskStatus> toggleTaskStatus(UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
