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

<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.ArtifactGroup.Type" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<% 
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
	String type = (String) request.getAttribute(ServiceUIConstants.REQ_FEATURES_TYPE);
%>

<form id="formMenu">
	<div id="subTabcontent">
		<nav>
			<div class="subLinks">
				<ul class="tabs subtabs">
					<li class="subtabsLi">
						<a href="#" class="active" id="technologies" name="featureTab" additionalParam="type=<%= Type.FEATURE.name() %>">
							<s:text name="lbl.hdr.comp.featrs.modules" />
						</a>
					</li>
					<li>
						<a href="#" class="inactive" id="technologies" name="featureTab" additionalParam="type=<%= Type.JAVASCRIPT.name() %>">
							<s:text name="lbl.hdr.comp.featrs.jslib"/>
						</a>
					</li>
					<li>
						<a href="#" class="inactive" id="technologies" name="featureTab" additionalParam="type=<%= Type.COMPONENT.name() %>">
							<s:text name="lbl.hdr.comp.component"/>
						</a>
					<li>
					
				</ul>
			</div>
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
		clickMenu($("a[name='featureTab']"), $("#featureContainer"), $('#formMenu'));
		
		//To load the page by default. Condition is checked when this page is returned after save
		loadContent("technologies", $('#formMenu'), $("#featureContainer"), "type=<%= Type.FEATURE.name() %>");
		
		//To activate the module menu by default
		activateMenu($("#module"));
	});
</script>