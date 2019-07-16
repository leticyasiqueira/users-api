package com.lsiqueira.user.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lsiqueira.user.entity.Authorities;
import com.lsiqueira.user.entity.Users;
import com.lsiqueira.user.exception.UserBadRequestException;
import com.lsiqueira.user.repository.AuthoritiesRepository;
import com.lsiqueira.user.repository.UserRepository;
import com.lsiqueira.user.utils.Password;

@RestController
@RequestMapping("/users")
public class UserController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthoritiesRepository authorityRepository;

	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody @Valid Users user) throws Exception {
		
		Optional<Users> searchUserName = userRepository.findByUsername(user.getUsername());
		
		if (searchUserName.isPresent()) {
			throw new UserBadRequestException("O usuário " + user.getUsername() + " já está cadastrado!");
		}
		
		Optional<Users> searchUserEmail = userRepository.findByEmail(user.getEmail());
		
		if(searchUserEmail.isPresent()) {
			throw new UserBadRequestException("O e-mail " + user.getEmail() + " já está cadastrado!");
		}
		
		Password pe = new Password();
		String passwordEncoder = pe.cryptPassword(user.getPassword());
		user.setPassword(passwordEncoder);
		user.setEnabled("true");
		
		Authorities authority =  new Authorities();
		authority.setUsername(user.getUsername());
		authority.setAuthority("ADMIN");
		
		Users savedUser = userRepository.save(user);
		authorityRepository.save(authority);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
	@GetMapping("/{email}/password")
	public ResponseEntity<?> getUser() {

			return new ResponseEntity<Iterable<Users>>(userRepository.findAll(), HttpStatus.OK);
		

	}

}
