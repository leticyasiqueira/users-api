package com.lsiqueira.planet.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lsiqueira.user.UserApiApplication;

/**
 * CLASSE DE CONFIGURAÇÃO PARA LEVANTAR O CONTEINER DO SPRING BOOT 
 * PARA EXECUÇÃO DOS TESTES DE INTEGRAÇÃO
 * 
 * @author leticya.siqueira
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApiApplication.class)
public class ApplicationTests {
	
	@Test
	public void contextLoads() {
	}

}
