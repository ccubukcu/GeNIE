package com.genie.enums;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.genie.utils.ResourceUtil;

public enum LeaderboardVisibility {
	ALL_VISIBLE(0),
	TOP_AND_BOTTOM(1),
	TOP(2);
	
	private int index;
	
	private LeaderboardVisibility(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	public static List<SelectItem> getAsSelectItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (LeaderboardVisibility lv : LeaderboardVisibility.values()) {
    		items.add(new SelectItem(lv.getIndex(), ResourceUtil.getLabel("leaderboardVisibility.enum." + lv.toString())));
        }
        return items;
    }
	
	 public String getLabel() {
    	return getLabel(this.index);
    }
    
    public static String getLabel(int lv) {
        return ResourceUtil.getLabel("leaderboardVisibility.enum." + getLeaderboardVisibility(lv).toString());
    }
    
    public static LeaderboardVisibility getLeaderboardVisibility(int index) {
    	for (LeaderboardVisibility lv : values()) {
    		if (lv.index == index) {
    			return lv;
    		}
    	}
    	return null;
    }
}
