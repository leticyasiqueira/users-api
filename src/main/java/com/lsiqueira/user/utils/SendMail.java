package com.lsiqueira.user.utils;

import java.io.Serializable;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Component;

@Component
public class SendMail implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	public String sendEmail(String titulo, String destinatario, String assunto) {
		
		
		try {
			HtmlEmail htmlEmail = new HtmlEmail();
			htmlEmail.setHtmlMsg(titulo);
			htmlEmail.addTo(destinatario);
			htmlEmail.setSubject(assunto);
			htmlEmail.setFrom("teamvanessarodrigues@gmail.com");
			htmlEmail.setHostName("smtp.gmail.com");
			htmlEmail.setSmtpPort(587);
			htmlEmail.setTLS(true);
			htmlEmail.setSSL(true);
			htmlEmail.setAuthenticator(new DefaultAuthenticator("teamvanessarodrigues@gmail.com", "n3wsdh03"));
			htmlEmail.send();
			return "Email Enviado";
		} catch (EmailException e) {
			e.printStackTrace();
			return "Não foi possivel recuperar a senha. Tente novamente mais tarde!";
			
		}

	}

}
