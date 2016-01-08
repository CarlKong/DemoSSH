<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/common/common.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/common/error.css">
<title><s:text name="_error_title"/></title>
</head>
<body>
<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
  <div class="html_body_div"> 
    <div class="error_background">
      <div class="message_background">
        <div class="error_image_500"></div>
        <div class="error_message_404_500">
          <div class="error_text_404_500"><s:text name="500ValidateError"/></div>
          <div><a class="error_click_home"><s:text name="backHome"/></a></div>
        </div>
      </div>
    </div>
  </div>
<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="<%=basePath %>js/common/error.js"></script>
</html>
