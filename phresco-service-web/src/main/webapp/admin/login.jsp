<%--

    Service Web Archive

    Copyright (C) 1999-2013 Photon Infotech Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<html>
	<head>
		<title>Phresco Admin</title>
		<link rel="icon" type="image/png" href="images/favicon.png">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
		<link rel="stylesheet" href="css/bootstrap.css">
		<link type="text/css" rel="stylesheet" href="theme/photon/css/phresco_default.css" id="phresco">
        <link type="text/css" rel="stylesheet" class="changeme" title="phresco" href="theme/photon/css/photon_theme.css">
		<!-- media queries css -->
		<link type="text/css" rel="stylesheet" href="theme/photon/css/media-queries.css" id="media-query">
		
		<!-- basic js -->
		<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
		
		<!-- right panel scroll bar -->
		<script type="text/javascript" src="js/home.js"></script>
		
		<!-- commons.js -->
		<script type="text/javascript" src="js/common.js"></script>
	   
		<!-- document resizer -->
		<script type="text/javascript" src="js/windowResizer.js"></script>
		
		<!-- Scrollbar -->
		<script type="text/javascript" src="js/scrollbars.js"></script>
		<script type="text/javascript" src="js/main.js"></script>
		<script type="text/javascript" src="js/jquery.event.drag-2.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.ba-resize.min.js"></script>
		<script type="text/javascript" src="js/jquery.mousewheel.js"></script>
		<script type="text/javascript" src="js/mousehold.js"></script>
		

		<script type="text/javascript">
			$(document).ready(function() {
				var localstore = localStorage['color'];
				if (localstore != null) {
					applyTheme();
				}
			});
			
		</script>
	</head>

	<body class="lgnBg">
		<header>
			<div class="header">
				<div class="Logo">
					 <a href="#" id="goToHome"><img class="headerlogoimg" src="theme/photon/images/phresco_header_red.png" alt="logo"></a>
				</div>
			</div>
			
			<div class="innoimg">
			   <img class="phtaccinno" src="theme/photon/images/acc_inov_red.png" alt="" border="0" onclick="window.open('http://www.photon.in','_blank');">
			</div>
		</header>
      
		<div class="lgnintro_container lgnContainer">
	        <div class="welcome" id="welcome">
                  <img class="welcomeimg" src="theme/photon/images/welcome_photon_red.png">
             </div> 
			<div class="lgnintro_container_left">
			<h1 class="l_align"><s:text name="lbl.login"/></h1><h1 class="lp_align"></h1>    
			   
				<form name="login" action="login" method="post" class="marginBottomZero">
					<!--  UserName starts -->
					<div class="clearfix">
						 <label class="labellg"><s:text name="lbl.login.username"/></label>
						<%
							String userName = (String)request.getAttribute(ServiceUIConstants.REQ_USER_NAME);
						%>
						 <input class="xlarge settings_text lgnField" id="xlInput" name="username" autofocus placeholder="<s:text name="place.hldr.login.name"/>" 
						 		value="<%= StringUtils.isNotEmpty(userName) ? userName : "" %>" type="text">
						</div>
					<!--  UserName ends -->
						  
					<!--  Password starts -->
					<div class="clearfix">
						<label class="labellg"><s:text name="lbl.login.pwd"/></label>
						<%
							String password = (String)request.getAttribute(ServiceUIConstants.REQ_PASSWORD);
						%>
						<input class="xlarge settings_text lgnField" id="xlInput" name="password" value="" 
								type="password" placeholder="<s:text name="place.hldr.login.pwd"/>">
						
						<script type="text/javascript">
							<% if (StringUtils.isNotEmpty(userName) && StringUtils.isEmpty(password)) { %>
									$('input[name="password"]').focus();
							<% } %>	
						</script>
					</div>
					<!--  Password ends -->
						  
					<!-- Remember me check starts  -->
					<div class="login_check">
						  <input id="rememberme" name="rememberme" type="checkbox">
						  <labelrem><s:text name="lbl.login.rembr.me"/></labelrem>
						
					</div>
					<!-- Remember me check ends  -->
				   
					
					<div class="clearfix">
						<div class="input lgnBtnLabel">
							<input type="hidden" name="loginFirst" value="false"> 
							<input type="submit" value="Login" class="btn btn-primary lgnBtn">
							<%
	                        	String loginError = (String)request.getAttribute(ServiceUIConstants.REQ_LOGIN_ERROR);
	                    	%>
							&nbsp;&nbsp;&nbsp;<div class="lgnError"><%= StringUtils.isNotEmpty(loginError) ? loginError : "" %></div>
						</div>
					</div>
				</form>
			</div>
		</div>
	
		<div class="footer_div login">
		   <footer>
			  <div class="copyrit">
				 &copy; 2013.Photon Infotech Pvt Ltd. |
			   <a href="http://www.photon.in/"> www.photon.in</a>
			 </div>
		   </footer>
		</div>
	</body>
</html>
<script type="text/javascript">
//$(document).ready(function() {
//});
	ReadCookie();
	function ReadCookie()
	{
	   var allcookies = document.cookie;

	   // Get all the cookies pairs in an array
	   cookiearray  = allcookies.split(';');

	   // Now take key value pair out of this array
	   for(var i=0; i<cookiearray.length; i++){
	      name = cookiearray[i].split('=')[0];
	      value = cookiearray[i].split('=')[1];
	      var newName = removeSpaces(name); 
	      if (newName === "username" && !isBlank(value)) {
	    	  $('input[name="username"]').prop("value", value);
	      }
	      if (newName === "password" && !isBlank(value)) {
	    	  $('input[name="password"]').prop("value", value);
	    	  document.getElementById("rememberme").checked=true
	      }
	   }
	}
	//IF CHECKBOX IS CHECKED, COOKIE WILL BE SET
	$("input:checkbox").change(function() {
		var status = $(this).attr("checked");
		if (status) {
			var un = $('input[name="username"]').val();
			var pwd = $('input[name="password"]').val();
			document.cookie = "username="+un;
			document.cookie = "password="+pwd;
		} else {
			eraseCookie();
		}
	});	
	
	function eraseCookie() {
		document.cookie = "username=";
		document.cookie = "password=";
		document.getElementById("rememberme").checked=false;
	}

//}
</script>