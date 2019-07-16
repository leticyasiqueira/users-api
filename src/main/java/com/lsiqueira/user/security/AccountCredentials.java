package com.lsiqueira.user.security;

/**
 * Utilizada para enviarmos as credenciais da conta a ser validada quando fizermos requisições do tipo POST à URL /token
 * 
 * @author leticya.siqueira
 *
 */

public class AccountCredentials {
	
	private String username;
	private String password;
	
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
}
