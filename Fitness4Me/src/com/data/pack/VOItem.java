package com.data.pack;

public class VOItem {
	private String label;
	private String desc;
	private String imgString;
	private String imgLocked;
	private String Id;

	public String getId() {
		return Id;
	}

	public String getLabel() {
		return label;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImgString() {
		return imgString;
	}

	public void setImgString(String imgString) {
		this.imgString = imgString;
	}

	public String getImgLocked() {
		return imgLocked;
	}

	public void setImgLocked(String imgLocked) {
		this.imgLocked = imgLocked;
	}

}
