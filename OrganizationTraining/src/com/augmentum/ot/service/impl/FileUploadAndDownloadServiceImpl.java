package com.augmentum.ot.service.impl;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.ActualCourseAttachmentDao;
import com.augmentum.ot.dao.CourseAttachmentDao;
import com.augmentum.ot.dao.PlanAttachmentDao;
import com.augmentum.ot.dataObject.UploadResult;
import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourseAttachment;
import com.augmentum.ot.model.CourseAttachment;
import com.augmentum.ot.model.PlanAttachment;
import com.augmentum.ot.service.FileUploadAndDownloadService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.DateHandlerUtils;
import com.augmentum.ot.util.FileSystemUtils;

@Component("fileUploadAndDownloadService")
public class FileUploadAndDownloadServiceImpl implements FileUploadAndDownloadService {

	private static Logger log = Logger.getLogger(FileUploadAndDownloadServiceImpl.class);
	private final static String ATTACHMENT_PATH_KEY = "uploadAttachmentPath";
	@Override
	public CourseAttachment createCourseAttachment(File srcFile, String fileName)
			throws ServerErrorException {
		UploadResult result = uploadFile(srcFile, fileName, "");

		CourseAttachment courseAttachment = new CourseAttachment();
        courseAttachment.setCourseAttachmentPath(result.getFileUrl().substring(1));
        courseAttachment.setCourseAttachmentvisible(FlagConstants.VISIBLE);
        courseAttachment.setCourseAttachmentIsDeleted(FlagConstants.UN_DELETED);
        courseAttachment.setCourseAttachmentName(fileName);
        courseAttachment.setSize(FileSystemUtils.getFileSize(srcFile));
        courseAttachment.setCreateDateTime(new Date());
        CourseAttachmentDao courseAttachmentDao = BeanFactory.getCourseAttachmentDao();
        // save the attachment file
        courseAttachmentDao.saveObject(courseAttachment);
        return courseAttachment;
	}

	@Override
	public PlanAttachment createPlanAttachment(File srcFile, String fileName)
			throws ServerErrorException {
		UploadResult result = uploadFile(srcFile, fileName, "");

		PlanAttachment planAttachment = new PlanAttachment();
		planAttachment.setPlanAttachmentPath(result.getFileUrl().substring(1));
		planAttachment.setPlanAttachmentIsVisible(FlagConstants.VISIBLE);
		planAttachment.setPlanAttachmentIsDeleted(FlagConstants.UN_DELETED);
		planAttachment.setPlanAttachmentName(fileName);
		planAttachment.setSize(FileSystemUtils.getFileSize(srcFile));
		planAttachment.setCreateDateTime(new Date());
        PlanAttachmentDao planAttachmentDao = BeanFactory.getPlanAttachmentDao();
        // save the attachment file
        planAttachmentDao.saveObject(planAttachment);
        return planAttachment;
	}

	@Override
	public ActualCourseAttachment createActualCourseAttachment(File srcFile, String fileName)
			throws ServerErrorException {
		UploadResult result = uploadFile(srcFile, fileName, "");

		ActualCourseAttachment actualCourseAttachment = new ActualCourseAttachment();
		actualCourseAttachment.setActualCourseAttachmentPath(result.getFileUrl().substring(1));
		actualCourseAttachment.setActualCourseAttachmentVisible(FlagConstants.VISIBLE);
		actualCourseAttachment.setActualCourseAttachmentIsDeleted(FlagConstants.UN_DELETED);
		actualCourseAttachment.setActualCoursAttachmentName(fileName);
		actualCourseAttachment.setSize(FileSystemUtils.getFileSize(srcFile));
		actualCourseAttachment.setCreateDateTime(new Date());
        ActualCourseAttachmentDao actualCourseAttachmentDao = BeanFactory.getActualCourseAttachmentDao();
        // save the attachment file
        actualCourseAttachmentDao.saveObject(actualCourseAttachment);
        return actualCourseAttachment;
	}

    private UploadResult uploadFile(File srcFile, String fileName, String resourceType){
		UploadResult result = new UploadResult();
		File parentFile = FileSystemUtils.uploadFolder(ATTACHMENT_PATH_KEY);
		
		if (parentFile == null) {
			result.setFileName("");
			result.setFileUrl("");
			return result;
		}
		String fix = FileSystemUtils.getFileSuffix(fileName);
		// If the suffix of the resource type is not right.
		if (!"".equals(resourceType)) {
			if (!FileSystemUtils.isAllowedExtensions(resourceType, fix)) {
				result.setFileName("");
				result.setFileUrl("");
				log.debug(LogConstants.message("The file suffix is not right", fix));
				return result;
			}
		}
		// Get the name of file in server.
		String serviceFileName = FileSystemUtils.makeFileName(fileName);
		// Get the path of file in server.
		String serverFilePath = parentFile.getAbsolutePath() + File.separator
				+ serviceFileName;
		// Upload file.
		FileSystemUtils.uploadFile(srcFile, serverFilePath);
		// get a new context path, it map another path (not in the current
		// server)
		// like images map 'c:\\uploadImages'
		String serviceContextPath = FileSystemUtils.getServiceFileContextPath();
		// get the virtual path, like http://localhost:8080/images/,real path
		// 'c:\\uploadImages'
		String virtualPath = FileSystemUtils.getFileVirtualPath(serviceContextPath);
		// the file url for http request
		String fileUrl = virtualPath + parentFile.getName() + "/"
				+ serviceFileName;
		result.setFileUrl(fileUrl);
		result.setFileName(fileName);
		result.setUploadDate(DateHandlerUtils.dateToString(DateFormatConstants.YYYY_MM_DD, new Date()));
		log.debug(LogConstants.pureMessage("Upload file successfully"));
		return result;
	}
}
