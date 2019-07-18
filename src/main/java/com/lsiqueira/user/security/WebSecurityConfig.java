package com.lsiqueira.user.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Classe de cponfiguração que restringe o acesso aos recursos
 * 
 * Nessa classe definimos que todos podem acessar /info, e que o endpoint /token está disponível apenas via 
 * requisições do tipo POST para a geração do token. Para todas as demais rotas a autenticação é necessária.
 * 
 * Essa classe tambem define o usuário e a senha de quem tem permissao para gerar o token 
 * 
 * @author leticya.siqueira
 *
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	
	private static final String[] AUTH_WHITELIST = {
	        "/swagger-resources/**",
	        "/swagger-ui.html",
	        "/v2/api-docs",
	        "/webjars/**"
	};

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeRequests()
			.antMatchers("/info").permitAll()
			.antMatchers(AUTH_WHITELIST).permitAll()
			.antMatchers(HttpMethod.POST, "/users").permitAll()
			.antMatchers(HttpMethod.GET, "/users/password/{username}").permitAll()
			.antMatchers(HttpMethod.PATCH, "/users/password/{id}").permitAll()
			.antMatchers(HttpMethod.POST, "/token").permitAll()
			.anyRequest().authenticated()
			.and()
			
			// filtra requisições de login
			.addFilterBefore(new JWTFilter("/token", authenticationManager()),
	                UsernamePasswordAuthenticationFilter.class)
			
			// filtra outras requisições para verificar a presença do JWT no header
			.addFilterBefore(new JWTAuthenticationFilter(),
	                UsernamePasswordAuthenticationFilter.class);
	}
	

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		//pega informações do banco 
		 auth.jdbcAuthentication().dataSource(dataSource)
         .passwordEncoder(new BCryptPasswordEncoder())
         .usersByUsernameQuery("SELECT username,password,enabled FROM Users where username=?")
         .authoritiesByUsernameQuery("SELECT username,authority FROM Authorities where username=?");

		 
	}
	
	
	
	
}
