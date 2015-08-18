package com.genie.pojo;

import java.io.Serializable;


public class ResourceImageWrapper implements Serializable {
	private static final long serialVersionUID = -9059016001417791811L;

	private int id;
	private String filename;
	private String encodedImage;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getEncodedImage() {
		return encodedImage;
	}
	public void setEncodedImage(String encodedImage) {
		this.encodedImage = encodedImage;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResourceImageWrapper other = (ResourceImageWrapper) obj;
        if(id == other.id) {
        	return true;
        }
        if(id > 0 && other.id > 0) {
        	if(id == other.id) {
            	return false;
            }
        }
        
        return true;
	}
}
