package com.nilotpal.api.userapi.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nilotpal.api.userapi.exceptions.UserNotFoundException;
import com.nilotpal.api.userapi.model.CreateUserRequest;
import com.nilotpal.api.userapi.model.UpdateUserRequest;
import com.nilotpal.api.userapi.model.User;
import com.nilotpal.api.userapi.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "users", description = "the users API")
@RestController
@RequestMapping(value = "/users", produces = { "application/json;charset=UTF-8" }, consumes = { "application/json" })
public class UsersApiController {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "list all users", nickname = "listUsingGET1", notes = "Multiple status values can be provided with comma separated strings", response = User.class, responseContainer = "List", tags = {
			"User", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = User.class, responseContainer = "List") })
	@GetMapping
	public ResponseEntity<List<User>> listUsingGET1(
			@ApiParam(value = "page") @Valid @RequestParam(value = "page", required = false) Integer page,
			@ApiParam(value = "size") @Valid @RequestParam(value = "size", required = false) Integer size) {
		List<User> body = userService.getAllUsers(page, size);
		return ResponseEntity.ok(body);
	}

	@ApiOperation(value = "Register a new user", nickname = "createUsingPUT", notes = "", response = User.class, tags = {
			"User" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = User.class) })
	@PutMapping
	public ResponseEntity<User> createUsingPUT(
			@ApiParam(value = "request", required = true) @Valid @RequestBody CreateUserRequest createUserRequest) {
		User createdUser = userService.createUser(createUserRequest);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(createdUser.getId()).toUri();
		return ResponseEntity.created(location).body(createdUser);
	}

	@ApiOperation(value = "Delete a user", nickname = "deleteUsingDELETE", notes = "", response = User.class, tags = {
			"User", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = User.class) })
	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUsingDELETE(
			@ApiParam(value = "id", required = true) @PathVariable("id") String id) throws UserNotFoundException {
		// TODO: Handle id type check here..
		User user = userService.deleteUser(id);
		return ResponseEntity.ok(user);
	}

	@ApiOperation(value = "Find a user via id", nickname = "getUsingGET", notes = "", response = User.class, tags = {
			"User", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = User.class) })
	@GetMapping("/{id}")
	public ResponseEntity<User> getUsingGET(@ApiParam(value = "id", required = true) @PathVariable("id") String id)
			throws UserNotFoundException {
		// TODO: Handle id type check here..
		User user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	@ApiOperation(value = "Update a user", nickname = "updateUsingPOST1", notes = "", response = User.class, tags = {
			"User", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = User.class) })
	@PostMapping("/{id}")
	public ResponseEntity<User> updateUsingPOST1(@ApiParam(value = "id", required = true) @PathVariable("id") String id,
			@ApiParam(value = "request", required = true) @Valid @RequestBody UpdateUserRequest updateUserRequest)
			throws UserNotFoundException {
		// TODO: Handle id type check here..
		User updatedUser = userService.updateUser(id, updateUserRequest);
		return ResponseEntity.ok(updatedUser);
	}
}
