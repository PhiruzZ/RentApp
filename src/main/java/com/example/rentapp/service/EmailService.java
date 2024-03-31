package com.example.rentapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

	@Value("${spring.mail.username}")
	private String emailFrom;

	private final JavaMailSender mailSender;

	public void sendMail(String mailText, String targetEmail) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
			helper.setTo(targetEmail);
			helper.setFrom(emailFrom);
			helper.setText(mailText, true);
			helper.setSubject("Fraud Attempt");
			mailSender.send(message);
		} catch (MessagingException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send email!");
		}

	}

}
