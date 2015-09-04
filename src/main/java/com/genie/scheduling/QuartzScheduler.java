package com.genie.scheduling;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.matchers.GroupMatcher;

/**
 * @author ccubukcu
 *
 */
public class QuartzScheduler {
	 
	private static Logger logger = Logger.getLogger(QuartzScheduler.class);
	
	private static StdScheduler stdScheduler;

	public QuartzScheduler(StdScheduler scheduler)
	{
		stdScheduler = scheduler;
		
		startScheduler();
	}
	
	/**
	 * Scheduler eğer çalışmıyorsa onu çalıştırır. 
	 * */
	public static void startScheduler()
	{
		try {
			if(stdScheduler != null)
			{
				if(!stdScheduler.isStarted())
				{
					stdScheduler.start();
					logger.info("Scheduler successfully started");
				}
				else logger.info("Scheduler already started.");
			}
			else logger.error("Scheduler was not initialized.");
		} catch (SchedulerException e) {
			logger.error("Exception while trying to start the scheduler.");
			e.printStackTrace();
		}
	}
	
	/**
	 * AbstractQuartzJob'dan türemiş bütün classların scheduler'a eklenmesini sağlar
	 * @param job Eklenecek job
	 * @return Başarıyla schedule edilirse true değilse false
	 * */
	public static boolean scheduleJob(AbstractQuartzJob job, boolean replaceIfExists)
	{
		boolean schedulingSuccessful = false;
		
		String jobName = "null";
		if(job.getJobName() != null)
			jobName = job.getJobName();
		
		String jobGroup = "";
		if(job.getJobGroup() != null)
			jobGroup = job.getJobGroup();
		
		//Tekrar aralığı
		int interval = 1;
		if(job.getJobRepeatIntervalInSeconds() > 1)
			interval = job.getJobRepeatIntervalInSeconds();
		
		//Tekrar sayısı
		int repeatCount = 0;
		if(job.getJobRepeatCount() > 0)
			repeatCount = job.getJobRepeatCount();
		
		
		// [start] Job Detail oluşturulması
		logger.info("Creating JobDetails Object. Job Name : " + jobName);
		
		JobBuilder jobBuilder = JobBuilder.newJob(job.getClass());
		
		if(!jobName.equals("null"))
			jobBuilder = jobBuilder.withIdentity(jobName, jobGroup);
		
		if(job.getJobDataMap() != null)
			jobBuilder = jobBuilder.usingJobData(job.getJobDataMap());
		
		JobDetail jobDetail = jobBuilder.build();
		
		if(jobName.equals("null"))
		{
			jobName = jobDetail.getKey().getName();
			logger.info("No job name given. Assigning unique Job Name : " + jobName);
		}
		// [end]
		
		// [start] Trigger'ın çalıştırılacağı takvimi belirleyen simple schedule'ın oluşturulması
		logger.info("Creating SimpleSchedulerBuilder Object. Job Name : " + jobName);
		
		SimpleScheduleBuilder simpleScheduleBuilder;
		
		if(repeatCount == 0)
			simpleScheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever(interval);
		else if (repeatCount == 1)
			simpleScheduleBuilder = null;
		else
			simpleScheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForTotalCount(repeatCount, interval);
		// [end]
		
		// [start] Trigger oluşturulması
		logger.info("Creating TriggerBuilder Object. Job Name : " + jobName);
		
		TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().withIdentity(job.getTriggerKey());
		
		if(simpleScheduleBuilder != null) 
			triggerBuilder = triggerBuilder.withSchedule(simpleScheduleBuilder);
			
		if(job.getJobStartDate() != null)
			triggerBuilder = triggerBuilder.startAt(job.getJobStartDate());
		
		if(job.getJobEndDate() != null)
			triggerBuilder = triggerBuilder.endAt(job.getJobEndDate());
		// [end]
		
		// [start] Job'ın schedule edilmesi
		logger.info("Scheduling job. Job Name : " + jobName);
		
		try {
			startScheduler();
			if(replaceIfExists && stdScheduler.checkExists(jobDetail.getKey())) {
				stdScheduler.deleteJob(jobDetail.getKey());
			}
			stdScheduler.scheduleJob(jobDetail, triggerBuilder.build());
			schedulingSuccessful = true;
		} catch (SchedulerException e) {
			System.out.println("Exception while trying to add job. Job Name : " + jobName);
			e.printStackTrace();
		}
		// [end]
		
		return schedulingSuccessful;
	}	

