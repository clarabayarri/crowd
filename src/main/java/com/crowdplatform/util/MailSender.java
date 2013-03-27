package com.crowdplatform.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.crowdplatform.model.PlatformUser;

public class MailSender {

	private JavaMailSender mailSender;
	private SimpleMailMessage simpleMailMessage;

	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void sendPasswordResetMail(PlatformUser user, Long uid) {
		sendMail(user.getUsername(), user.getEmail(), String.valueOf(uid));
	}

	private void sendMail(String dear, String address, String content) {

		MimeMessage message = mailSender.createMimeMessage();

		try{
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(simpleMailMessage.getFrom());
			helper.setTo(address);
			helper.setSubject(simpleMailMessage.getSubject());
			helper.setText(String.format(
					simpleMailMessage.getText(), dear, content));

		}catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);
	}

}
