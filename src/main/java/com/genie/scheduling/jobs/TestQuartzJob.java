package com.genie.scheduling.jobs;

import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.genie.email.MailUser;
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
		MailUser test = (MailUser) jobData.get(KEY);
		
		System.out.println(test.getEmail());
		System.out.println(test.isCc());
		System.out.println("*********************");
	}
	
	
	public static boolean scheduleNewJob() {
		TestQuartzJob job = new TestQuartzJob();
		MailUser user = new MailUser("aerogelius@gmail.com", true);
		
		job.insertDataToJobDataMap(KEY, user);
		
		DateTime startDate = DateTime.now();
		startDate = startDate.secondOfMinute().addToCopy(100);
		job.configureScheduledSingleRun("testJob", "testJobGroup", startDate.toDate());
		
		return QuartzScheduler.scheduleJob(job, true);
	}
}
