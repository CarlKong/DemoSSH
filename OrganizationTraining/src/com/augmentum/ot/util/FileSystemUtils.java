package com.augmentum.ot.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.augmentum.ot.dataObject.constant.ConfigureConstants;

/**
 * A util class to make file upload remove and download.
 * 
 * @version 0.2,02/03/2012
 */
public abstract class FileSystemUtils {
    private static Logger logger = Logger.getLogger(FileSystemUtils.class);
    private static Properties props;
	
	static{
		props = getProperties(ConfigureConstants.UPLOAD_FILE_CONF);
	}
    /**
     * find parant path from properties
     * 
     * @param propertiesPath
     *            this is the address, here is "upload.Properties".
     * @return parentPath a absolute address like "c:/temp".
     */
    public static String findParantPath(String propertiesPath) {
        String parentPath = null;
        Properties properties = new Properties();
        InputStream is;
        try {
            is = Thread.currentThread().getContextClassLoader().getResource(
                    propertiesPath).openStream();
        } catch (IOException e1) {
            logger.error(e1);
            logger.debug(e1);
            return null;
        }
        try {
            properties.load(is);
            parentPath = properties.getProperty("attachmentPath");
        } catch (IOException e) {
            logger.error(e);
            logger.debug(e);
            return null;
        }
        return parentPath;
    }

    /**
     * upload file
     * 
     * @param srcFile  
     *           src  file
     * @param destFilePath 
     *            dest file path
     */
	public static void uploadFile(File srcFile,String destFilePath){
		uploadFile(srcFile,new File(destFilePath));
	}
    
