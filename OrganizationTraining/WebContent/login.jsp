<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="icon" href="<%=path %>/image/headIMG/favicon.ico" type="image/x-icon">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=basePath %>js/jquery-1.6.4.js"></script>
<script type="text/javascript" src="<%=basePath %>js/login/login.js"></script>
<script type="text/javascript" src="<%=basePath %>js/login/md5.js"></script>
<script type="text/javascript" src="<%=basePath %>js/message.js.jsp"></script>
<title><s:text name="login"/></title>
<link type="text/css" href="<%=basePath %>css/login.css" rel="stylesheet">
</head>
<body>
 <input type="hidden" id="lang" />
 <s:if test="!(#session.language eq null)">
   <script type="text/javascript">
     document.getElementById("lang").value='<s:property value="#session.language"/>';
   </script>
</s:if>
<div id="container">
  <div id="headDiv">
    <div id="logoDiv">
         <div id="logo"></div>
         <div class="clear"></div>
         <div id="lanDiv">
         <div id="systemDescription"><s:text name="OnlineTrainingProgramManagementSystem"></s:text></div>
             <span class="english"><input type="button" class="englishBtn" value="English"></span> 
             <span class="chiness"><input type="button"  class="chineseBtn" value="中文"></span>
         </div>
    </div>
     </div>
      <div id="headBottom"></div>
     <div class="clear"></div>
       <div id="loginContext">
       <form id="createCourseFrom" action="<%=basePath %>login" method="post">
     	<div id="loginFormLeft"><img src="<%=basePath %>/image/login/IMG_Drawing_577x395.png"></img> 
     	</div>
     	<div id="loginFormRight">
     		<div id="loginForm">
     			<span id="loginFormTitle"><s:text name="login"/></span>
     			<div id="loginFormInput">
     			<p class="loginFormP">
     				<label><s:text name="username"/>:</label>
     				<input id="showUsername" value="<s:text name="loginInputUserName"/>">
     				<input id="username" name="employeeName" type="text" value="" style="display: none;">
     			</p>
     			<p class="loginFormP">
     				<label><s:text name="password"/>:</label>
     				<input id="password" type="password" value="" style="display: none;">
     				<input id="showPassword" type="text" value="<s:text name="loginInputUserPassword"/>">
     				<input id="passwordEncode" name="employeePassword" type="password" >
     			</p>
     			</div>
     			<div id="loginMess">
     			<span id="loginMessSpan">${requestScope.loginMessage}</span>
     			</div>
     			<div class="loginFormBottom"><span></span></div>
     			<input id="loginFomrSubmit" type="submit" value="<s:text name="login"/>" >
     		</div>
     		<div id="shadowMiddle"></div>
     	</div>
     	</form>
      </div>
      <div class="clear"></div>
      <div id="shadowDiv"/>
      </div>
      <div class="clear"></div>
      <div id="footerArea">
           <div>
		     <div id="footer">
		       <span class="copyRight">Copyright © </span>
		       <span id="time">2012</span>
		       <span id="companyName">Augmentum.Inc</span>
		       <span>All Right Reserved</span>
		     </div>
		  </div>
      </div>
   </div>
</body>
</html>
