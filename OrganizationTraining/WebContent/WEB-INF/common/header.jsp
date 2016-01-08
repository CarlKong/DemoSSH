<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="bread" uri="/WEB-INF/tld/breadCrumbs.tld"%>
<%@ taglib prefix="privilege" uri="/WEB-INF/tld/privilege" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Header</title>
<link rel="icon" href="<%=path %>/image/headIMG/favicon.ico" type="image/x-icon">
<script type="text/javascript" src="<%=basePath %>js/message.js.jsp"></script>
<link type="text/css" href="<%=basePath %>css/base/resetCss.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/base/base.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/headAndFoot/headCss.css" rel="stylesheet">
</head>
<body>
<input type="hidden" id="basePath" value="<%=basePath %>">
    <div id="header_body">
        <div id="header_top">
            <div id="header_logo"></div>
            <div id="header_text">
                <a>
                    <s:text name="_project_full_name"></s:text>
                </a>
            </div>
            <div id="header_top_right">
                <div id="header_login">
                    <a id="header_login_name">
                        <s:text name="_welcome"></s:text>
                        ${loginEmployee.augUserName}
                    </a>
                    <input type="hidden" id="loginEmployeeName" value="${loginEmployee.augUserName}"></input>
                    <a class="decoration_hover cursor_hand" id="header_login_control"  href="<%=basePath %>common/logOut">
                        <s:text name="_logout"></s:text>
                    </a>
                </div>
                <div id="header_i18n">
                	<input type="hidden" id="i18nLan" value="${language}"></input>
                	<s:if test="locale.getLanguage()=='en'">
						<a onclick="i18n('<%=basePath%>','zh_CN')">
                        	<s:text name="_current_language"></s:text>
                        </a>
                    </s:if>
                    <s:else>
                    	<a onclick="i18n('<%=basePath%>','en_US')">
                        	<s:text name="_current_language"></s:text>
                        </a>
                    </s:else>
                </div>
            </div>
        </div>
        <div id="header_navigation">
            <div class="header_navigation_start"></div>
            <div class="header_navigation_button">
                <div class="cursor_hand">
				    <a href="<%=basePath %>dashboard/dashboard_dashboard">
                        <s:text name="_home"></s:text>
                    </a>
				</div>
            </div>
            <div class="header_navigation_button">
            	<div class="cursor_hand">
                    <a id="header_course">
                        <s:text name="_course"></s:text>
                    </a>
				</div>
                <ul>
                 <!-- Search Course Menu --> 
                	<privilege:operate operateID="course[course_searchCourse]">
                       <li id="course_searchCourse">
                         <a href="<%=basePath %>course/course_searchCourse">
                            <s:text name="_search_course"></s:text>
                         </a>
                      </li>
                    </privilege:operate>
                    
                 <!-- Create Course Menu --> 
                    <privilege:operate operateID="course[course_createCourse]">
                       <li>
                         <a href="<%=basePath %>course/course_createCourse">
                            <s:text name="_create_course"></s:text>
                         </a>
                       </li>
                     </privilege:operate>
                     
                  <!-- My Course Menu -->
                    <li>
                        <a href="<%=basePath %>plan/plan_myCourse">
                            <s:text name="_my_courses"></s:text>
                        </a>
                    </li>
                </ul>
            </div>
            <div class="header_navigation_button">
            	<div class="cursor_hand">
                    <a id="header_plan">
                        <s:text name="_plan"></s:text>
                    </a>
				</div>
                <ul>
                 <!-- Search Plan Menu --> 
                    <privilege:operate operateID="plan[plan_searchPlan]">
                       <li id="plan_searchPlan">
                         <a href="<%=basePath %>plan/plan_searchPlan">
                            <s:text name="_search_plan"></s:text>
                         </a>
                      </li>
                     </privilege:operate>
                     
                  <!-- Create Plan Menu --> 
                    <privilege:operate operateID="plan[plan_createPlan]">
                      <li>
                        <a href="<%=basePath %>plan/plan_createPlan">
                            <s:text name="_create_plan"></s:text>
                        </a>
                      </li>
                     </privilege:operate>
                     
                   <!-- My Plan Menu -->
                    <li>
                        <a href="<%=basePath %>plan/plan_myPlan">
                            <s:text name="_my_plans"></s:text>
                        </a>
                    </li>
                </ul>
            </div>
         <!-- Admin Menu -->
          <privilege:operate visibleRoles="Admin">
            <div class="header_navigation_button">
            	<div class="cursor_hand">
                    <a id="header_role">
                        <s:text name="_admin"></s:text>
                    </a>
				</div>
				<privilege:operate operateID="roleList[roleList_roleList]">
                  <ul>
                    <li id="roleList_roleList">
                        <a  href="<%=basePath %>roleList/roleList_roleList">
                            <s:text name="_configure_roles"></s:text>
                        </a>
                    </li>
                  </ul>
                </privilege:operate>
            </div>
          </privilege:operate>
          
          <!-- Create Course and Create Plan -->
          <privilege:operate visibleRoles="Admin,Training Master">
            <div class="header_navigation_end">
                <div class="header_navigation_create cursor_hand">
                    <div class="header_navigation_create_button">
                        <a>
                            <s:text name="_create_new"></s:text>
                        </a>
                        <div class="show_dropdown"></div>
                    </div>
                    <div id="header_navigation_create_down">
                        <ul>
                            <li>
                                <div style="width: 120px; height: 2px;"></div>
                                 <a href="<%=basePath %>course/course_createCourse">
                                    <s:text name="_course"></s:text>
                                 </a>
                            </li>
                            <li>
                                <a href="<%=basePath %>plan/plan_createPlan">
                                    <s:text name="_plan"></s:text>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
          </privilege:operate> 
        </div>
        <div id="header_crumbs">
            <bread:bread/>
        </div>
    </div>
</body>
<script type="text/javascript" src="<%=basePath %>/js/jquery-1.6.4.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/common/menu.js"></script>
</html>
