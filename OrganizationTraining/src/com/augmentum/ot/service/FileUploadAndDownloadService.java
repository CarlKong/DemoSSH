package com.augmentum.ot.service;

import java.io.File;

import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourseAttachment;
import com.augmentum.ot.model.CourseAttachment;
import com.augmentum.ot.model.PlanAttachment;

public interface FileUploadAndDownloadService {
	
	/**
	 * Create course attachment for upload.
	 * @param srcFile
	 * @param fileName
	 * @return a UploadResult object, describe uploaded file information
	 * @throws CourseServerErrorException
	 */
	CourseAttachment createCourseAttachment(File srcFile, String fileName) throws ServerErrorException;
	
	/**
	 * Create plan attachment for upload.
	 * @param srcFile
	 * @param fileName
	 * @return a UploadResult object, describe uploaded file information
	 * @throws PlanServerErrorException
	 */
	PlanAttachment createPlanAttachment(File srcFile, String fileName) throws ServerErrorException;
	
	/**
	 * Create actual course attachment for upload.
	 * @param srcFile
	 * @param fileName
	 * @return a UploadResult object, describe uploaded file information
	 * @throws ServerErrorException
	 */
	ActualCourseAttachment createActualCourseAttachment(File srcFile, String fileName) throws ServerErrorException;
}
