package com.demo.handsOn.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.demo.handsOn.entity.UserEntity;
import com.demo.handsOn.exception.ResourceNotFoundException;
import com.demo.handsOn.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void testGetAllUsers() throws Exception {
		when(service.getAllUsers()).thenReturn(new ArrayList<UserEntity>());
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/users");
		mockMvc.perform(request).andExpect(status().isOk());
	}
	
	@Test
	public void testCreateUser_all_details() throws Exception {
		UserEntity user = new UserEntity("Test", "Test", "test@domain.com", 9876543210L);
		when(service.createNewUser(any(UserEntity.class))).thenReturn(user);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users").
				contentType("application/json").content(objectMapper.writeValueAsString(user));
		
		mockMvc.perform(requestBuilder ).andExpect(status().isCreated());
	}
	
	@Test
	public void testCreateUser_Null_Email() throws Exception {
		UserEntity user = new UserEntity("Test", "Test", null, 9876543210L);
		when(service.createNewUser(any(UserEntity.class))).thenReturn(user);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users").
				contentType("application/json").content(objectMapper.writeValueAsString(user));
		
		mockMvc.perform(requestBuilder ).andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void testCreateUser_Null_Name() throws Exception {
		UserEntity user = new UserEntity(null, "Test", "test@domain.com", 9876543210L);
		when(service.createNewUser(any(UserEntity.class))).thenReturn(user);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users").
				contentType("application/json").content(objectMapper.writeValueAsString(user));
		
		mockMvc.perform(requestBuilder ).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testGetUserById() throws Exception {
		UserEntity user = new UserEntity("Test", "Test", "test@domain.com", 9876543210L);
		user.setId(1);
		when(service.findUserById(anyInt())).thenReturn(user);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/1");
		
		mockMvc.perform(requestBuilder ).andExpect(status().isOk())
		.andExpect(content().json(objectMapper.writeValueAsString(user)));
	}
	
	@Test
	public void testGetUserById_throws_UserNotFoundException() throws Exception {
		when(service.findUserById(anyInt())).thenThrow(new ResourceNotFoundException(""));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/1");
		
		mockMvc.perform(requestBuilder ).andExpect(status().isNotFound());
	}
	
	
	
}
