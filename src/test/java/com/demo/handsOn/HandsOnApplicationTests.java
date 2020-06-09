package com.demo.handsOn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.demo.handsOn.entity.UserEntity;
import com.demo.handsOn.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class HandsOnApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository repository;

	@Test
	public void testGetAllUsers() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	public void testCreateUser_With_All_Valid_Details() throws Exception {

		UserEntity user = new UserEntity("Test", "test", "test@gmail.com", 9876543210L);
		RequestBuilder request = MockMvcRequestBuilders.post("/api/users").contentType("application/json")
				.content(objectMapper.writeValueAsString(user));

		mockMvc.perform(request).andExpect(status().isCreated());

		UserEntity newUser = repository.findByEmail("test@gmail.com");

		assertEquals(user.getFirstName(), newUser.getFirstName());


		repository.deleteById(newUser.getId());
	}

	@Test
	public void testCreateUser_With_Invalid_Email_Format() throws Exception {

		UserEntity user = new UserEntity("Test", "Test", "TestEmail", 9876543210L);
		RequestBuilder request = MockMvcRequestBuilders.post("/api/users").contentType("application/json")
				.content(objectMapper.writeValueAsString(user));

		mockMvc.perform(request).andExpect(status().isBadRequest());

	}

	@Test
	public void testCreateUser_With_Duplicate_Email() throws Exception {

		UserEntity user = new UserEntity("Test", "Test", "Test@gmail.com", 9876543210L);

		repository.save(user);
		int id = user.getId();
		user.setId(0);

		RequestBuilder request = MockMvcRequestBuilders.post("/api/users").contentType("application/json")
				.content(objectMapper.writeValueAsString(user));

		assertThrows(Exception.class, () -> mockMvc.perform(request));

		repository.deleteById(id);
	}

	@Test
	public void testCreateUser_With_Null_Email() throws Exception {

		UserEntity user = new UserEntity("Test", "Test", null, 9876543210L);
		RequestBuilder request = MockMvcRequestBuilders.post("/api/users").contentType("application/json")
				.content(objectMapper.writeValueAsString(user));

		mockMvc.perform(request).andExpect(status().isBadRequest());

	}

	@Test
	public void testCreateUser_With_Null_Name() throws Exception {

		UserEntity user = new UserEntity(null, "Test", "Test@gmail.com", 9876543210L);
		RequestBuilder request = MockMvcRequestBuilders.post("/api/users").contentType("application/json")
				.content(objectMapper.writeValueAsString(user));

		mockMvc.perform(request).andExpect(status().isBadRequest());

	}

	@Test
	public void testGetUSerById() throws Exception {
		UserEntity user = new UserEntity("Test", "Test", "TestEmail@gmail.com", 9876543210L);
		repository.save(user);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/users/" + user.getId());
		mockMvc.perform(request).andExpect(status().isOk());

		repository.delete(user);
	}

	@Test
	public void testUpdateUser() throws Exception {
		UserEntity user = new UserEntity("Test", "Test", "TestEmail@gmail.com", 9876543210L);

		repository.save(user);

		user.setFirstName("updated");

		RequestBuilder request = MockMvcRequestBuilders.put("/api/users/" + user.getId())
				.contentType("application/json").content(objectMapper.writeValueAsString(user));
		mockMvc.perform(request).andExpect(status().isOk());

		UserEntity updatedUser = repository.findByEmail("TestEmail@gmail.com");

		assertEquals("updated", updatedUser.getFirstName());

		repository.delete(updatedUser);
	}

	@Test
	public void testGetUserByID_for_Invalid_User_Id() throws Exception {
		UserEntity user = new UserEntity("Test", "Test", "TestEmail@gmail.com", 9876543210L);

		repository.save(user);

		repository.deleteById(user.getId());
		RequestBuilder request = MockMvcRequestBuilders.get("/api/users/" + user.getId());
		mockMvc.perform(request).andExpect(status().isNotFound());

	}

}
