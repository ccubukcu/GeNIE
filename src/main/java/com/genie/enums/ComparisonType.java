package com.genie.enums;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.genie.utils.ResourceUtil;

public enum ComparisonType {
	SMALLER(1),
	SMALLER_OR_EQUAL(10),
	EQUAL(20),
	GREATER_OR_EQUAL(30),
	GREATER(40);
	
	private int index;
	
	private ComparisonType(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	public static List<SelectItem> getAsSelectItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (ComparisonType ct : ComparisonType.values()) {
    		items.add(new SelectItem(ct.getIndex(), ResourceUtil.getLabel("comparisonType.enum." + ct.toString())));
        }
        return items;
    }
	
	public String getLabel() {
    	return getLabel(this.index);
    }
    
    public static String getLabel(int ct) {
        return ResourceUtil.getLabel("comparisonType.enum." + getComparisonType(ct).toString());
    }
    
    public static ComparisonType getComparisonType(int index) {
    	for (ComparisonType ct : values()) {
    		if (ct.index == index) {
    			return ct;
    		}
    	}
    	return null;
    }
    
    public static boolean compare(int typeIndex, float grade, float threshold) {
    	ComparisonType type = getComparisonType(typeIndex);
    	
    	if(type == SMALLER) {
    		return grade < threshold;
    	} else if(type == SMALLER_OR_EQUAL) {
    		return grade <= threshold;
    	} else if(type == EQUAL) {
    		return grade == threshold;
    	} else if(type == GREATER_OR_EQUAL) {
    		return grade >= threshold;
    	} else if(type == GREATER) {
    		return grade > threshold;
    	}
    	
    	return false;
    }
    
    public static boolean compare(int typeIndex, DateTime submitDate, DateTime checkDate) {
    	ComparisonType type = getComparisonType(typeIndex);
    	
    	LocalDate submitLocalDate = submitDate.toLocalDate();
    	LocalDate checkLocalDate = checkDate.toLocalDate();
    	
    	if(type == SMALLER) {
    		return submitLocalDate.isBefore(checkLocalDate);
    	} else if(type == SMALLER_OR_EQUAL) {
    		return submitLocalDate.isBefore(checkLocalDate) || submitLocalDate.isEqual(checkLocalDate);
    	} else if(type == EQUAL) {
    		return submitLocalDate.isEqual(checkLocalDate);
    	} else if(type == GREATER_OR_EQUAL) {
    		return submitLocalDate.isAfter(checkLocalDate) || submitLocalDate.isEqual(checkLocalDate);
    	} else if(type == GREATER) {
    		return submitLocalDate.isAfter(checkLocalDate);
    	}
    	
    	return false;
    }

	public static String getTextLabel(int ct) {
        return ResourceUtil.getLabel("comparisonType.label." + getComparisonType(ct).toString());
	}
}
