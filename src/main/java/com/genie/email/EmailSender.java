package com.genie.email;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author ccubukcu
 *
 */
public final class EmailSender {
	private static String smtpHost = "";
	private static String user = "";
	private static String password = "";
	
	public EmailSender(String _smtpHost, String _user, String _password) {
		smtpHost = _smtpHost;
		user = _user;
		password = _password;
	}
	
	public java.lang.String getSmtpHost() {
		return smtpHost;
	}
	
	/**
	 * Sets the smtpHost
	 * @param newSmtpHost The smtpHost to set
	 */
	public static void setSmtpHost(String newSmtpHost) {
		smtpHost = newSmtpHost;
	}
	
	/**
	 * Send Email 
	 * @param email
	 */
    @SuppressWarnings("rawtypes")
	public boolean send(Email email) throws Exception {
		boolean successful = false;
	    try {
	        Properties props = new Properties();
	        if (smtpHost != null) {
	        	props.put("mail.smtp.host", smtpHost);
	        }
	        //Authentication if required
	        Session session = null;
			Authenticator authenticator = null;
			if (user!=null && password!=null )
			{
				props.put("mail.smtp.auth", "true");
				authenticator = new Authenticator()
				{
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(user, password);
					}
				};
					session = javax.mail.Session.getInstance(props, authenticator);
			}  else{
		           session = Session.getDefaultInstance(props, null);
		    }

			//session.setDebug(true);
	        MimeMessage mimeMessage = new MimeMessage(session);
	
	        String toEmail = email.getTo();
	        String fromEmail = email.getFrom();
	        String sendCC = email.getCc();
	        String sendBCC = email.getBcc();
	
	
	        if ((fromEmail != null)) {
	        	if((!fromEmail.equals(""))) {
	        		mimeMessage.setFrom(new InternetAddress(fromEmail));
	        	}
	        } else {
	            mimeMessage.setFrom();
	        }
	
	
	        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	
	
	        if (!((sendCC == null) || (sendCC.equals("")))) {
	            mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(sendCC, false));
	        }
	        if (!((sendBCC == null) || (sendBCC.equals(""))))
	            mimeMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(sendBCC, false));
	
	
	        mimeMessage.setSubject(email.getSubject(), "utf-8");
	
	
	        MimeMultipart mimeMultipart = new MimeMultipart();
			Enumeration bodyEnum = email.getBody().elements();
	        
	        while (bodyEnum.hasMoreElements()) {
	            MimeBodyPart mimeBodyPart = (MimeBodyPart) bodyEnum.nextElement();
	            mimeMultipart.addBodyPart(mimeBodyPart);
	        }
	        Enumeration attachEnum = email.getAttachment().elements();
	        while (attachEnum.hasMoreElements()) {
	            MimeBodyPart mimeBodyPart = (MimeBodyPart) attachEnum.nextElement();
	            mimeMultipart.addBodyPart(mimeBodyPart);
	        }
	        
	        mimeMessage.setContent(mimeMultipart);
	        mimeMessage.setSentDate(new Date());
	        //System.out.println("EmailSenderin icindeyiz Transport.send() den once");
	        Transport.send(mimeMessage);
	        successful = true;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw e;
	    }
	    return successful;
	}
	
	public boolean sendEmailByAuthority() {
		boolean success = false;
		
		return success;
	}
}