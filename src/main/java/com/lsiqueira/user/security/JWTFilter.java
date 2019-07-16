package com.lsiqueira.user.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe responsável por interceptar as requisições do tipo POST feitas em /token e tentar autenticar o usuário. 
 * Quando o usuário for autenticado com sucesso, um método irá retornar um JWT com a autorização Authorization no cabeçalho da resposta.
 * 
 * o método attemptAuthentication é quem lida com a tentativa de autenticação. 
 * Pegamos o usernamee password da requisição, e utilizamos o AuthenticationManager para verificar se os dados são 
 * correspondentes aos dados do nosso usuário existente. Caso os dados estejam corretos, invocamos 
 * o método successfulAuthentication para enviar ao service TokenAuthenticationService o username 
 * do usuário para que este service adicione um JWT à nossa resposta (response).
 * 
 * @author leticya.siqueira
 *
 */
public class JWTFilter extends AbstractAuthenticationProcessingFilter {

	protected JWTFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		AccountCredentials credentials = new ObjectMapper().readValue(request.getInputStream(),
				AccountCredentials.class);

		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
				credentials.getUsername(), credentials.getPassword(), Collections.emptyList()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain, Authentication auth) throws IOException, ServletException {

		TokenAuthenticationService.addAuthentication(response, auth.getName());

	}

}
