package com.genie.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.faces.model.SelectItem;

import com.genie.pojo.ResourceImageWrapper;
import com.genie.utils.ResourceUtil;

public enum SampleImageFolder {
	BASIC(1, "sample-images/basic"),
	COLLEGE(10, "sample-images/college"),
	EDUCATION(20, "sample-images/education"),
	GAMES(30, "sample-images/games"),
	STARS(40, "sample-images/stars"),
	AWARDS(50, "sample-images/award"),
	STUDY(60, "sample-images/study"),
	ECOPACK(70, "sample-images/ecopack"),
	PLAYGROUND(80, "sample-images/playground"),
	ENTERPRISE(90, "sample-images/enterprise");
	
	private int index;
	private String resourceFolderPath;
	
	private static final List<SampleImageFolder> valueList = Arrays.asList(values());
	private static final int size = valueList.size();
	
	private SampleImageFolder(int index, String path) {
		this.index = index;
		this.resourceFolderPath = path;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getResourceFolderPath() {
		return resourceFolderPath;
	}
	
	public static List<ResourceImageWrapper> getImagesByIndex(int index) {
		SampleImageFolder sif = getSampleImageFolder(index);
		
		if(sif != null)
			return ResourceUtil.getAllImagesBase64EncodedFromResourceFolder(sif.getResourceFolderPath());
		
		return null;
	}
	
	public static List<SelectItem> getAsSelectItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (SampleImageFolder sif : SampleImageFolder.values()) {
    		items.add(new SelectItem(sif.getIndex(), ResourceUtil.getLabel("sampleImageFolder.enum." + sif.toString())));
        }
        return items;
    }
	
	public String getLabel() {
    	return getLabel(this.index);
    }
    
    public static String getLabel(int sif) {
        return ResourceUtil.getLabel("sampleImageFolder.enum." + getSampleImageFolder(sif).toString());
    }
    
    public static SampleImageFolder getSampleImageFolder(int index) {
    	for (SampleImageFolder sif : values()) {
    		if (sif.index == index) {
    			return sif;
    		}
    	}
    	return null;
    }
    
    public static SampleImageFolder randomFolder()  {
        return valueList.get(new Random().nextInt(size));
      }
}
