package com.demo.handsOn.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.demo.handsOn.entity.UserEntity;
import com.demo.handsOn.exception.ResourceNotFoundException;
import com.demo.handsOn.repositories.UserRepository;
import com.demo.handsOn.services.UserService;

@WebMvcTest(UserService.class)
public class UserServiceTest {

	
	@Autowired
	private UserService service;
	
	@MockBean
	private UserRepository repository;
	
	@Test
	public void testGetById_withValidUser() {
		UserEntity user = new UserEntity("Akshay", "Chavan", "ac@hh", 9876543210L);
		when(repository.findById(1)).thenReturn(Optional.of(user));
		
		assertEquals(user, service.findUserById(1));
	}
	
	@Test
	public void testGetById_withNoUser() {
		when(repository.findById(1)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, ()->service.findUserById(1));
	}
	
	@Test
	public void testUpdateUSer_forValidUser() {
		when(repository.findById(1)).thenReturn(Optional.empty());
		
		UserEntity userDetails = new UserEntity("Akshay", "Chavan", "ac@hh", 9876543210L);
		assertThrows(ResourceNotFoundException.class, ()->service.updateUSer(1, userDetails));
	}
	
	@Test
	public void testUpdateUser_withValidUser() {
		UserEntity user = new UserEntity("Akshay", "Chavan", "ac@hh", 9876543210L);
		when(repository.findById(1)).thenReturn(Optional.of(user));
		
		when(repository.save(user)).thenReturn(user);
		
		assertEquals(user, service.updateUSer(1, user));
	}
}
