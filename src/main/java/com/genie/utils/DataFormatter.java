package com.genie.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author ccubukcu
 *
 */
public class DataFormatter {

	private static final DecimalFormat defaultPercentageFormat = new DecimalFormat("00 %");
	private static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd/MM/yyyy", PortalConstants.PORTAL_LOCALE);
	private static final DateFormat fileDateFormat = new SimpleDateFormat("ddMMyyyy", PortalConstants.PORTAL_LOCALE);
	private static final DateFormat sessionDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SSS", PortalConstants.PORTAL_LOCALE);
	private static final SimpleDateFormat defaultDateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", PortalConstants.PORTAL_LOCALE);
	private static final SimpleDateFormat defaultDateToSecondFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", PortalConstants.PORTAL_LOCALE);
	private static final DateFormat defaultTimeFormat = new SimpleDateFormat("HH:mm", PortalConstants.PORTAL_LOCALE);
	private static final DateFormat formDateFormat = new SimpleDateFormat("ddMMyy", PortalConstants.PORTAL_LOCALE);
	private static final DateFormat defaultChartFormat = new SimpleDateFormat("dd/MM", PortalConstants.PORTAL_LOCALE);
	private static final DateFormat defaultFileFormat = new SimpleDateFormat("dd.MM.yyyy", PortalConstants.PORTAL_LOCALE);
	
	public static DateTime getDateTimeFromString(String date) {
		DateTimeFormatter dateStringFormat = DateTimeFormat.forPattern(defaultDateTimeFormat.toPattern());
		return dateStringFormat.parseDateTime(date);
	}
	
	public static boolean validateTime(String dateStr){
		StringTokenizer paramsToken = new StringTokenizer(dateStr, ":");
		int cnt = paramsToken.countTokens();
		if(cnt != 2){
			return false;
		} else {
			String hourStr = paramsToken.nextToken();  
			String minuteStr = paramsToken.nextToken();
			int hour = 0;
			int minute = 0;
			try {
				hour = Integer.parseInt(hourStr);
				minute = Integer.parseInt(minuteStr);
				if (hour > 23 || minute > 59) {
					return false;
				}
				
			} catch (Exception e){
				return false;
			}
		}
			
		return true;
	}
	
	public static Date stringToTime(String dateStr) {
		Date retDate = null;
		if (dateStr == null || dateStr.trim().equals("")) {
			return null;
		}
		
		if (!validateTime(dateStr)){
			return null;
		}

		try {
			retDate = defaultTimeFormat.parse(dateStr);
		} catch (Exception e) {
			retDate = null;
		}
		return retDate;
	}
	
	public static Date stringToDate(String dateStr) {
		Date retDate = null;
		if (dateStr == null || dateStr.trim().equals("")) {
			return null;
		}

		try {
			retDate = defaultDateFormat.parse(dateStr);
		} catch (Exception e) {
			retDate = null;
		}
		return retDate;
	}	
	
	public static String dateToString(Date date) {
		String retStr = null;
		if (date == null) {
			return null;
		}

		try {
			retStr = defaultDateFormat.format(date);
		} catch (Exception e) {
			retStr = null;
		}
		return retStr;
	}
	
	public static String formatShiftTime(Date date) {
		return defaultTimeFormat.format(date);
	}

	public static String formatDate(Date date) {
		return defaultDateFormat.format(date);
	}
	
	public static String formatDateForChart(Date date) {
		return defaultChartFormat.format(date);
	}
	
	public static String formatDateForFilename(Date date) {
		return defaultFileFormat.format(date);
	}
	
	public static String formatFileDate(Date date) {
		return fileDateFormat.format(date);
	}
	
	public static String formatFormDate(Date date) {
		return formDateFormat.format(date);
	}

	public static String formatDateTime(Date date) {
		String test = defaultDateTimeFormat.format(date);
		return test;
	}

	public static String formatDateToSecond(Date date) {
		String test = defaultDateToSecondFormat.format(date);
		return test;
	}
	
