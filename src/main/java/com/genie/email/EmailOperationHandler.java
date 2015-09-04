package com.genie.email;

import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.mail.internet.MimeBodyPart;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class EmailOperationHandler extends SpringBeanAutowiringSupport {

	private static Logger logger = Logger.getLogger(EmailOperationHandler.class);
	
	private static String MAIL_FROM = "";
	private static EmailSender sender;
	
	public static void sendSingularMail(String subject, String body, String to, String cc, String bcc) {
		EmailOperationHandler handler = new EmailOperationHandler();
		String from = handler.getFromMail();
		Email email = handler.createEmail(from, to, cc, bcc, subject, body);
		try {
			if(sender == null) {
				initializeSender();
			}
			
			if(sender != null) {
				sender.send(email);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void initializeSender() {
		try {
			Resource resource = new ClassPathResource("/application.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			
			String host = props.getProperty("smtp.host");
			String user = props.getProperty("smtp.username");
			String pass = props.getProperty("smtp.password");
			String port = props.getProperty("smtp.port");
			
			if(host != null && !host.isEmpty() && user != null && !user.isEmpty()
					&& pass != null && !pass.isEmpty() && port != null && !port.isEmpty()) {
				Boolean ssl = Boolean.parseBoolean(props.getProperty("smtp.sslEnabled"));
				
				if(ssl != null)
					sender = new EmailSender(host, user, pass, port, ssl);
			}
		
			MAIL_FROM = props.getProperty("email.from_address");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Email createEmail(String from, String to, String cc, String bcc, String subject, String body) {
		Email email = new Email();
		
		try {
			Vector vBody = new Vector();
			MimeBodyPart textBody = new MimeBodyPart();
			textBody.setDataHandler(new DataHandler("<html><body>" + body + "</body></html>", "text/html; charset=utf-8"));
			vBody.add(textBody);
			email.setBody(vBody);
			email.setTo(to);
			email.setFrom(from);
			email.setCc(cc);
			email.setBcc(bcc);
			email.setSubject(subject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return email;
	}	
	
	public String getFromMail() {
		return MAIL_FROM;
	}

	public static EmailSender getSender() {
		return sender;
	}
	
}
