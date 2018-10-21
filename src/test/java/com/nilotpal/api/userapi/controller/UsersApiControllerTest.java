package com.nilotpal.api.userapi.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nilotpal.api.userapi.exceptions.UserNotFoundException;
import com.nilotpal.api.userapi.model.CreateUserRequest;
import com.nilotpal.api.userapi.model.UpdateUserRequest;
import com.nilotpal.api.userapi.model.User;
import com.nilotpal.api.userapi.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersApiController.class)
public class UsersApiControllerTest {
	private static ObjectMapper objectMapper;
	private static User createdUser;
	private static User updatedUser;
	private static User deletedUser;
	private static List<User> dummyUsers;
	private static CreateUserRequest userToCreate;
	private static UpdateUserRequest userToUpdate;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private UserService userService;
	
	@BeforeClass
	public static void setUp() {
		objectMapper = new ObjectMapper();
		dummyUsers = new ArrayList<>();
		dummyUsers.add(new User("1", "test1@testing.com", "TestUser1", "password", "username1"));
		dummyUsers.add(new User("2", "test2@testing.com", "TestUser2", "password", "username2"));
		dummyUsers.add(new User("3", "test3@testing.com", "TestUser3", "password", "username3"));
		
		userToCreate = new CreateUserRequest("test4@testing.com", "TestUser4", "password", "username4");
		userToUpdate = new UpdateUserRequest("test5@testing.com", "TestUser5", "password", "username5");

		createdUser = new User("4", "test4@testing.com", "TestUser4", "password", "username4");
		updatedUser = new User("5", "test5@testing.com", "TestUser5", "password5", "username5");
		deletedUser = new User("6", "test6@testing.com", "TestUser6", "password", "username6");
	}

	@Test
	public void testListUsingGET1_WithoutPaginationDetails() throws Exception {
		when(userService.getAllUsers(null, null)).thenReturn(dummyUsers);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/users")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_UTF8);

		mockMvc.perform(request).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$.[0].id", equalTo("1")))
			.andExpect(jsonPath("$.[1].email", equalTo("test2@testing.com")))
			.andExpect(jsonPath("$.[2].name", equalTo("TestUser3")))
			.andReturn();
	}

	@Test
	public void testListUsingGET1_WithPaginationDetails() throws Exception {
		when(userService.getAllUsers(Mockito.anyInt(), Mockito.anyInt())).thenReturn(dummyUsers);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/users?page=3&size=10")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_UTF8);

		mockMvc.perform(request).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$.[0].id", equalTo("1")))
			.andExpect(jsonPath("$.[1].email", equalTo("test2@testing.com")))
			.andExpect(jsonPath("$.[2].name", equalTo("TestUser3")))
			.andReturn();
	}

	@Test
	public void testGetUsingGET_ExistingUser() throws Exception {
		when(userService.getUserById(Mockito.anyString())).thenReturn(dummyUsers.get(0));
		
		RequestBuilder request = MockMvcRequestBuilders.get("/users/1")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_UTF8);

		mockMvc.perform(request).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", equalTo("1")))
			.andExpect(jsonPath("$.email", equalTo("test1@testing.com")))
			.andExpect(jsonPath("$.name", equalTo("TestUser1")))
			.andExpect(jsonPath("$.password", equalTo("password")))
			.andExpect(jsonPath("$.username", equalTo("username1")));
	}

	@Test
	public void testGetUsingGET_NonExistentUser() throws Exception {
		when(userService.getUserById(Mockito.anyString())).thenThrow(UserNotFoundException.class);

		RequestBuilder request = MockMvcRequestBuilders.get("/users/15")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_UTF8);

		mockMvc.perform(request).andExpect(status().isNotFound())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.message", equalTo("User Not Found")));
	}

	@Test
	public void testCreateUsingPUT() throws Exception {
		when(userService.createUser(Mockito.any(CreateUserRequest.class))).thenReturn(createdUser);
		
		String userToCreateString = objectMapper.writeValueAsString(userToCreate);
		RequestBuilder request = MockMvcRequestBuilders.put("/users")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.content(userToCreateString);

		mockMvc.perform(request).andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(header().string("location", "http://localhost/users/4"))
			.andExpect(jsonPath("$.id", equalTo("4")))
			.andExpect(jsonPath("$.email", equalTo("test4@testing.com")))
			.andExpect(jsonPath("$.name", equalTo("TestUser4")))
			.andExpect(jsonPath("$.password", equalTo("password")))
			.andExpect(jsonPath("$.username", equalTo("username4")));
	}

	@Test
	public void testUpdateUsingPOST1_ExistingUser() throws Exception {
		when(userService.updateUser(Mockito.anyString(), Mockito.any(UpdateUserRequest.class))).thenReturn(updatedUser);
		
		String userToUpdateString = objectMapper.writeValueAsString(userToUpdate);
		RequestBuilder request = MockMvcRequestBuilders.post("/users/5")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.content(userToUpdateString);

		mockMvc.perform(request).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", equalTo("5")))
			.andExpect(jsonPath("$.email", equalTo("test5@testing.com")))
			.andExpect(jsonPath("$.name", equalTo("TestUser5")))
			.andExpect(jsonPath("$.password", equalTo("password5")))
			.andExpect(jsonPath("$.username", equalTo("username5")));
	}

	@Test
	public void testUpdateUsingPOST1_NonExistentUser() throws Exception {
		when(userService.updateUser(Mockito.anyString(), Mockito.any(UpdateUserRequest.class)))
			.thenThrow(UserNotFoundException.class);
		
		String userToUpdateString = objectMapper.writeValueAsString(userToUpdate);
		RequestBuilder request = MockMvcRequestBuilders.post("/users/15")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.content(userToUpdateString);

		mockMvc.perform(request).andExpect(status().isNotFound())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.message", equalTo("User Not Found")));
	}

	@Test
	public void testDeleteUsingDELETE_ExistingUser() throws Exception {
		when(userService.getUserById(Mockito.anyString())).thenReturn(deletedUser);
		when(userService.deleteUser(Mockito.anyString())).thenReturn(deletedUser);
		
		RequestBuilder request = MockMvcRequestBuilders.delete("/users/6")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_UTF8);

		mockMvc.perform(request).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", equalTo("6")))
			.andExpect(jsonPath("$.email", equalTo("test6@testing.com")))
			.andExpect(jsonPath("$.name", equalTo("TestUser6")))
			.andExpect(jsonPath("$.password", equalTo("password")))
			.andExpect(jsonPath("$.username", equalTo("username6")));
	}

	@Test
	public void testDeleteUsingDELETE_NonExistentUser() throws Exception {
		doThrow(UserNotFoundException.class).when(userService).deleteUser(Mockito.anyString());
		
		RequestBuilder request = MockMvcRequestBuilders.delete("/users/15")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_UTF8);

		mockMvc.perform(request).andExpect(status().isNotFound())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.message", equalTo("User Not Found")));
	}
}
