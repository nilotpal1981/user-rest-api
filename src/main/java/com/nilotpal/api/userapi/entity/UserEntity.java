package com.nilotpal.api.userapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;

import com.nilotpal.api.userapi.model.CreateUserRequest;
import com.nilotpal.api.userapi.model.UpdateUserRequest;

@Entity
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String email;
	private String name;
	private String password;
	private String username;

	public UserEntity() {
	}

	public UserEntity(final String email, final String name, final String password, final String username) {
		super();
		this.email = email;
		this.name = name;
		this.password = password;
		this.username = username;
	}

	public UserEntity(@Valid CreateUserRequest createUserRequest) {
		this.email = createUserRequest.getEmail();
		this.name = createUserRequest.getName();
		this.password = createUserRequest.getPassword();
		this.username = createUserRequest.getUsername();
	}

	public UserEntity merge(@Valid UpdateUserRequest updateUserRequest) {
		this.email = updateUserRequest.getEmail();
		this.name = updateUserRequest.getName();
		this.password = updateUserRequest.getPassword();
		this.username = updateUserRequest.getUsername();
		return this;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", email=" + email + ", name=" + name + ", username=" + username + "]";
	}
}
