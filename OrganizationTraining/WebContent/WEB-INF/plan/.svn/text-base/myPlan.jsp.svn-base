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
<title><s:text name='_my_plans'></s:text></title>
<link type="text/css" href="<%=basePath %>css/common/common.css" rel="stylesheet">
<link type="text/css" href="<%=basePath%>css/common/commonTab.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>searchList/css/dataList.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>filterBox/css/filterBox.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>jquery.poshytip/css/tip-green.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>jquery.ui/css/jquery-ui-1.8.18.custom.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>datepicker/css/datepicker.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/search/searchCommon.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>attachmentUI/csslib/attachmentUI.css" rel="stylesheet" />
</head>
<body>
<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
<!-- get employeeRoleNames from session-->
<input type="hidden" id="roleNames" value="<%=session.getAttribute("employeeRoleNames")%>">
<!-- tab -->
<input type="hidden" id="tabName_master" value="<s:text name='dashboard_master'></s:text>"></input>
<input type="hidden" id="tabName_trainer" value="<s:text name='dashboard_trainer'></s:text>"></input>
<input type="hidden" id="tabName_trainee" value="<s:text name='dashboard_trainee'></s:text>"></input>
<!-- status -->
<input type="hidden" id="red_message" value="<s:text name='red_message'></s:text>"></input>
<input type="hidden" id="green_message" value="<s:text name='green_message'></s:text>"></input>
<input type="hidden" id="yellow_message" value="<s:text name='yellow_message'></s:text>"></input>
<input type="hidden" id="gray_message" value="<s:text name='gray_message'></s:text>"></input>
<!-- Down load popup -->
<div id="downLoadPopup">
	<div id="downLoadDiv" class="content">
        <div id = "downLoadContent" >
        	<div id="upload_attachment_list">
           		<s:hidden id="attachmentList"></s:hidden>
            </div>
        </div>
        <img id="downClosePic" src="<%=basePath %>image/buttonIMG/ICN_Close_11x11.png">
	</div>
</div>
<!-- my plan part -->
<div class="content">
	<div id="myPlan_tab">
		<!-- draw tab use common tab -->
	</div>
	<div id="myPlan_search">
		<jsp:include page="/WEB-INF/plan/searchPlanContent.jsp"></jsp:include>
	</div>
</div>
<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="<%=basePath %>js/common/commonTab.js"></script>
<script type="text/javascript" src="<%=basePath%>js/plan/searchToDetail.js"></script>
<script type="text/javascript" src="<%=basePath%>js/search/searchCommon.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/common/jquery.layer.js" ></script>
<script type="text/javascript" src="<%=basePath %>jquery.poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=basePath %>searchList/js/DataList.js"></script>
<script type="text/javascript" src="<%=basePath%>filterBox/js/filterBox.js"></script>
<script type="text/javascript" src="<%=basePath%>jquery.ui/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath %>attachmentUI/script/attachmentUI.js"></script>
<script type="text/javascript" src="<%=basePath %>js/plan/myPlan.js"></script>
</html>