	public static String formatDate(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public static String formatPercent(double value) {
		return defaultPercentageFormat.format(value);
	}
	
	public static String formatPercent(double value, String pattern) {
		return new DecimalFormat("pattern").format(value);
	}
	
	public static String formatPercentRoundUp(double value) {
		return defaultPercentageFormat.format((double)Math.round(value * 100)/100);
	}
	
	public static String convertTrChars(String text) {
		if(text != null && text.trim().length() > 0) {
			text = text.trim();

			text = text.replace("ü", "u");
			text = text.replace("Ü", "U");
			text = text.replace("Ğ", "G");
			text = text.replace("ğ", "g");
			text = text.replace("Ş", "S");
			text = text.replace("ş", "s");
			text = text.replace("İ", "I");
			text = text.replace("ı", "i");
			text = text.replace("Ö", "O");
			text = text.replace("ö", "o");
			text = text.replace("ç", "c");
			text = text.replace("Ç", "C");
			
			return text;
		}
		return "";
	}
	
	public static Date formatForStartOfDay(Date date) {
		DateTime dt = new DateTime();
		dt = dt.secondOfMinute().setCopy(0);
		dt = dt.minuteOfHour().setCopy(0);
		dt = dt.hourOfDay().setCopy(0);
		return dt.toDate();
	}
	
	public static Date formatForEndOfDay(Date date) {
		DateTime dt = new DateTime();
		dt = dt.secondOfMinute().setCopy(59);
		dt = dt.minuteOfHour().setCopy(59);
		dt = dt.hourOfDay().setCopy(23);
		return dt.toDate();
	}
	
	public static Date getStartOfToday() {
		return formatForStartOfDay(new Date());
	}
	
	public static Date getEndOfToday() {
		return formatForEndOfDay(new Date());
	}
	
	public static String getDateTimeFormat() {
		return defaultDateTimeFormat.toPattern();
	}

	public static Date getStartOfPreviousDay() {
		DateTime dt = new DateTime();
		
		dt = dt.millisOfSecond().setCopy(0);
		dt = dt.secondOfMinute().setCopy(0);
		dt = dt.minuteOfHour().setCopy(0);
		dt = dt.hourOfDay().setCopy(0);
		dt = dt.plusDays(-1);
		
		return dt.toDate();
	}
	public static Date getStartOfPreviousDay(int day) {
		DateTime dt = new DateTime();
		
		dt = dt.millisOfSecond().setCopy(0);
		dt = dt.secondOfMinute().setCopy(0);
		dt = dt.minuteOfHour().setCopy(0);
		dt = dt.hourOfDay().setCopy(0);
		dt = dt.plusDays(-day);
		
		return dt.toDate();
	}
	
	public static Date mergeDateAndTime(Date date, Date time) {
		DateTime dt = new DateTime(date);
		DateTime tm = new DateTime(time);
		
		dt = dt.hourOfDay().setCopy(tm.getHourOfDay());
		dt = dt.minuteOfHour().setCopy(tm.getMinuteOfHour());
		dt = dt.secondOfMinute().setCopy(tm.getSecondOfMinute());
		
		return dt.toDate();
	}
	
	public static List<SelectItem> getMonthsAsSelectItems() {
		List<SelectItem> monthItems = new ArrayList<SelectItem>();
		
		for(int i=1;i<13;i++) {
			monthItems.add(new SelectItem(i, ResourceUtil.getLabel("filterOptions.month." + i)));
		}
		
		return monthItems;
	}

	public static String getDateFormat() {
		return defaultDateFormat.toPattern();
	}
	
	public static long getChartMax(long number) {
		number = number + (long) ((double)number * 0.0625);
		if(number % 4 == 0) {
			return number;
		} else {
			return number + (4 - (number % 4)); 
		}
	}
	public static long getChartMax(long number, int rowCount) {
		number = number + (long) ((double)number * 0.0625);
		if(number % rowCount == 0) {
			return number;
		} else {
			return number + (rowCount - (number % rowCount)); 
		}
	}
	
	private static String getCurrentDateForSession() {
		return sessionDateFormat.format(new Date());
	}
	
	public static String getUniqueFilename() {
		FacesContext fCtx = JsfUtil.getFacesContext();
		HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
		return session.getId() + "-" + getCurrentDateForSession() + ".png";
	}
	
	public static String getUniqueFilename(String identifier, boolean timestamp) {
		FacesContext fCtx = JsfUtil.getFacesContext();
		HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
		return session.getId() + "-" + identifier + (timestamp ? "-" + getCurrentDateForSession() : "") + ".png";
	}
	
	public static String generateRandomToken() {
		String token = "";
		
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
			int count = new Random().nextInt(PortalConstants.RANDOM_TOKEN_MAX_LENGTH) + PortalConstants.RANDOM_TOKEN_MIN_LENGTH;
			token = new BigInteger(count, sr).toString(32);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return token;
	}
	
	public static String getUrlForAppRootWithSeparator() {
		String fullpath = JsfUtil.getHttpServletRequest().getRequestURL().toString();
		fullpath = fullpath.substring(0, fullpath.lastIndexOf("/")+1);
		return fullpath;
	}
	
	public static String md5String(String input) {
		String md5 = null;  
        
        if(null == input) return null;  
        
        try {  
	        MessageDigest digest = MessageDigest.getInstance("MD5");  
	        digest.update(input.getBytes(), 0, input.length());  
	        md5 = new BigInteger(1, digest.digest()).toString(16);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        return md5;  
	}
	
	public static String getBase64EncodedImage(Blob image) {
		try {
			if(image != null && image.length() > 0) {
				byte[] imgBytes = image.getBytes(1, (int)image.length());
				sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
				String encoded =  base64Encoder.encode(imgBytes);
				
				return "data:image/png;base64," + encoded;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getBase64EncodedImage(File image) {
		try {
			if(image != null && image.length() > 0) {
				byte[] imgBytes = IOUtils.toByteArray(new FileInputStream(image));
				sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
				String encoded =  base64Encoder.encode(imgBytes);
				
				return "data:image/png;base64," + encoded;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
