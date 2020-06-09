package com.demo.handsOn.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.handsOn.entity.UserEntity;
import com.demo.handsOn.exception.ResourceNotFoundException;
import com.demo.handsOn.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	public UserEntity createNewUser(UserEntity user) throws Exception {
		if(user==null) throw new Exception("user is null");
		 return repository.save(user);
	}
	
	
	public List<UserEntity> getAllUsers(){
		return repository.findAll();
	}
	
	
	public UserEntity findUserById(int id) throws ResourceNotFoundException {
		Optional<UserEntity> user = repository.findById(id);
		
		if(user.isPresent()) return user.get();
		else throw new ResourceNotFoundException(String.format("User not found with id %d", id));
	}
	
	public UserEntity updateUSer(int id, UserEntity userDetails) throws ResourceNotFoundException {
		UserEntity user = repository.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format("User not found with id %d", id)));
		
		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		user.setEmail(userDetails.getEmail());
		user.setContactNumber(userDetails.getContactNumber());
		
		return repository.save(user);
	}
	
	

}
