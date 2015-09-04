package com.genie.email;

import java.util.Vector;

import javax.activation.DataHandler;
import javax.mail.internet.MimeBodyPart;

/**
 * @author ccubukcu
 *
 */
public class Email {

	private String to = null;

	private String bcc = null;

	private String cc = null;

	private String from = null;

	private String subject = null;

	private Vector body = new Vector();

	private Vector attachment = new Vector();

	/**
	 * Email constructor comment.
	 */
	public Email() {
		super();
	}

	public void addAttachment(MimeBodyPart mimeBodyPart) throws Exception {
		getAttachment().addElement(mimeBodyPart);
	}

	public void addBody(MimeBodyPart mimeBodyPart) throws Exception {
		getBody().addElement(mimeBodyPart);
	}

	public void addHtmlAttachment(Object content) throws Exception {
		addAttachment(getHtmlBody(content));
	}

	public void addHtmlBody(Object content) throws Exception {
		addBody(getHtmlBody(content));
	}

	public void addTextAttachment(String content) throws Exception {
		addAttachment(getTextBody(content));
	}

	public void addTextBody(String content) throws Exception {
		addBody(getTextBody(content));
	}

	public java.util.Vector getAttachment() {
		return attachment;
	}

	public java.lang.String getBcc() {
		return bcc;
	}

	public java.util.Vector getBody() {
		return body;
	}

	public java.lang.String getCc() {
		return cc;
	}

	public java.lang.String getFrom() {
		return from;
	}

	public MimeBodyPart getHtmlBody(Object content) throws Exception {
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setDataHandler(new DataHandler(content,
				"text/html; charset=utf-8"));
		return mimeBodyPart;
	}

	public java.lang.String getSubject() {
		return subject;
	}

	public MimeBodyPart getTextBody(String content) throws Exception {
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setText(content, "utf-8");
		return mimeBodyPart;
	}

	public java.lang.String getTo() {
		return to;
	}

	public void newMethod() {
	}

	public void setAttachment(java.util.Vector newAttachment) {
		attachment = newAttachment;
	}

	public void setBcc(java.lang.String newBcc) {
		bcc = newBcc;
	}

	public void setBody(java.util.Vector newBody) {
		body = newBody;
	}

	public void setCc(java.lang.String newCc) {
		cc = newCc;
	}

	public void setFrom(java.lang.String newFrom) {
		from = newFrom;
	}

	public void setSubject(java.lang.String newSubject) {
		subject = newSubject;
	}

	public void setTo(java.lang.String newTo) {
		to = newTo;
	}

	public String toString() {
		return "Subject=" + getSubject() + " From=" + getFrom() + " To="
				+ getTo();
	}

}