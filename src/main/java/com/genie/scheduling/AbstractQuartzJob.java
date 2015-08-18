package com.genie.scheduling;

import java.util.Date;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @author ccubukcu
 *
 */
public abstract class AbstractQuartzJob extends SpringBeanAutowiringSupport implements Job {
	Date jobStartDate;
	Date jobEndDate;
	String jobName;
	String jobGroup;
	int jobRepeatCount;
	int jobRepeatIntervalInSeconds;
	JobDataMap jobDataMap;
	TriggerKey triggerKey;
        
	public AbstractQuartzJob()
	{
		jobStartDate = null;
		jobEndDate = null;
		jobName = "";
		jobGroup = "";
		jobRepeatCount = 0;
		jobRepeatIntervalInSeconds = 0;
		jobDataMap = new JobDataMap();
	}
	
	public void createTriggerKey(String triggerName, String triggerGroup)
	{
		triggerKey = new TriggerKey(triggerName, triggerGroup);
	}
	
	public void createTriggerKey(String suffix, String prefix, String triggerGroup)
	{
		String tName = prefix + suffix;
		String tGroup = triggerGroup;
		
		triggerKey = new TriggerKey(tName, tGroup);
	}
	
	public TriggerKey getTriggerKey()
	{
		return triggerKey;
	}
	
	/**
	 * Anında ve bir kere çalışacak job bilgilerini düzenler
	 * @param jobName Job'ın Adı
	 * @param jobGroup Job'ın dahil olduğu grubun adı
	 * */
	public void configureImmediateSingleRun(String jobName, String jobGroup)
	{
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobRepeatCount = 1;
	}
	
	/**
	 * Anında başlayacak, verilen tekrar sayısı ve bekleme süresiyle tekrar edecek job bilgilerini düzenler
	 * @param jobName Adı
	 * @param jobGroup Dahil olduğu grubun adı
	 * @param jobRepeatCount Tekrar sayısı, sonsuz için 0
	 * @param jobRepeatIntervalInSeconds Saniye cinsinden bekleme miktarı
	 * */
	public void configureImmediateRepeatingRun(String jobName, String jobGroup, int jobRepeatCount, int jobRepeatIntervalInSeconds)
	{
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobRepeatCount = jobRepeatCount;
		this.jobRepeatIntervalInSeconds = jobRepeatIntervalInSeconds;
	}

	/**
	 * Belirlenen iki tarih arasında, verilen bekleme süresiyle tekrar edecek job bilgilerini düzenler
	 * @param jobName Adı
	 * @param jobGroup Dahil olduğu grubun adı
	 * @param jobRepeatIntervalInSeconds Saniye cinsinden bekleme miktarı
	 * @param jobStartDate Başlangıç tarihi
	 * @param jobEndDate Bitiş tarihi
	 * */
	public void configureTimeframeScheduledRepeatingRun(String jobName, String jobGroup, int jobRepeatIntervalInSeconds, Date jobStartDate, Date jobEndDate)
	{
		this.jobStartDate = jobStartDate;
		this.jobEndDate = jobEndDate;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobRepeatCount = 1;
		this.jobRepeatIntervalInSeconds = jobRepeatIntervalInSeconds;
	}
	
	/**
	 * Belirli bir tarihte başlayıp, verilen tekrar sayısı ve bekleme süresiyle tekrar edecek job bilgilerini düzenler
	 * @param jobName Adı
	 * @param jobGroup Dahil olduğu grubun adı
	 * @param jobRepeatCount Tekrar sayısı, sonsuz için 0
	 * @param jobRepeatIntervalInSeconds Saniye cinsinden bekleme miktarı
	 * @param jobStartDate Başlangıç tarihi
	 * */
	public void configureScheduledRepeatingRun(String jobName, String jobGroup, int jobRepeatCount, int jobRepeatIntervalInSeconds, Date jobStartDate)
	{
		this.jobStartDate = jobStartDate;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobRepeatCount = jobRepeatCount;
		this.jobRepeatIntervalInSeconds = jobRepeatIntervalInSeconds;
	}
	
	/**
	 * İleriki bir tarihte tek bir sefer çalışacak job bilgilerini düzenler.
	 * @param jobName Adı
	 * @param jobGroup Dahil olduğu grubun adı
	 * @param jobStartDate Başlangıç tarihi
	 * */
	public void configureScheduledSingleRun(String jobName, String jobGroup, Date jobStartDate)
	{
		this.jobStartDate = jobStartDate;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobRepeatCount = 1;
	}
	
