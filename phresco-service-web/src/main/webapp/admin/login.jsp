<%--

    Framework Web Archive

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
<!doctype html>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>
<html>
<head>
	<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
	<meta content="width=device-width" name="viewport">
	<title>Phresco</title>
	<link rel="icon" type="image/png" href="images/favicon.png">

	<!-- basic js -->
	<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>

	<!-- commons.js -->
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/jquery.activity-indicator-1.0.0.js"></script>

	<!-- Pop Up box -->
		<script type="text/javascript" src="js/bootstrap-modal.js"></script>
		<script type="text/javascript" src="js/bootstrap-transition.js"></script>

	<link rel="stylesheet" href="css/bootstrap.css">
	<link type="text/css" rel="stylesheet" href="theme/photon/css/phresco_default.css" id="phresco">
	<link type="text/css" rel="stylesheet" class="changeme" title="phresco" href="theme/photon/css/photon_theme.css">
	<!-- media queries css -->
	<link type="text/css" rel="stylesheet" href="theme/photon/css/media-queries.css" id="media-query">

<script type="text/javascript">
$(document).ready(function() {
	var localstore = localStorage['color'];
	if (localstore != null) {
		applyTheme();
	}
    $('#popupOk').css('color','white');
    <%
		String cmdLogin = (String) request.getAttribute("cmdLogin");
		if (cmdLogin != null) {
	%>
		createBookmarkLink('Phresco', '<%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= request.getContextPath() %>');
	<% } %>
});

/* function createBookmarkLink(title, url) {
	if (window.sidebar)  {							// firefox
		window.sidebar.addPanel(title, url, "");
	} else if(window.opera && window.print) { 		// opera
		var elem = document.createElement('a');
		elem.setAttribute('href',url);
		elem.setAttribute('title',title);
		elem.setAttribute('rel','sidebar');
		elem.click();
	} else if(document.all) {						// ie
		window.external.AddFavorite(url, title);
	} else if (window.chrome) {
		chrome.bookmarks.create({'parentId': bookmarkBar.id,'title': 'Extension bookmarks'},
			function(newFolder) {
			console.log("added folder: " + newFolder.title);
		});
	}
} */

</script>
</head>
<body class="lgnBg">
    <div class="logincontainer">
		<div class="logindiv">
			<div class="loginhead">
				<span><img src="theme/photon/images/userlogin.png"></img></span>
				<div class="alert alert-error">
					<%
			     		String loginError = (String)request.getAttribute(ServiceUIConstants.REQ_LOGIN_ERROR);
					%>
					<div id="logimErrMesg" class="lgnError"><%= StringUtils.isEmpty(loginError) ? "" : loginError %></div>
				</div>
				<div class="clear"></div>
			</div>
		<form name="login" action="login" method="post" class="marginBottomZero">
			<img class="leftslideone" src="theme/photon/images/leftslide.png"></img>
			<div class="loginuser">
				<div class="loginimg">
					<img src="theme/photon/images/user.png"></img>
				</div>
				<div class="logintext clearfix">	
				     <label class="labellg" for="xlInput" class="lgnfieldLb1">Username:</label>
						<%
							String userName = (String)request.getAttribute(ServiceUIConstants.REQ_USER_NAME);
						%>
				    <input class="xlarge settings_text lgnField" id="xlInput" id="username" name="username" type="text" 
				     	autofocus maxlength="63" value="<%= StringUtils.isNotEmpty(userName) ? userName : "" %>" title="63 Characters only" placeholder="Enter the username" />
				</div>
				<div class="clear"></div>
			</div>
			<img class="rightslideone" src="theme/photon/images/rightslide.png"></img>
			<img class="leftslidetwo" src="theme/photon/images/leftslide.png"></img>
			<div class="loginpassword">
				<div class="loginimg">
					<img src="theme/photon/images/password.png"></img>
				</div>
				<div class="logintext clearfix">	
	                <label class="labellg" for="xlInput" class="lgnFieldLbl">Password:</label>
		                <%
							String password = (String)request.getAttribute(ServiceUIConstants.REQ_PASSWORD);
		                	String fromPage = (String)request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
						%>
	                <input class="xlarge settings_text lgnField" id="xlInput" id="password" name="password" type="password"
	                	maxlength="63" title="63 Characters only" value ="" placeholder="Enter the password"/>
	               	
					<script type="text/javascript">
						<% if (StringUtils.isNotEmpty(userName) && StringUtils.isEmpty(password)) { %>
								$('input[name="password"]').focus();
						<% } %>	
					</script>
				</div>
				<div class="clear"></div>
			</div>
			<img class="rightslidetwo" src="theme/photon/images/rightslide.png"></img>
			<div class="loginfoot">
				<label class="checkbox login_check">
	                <input id="rememberMe" type="checkbox" name="rememberme">
	                <labelrem>Remember me</labelrem>
				</label>
				<span class="clearfix ">
	                <div class="input lgnBtnLabel">
	                    <input type="submit" value="LOGIN" class="btn loginbutton" id="Login">
	            	</div>
	            </span>
            	<a href="JavaScript:void(0);" id="forgotPassword" >Forgot Password</a>
	            <input type="hidden" name="loginFirst" value="false"/>
	            <input type="hidden" name="fromPage" value="<%= fromPage %>"/>
			</div>	
		</form>
		</div>	
	</div>	
	<div id="loadingIconDiv" class="hideContent"> 
			
		</div>
		
			<!-- Popup div Starts-->
	    <div class="popup_div" id="popup_div">
	    
	    </div>
	    <!-- Popup div Ends-->
	    
	    <!-- Popup Starts-->
	    <div id="popupPage" class="modal hide fade popup">
			<div class="modal-header">
				<a class="close" data-dismiss="modal" >&times;</a>
				<h3 id="popupTitle"><s:text name='lbl.progress'/></h3>
			</div>
			<div class="modal-body" id="popupPage_modal-body">
			</div>
			<div class="modal-footer">
				<div class="errMsg" id="reportMsg"></div>
				<div class="popuploadingIcon" id="popuploadingIcon"></div>
				<div id="updateMsg" class="updateMsg"></div>
				<input type="button" class="btn btn-primary" data-dismiss="modal" id="popupCancel" value="Cancel"/>
				<input type="button" class="btn btn-primary popupOk"  id="popupOk" onClick="popupOnOk(this);" value="Submit"/>
			</div>
		</div>
	    <!-- Popup Ends -->
</body>
</html>
<script type="text/javascript">
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
	    	  document.getElementById("rememberMe").checked=true
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
		document.getElementById("rememberMe").checked=false;
	}
	$("#forgotPassword").click(function() {
		console.info("enterd")
		yesnoPopup('showForgotPwdPopup', 'Forgot Password', 'forgotPassword');
	});

//}
</script>