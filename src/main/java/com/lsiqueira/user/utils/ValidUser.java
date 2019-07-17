package com.lsiqueira.user.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lsiqueira.user.entity.Users;
import com.lsiqueira.user.exception.UserBadRequestException;
import com.lsiqueira.user.exception.UserNotFoundException;
import com.lsiqueira.user.repository.UserRepository;

@Service
public class ValidUser {

	@Autowired
	private UserRepository userRepository;

	public void validCreateUser(Users user) {
		Optional<Users> searchUserName = userRepository.findByUsername(user.getUsername());

		if (searchUserName.isPresent()) {
			throw new UserBadRequestException("O usuário " + user.getUsername() + " já está cadastrado!");
		}

	
	}

	public Users validUserForReset(Users user, long id) {

		if (user.getUsername() == null) {
			throw new UserBadRequestException("O campo Usuário é obrigatórios!.");
		}

		Users userCadastrado = userRepository.findById(id);

		if (userCadastrado == null) {
			throw new UserNotFoundException("Id não encontrado!");

		} else if (!userCadastrado.getUsername().equals(user.getUsername())) {
			throw new UserBadRequestException("O usuario não pertente ao ID informado.");
		}

		return userCadastrado;

	}
	
	public Users validUserForNew(Users user, long id) {

		if (user.getUsername() == null || user.getPassword() == null) {
			throw new UserBadRequestException("Os campos Usuário e Senha são obrigatórios!.");
		}

		Users userCadastrado = userRepository.findById(id);

		if (userCadastrado == null) {
			throw new UserNotFoundException("Id não encontrado!");

		} else if (!userCadastrado.getUsername().equals(user.getUsername())) {
			throw new UserBadRequestException("O usuario não pertente ao ID informado.");
		}

		return userCadastrado;

	}
}
