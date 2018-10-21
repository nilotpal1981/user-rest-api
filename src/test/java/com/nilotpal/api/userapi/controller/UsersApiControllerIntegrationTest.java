package com.nilotpal.api.userapi.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nilotpal.api.userapi.model.CreateUserRequest;
import com.nilotpal.api.userapi.model.UpdateUserRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsersApiControllerIntegrationTest {
	private ObjectMapper objectMapper = new ObjectMapper();

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	HttpHeaders headers = new HttpHeaders();

	@Test
	public void testGetAllUsers_WithoutPaginationDetails() throws Exception {
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/users"), 
			HttpMethod.GET, entity, String.class);
		String responseString = response.getBody();
		JsonNode jsonNode = objectMapper.readTree(responseString);

		assertEquals(8, jsonNode.size());
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	@Test
	public void testGetAllUsers_WithPaginationDetails() throws Exception {
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/users?page=1&size=3"),
			HttpMethod.GET, entity, String.class);
		String responseString = response.getBody();
		JsonNode jsonNode = objectMapper.readTree(responseString);
		assertEquals(3, jsonNode.size());
	}

	@Test
	public void testGetUserById_ExistingUser() throws Exception {
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/users/10001"), 
			HttpMethod.GET, entity, String.class);
		String responseString = response.getBody();
		JsonNode jsonNode = objectMapper.readTree(responseString);

		assertEquals("10001", jsonNode.get("id").asText());
		assertEquals("a@test.com", jsonNode.get("email").asText());
		assertEquals("User1", jsonNode.get("name").asText());
	}

	@Test
	public void testGetUserById_NonExistentUser() throws Exception {
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/users/100012"), 
			HttpMethod.GET, entity, String.class);
		String responseString = response.getBody();
		JsonNode jsonNode = objectMapper.readTree(responseString);

		assertEquals("NOT_FOUND", jsonNode.get("status").asText());
		assertEquals("No user found with id: 100012", jsonNode.get("message").asText());
	}

	@Test
	public void testCreateUserAndDeleteUser() throws Exception {
		CreateUserRequest createdUser = new CreateUserRequest("test4@testing.com", 
			"TestUser4", "password", "username4");
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<CreateUserRequest> entity = new HttpEntity<CreateUserRequest>(createdUser, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/users"), 
			HttpMethod.PUT, entity, String.class);
		String responseString = response.getBody();
		JsonNode jsonNode = objectMapper.readTree(responseString);

		String createdUserId = jsonNode.get("id").asText();
		String createdUserURIString = response.getHeaders().getLocation().toString();
		
		assertNotNull(createdUserId);
		assertNotNull(createdUserURIString);
		assertEquals(createURLWithPort("/users/" + createdUserId), createdUserURIString);
		assertEquals("test4@testing.com", jsonNode.get("email").asText());
		assertEquals("TestUser4", jsonNode.get("name").asText());
		assertEquals("username4", jsonNode.get("username").asText());

		HttpEntity<String> entityToDelete = new HttpEntity<String>(null, headers);
		response = restTemplate.exchange(
			createURLWithPort("/users/" + createdUserId), 
			HttpMethod.DELETE, entityToDelete, String.class);
		responseString = response.getBody();
		jsonNode = objectMapper.readTree(responseString);

		assertEquals("test4@testing.com", jsonNode.get("email").asText());
		assertEquals("TestUser4", jsonNode.get("name").asText());
		assertEquals("username4", jsonNode.get("username").asText());
	}

	@Test
	public void testUpdateUser_ExistingUser() throws Exception {
		UpdateUserRequest userToUpdate = new UpdateUserRequest("test5@testing.com", 
			"TestUser5", "password", "username5");
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<UpdateUserRequest> entity = new HttpEntity<UpdateUserRequest>(userToUpdate, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/users/10002"), 
			HttpMethod.POST, entity, String.class);
		String responseString = response.getBody();
		JsonNode jsonNode = objectMapper.readTree(responseString);

		assertEquals("10002", jsonNode.get("id").asText());
		assertEquals("test5@testing.com", jsonNode.get("email").asText());
		assertEquals("TestUser5", jsonNode.get("name").asText());
		assertEquals("username5", jsonNode.get("username").asText());
	}

	@Test
	public void testUpdateUser_NonExistentUser() throws Exception {
		UpdateUserRequest userToUpdate = new UpdateUserRequest("test5@testing.com", 
			"TestUser5", "password", "username5");
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<UpdateUserRequest> entity = new HttpEntity<UpdateUserRequest>(userToUpdate, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/users/100012"), 
			HttpMethod.POST, entity, String.class);
		String responseString = response.getBody();
		JsonNode jsonNode = objectMapper.readTree(responseString);

		assertEquals("NOT_FOUND", jsonNode.get("status").asText());
		assertEquals("No user found with id: 100012", jsonNode.get("message").asText());
	}

	@Test
	public void testDeleteUser_NonExistentUser() throws Exception {
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/users/100012"), 
			HttpMethod.DELETE, entity, String.class);
		String responseString = response.getBody();
		JsonNode jsonNode = objectMapper.readTree(responseString);

		assertEquals("NOT_FOUND", jsonNode.get("status").asText());
		assertEquals("No user found with id: 100012", jsonNode.get("message").asText());
	}
}
