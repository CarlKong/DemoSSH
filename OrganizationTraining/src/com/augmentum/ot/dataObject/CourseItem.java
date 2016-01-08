package com.augmentum.ot.dataObject;

import java.io.Serializable;

public class CourseItem implements Serializable{

	private static final long serialVersionUID = -1274633091044480240L;
	
	private int courseId;
	
	private String courseName;
	
	private String time;
	
	private int isJoinCourse;

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getIsJoinCourse() {
		return isJoinCourse;
	}

	public void setIsJoinCourse(int isJoinCourse) {
		this.isJoinCourse = isJoinCourse;
	}
	
	

}
