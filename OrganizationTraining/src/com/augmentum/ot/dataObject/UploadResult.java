package com.augmentum.ot.dataObject;

/**
 * Description upload file result
 * 
 * @version 0.1, 02/15/2012
 */
public class UploadResult {

	/**the file URL at server**/
	private String fileUrl;
	
    /**file name**/
	private String fileName;
	
	/**attachment size **/
	private String fileSize;
	
	/**attachmentId in table course_attachment_tb**/
	private Integer attachmentId;
	
	/**upload date**/
	private String uploadDate;
	
	private Integer attachmentIsDeleted;
	
	private Integer attachmentvisible;
	
	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Integer getAttachmentIsDeleted() {
		return attachmentIsDeleted;
	}

	public void setAttachmentIsDeleted(Integer attachmentIsDeleted) {
		this.attachmentIsDeleted = attachmentIsDeleted;
	}

	public Integer getAttachmentvisible() {
		return attachmentvisible;
	}

	public void setAttachmentvisible(Integer attachmentvisible) {
		this.attachmentvisible = attachmentvisible;
	}
	
	
	
}