	/**
	 * Verilen Trigger'ın çalışacağı tarihi değiştirir
	 * */
	public static boolean rescheduleJob(TriggerKey triggerKey, Date newStartDate, int hourInterval)
	{
		boolean reschedulingSuccesful = false;
		
		try {
			startScheduler();
			Trigger trigger = stdScheduler.getTrigger(triggerKey);
			
			if(trigger != null) {
				SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.repeatHourlyForever(hourInterval);
				
				TriggerBuilder triggerBuilder = trigger.getTriggerBuilder();
				
				Trigger newTrigger = triggerBuilder.startAt(newStartDate).withSchedule(simpleScheduleBuilder)
						.build();
				
				stdScheduler.rescheduleJob(triggerKey, newTrigger);
				
				reschedulingSuccesful = true;
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		return reschedulingSuccesful;
	}
	
	/**
	 * Verilen Trigger'ın çalışacağı tarihi değiştirir
	 * */
	public static boolean rescheduleJob(TriggerKey triggerKey, Date newStartDate)
	{
		try {
			startScheduler();
			Trigger trigger = stdScheduler.getTrigger(triggerKey);
			
			TriggerBuilder triggerBuilder = trigger.getTriggerBuilder();
			
			Trigger newTrigger = triggerBuilder.startAt(newStartDate).build();
			
			stdScheduler.rescheduleJob(triggerKey, newTrigger);
			
			return true;
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Verilen Job'ı ve ona bağlı triggerları durdurur
	 * */
	public static boolean removeJob(JobKey jobKey)
	{
		try {
			startScheduler();
			
			if(stdScheduler.checkExists(jobKey))
				stdScheduler.deleteJob(jobKey);
			
			return true;
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Cron ile bir job schedule eder
	 * */
	public static boolean scheduleCronJob(AbstractQuartzJob job, String cronExpression, boolean replaceIfExists)
	{
		boolean schedulingSuccessful = false;
		
		String jobName = "null";
		if(job.getJobName() != null)
			jobName = job.getJobName();
		
		String jobGroup = "";
		if(job.getJobGroup() != null)
			jobGroup = job.getJobGroup();
		
		//Tekrar aralığı
		int interval = 1;
		if(job.getJobRepeatIntervalInSeconds() > 1)
			interval = job.getJobRepeatIntervalInSeconds();
		
		//Tekrar sayısı
		int repeatCount = 0;
		if(job.getJobRepeatCount() > 0)
			repeatCount = job.getJobRepeatCount();
		
		
		// [start] Job Detail oluşturulması
		logger.info("Creating JobDetails Object. Job Name : " + jobName);
		
		JobBuilder jobBuilder = JobBuilder.newJob(job.getClass());
		
		if(!jobName.equals("null"))
			jobBuilder = jobBuilder.withIdentity(jobName, jobGroup);
		
		if(job.getJobDataMap() != null)
			jobBuilder = jobBuilder.usingJobData(job.getJobDataMap());
		
		JobDetail jobDetail = jobBuilder.build();
		
		if(jobName.equals("null"))
		{
			jobName = jobDetail.getKey().getName();
			logger.info("No job name given. Assigning unique Job Name : " + jobName);
		}
		// [end]
		
		// [start] Trigger oluşturulması
		logger.info("Creating TriggerBuilder Object. Job Name : " + jobName);
		
		TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger()
											.withIdentity(job.getTriggerKey())
											.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
			
		if(job.getJobStartDate() != null)
			triggerBuilder = triggerBuilder.startAt(job.getJobStartDate());
		
		if(job.getJobEndDate() != null)
			triggerBuilder = triggerBuilder.endAt(job.getJobEndDate());
		// [end]
		
		// [start] Job'ın schedule edilmesi
		logger.info("Scheduling job. Job Name : " + jobName);
		
		try {
			startScheduler();
			if(replaceIfExists && stdScheduler.checkExists(jobDetail.getKey())) {
				stdScheduler.deleteJob(jobDetail.getKey());
			}
			
			stdScheduler.scheduleJob(jobDetail, triggerBuilder.build());
			schedulingSuccessful = true;
		} catch (SchedulerException e) {
			System.out.println("Exception while trying to add job. Job Name : " + jobName);
			e.printStackTrace();
		}
		// [end]
		
		return schedulingSuccessful;
	}
	
	/**
	 * Verilen trigger'ın cron bilgisini ve çalışma tarihini günceller
	 * */
	public static boolean rescheduleCronJob(TriggerKey triggerKey, Date newDate, String newExpression)
	{
		try {
			startScheduler();
			Trigger trigger = stdScheduler.getTrigger(triggerKey);
			
			//triggerOfBranchQueryOftestBranch
			TriggerBuilder triggerBuilder = trigger.getTriggerBuilder();
			Trigger newTrigger = triggerBuilder.startAt(newDate)
											.withSchedule(CronScheduleBuilder.cronSchedule(newExpression))
											.build();
			
			stdScheduler.rescheduleJob(triggerKey, newTrigger);
			
			return true;
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Mevcut bir job'a yeni bir cron trigger'ı ekler.
	 * */
	public static boolean createAdditionalCronTrigger(JobKey jobKey, TriggerKey triggerKey, String cronExpression) {
		try {
			startScheduler();
			
			TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().forJob(jobKey).withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
			
			stdScheduler.scheduleJob(triggerBuilder.build());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Mevcut bir job'a yeni bir trigger ekler.
	 * */
	public static boolean createAdditionalTrigger(JobKey jobkey, TriggerKey triggerKey, Date startDate) {
		try {
			startScheduler();
			
			TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().forJob(jobkey).withIdentity(triggerKey).startAt(startDate);
			
			Date dt = stdScheduler.scheduleJob(triggerBuilder.build());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Bir grupa dahil butun joblari temizler
	 * */
	public static boolean removeGroupOfJobs(String groupName) {
		try {
			startScheduler();
			
			GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals(groupName);
			ArrayList<JobKey> keys = new ArrayList<JobKey>();
			keys.addAll(stdScheduler.getJobKeys(matcher));
			
			stdScheduler.deleteJobs(keys);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Bir job'ın var olup olmadigini, varsa calisip calismadigini kontrol eder
	 * */
	public static boolean isJobScheduled(String jobName, String jobGroupName) {
		try {
			startScheduler();
			
			JobKey jobKey = new JobKey(jobName, jobGroupName);
			JobDetail jobDetail = stdScheduler.getJobDetail(jobKey);
			if(jobDetail == null) {
				return false;
			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Bir job'ın var olup olmadigini, varsa calisip calismadigini kontrol eder
	 * */
	public static boolean isJobScheduled(JobKey jobKey, TriggerKey triggerKey) {
		try {
			startScheduler();
			
			boolean job = stdScheduler.checkExists(jobKey);
			boolean trigger = stdScheduler.checkExists(triggerKey);
			
			if(job == false || trigger == false) {
				return false;
			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static StdScheduler getScheduler() {
		return stdScheduler;
	}

	public static void setScheduler(StdScheduler scheduler) {
		stdScheduler = scheduler;
	}
}