	/**
	 * Verilen tarihte çalışmaya başlayacak cron joblar içindir
	 * @param jobName Adı
	 * @param jobGroup Dahil olduğu grubun adı
	 * @param jobStartDate Başlangıç tarihi, anında başlaması için "new Date()"
	 * */
	public void configureCronRun(String jobName, String jobGroup, Date jobStartDate)
	{
		configureScheduledSingleRun(jobName, jobGroup, jobStartDate);
	}
	
	public static boolean removeJob(String jobPrefix, String identifier, String jobGroup) {
		String jobName = jobPrefix + identifier;
		return QuartzScheduler.removeJob(new JobKey(jobName, jobGroup));
	}
	
	public static boolean rescheduleJob(String triggerPrefix, String identifier, String triggerGroup, Date newStartDate) {
		//TriggerKey oluşturulması
		String triggerName = triggerPrefix + identifier;
		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup); 
		
		return QuartzScheduler.rescheduleJob(triggerKey, newStartDate);
	}
	
	public static boolean rescheduleJob(String triggerPrefix, String identifier, String triggerGroup, Date newStartDate, int hourInterval) {
		//TriggerKey oluşturulması
		String triggerName = triggerPrefix + identifier;
		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup); 
		
		return QuartzScheduler.rescheduleJob(triggerKey, newStartDate, hourInterval);
	}
	
	public void insertDataToJobDataMap(Map<? extends String,? extends Object> map)
	{
		if(jobDataMap == null) jobDataMap = new JobDataMap();
		
		jobDataMap.putAll(map);
	}

	public void insertDataToJobDataMap(String key, Date value)
	{
		if(jobDataMap == null) jobDataMap = new JobDataMap();
		
		jobDataMap.put(key,value);
	}
	
	public void insertDataToJobDataMap(String key, Boolean value)
	{
		if(jobDataMap == null) jobDataMap = new JobDataMap();
		
		jobDataMap.put(key,value);
	}

	public void insertDataToJobDataMap(String key, Integer value)
	{
		if(jobDataMap == null) jobDataMap = new JobDataMap();
		
		jobDataMap.put(key,value);
	}

	public void insertDataToJobDataMap(String key, Double value)
	{
		if(jobDataMap == null) jobDataMap = new JobDataMap();
		
		jobDataMap.put(key,value);
	}

	public void insertDataToJobDataMap(String key, Float value)
	{
		if(jobDataMap == null) jobDataMap = new JobDataMap();
		
		jobDataMap.put(key,value);
	}
	
	public void insertDataToJobDataMap(String key, String value)
	{
		if(jobDataMap == null) jobDataMap = new JobDataMap();
		
		jobDataMap.put(key,value);
	}

	public void insertDataToJobDataMap(String key, Long value)
	{
		if(jobDataMap == null) jobDataMap = new JobDataMap();
		
		jobDataMap.put(key,value);
	}

	public void insertDataToJobDataMap(String key, Object value)
	{
		if(jobDataMap == null) jobDataMap = new JobDataMap();
		
		jobDataMap.put(key,value);
	}
	
	public Date getDateFromJobDataMap(String key) throws ClassCastException {
		return (Date) jobDataMap.get(key);
	}

	public String getStringFromJobDataMap(String key) {
		return jobDataMap.getString(key);
	}
	
	public boolean getBooleanFromJobDataMap(String key) {
		return jobDataMap.getBooleanValue(key);
	}

	public Double getDoubleFromJobDataMap(String key) {
		return jobDataMap.getDoubleValue(key);
	}
	
	public Float getFloatFromJobDataMap(String key) {
		return jobDataMap.getFloatValue(key);
	}
	
	public Integer getIntegerFromJobDataMap(String key) {
		return jobDataMap.getIntValue(key);
	}
	
	public Long getLongFromJobDataMap(String key) {
		return jobDataMap.getLongValue(key);
	}
	
	public Date getJobStartDate() {
		return jobStartDate;
	}

	public void setJobStartDate(Date jobStartDate) {
		this.jobStartDate = jobStartDate;
	}

	public Date getJobEndDate() {
		return jobEndDate;
	}

	public void setJobEndDate(Date jobEndDate) {
		this.jobEndDate = jobEndDate;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public int getJobRepeatCount() {
		return jobRepeatCount;
	}

	public void setJobRepeatCount(int jobRepeatCount) {
		this.jobRepeatCount = jobRepeatCount;
	}

	public int getJobRepeatIntervalInSeconds() {
		return jobRepeatIntervalInSeconds;
	}

	public void setJobRepeatIntervalInSeconds(int jobRepeatIntervalInSeconds) {
		this.jobRepeatIntervalInSeconds = jobRepeatIntervalInSeconds;
	}

	public JobDataMap getJobDataMap() {
		return jobDataMap;
	}

	public void setJobDataMap(JobDataMap jobDataMap) {
		this.jobDataMap = jobDataMap;
	}
}
