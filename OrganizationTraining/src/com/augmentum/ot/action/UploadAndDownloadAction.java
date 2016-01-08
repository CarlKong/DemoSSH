package com.augmentum.ot.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dataObject.UploadResult;
import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourseAttachment;
import com.augmentum.ot.model.CourseAttachment;
import com.augmentum.ot.model.PlanAttachment;
import com.augmentum.ot.service.FileUploadAndDownloadService;
import com.augmentum.ot.util.DateJsonValueProcessor;
import com.augmentum.ot.util.FileSystemUtils;

@Component("uploadAndDownloadAction")
@Scope("prototype")
public class UploadAndDownloadAction extends BaseAction implements ServletResponseAware{

	private static final long serialVersionUID = -3438948906270816975L;
	private Logger logger = Logger.getLogger(UploadAndDownloadAction.class);
	private FileUploadAndDownloadService fileUploadAndDownloadService;
	private File file;
	private String fileFileName;
	private String fileRealName;
	private String attachmentFileName;
	private HttpServletResponse response;
	private File attachment;
	private JSONObject jsonObject;
	
	public InputStream getInputStream() throws IOException {
        HttpServletRequest httpRequest = ServletActionContext.getRequest();
        return FileSystemUtils.getInputStream(fileFileName, httpRequest);
    }
	
	public String getDownLoadFileName() {
    	String downLoadFileName = fileRealName;
    	try {
			downLoadFileName = new String(downLoadFileName.getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			logger.error("When downLoad file, appear unsupported encoding exception, "+e);
		}

    	return downLoadFileName;
    }
	
	
	public String uploadImage() {
		UploadResult result = uploadImageFile();
		if (null != result) {
			jsonObject = JSONObject.fromObject(result);
		} else {
			jsonObject = new JSONObject();
			jsonObject.put(JsonKeyConstants.ERR, "Error");
		}
		return SUCCESS;
	}
	
	private UploadResult uploadImageFile() {
		UploadResult result = new UploadResult();
        File parentFile = FileSystemUtils.uploadFolder("uploadImagePath");
        if (parentFile == null) {
            result.setFileName("");
            result.setFileUrl("");
            return result;
        }
        
        String serviceFileName = FileSystemUtils.makeFileName(fileFileName);
        String destFilePath = parentFile.getAbsolutePath() + File.separator
                + serviceFileName;
        FileSystemUtils.uploadFile(file, destFilePath);
        String serviceContextPath = FileSystemUtils.getServiceContextPath();
        String virtualPath = FileSystemUtils.getFileVirtualPath(serviceContextPath);
        // the file url for http request
        String fileUrl = virtualPath + parentFile.getName() + "/"
                + serviceFileName;
        result.setFileUrl(fileUrl);
        result.setFileName(serviceFileName);
        return result;
	}
	
	public String uploadCourseAttachment() {
		if (attachment == null && attachmentFileName != null) {
			//TODO
		} else {
			CourseAttachment courseAttachment = null;
			try {
				courseAttachment = fileUploadAndDownloadService.createCourseAttachment(attachment, attachmentFileName);
			} catch (ServerErrorException e) {
				logger.error(e);
			}
			JsonConfig jsonConfig = new JsonConfig();
	        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
	                new DateJsonValueProcessor(DateFormatConstants.TO_DATABASE));
			jsonObject = JSONObject.fromObject(courseAttachment, jsonConfig);
		}
		return SUCCESS;
		
	}
	
	public String uploadPlanAttachment() {
		if (attachment == null && attachmentFileName != null) {
			//TODO
		} else {
			PlanAttachment planAttachment = null;
			try {
				planAttachment = fileUploadAndDownloadService.createPlanAttachment(attachment, attachmentFileName);
			} catch (Exception e) {
				logger.error(e);
			}
			JsonConfig jsonConfig = new JsonConfig();
	        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
	                new DateJsonValueProcessor(DateFormatConstants.TO_DATABASE));
			jsonObject = JSONObject.fromObject(planAttachment, jsonConfig);
		}
		return SUCCESS;
	}
	
	public String uploadActualCourseAttachment() {
		if (attachment == null && attachmentFileName != null) {
			//TODO
		} else {
			ActualCourseAttachment actualCourseAttachment = null;
			try {
				actualCourseAttachment = fileUploadAndDownloadService.createActualCourseAttachment(attachment, attachmentFileName);
			} catch (ServerErrorException e) {
				this.handleExceptionByServerErrorException(e, true);
			}
			JsonConfig jsonConfig = new JsonConfig();
	        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
	                new DateJsonValueProcessor(DateFormatConstants.TO_DATABASE));
			jsonObject = JSONObject.fromObject(actualCourseAttachment, jsonConfig);
		}
		
		return SUCCESS;
	}
	
	public String downLoadAttachment() {
        return SUCCESS;
    }

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileRealName() {
		return fileRealName;
	}

	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}

	public File getAttachment() {
		return attachment;
	}

	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}
    @Resource
	public void setFileUploadAndDownloadService(
			FileUploadAndDownloadService fileUploadAndDownloadService) {
		this.fileUploadAndDownloadService = fileUploadAndDownloadService;
	}

	public String getAttachmentFileName() {
		return attachmentFileName;
	}

	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	
}
