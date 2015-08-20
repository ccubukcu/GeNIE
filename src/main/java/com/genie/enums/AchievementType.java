package com.genie.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.faces.model.SelectItem;

import com.genie.utils.ResourceUtil;

public enum AchievementType {
	GRADE(1),
	ATTENDANCE_OVERALL(10),
	ATTENDANCE_CONSECUTIVE(20),
	SUBMISSION_OVERALL(30),
	SUBMISSION_EARLY(40);
	
	private int index;
	
	private static final List<AchievementType> valueList = Arrays.asList(values());
	private static final int size = valueList.size();
	
	private AchievementType(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	public static List<SelectItem> getAsSelectItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (AchievementType at : AchievementType.values()) {
    		items.add(new SelectItem(at.getIndex(), ResourceUtil.getLabel("achievementType.enum." + at.toString())));
        }
        return items;
    }
	
	public String getLabel() {
    	return getLabel(this.index);
    }
    
    public static String getLabel(int at) {
        return ResourceUtil.getLabel("achievementType.enum." + getAchievementType(at).toString());
    }
    
    public static AchievementType getAchievementType(int index) {
    	for (AchievementType at : values()) {
    		if (at.index == index) {
    			return at;
    		}
    	}
    	return null;
    }

	public static AchievementType getRandomType() {
		return valueList.get(new Random().nextInt(size));
	}
}
