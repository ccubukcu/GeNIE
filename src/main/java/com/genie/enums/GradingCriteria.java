package com.genie.enums;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.genie.utils.ResourceUtil;

public enum GradingCriteria {
	ATTENDANCE(10),
	ASSIGNMENT(20),
	EXAM(30),
	GAMIFICATION(40),
	OTHER(100);
	
	private int index;
	
	private GradingCriteria(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	public static List<SelectItem> getAsSelectItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (GradingCriteria gc : GradingCriteria.values()) {
    		items.add(new SelectItem(gc.getIndex(), ResourceUtil.getLabel("gradingCriteria.enum." + gc.toString())));
        }
        return items;
    }
	
	public String getLabel() {
    	return getLabel(this.index);
    }
    
    public static String getLabel(int gc) {
        return ResourceUtil.getLabel("gradingCriteria.enum." + getGradingCriteria(gc).toString());
    }
    
    public static GradingCriteria getGradingCriteria(int index) {
    	for (GradingCriteria gc : values()) {
    		if (gc.index == index) {
    			return gc;
    		}
    	}
    	return null;
    }

	public static List<SelectItem> getAsSelectItemsForAchievements() {
        List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(ASSIGNMENT.getIndex(), ResourceUtil.getLabel("gradingCriteria.label." + ASSIGNMENT.toString())));
		items.add(new SelectItem(EXAM.getIndex(), ResourceUtil.getLabel("gradingCriteria.label." + EXAM.toString())));
		items.add(new SelectItem(OTHER.getIndex(), ResourceUtil.getLabel("gradingCriteria.label." + OTHER.toString())));
        return items;
    }

	public static String getTextLabel(Integer targetGradingCriteria) {
		return ResourceUtil.getLabel("gradingCriteria.label." + getGradingCriteria(targetGradingCriteria).toString());
	}
}
