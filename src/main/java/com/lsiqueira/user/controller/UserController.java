package com.lsiqueira.user.controller;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lsiqueira.user.entity.Authorities;
import com.lsiqueira.user.entity.Users;
import com.lsiqueira.user.exception.UserNotFoundException;
import com.lsiqueira.user.repository.AuthoritiesRepository;
import com.lsiqueira.user.repository.UserRepository;
import com.lsiqueira.user.utils.Password;
import com.lsiqueira.user.utils.ValidUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ResponseHeader;

@RestController
@RequestMapping("/users")
@Api(basePath = "/users", produces = "application/json", tags = "users", description = "Usuário Resource")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthoritiesRepository authorityRepository;

	@Autowired
	private ValidUser valid;

//	@Autowired
//	private SendMail mail;

	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Cadastra um usuário", notes = "Cadastra um usuário")
	@ApiResponses({ @ApiResponse(code = 201, message = "CREATED", responseHeaders = {
			@ResponseHeader(name = HttpHeaders.LOCATION, response = String.class, description = "Mapeamento do recurso criado") }),
			@ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 404, message = "Not Found") })
	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody @Valid Users user) throws Exception {

		valid.validCreateUser(user);

		Password pe = new Password();
		String passwordEncoder = pe.cryptPassword(user.getPassword());
		user.setPassword(passwordEncoder);
		user.setEnabled("true");

		Authorities authority = new Authorities();
		authority.setUsername(user.getUsername());
		authority.setAuthority("USER");

		Users savedUser = userRepository.save(user);
		authorityRepository.save(authority);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}

	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Consulta um usuário", notes = "Consulta um usuário por email", response = Users.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "OK", response = String.class),
			@ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 404, message = "Not Found") })
	@GetMapping("/{username}/password")
	public String getUserEmail(@PathVariable String username) throws JSONException {

		Optional<Users> user = userRepository.findByUsername(username);

		if (!user.isPresent()) {
			throw new UserNotFoundException("Email não encontrado!");
		}

		JSONObject json = new JSONObject();
		json.put("id", user.get().getId());
		json.put("username", user.get().getUsername());
		String json_string = json.toString();

		return json_string;

	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Reseta senha do usuário", notes = "Reseta senha do usuário")
	@ApiResponses({ @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found") })
	@PatchMapping("/{id}/password")
	public ResponseEntity<Void> resetPassword(@RequestBody Users user, @PathVariable long id) throws EmailException {

		Users userCadastrado = valid.validUserForReset(user, id);

		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString();
		Password pe = new Password();
		String passwordEncoder = pe.cryptPassword(myRandom.substring(0, 8));

		userCadastrado.setId(id);
		userCadastrado.setPassword(passwordEncoder);

		System.out.println(myRandom.substring(0, 8));

//		String resposta = mail.sendEmail("Sua nova senha é: " + myRandom, userCadastrado.getUsername(),
//				"Team Vaness Rodrigues - Reset de Senha");
//
//		if (!resposta.equals("Email Enviado")) {
//			throw new EmailException("Não foi possivel recuperar a senha. Tente novamente mais tarde!");
//		}

		userRepository.save(userCadastrado);

		return ResponseEntity.noContent().build();

	}

	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Consulta um usuário por ID", notes = "Consulta um usuário por ID", response = Users.class, authorizations = {
			@Authorization(value = "apiKey") })
	@ApiResponses({ @ApiResponse(code = 200, message = "OK", response = Users.class),
			@ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 404, message = "Not Found") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header") })
	@GetMapping("{id}")
	public ResponseEntity<Users> getUserId(@PathVariable long id) {

		Users user = userRepository.findById(id);

		if (user == null) {
			throw new UserNotFoundException("Id não encontrado!");
		}

		user.setPassword(null);

		return new ResponseEntity<Users>(user, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Cadastra nova senha", notes = "Cadastra nova senha para o usuário", authorizations = {
			@Authorization(value = "apiKey") })
	@ApiResponses({ @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header") })
	@PatchMapping("{id}")
	public ResponseEntity<Void> newPassword(@RequestBody Users user, @PathVariable long id) throws EmailException {

		Users userCadastrado = valid.validUserForNew(user, id);

		Password pe = new Password();
		String passwordEncoder = pe.cryptPassword(user.getPassword());

		userCadastrado.setId(id);
		userCadastrado.setPassword(passwordEncoder);
		userRepository.save(userCadastrado);

		return ResponseEntity.noContent().build();

	}

}
