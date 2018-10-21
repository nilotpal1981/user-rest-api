package com.nilotpal.api.userapi.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModelProperty;

@Validated
public class CreateUserRequest   {
	private String email;
	private String name;
	private String password;
	private String username;

	public CreateUserRequest(String email, String name, String password, String username) {
		super();
		this.email = email;
		this.name = name;
		this.password = password;
		this.username = username;
	}

	@ApiModelProperty(value = "Email id of the new user")
	public String getEmail() {
		return email;
	}

	@ApiModelProperty(value = "Name of the new user")
	public String getName() {
		return name;
	}

	@ApiModelProperty(value = "Login password of the new user")
	public String getPassword() {
		return password;
	}

	@ApiModelProperty(value = "Login username of the new user")
	public String getUsername() {
		return username;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return Objects.equals(this.email, user.getEmail()) 
			&& Objects.equals(this.name, user.getName()) 
			&& Objects.equals(this.password, user.getPassword())
			&& Objects.equals(this.username, user.getUsername());
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, name, password, username);
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", name=" + name + ", username=" + username + "]";
	}
}

