package com.springrestsecuritycoreboilerplate.mail;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.springrestsecuritycoreboilerplate.user.AppUser;

@Service
public class Mailer implements Serializable{
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageSource messages;
	
	@Autowired
	private Environment env;

	
	public void sendRegistrationEmailMessage(final AppUser user, final String token) {
		final String recipientAddress = user.getEmail();
		final String subject = "Registration Confirmation";
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText("Token is " + token);
		email.setFrom(env.getProperty("support.email"));
		mailSender.send(email);
	}
	
}
