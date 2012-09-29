<%--
  ###
  Framework Web Archive
  
  Copyright (C) 1999 - 2012 Photon Infotech Inc.
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ###
  --%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<% 
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
	String type = (String) request.getAttribute(ServiceUIConstants.REQ_FEATURES_TYPE);
%>

<form id="formMenu">
	<div id="subTabcontent">
		<nav>
			<ul class="tabs subtabs">
				<li class="subtabsLi">
					<a href="#" class="active" id="modulesTechnologies" name="featureTab">
						<s:label key="lbl.hdr.comp.featrs.modules" theme="simple"/>
					</a>
				</li>
				<li>
					<a href="#" class="inactive" id="jsLibTechnologies" name="featureTab">
						<s:label key="lbl.hdr.comp.featrs.jslib" theme="simple"/>
					</a>
				</li>
			</ul>
		</nav>
		
		<div id="featureContainer" style="height:76%;">
	
		</div>
	</div>
	
	<!-- Hidden Fields -->
    <input type="hidden" name="customerId" value="<%= customerId %>">
</form>

<script type="text/javascript">
	$(document).ready(function() {
		//Handles the click event of the sub tabs
		clickMenu($("a[name='featureTab']"), $("#featureContainer"));
		
		//To load the page by default. Condition is checked when this page is returned after save
		<% if (ServiceUIConstants.REQ_FEATURES_TYPE_JS.equals(type)) { %>
			loadContent("jsLibTechnologies", $('#formMenu'), $("#featureContainer"));
		<% } else { %>
			loadContent("modulesTechnologies", $('#formMenu'), $("#featureContainer"));
		<% } %>
		
		//To activate the module menu by default
		activateMenu($("#module"));
	});
</script>