package com.Diary.model;

public class DiaryType {

	private int diaryTypeId;
	private String typeName;
	
	//每个类别下有多少条数据
	private int diaryCount;
	
	
	
	public DiaryType(String typeName) {
		super();
		this.typeName = typeName;
	}
	public DiaryType() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getDiaryTypeId() {
		return diaryTypeId;
	}
	public void setDiaryTypeId(int diaryTypeId) {
		this.diaryTypeId = diaryTypeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getDiaryCount() {
		return diaryCount;
	}
	public void setDiaryCount(int diaryCount) {
		this.diaryCount = diaryCount;
	}
	
	
}
