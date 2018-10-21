package com.nilotpal.api.userapi.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.nilotpal.api.userapi.exceptions.UserNotFoundException;
import com.nilotpal.api.userapi.model.CreateUserRequest;
import com.nilotpal.api.userapi.model.UpdateUserRequest;
import com.nilotpal.api.userapi.model.User;

@Service
public interface UserService {
	List<User> getAllUsers(final Integer page, final Integer size);
	User getUserById(final String id) throws UserNotFoundException;
	User createUser(@Valid final CreateUserRequest createUserRequest);
	User updateUser(final String id, @Valid final UpdateUserRequest updateUserRequest) throws UserNotFoundException;
	User deleteUser(String id) throws UserNotFoundException;
}
