package com.genie.services;

import com.genie.email.MailingPoint;
import com.genie.scheduling.jobs.SingularMailSenderJob;

/**
 * @author ccubukcu
 *
 */
public class MailingService {

	private static final String SUBJECT_SUFFIX = "subject.";
	private static final String BODY_SUFFIX = "body.";

	public static String getMailBody(MailingPoint type, Object...params) {
		return "";
//		return JsfUtil.getI18NMailLabel(BODY_SUFFIX + type.toString(), params);
	}
	
	public static String getMailSubject(MailingPoint type) {
		return "";
//		return JsfUtil.getI18NMailLabel(SUBJECT_SUFFIX + type.toString());
	}

	public static String getFooter() {
		return "";
//		return JsfUtil.getI18NMailLabel("footer");
	}
	
	
	public static boolean sendSingularMail(String uniqueJobName, String subject, String body, String to, String cc, String bcc) {
		return SingularMailSenderJob.scheduleJob(uniqueJobName, subject, body, to, cc, bcc);
	}
}
