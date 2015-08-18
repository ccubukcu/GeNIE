package com.genie.utils;

import java.util.Locale;

import javax.faces.model.SelectItem;

/**
 * @author ccubukcu
 *
 */
public interface PortalConstants {
	Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	SelectItem noFilterItem = new SelectItem("", "");

	public static final int LEADERBOARD_FILLER_COUNT = 3;

	public static final String DEFAULT_POINTS_NAME_KEY = "gamificationSettings.label.pointsDefaultName";
	public static final int DEFAULT_MAX_CONVERTABLE_POINTS = 1000;

	public static final int RANDOM_TOKEN_MIN_LENGTH = 60;
	public static final int RANDOM_TOKEN_MAX_LENGTH = 80;
	
	public static final int PASS_TOKEN_VALIDITY_DAYS = 2;
	
	public static final String DEFAULT_PORTAL_ACTION_USER = "system";
	
	public static final Locale PORTAL_LOCALE = Locale.UK;

	public static final int GLOBAL_FILE_LIMIT = 5 * 1024 * 1024; // 5MB
	public static final int PROFILE_PICTURE_FILE_LIMIT = 1 * 1024 * 1024; // 1MB
}
