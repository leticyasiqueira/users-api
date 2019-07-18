package com.lsiqueira.user.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(value="Users", description="Classe modelo que representa o usuário")
public class Users {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonIgnore
	private long id;
	
	@NotBlank(message = "{username.not.blank}")
	@Email
	@ApiModelProperty(notes = "Email do usuário", required = true)
	private String username;
	
	@NotBlank(message = "{password.not.blank}")
	@ApiModelProperty(notes = "Senha do usuário", required = true)
	private String password;

	
	@NotBlank(message = "{applicationAccess.not.blank}")
	@ApiModelProperty(notes = "Sistema origem do cadastro", required = true)
	private String applicationAccess;
	
	@JsonIgnore
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
