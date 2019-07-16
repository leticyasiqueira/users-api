package com.lsiqueira.user.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Users {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotBlank(message = "{username.not.blank}")
	private String username;
	
	@NotBlank(message = "{password.not.blank}")
	private String password;
	
	@Email
	@NotBlank(message = "{email.not.blank}")
	private String email;
	
	@NotBlank(message = "{applicationAccess.not.blank}")
	private String applicationAccess;
	
	private String enabled;
	
	public Users() {
		// Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getApplicationAccess() {
		return applicationAccess;
	}

	public void setApplicationAccess(String applicationAccess) {
		this.applicationAccess = applicationAccess;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
	}
	
	
	
	
}
