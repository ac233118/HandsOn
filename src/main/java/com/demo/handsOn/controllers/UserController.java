package com.demo.handsOn.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.handsOn.entity.UserEntity;
import com.demo.handsOn.exception.ResourceNotFoundException;
import com.demo.handsOn.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping("/users")
	public ResponseEntity<UserEntity> createUser(@Valid @RequestBody UserEntity user) throws Exception {

		UserEntity entity = service.createNewUser(user);

		return new ResponseEntity<UserEntity>(entity, HttpStatus.CREATED);

	}

	@GetMapping("/users")
	public List<UserEntity> getAllUsers() {
		return service.getAllUsers();
	}

	@GetMapping("/users/{id}")
	public UserEntity getUserById(@PathVariable int id) throws ResourceNotFoundException {
		return service.findUserById(id);
	}

	@PutMapping("/users/{id}")
	public UserEntity updateUser(@PathVariable int id, @Valid @RequestBody UserEntity user)
			throws ResourceNotFoundException {

		return service.updateUSer(id, user);
	}
}
