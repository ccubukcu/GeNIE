package com.genie.services;

import com.genie.scheduling.jobs.SingularMailSenderJob;

/**
 * @author ccubukcu
 *
 */
public class MailingService {
	public static boolean sendSingularMail(String uniqueJobName, String subject, String body, String to, String cc, String bcc) {
		return SingularMailSenderJob.scheduleJob(uniqueJobName, subject, body, to, cc, bcc);
	}
}
