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

	
	public void sendVerificationEmailMessage(final AppUser user, final String mailSubject) {
		final String recipientAddress = user.getEmail();
		final String subject = mailSubject;
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText("Token is " + user.getToken().getToken());
		email.setFrom(env.getProperty("support.email"));
		mailSender.send(email);
	}
	
}
