package com.nilotpal.api.userapi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.nilotpal.api.userapi.entity.UserEntity;
import com.nilotpal.api.userapi.exceptions.UserNotFoundException;
import com.nilotpal.api.userapi.model.CreateUserRequest;
import com.nilotpal.api.userapi.model.UpdateUserRequest;
import com.nilotpal.api.userapi.model.User;
import com.nilotpal.api.userapi.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	private static UserEntity createdUser;
	private static UserEntity updatedUser;
	private static UserEntity deletedUser;
	private static List<UserEntity> dummyUsers;
	private static CreateUserRequest userToCreate;
	private static UpdateUserRequest userToUpdate;

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserRepository userRepository;

	@BeforeClass
	public static void setUp() {
		dummyUsers = new ArrayList<>();
		dummyUsers.add(new UserEntity("test1@testing.com", "TestUser1", "password", "testusername1"));
		dummyUsers.add(new UserEntity("test2@testing.com", "TestUser2", "password", "testusername2"));
		dummyUsers.add(new UserEntity("test3@testing.com", "TestUser3", "password", "testusername3"));

		userToCreate = new CreateUserRequest("test4@testing.com", "TestUser4", "password", "testusername4");
		userToUpdate = new UpdateUserRequest("test5@testing.com", "TestUser5", "password", "testusername5");

		createdUser = new UserEntity("test4@testing.com", "TestUser4", "password", "testusername4");
		updatedUser = new UserEntity("test5@testing.com", "TestUser5", "password5", "testusername5");
		deletedUser = new UserEntity("test6@testing.com", "TestUser6", "password", "testusername6");
	}

	@Test
	public void testGetAllUsers_WithoutPaginationDetails() throws Exception {
		when(userRepository.findAll()).thenReturn(dummyUsers);
		List<User> allUsers = userService.getAllUsers(null, null);

		assertEquals(3, allUsers.size());
		assertEquals("testusername1", allUsers.get(0).getUsername());
		assertEquals("test2@testing.com", allUsers.get(1).getEmail());
		assertEquals("TestUser3", allUsers.get(2).getName());
	}

	@Test
	public void testGetAllUsers_WithPaginationDetails() throws Exception {
		when(userRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(new PageImpl<UserEntity>(dummyUsers));
		List<User> allUsers = userService.getAllUsers(0, 3);

		assertEquals(3, allUsers.size());
		assertEquals("testusername1", allUsers.get(0).getUsername());
		assertEquals("test2@testing.com", allUsers.get(1).getEmail());
		assertEquals("TestUser3", allUsers.get(2).getName());
	}

	@Test
	public void testGetUserById_UserExists() throws Exception {
		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(dummyUsers.get(0)));
		User user = userService.getUserById("1");

		assertNotNull(user);
		assertEquals("test1@testing.com", user.getEmail());
		assertEquals("TestUser1", user.getName());
		assertEquals("password", user.getPassword());
		assertEquals("testusername1", user.getUsername());
	}

	@Test(expected = UserNotFoundException.class)
	public void testGetUserById_UserNotExists() throws Exception {
		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		userService.getUserById("10");
	}

	@Test
	public void testCreateUser() throws Exception {
		when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(createdUser);
		User user = userService.createUser(userToCreate);

		assertNotNull(user);
		assertEquals("test4@testing.com", user.getEmail());
		assertEquals("TestUser4", user.getName());
		assertEquals("password", user.getPassword());
		assertEquals("testusername4", user.getUsername());
	}

	@Test
	public void testUpdateUser_ExistingUser() throws Exception {
		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(updatedUser));
		when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(updatedUser);
		User user = userService.updateUser("5", userToUpdate);

		assertNotNull(user);
		assertEquals("test5@testing.com", user.getEmail());
		assertEquals("TestUser5", user.getName());
		assertEquals("password", user.getPassword());
		assertEquals("testusername5", user.getUsername());
	}

	@Test(expected = UserNotFoundException.class)
	public void testUpdateUser_NonExistentUser() throws Exception {
		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		userService.updateUser("15", userToUpdate);
	}

	@Test
	public void testDeleteUser_ExistingUser() throws Exception {
		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(deletedUser));
		doNothing().when(userRepository).delete(Mockito.any(UserEntity.class));

		User user = userService.deleteUser("6");
		assertNotNull(user);
		assertEquals("test6@testing.com", user.getEmail());
		assertEquals("TestUser6", user.getName());
		assertEquals("password", user.getPassword());
		assertEquals("testusername6", user.getUsername());
	}

	@Test(expected = UserNotFoundException.class)
	public void testDeleteUser_NonExistentUser() throws Exception {
		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		userService.deleteUser("15");
	}
}
