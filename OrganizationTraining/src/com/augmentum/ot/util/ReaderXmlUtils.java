package com.augmentum.ot.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.augmentum.ot.dataObject.ErrorCode;
import com.augmentum.ot.dataObject.constant.ConfigureConstants;

public class ReaderXmlUtils {
	private Logger logger = Logger.getLogger(ReaderXmlUtils.class);
	private SAXReader saxReader;
	private Document doc;
	private Element root;
	private Element element;
	private ErrorCode errorCode;
	private String id;
	private String errorMessage;
	private String logMessage;
	private String flag;
	private InputStream is;
	private static final Map<String, ErrorCode> errorCodes = new HashMap<String, ErrorCode>();
	@SuppressWarnings("unchecked")
	public void readXML() {
		is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(ConfigureConstants.EXCEPTION_XML_FILE);
		saxReader = new SAXReader();
		Iterator i;
		Iterator childIterator;
		if (is != null) {
			try {
				doc = saxReader.read(is);
			} catch (DocumentException e) {
				logger.error(e);
			}
			root = doc.getRootElement();
		}

		if (root != null) {
			i = root.elementIterator();
			while (i.hasNext()) {
				errorCode = new ErrorCode();
				element = (Element) i.next();
				childIterator = element.elementIterator();
				id = element.attributeValue("id");
				errorCode.setErrorCodeId(id);
				while (childIterator.hasNext()) {
					element = (Element) childIterator.next();
					if (element.getName().equals("errorMessageKey")) {
						errorMessage = element.getText();
						errorCode.setErrorMessageKey(errorMessage);
					} else if (element.getName().equals("flag")) {
						flag = element.getText();
						errorCode.setFlag(flag);
					} else if (element.getName().equals("logMessage")) {
						logMessage = element.getText();
						errorCode.setLogMessage(logMessage);
					}
					errorCodes.put(id, errorCode);
				}
			}
		}
	}

	public static Map<String, ErrorCode> getErrorCodes() {
		if (errorCodes.isEmpty()) {
			ReaderXmlUtils xmlReader = new ReaderXmlUtils();
			xmlReader.readXML();
			return errorCodes;
		} else {
			return errorCodes;
		}
	}
	
	public static String loadXML(String fileName) {
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(fileName);
		SAXReader reader = new SAXReader();
		Document doc = null;
		String xml = null;
		try {
			doc = reader.read(is);
			xml = doc.asXML();
		} catch (DocumentException e) {
			//no operate
		}
		return xml;
	}
	/*
	public static void main(String[] args) {
		Map<String, ErrorCode> messages = getErrorCodes();
		for (String errorCodeId : messages.keySet()) {
			System.out.println(errorCodeId);
			System.out.println(messages.get(errorCodeId).getErrorMessageKey());
			System.out.println(messages.get(errorCodeId).getLogMessage());
		}
	}
	*/
}
