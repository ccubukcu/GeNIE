package com.genie.security;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.genie.utils.ResourceUtil;

/**
 * @author ccubukcu
 *
 */
public enum Role {
	
	ROLE_STUDENT(10),
	ROLE_DEMONSTRATOR(11),
	ROLE_INSTRUCTOR(12),

	/**Administrator*/
	ROLE_ADMINISTRATOR(20),
	
	/**Anonymous Users*/
	ROLE_ANONYMOUS(100);

	private final int value;
	
    private Role(int value) {
    	 this.value = value;
    }
    
    public int getValue() {
		return value;
	}
    
    public String getLabel() {
    	return getRoleLabel(this.value);
    }
    
    public static String getLabelOf(String authority) {
    	return ResourceUtil.getLabel("role.enum." + authority);
    }
    
    public static String getRoleLabel(Role role) {
        return ResourceUtil.getLabel("role.enum." + role.toString());
    }
    
    public static String getRoleLabel(int roleValue) {
        for (Role r : values()) {
            if (r.value == roleValue) {
                return getRoleLabel(r);
            }
        }
        return null;
    }
    
    public static Role getRole(int roleValue) {
    	for (Role r : values()) {
    		if (r.value == roleValue) {
    			return r;
    		}
    	}
    	return null;
    }
    
	public static List<SelectItem> getAsSelectItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (Role role : Role.values()) {
        	if(role.getValue() < 100)
        		items.add(new SelectItem(role.toString(), ResourceUtil.getLabel("role.enum." + role.toString())));
        }
        return items;
    }
    
	public static List<SelectItem> getAsRegisterSelectItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(ROLE_STUDENT.toString(), ResourceUtil.getLabel("role.enum." + ROLE_STUDENT.toString())));
        items.add(new SelectItem(ROLE_INSTRUCTOR.toString(), ResourceUtil.getLabel("role.enum." + ROLE_INSTRUCTOR.toString())));
        return items;
    }
}
