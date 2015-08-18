package com.genie.enums;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.genie.utils.ResourceUtil;

/**
 *
 * @author ccubukcu
 */
public enum AuditLogOperation {
    
    INSERT(1),
    UPDATE(2),
    DELETE(3);
    
    private int value;
    
    private AuditLogOperation(int value) {
        this.value = value;
    }
    
	/**
	 * @param intValue
	 * @return
	 */
	public static AuditLogOperation getEnumValue(int intValue) {
		switch (intValue) {
		case 1:
			return AuditLogOperation.INSERT;
		case 2:
			return AuditLogOperation.UPDATE;
		case 3:
			return AuditLogOperation.DELETE;
		}
		return null;
	}

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    public static List<SelectItem> getAsSelectItems() {
    List<SelectItem> items = new ArrayList<SelectItem>();
    for (AuditLogOperation op : AuditLogOperation.values()) {
        items.add(new SelectItem(op.getValue(), ResourceUtil.getLabel("operationType.enum." + op.toString())));
    }
    return items;
    }
        
    public static String getOperationTypeLabel(AuditLogOperation aop) {
        return ResourceUtil.getLabel("operationType.enum." + aop.toString());
    }
    
    public static String getLabel(int operationTypeValue) {
        for (AuditLogOperation op : values()) {
            if (op.getValue() == operationTypeValue) {
                return getOperationTypeLabel(op);
            }
        }
        return null;
    }
    
}
