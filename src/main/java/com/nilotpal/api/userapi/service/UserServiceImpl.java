package com.nilotpal.api.userapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nilotpal.api.userapi.entity.UserEntity;
import com.nilotpal.api.userapi.exceptions.UserNotFoundException;
import com.nilotpal.api.userapi.model.CreateUserRequest;
import com.nilotpal.api.userapi.model.UpdateUserRequest;
import com.nilotpal.api.userapi.model.User;
import com.nilotpal.api.userapi.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;

	@Override
	public List<User> getAllUsers(final Integer page, final Integer size) {
		List<UserEntity> allUsers = new ArrayList<>();

		if (page == null || size == null) {
			allUsers = userRepository.findAll();
		} else {
			PageRequest request = PageRequest.of(page, size);
			allUsers = userRepository.findAll(request).getContent();
		}

		return allUsers.stream().map(UserServiceImpl::convertUserEntityToUser).collect(Collectors.toList());
	}

	@Override
	public User getUserById(final String id) throws UserNotFoundException {
		Optional<UserEntity> userEntity = userRepository.findById(Long.parseLong(id));
		if (userEntity.isPresent()) {
			return convertUserEntityToUser(userEntity.get());
		}

		throw new UserNotFoundException("No user found with id: " + id);
	}

	@Override
	public User createUser(@Valid final CreateUserRequest createUserRequest) {
		UserEntity userEntity = new UserEntity(createUserRequest);
		return convertUserEntityToUser(userRepository.save(userEntity));
	}

	@Override
	public User updateUser(final String id, @Valid final UpdateUserRequest updateUserRequest)
			throws UserNotFoundException {
		Optional<UserEntity> userEntityOptional = userRepository.findById(Long.parseLong(id));
		if (!userEntityOptional.isPresent()) {
			throw new UserNotFoundException("No user found with id: " + id);
		}

		UserEntity userEntity = userEntityOptional.get();
		userEntity = userEntity.merge(updateUserRequest);
		
		return convertUserEntityToUser(userRepository.save(userEntity));
	}

	@Override
	public User deleteUser(final String id) throws UserNotFoundException {
		Optional<UserEntity> userEntityOptional = userRepository.findById(Long.parseLong(id));
		if (!userEntityOptional.isPresent()) {
			throw new UserNotFoundException("No user found with id: " + id);
		}

		UserEntity userEntity = userEntityOptional.get();
		userRepository.delete(userEntity);
		return convertUserEntityToUser(userEntity);
	}

	private static User convertUserEntityToUser(UserEntity userEntity) {
		return new User(String.valueOf(userEntity.getId()), userEntity.getEmail(), userEntity.getName(),
				userEntity.getPassword(), userEntity.getUsername());
	}
}
