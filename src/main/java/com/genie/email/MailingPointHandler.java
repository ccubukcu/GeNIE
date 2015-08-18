package com.genie.email;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.genie.dao.UserDAO;
import com.genie.services.MailingService;
import com.genie.utils.DataFormatter;

public class MailingPointHandler extends SpringBeanAutowiringSupport {

	private static Logger logger = Logger.getLogger(MailingPointHandler.class);
	
	private static String MAIL_FROM = "";
	private static EmailSender sender;

	@Autowired
	private UserDAO userDAO;
	
	public static boolean sendAttachedMail(long mailingPointId, long unitId, HSSFWorkbook wb, String filename, Object... params) {
		MailingPointHandler handler = new MailingPointHandler();
		if(wb == null || filename == null) {
			return false;
		}
		return handler.scheduledMailSender(mailingPointId, unitId, wb, filename, params);
	}
	
	public static boolean sendScheduledMail(long mailingPointId, long unitId, Object... params) {
		MailingPointHandler handler = new MailingPointHandler();
		return handler.scheduledMailSender(mailingPointId, unitId, null, "", params);
	}
	
	public boolean scheduledMailSender(long mailingPointId, long unitId, HSSFWorkbook wb, String filename, Object... params) {
		boolean mailingSuccessful = false;
		
		List<MailUser> mailUsers = null;
		log("Collecting mail user list.");
		
		mailUsers = new ArrayList<MailUser>();
		boolean isPending = false;
		if(params.length > 2) {
			isPending = (Boolean) params[2];
		}
		mailUsers = getMailUsers(mailingPointId, unitId, isPending, params);
		
		// test icin
		if(mailUsers == null) {
			mailUsers = new ArrayList<MailUser>();
		}
		
		if(mailUsers == null || (mailUsers != null && mailUsers.isEmpty())) {
			log("No users found for reminder mail sending. Returning.");
			return false;
		}
		
		try {
			log("Creating Email object.");
			Email email = new Email();
			String ccString = "";
			String toString = "";
			String subject = MailingService.getMailSubject(MailingPoint.getObject(mailingPointId));
			String body = "";
//			String body = getMailBody(mailingPointId, unitId, stationRequest, params);
			
			log("Sending reminder mails to gathered user list.");
			for (MailUser mailUser : mailUsers) {
//				if(EMailAddressValidator.isValidAddress(mailUser.getEmail())) {
					if(mailUser.isCc()) {
						ccString += mailUser.getEmail() + ", ";
					} else {
						toString += mailUser.getEmail() + ", ";
					}
//				}
			}
			
			body += "<br />" + MailingService.getFooter();
			
			if(wb != null)
				email = createEmail(MAIL_FROM, toString, ccString, "", subject, body, DataFormatter.convertTrChars(filename), wb);
			else {
				email = createEmail(MAIL_FROM, toString, ccString, "", subject, body);
			}
			
			mailingSuccessful = sender.send(email);
			
			log("Reminder mails were succesfully sent.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mailingSuccessful;
	}
	
	private List<MailUser> getMailUsers(long mailingPointId, long unitId, boolean isPending, Object...params) {
		List<MailUser> mailUsers = new ArrayList<MailUser>();
		
//		mailUsers =  userDAO.getAll();
		
		return mailUsers;
	}
	
	public String getMailBody(long mailingPointId, Object...params) {
		String body = "";
		body = MailingService.getMailBody(MailingPoint.getObject(mailingPointId));
		return body;
	}
	
	public static void sendSingularMail(String subject, String body, String to, String cc, String bcc) {
		MailingPointHandler handler = new MailingPointHandler();
		String from = handler.getFromMail();
		Email email = handler.createEmail(from, to, cc, bcc, subject, body);
		try {
			sender.send(email);
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
	
	public Email createEmail(String from, String to, String cc, String bcc, String subject, String body, String filename, HSSFWorkbook workbook) {
		Email email = new Email();
		
		try {
			Vector vBody = new Vector();
			MimeBodyPart textBody = new MimeBodyPart();
			textBody.setDataHandler(new DataHandler("<html><body>" + body + "</body></html>", "text/html; charset=utf-8"));
			vBody.add(textBody);
			email.addBody(textBody);			
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			workbook.write(bos);
			ByteArrayDataSource ds = new ByteArrayDataSource(bos.toByteArray(), "application/vnd.ms-excel");

			MimeBodyPart excelPart = new MimeBodyPart();
			excelPart.setDataHandler(new DataHandler(ds));
			excelPart.setFileName(filename);
			email.addAttachment(excelPart);
			
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
	
	private static void log(String line) {
		if(logger.isDebugEnabled())
			logger.debug(line);
	}
	
	public String getFromMail() {
		return MAIL_FROM;
	}

	public static EmailSender getSender() {
		return sender;
	}
	
}
