<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib  prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="_search_plan"></s:text></title>
<link type="text/css" href="<%=basePath %>css/common/common.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>searchList/css/dataList.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>filterBox/css/filterBox.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>jquery.poshytip/css/tip-green.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>jquery.ui/css/jquery-ui-1.8.18.custom.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>datepicker/css/datepicker.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/search/searchCommon.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>messageBar/css/messagebar.css" type="text/css">
<link type="text/css" href="<%=basePath %>attachmentUI/csslib/attachmentUI.css" rel="stylesheet" />
</head>
<body>
<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
<!-- message bar -->
<input type="hidden" id="operationFlag" value="${param.operationFlag}"></input>
<input type="hidden" id="plan_prefixIDValue" value="${param.prefixIDValue}"></input>
<div id ="messageBar">
	<span id="messageBar_delete" class="message_span"><s:text name="_plan_messageBar_delete"></s:text></span>
	<span id="messageBar_cancel" class="message_span"><s:text name="_plan_messageBar_cancel"></s:text></span>
</div>
<!-- Down load popup -->
<div id="downLoadPopup">
	<div id="downLoadDiv" class="content">
		<img id="downClosePic" src="<%=basePath %>image/buttonIMG/ICN_Close_11x11.png">
        <div id = "downLoadContent" >
        	<div id="upload_attachment_list">
           		<s:hidden id="attachmentList"></s:hidden>
            </div>
        </div>
	</div>
</div>
<div class="content">
	<jsp:include page="/WEB-INF/plan/searchPlanContent.jsp"></jsp:include>
</div>
<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="<%=basePath%>js/search/searchCommon.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/common/jquery.layer.js" ></script>
<script type="text/javascript" src="<%=basePath %>jquery.poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=basePath %>searchList/js/DataList.js"></script>
<script type="text/javascript" src="<%=basePath%>filterBox/js/filterBox.js"></script>
<script type="text/javascript" src="<%=basePath%>js/plan/searchPlan.js"></script>
<script type="text/javascript" src="<%=basePath%>js/plan/searchToDetail.js"></script>
<script type="text/javascript" src="<%=basePath%>jquery.ui/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath %>messageBar/js/messagebar.js"></script>
<script type="text/javascript" src="<%=basePath %>attachmentUI/script/attachmentUI.js"></script>
</html>