    /**
	 * Upload file
	 * 
	 * @param srcFile   
	 *             src file
	 * @param destFile  
	 *            dest file
	 */
	public static void uploadFile(File srcFile,File destFile){
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			logger.error(e);
		}
	}

    /**
     * remove a file
     * 
     * @param fileUri
     *            a address like "c:/temp/new.text".
     * @return true false
     */
    public static Boolean deleteFile(String fileURL) {
        File file = new File(fileURL);
        if (file.exists()) {
            file.delete();
            file.deleteOnExit();
            return true;
        }
        return false;
    }

    /**
	 * Decide whether the type is allowed
	 * @param allowedResourceType
	 *                upload resource type
	 * @return
	 */
	public static Boolean isAllowedType(String allowedResourceType){
		String[] allowResourse = props.getProperty("resourceType").split(",");
		List<String> allowedList= Arrays.asList(allowResourse);
		if(allowedList.contains(allowedResourceType)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Decide whether the extensions suffix is allowed
	 * @param allowedType
	 *               upload file type
	 * @param fileSuffix
	 *               file suffix
	 * @return
	 */
	public static Boolean isAllowedExtensions(String allowedResourceType,String fileSuffix){
		String[] allowedExtensions = props.getProperty(allowedResourceType+"Subffix").split(",");
		List<String> allowedList= Arrays.asList(allowedExtensions);
		if(allowedList.contains(fileSuffix.toLowerCase())){
			return true;
		}else{
			return false;
		}
	}
	
    /**
     * down a file
     * 
     * @param fileName
     * @return InputStream
     * @throws IOException 
     */
    public static InputStream getInputStream(String fileName,HttpServletRequest request) {
        InputStream is = null;
        URL url=null;
        String urlPath=request.getScheme() + "://" + "localhost"
        + ":" + request.getServerPort() + "/"+fileName;
        try {
            url = new URL(urlPath);
            is=url.openConnection().getInputStream();
        } catch (MalformedURLException e) {
            logger.info(e);
        } catch (IOException e) {
            logger.info(e);
        }
        return is;
    }

    public static List<String> uploadFiles(List<File> icon,
            List<String> iconFileName, String parentPath) throws Exception {
        List<String> iconNames = new ArrayList<String>();
        String parent = ServletActionContext.getServletContext().getRealPath(
                parentPath);
        File file = new File(parent);
        if (!file.exists()) {
            file.mkdir();
        }
        for (int i = 0; i < icon.size(); i++) {
            String iconName = System.currentTimeMillis()
                    + iconFileName.get(i).substring(
                            iconFileName.get(i).indexOf("."));
            File iconNameFile = new File(parent, iconName);
            InputStream is = new BufferedInputStream(new FileInputStream(icon
                    .get(i)));
            OutputStream os = new BufferedOutputStream(new FileOutputStream(
                    iconNameFile));
            Streams.copy(is, os, true);
            iconNames.add(iconName);
        }
        return iconNames;
    }
    
    
    /**
	 * create a folder every day
	 * 
	 * @param keyPath
	 *        in the configuration file path key 
	 * @return
	 *       return a file, a created new file or a existed file which name must be some
	 */
	public static File uploadFolder(String keyPath){
		String parentPath = props.getProperty(keyPath);
		File parentFile = new File(parentPath);
		if(!parentFile.exists()){
			//throw new RuntimeException("the "+ parentPath + " must be existed");
			parentFile.mkdir();
		}
		//for Liunx, get the write permission
		parentFile.setWritable(true);
		String folderName = makeFolderName();
		String folderPath = parentPath + File.separator+ folderName;
		File folderFile = null;
		if(folderName != null){
			folderFile = new File(folderPath);
		}
		if(folderFile != null){
			if(!folderFile.exists()){
				//for Liunx, get the write permission
				folderFile.setWritable(true);
				folderFile.mkdir();
			}
		}
		
		return folderFile;
	}
	
	public static void mkdir(String path){
		File file = new File(path);
		if(!file.exists()){
			file.mkdir();
		}
		//for Liunx, get the write permission
		file.setWritable(true);
	}
	
	/**
	 * get service context path from properties file
	 * @return
	 */
	public static String getServiceContextPath(){
		String serviceContextPath = props.getProperty("serviceContextPath");
		if(serviceContextPath == null){
			return "";
		}
		return serviceContextPath;
	}
	
	
	/**
	 * get service file context path from properties file
	 * @return
	 */
	public static String getServiceFileContextPath(){
		String serviceContextPath = props.getProperty("serviceFileContextPath");
		if(serviceContextPath == null){
			return "";
		}
		return serviceContextPath;
	}
	
	/**
	 * make the folder name by the defined format
	 * 
	 * @return
	 *       the folder name
	 */
	private static String makeFolderName(){
		String folderNameFormat = props.getProperty("folderNameFormat");
		if(folderNameFormat == null){
			folderNameFormat = "yyyyMMdd";
		}
		SimpleDateFormat format = new SimpleDateFormat(folderNameFormat);
		String folderName = format.format(new Date());
		return folderName;
	}
	
	/**
	 * get properties file
	 * 
	 * @param path
	 *        the resource properties path
	 * @return
	 *        get the properties object, and return it
	 */
	public static Properties getProperties(String path){
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			logger.error(e);
		}
		return props;
	}
	
	/**
	 * get the base virtual path
	 * 
	 * @param request
	 *         request
	 * @param serviceContextPath
	 *        context path
	 * @return
	 */
	public static String getFileVirtualPath(String serviceContextPath) {
		String basePath = "/"+serviceContextPath + "/";
		return basePath;
	}
	
	
	/**
	 * create file name
	 * 
	 * @param fileName
	 *           file name
	 * @return
	 */
	public static String makeFileName(String fileName) {
		String fileNameFormat = props.getProperty("fileNameFormat");
		if(fileNameFormat == null){
			fileNameFormat = "yyyyMMddHHmmss";
		}
		String fix = getFileSuffix(fileName);
		SimpleDateFormat format = new SimpleDateFormat(fileNameFormat);
		String strFileName = format.format(new Date()) + RandomChar.getChars(RandomChar.RANDOM_CHAR_ALL, 3) + "." + fix;
		return strFileName;
	}

	
	/**
	 *  get the file suffix
	 * @param fileName
	 *          file name
	 * @return
	 */
	public static String getFileSuffix(String fileName){
		return fileName.substring(fileName.lastIndexOf(".") + 1, fileName
				.length());
	}

	/**
	 * get the file size with one bit after '.', auto choose K/M;
	 * @param file
	 * @return
	 */
	public static String getFileSize(File file) {
		double length = file.length()/1024.0;
		
		if((length / 1024) > 1) {
			length = length / 1024;	
			int j = (int)Math.round(length * 10);
			double k = (double)j / 10.0;
			return String.valueOf(k) + "M";
		} 
		int j = (int)Math.round(length * 10);
		double k = (double)j / 10.0;
		return String.valueOf(k) + "K";
	}
	
}
