package com.genie.scheduling.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.genie.email.EmailOperationHandler;
import com.genie.scheduling.AbstractQuartzJob;
import com.genie.scheduling.QuartzScheduler;

/**
 * @author ccubukcu
 *
 */
public class SingularMailSenderJob extends AbstractQuartzJob{
	
	public static final String SUBJECT_KEY = "mail_subject";
	public static final String BODY_KEY = "mail_body";
	public static final String TO_KEY = "mail_to";
	public static final String CC_KEY = "mail_cc";
	public static final String BCC_KEY = "mail_bcc";

	public static final String JOB_PREFIX = "prefix_";
	public static final String TRIGGER_PREFIX = "prefix_";
	public static final String JOB_GROUP  = "job_group";
	public static final String TRIGGER_GROUP  = "trigger_Group";
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobData = context.getJobDetail().getJobDataMap();
		
		String subject = jobData.getString(SUBJECT_KEY);
		String body = jobData.getString(BODY_KEY);
		String to = jobData.getString(TO_KEY);
		String cc = jobData.getString(CC_KEY);
		String bcc = jobData.getString(BCC_KEY);
		
		EmailOperationHandler.sendSingularMail(subject, body, to, cc, bcc);
	}
	
	public static boolean scheduleJob(String uniqueJobName, String subject, String body, String to, String cc, String bcc) {
		SingularMailSenderJob sms = new SingularMailSenderJob();
		
		String jobName = JOB_PREFIX + uniqueJobName;
		String triggerName = TRIGGER_PREFIX + uniqueJobName;
		
		sms.createTriggerKey(triggerName, TRIGGER_GROUP);
		sms.configureImmediateSingleRun(jobName, JOB_GROUP);
		sms.insertDataToJobDataMap(SUBJECT_KEY, subject);
		sms.insertDataToJobDataMap(BODY_KEY, body);
		sms.insertDataToJobDataMap(TO_KEY, to);
		sms.insertDataToJobDataMap(CC_KEY, cc);
		sms.insertDataToJobDataMap(BCC_KEY, bcc);
		
		return QuartzScheduler.scheduleJob(sms, true);
	}
}
