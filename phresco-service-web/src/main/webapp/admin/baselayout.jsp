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
<%@ taglib uri="/struts-tags" prefix="s"%>

<%@ page import="com.photon.phresco.commons.model.User"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<!DOCTYPE html>
<html>
	<head>
		<title>Helios</title>
		<link rel="icon" type="image/png" href="images/favicon.png">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
		
		<link rel="stylesheet" href="css/bootstrap.css">
		<link rel="stylesheet" href="theme/photon/css/phresco_default.css" id="phresco">
		<link rel="stylesheet" href="theme/photon/css/photon_theme.css" class="changeme" title="phresco">
		<link rel="stylesheet" href="theme/photon/css/media-queries.css" id="media-query">
		<link rel="stylesheet" href="css/datepicker.css"> <!-- used for date picker-->
		<link rel="stylesheet" href="css/jquery.ui.all.css"> <!-- used for date picker -->
 		<link rel="stylesheet" href="css/fileuploader.css"> <!-- used for file upload -->
		
		<!-- basic js -->
		<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
		
		<!-- document resizer -->
		<script type="text/javascript" src="js/windowResizer.js"></script>
		
		<!-- Progress Bar -->
		<script type="text/javascript" src="js/jquery.loadmask.js"></script>
		<script type="text/javascript" src="js/loading.js"></script>
		
		<!-- Pop Up box -->
		<script type="text/javascript" src="js/bootstrap-modal.js"></script>
		<script type="text/javascript" src="js/bootstrap-transition.js"></script>
		
		<!-- right panel scroll bar -->
		<script type="text/javascript" src="js/home.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		
		<!-- file upload -->
		<script type="text/javascript" src="js/fileuploader.js"></script>

		<!-- date picker -->
		<script type="text/javascript" src="js/jquery.ui.datepicker.js"></script>
	   	<script type="text/javascript" src="js/jquery.cookie.js"></script>
		
		<!-- Scrollbar -->
		<script type="text/javascript" src="js/scrollbars.js"></script>
		<script type="text/javascript" src="js/main.js"></script>
		<script type="text/javascript" src="js/jquery.event.drag-2.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.ba-resize.min.js"></script>
		<script type="text/javascript" src="js/jquery.mousewheel.js"></script>
		<script type="text/javascript" src="js/mousehold.js"></script>
		<script type="text/javascript" src="js/jquery.fileDownload.js"></script>
		<!-- Editable ComboBox -->
		<script type="text/javascript" src="js/jquery.editable.combobox.js"></script>
		<script type="text/javascript" src="js/ddslick.js"></script>
		<script type="text/javascript" src="js/jquery.activity-indicator-1.0.0.js"></script>
		
			
		<script type="text/javascript">
		    $(document).ready(function() {
		    	enableScreen();
		    	applyTheme();
		    	$(".styles").click(function() {
					localStorage.clear();
					var value = $(this).attr("rel");
					localStorage["color"]= value;
					applyTheme();
				});

				// function to show user info in toggle 
				$('div aside.usersettings div').hide(0);
				$('div aside.usersettings').click(function() {
					$('div aside.usersettings div').slideToggle(0);
				});

				// to show user info on mouse over
				$('#signOut aside').mouseenter(function() {
					$("div aside.usersettings div").hide(0);
					$(this).children("div aside.usersettings div").show(0);
				}).mouseleave(function() {
					$("div aside.usersettings div").hide(0);
				});

				clickMenu($("a[name='headerMenu']"), $("#container"));
				loadContent("dashboard", '', $("#container"));
				activateMenu($("#dashboard"));
				showWelcomeImage();
				
				$('#clipboard').click(function(){
					copyToClipboard($('.modal-body').text());
				});

		    	$(".lefttopnav").click(function(){
		    		$('#dashboard').attr('class', 'active');
		    	})
		    	
		    	$(".leftbotnav").click(function(){
		    		$('#components').attr('class', 'active');
		    	})
		    	
		    	$(".rightbotnav").click(function(){
		    		$('#adminMenu').attr('class', 'active');
		    	})
		    	
		    	//Call for about service
				$("#about, #abtPopUp").click(function() {
					yesnoPopup('about', '<s:text name="lbl.abt.service"/>');
				});
		    	
		    	$("#goToHome").click(function() {
		    		var custId = $(".dd-selected-value").val();
		    		if ('photon' == custId) {
		    			window.open("http://www.photon.in", '_blank');
		    		}
		    	});
		    		
		    	
		    	$(".close, #popupClose").click(function() {
		    		 showParentPage(); 
	    			$("#updateMsg").empty();
	    			$("#reportMsg").empty();
		    	});
		    	
		    	var logoImgUrl = '<%= request.getAttribute("enCodedLogo") %>';
				$('#logoImg').attr("src",  "data:image/png;base64," + logoImgUrl);
			});
		</script>
	</head>
	<body>
        <%
            User userInfo = (User) session.getAttribute(ServiceUIConstants.SESSION_USER_INFO);
            String displayName = "";
            if (userInfo != null) {
                displayName = userInfo.getDisplayName();
            }
        %>
	   
		<div class="modal-backdrop fade in popupalign"></div>
		
		
		<!-- In Progress starts -->
		<div id="progressbar" class="progressPosition">
			<div id="indicatorInnerElem">
				<span id="progressnum"></span>
			</div>
			<div id="indicator"></div>
		</div>
		<!-- In Progress Ends -->
		
		<div id="loadingIconDiv" class="hideContent"> 
			
		</div>
		
		<!-- Header Starts Here -->
		<header>
			<div class="header">
				<div class="Logo">
					 <a href="#" id="goToHome"><img class="headerlogoimg" id="logoImg" src="" alt="logo"></a>
				</div>
				<div class="headerInner">
					<div class="nav_slider">
						<nav class="headerInnerTop">
							<ul>
								<li class="wid_home"><a href="#" class="inactive" name="headerMenu" id="dashboard">
								    <s:label key="lbl.hdr.dash"  theme="simple"/></a>
                                </li>
								<li class="wid_app"><a href="#" class="inactive" name="headerMenu" id="components">
								    <s:label key="lbl.hdr.comp" theme="simple"/></a>
								</li>
								<li class="wid_set"><a href="#" class="inactive" name="headerMenu" id="adminMenu">
								    <s:label key="lbl.hdr.adm"  theme="simple"/></a>
								</li>
							</ul>
							<div class="close_links" id="close_links">
								<a href="JavaScript:void(0);">
									<div class="headerInnerbottom">
										<img src="images/uparrow.png" alt="logo">
									</div>
								</a>
							</div>
						</nav>
					</div>
					<div class="quick_lnk" id="quick_lnk">
						<a href="JavaScript:void(0);">
							<div class="quick_links_outer">
								<s:label key="lbl.hdr.quicklink" theme="simple"/>
							</div>
						</a>
					</div>
				</div>
				<div id="signOut" class="signOut">
					<aside class="usersettings">
						<%= displayName %>
						<img src="images/downarrow.png" class="arrow">
                        <div class="userInfo" >
                            <ul>
                            	<li id="themeContainer" class="theme_change"><a href="#">Themes</a>
                                	<ul>
                                    	<li>Photon&nbsp;<a href="#" class="styles" href="#" rel="theme/photon/css/photon_theme.css"><img src="images/photon_theme.png"></a></li>
                                        
                                    </ul>
                                </li>
                                <li><a href="#" id="about" ><s:text name="lbl.usrset.abtservice"/></a></li>
                                <li><a href="<s:url action='admin/logout'/>"><s:text name="lbl.usrset.signout"/></a></li>
                            </ul>
                        </div>
					</aside>
				</div>
			</div>
		</header>
		<!-- Header Ends Here -->
		
		
		<!-- Content Starts Here -->
		<section class="main_wrapper">
			<section class="wrapper">
			
				<!-- Shortcut Top Arrows Starts Here -->
				<aside class="shortcut_top">
					<div class="lefttopnav">
						<a href="JavaScript:void(0);" id="dashboard" name="headerMenu"
							class="arrow_links_top">
							<span class="shortcutRed" id=""></span>
							<span class="shortcutWh" id="">
							<s:label key="lbl.hdr.topleftnavlab"  theme="simple"/></span>
						</a>
					</div>
					<div class="righttopnav">
						<a href="JavaScript:void(0);" class="abtPopUp" class="arrow_links_top"><span
							class="shortcutRed" id=""></span><span class="shortcutWh"
							id="">
							<s:label key="lbl.hdr.toprightnavlab" theme="simple"/></span>
						</a>
					</div>
				</aside>
				<!-- Shortcut Top Arrows Ends Here -->
				
				<section id="container" class="container">
				
					<!-- Content Comes here-->
					
				</section>
				
				<!-- Shortcut Bottom Arrows Starts Here -->
				<aside class="shortcut_bottom">
				   <div class="leftbotnav">
						<a href="JavaScript:void(0);" id="components" name="headerMenu"
							class="arrow_links_bottom"><span class="shortcutRed" id=""></span><span
							class="shortcutWh" id=""><s:label key="lbl.hdr.bottomleftnavlab"  theme="simple"/></span>
						</a>
					</div>
					<div class="rightbotnav">
						<a href="JavaScript:void(0);" id="adminMenu" name="headerMenu"
							class="arrow_links_bottom"><span class="shortcutRed" id="lf_tp1"></span><span
							class="shortcutWh" id="lf_tp2"><s:label key="lbl.hdr.bottomrightnavlab" theme="simple"/></span>
						</a>
					</div>
				</aside>
				
				<!-- Shortcut Bottom Arrows Ends Here -->
			</section>
			
			<!-- Slide News Panel Starts Here -->
			<aside>
				<section>
					<div class="right">
						<div class="right_navbar active">
							<div class="barclose">
								<div class="lnclose">Latest&nbsp;News</div>
							</div>
						</div>
						<div class="right_barcont">
							<div class="searchsidebar">
								<div class="newstext">
									Latest<span>News</span>
								</div>
								<div class="topsearchinput">
									<input name="" type="text">
								</div>
								<div class="linetopsearch"></div>
							</div>
							<div id="tweets" class="sc7 scrollable dymanic paddedtop">
								<div class="tweeterContent"></div>
							</div>
						</div>
						<br clear="left">
					</div>
				</section>
			</aside>
			<!-- Slide News Panel Ends Here -->
		</section>
		<!-- Content Ends Here -->
		
		<!-- Footer Starts Here -->
		<footer>
			<address class="copyrit">&copy; 2013 Photon Infotech Pvt Ltd. |<a onclick="open_win();"> www.photon.in</a></address>
		</footer>
		<!-- Footer Ends Here -->
		
		<!-- Popup div Starts-->
	    <div class="popup_div" id="popup_div">
	    
	    </div>
	    <!-- Popup div Ends-->
	    
	    <!-- Popup Starts-->
	    <div id="popupPage" class="modal hide fade">
			<div class="modal-header">
				<a class="close" data-dismiss="modal" >&times;</a>
				<h3 id="popupTitle"><s:text name='lbl.progress'/></h3>
			</div>
			<div class="modal-body" id="popupPage_modal-body">
			</div>
			<div class="modal-footer">
			    <img class="popuploadingImg"></img>
				<div class="errMsg" id="reportMsg"></div>
				<div id="updateMsg" class="updateMsg"></div>
				<input type="button" class="btn btn-primary" data-dismiss="modal" id="popupCancel" value="<s:text name='lbl.btn.cancel'/>"/>
				<input type="button" class="btn btn-primary popupOk" onClick="popupOnOk(this);" value="<s:text name='lbl.btn.ok'/>"/>
			</div>
		</div>
	    <!-- Popup Ends -->
	    
	    <!-- Command Display starts -->
		<div class="build_cmd_div" id="build-output">
		</div>
	</body>
</html>