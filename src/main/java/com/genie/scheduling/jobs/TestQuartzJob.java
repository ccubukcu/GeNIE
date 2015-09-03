package com.genie.scheduling.jobs;

import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.genie.email.Email;
import com.genie.scheduling.AbstractQuartzJob;
import com.genie.scheduling.QuartzScheduler;

/**
 * @author ccubukcu
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TestQuartzJob extends AbstractQuartzJob {
	private static String KEY = "testJobDataKey";
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobData = context.getJobDetail().getJobDataMap();
		Email test = (Email) jobData.get(KEY);
		
		System.out.println(test.getFrom());
		System.out.println(test.getTo());
		System.out.println("*********************");
	}
	
	
	public static boolean scheduleNewJob() {
		TestQuartzJob job = new TestQuartzJob();
		Email testEmail = new Email();
		testEmail.setFrom("testFrom@test.com");
		testEmail.setTo("testTo@test.com");
		
		job.insertDataToJobDataMap(KEY, testEmail);
		
		DateTime startDate = DateTime.now();
		startDate = startDate.secondOfMinute().addToCopy(100);
		job.configureScheduledSingleRun("testJob", "testJobGroup", startDate.toDate());
		
		return QuartzScheduler.scheduleJob(job, true);
	}
}
