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

<%@ page import="com.photon.phresco.model.Technology" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<% 
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
%>

<form id="formMenu">
	<div id="subTabcontent">
		<div id="navigation">
			<ul>
				<li><a href="#" class="selected" id="module" name="featureTab"><s:text name='lbl.hdr.comp.featrs.modules'/></a></li>
				<li><a href="#" class="unselected" id="js" name="featureTab"><s:text name='lbl.hdr.comp.featrs.jslib'/></a></li>
			</ul>
		</div>
		
		<div id="feature_tab" style="height:76%;">
	
		</div>
	</div>
	
	<!-- Hidden Fields -->
    <input type="hidden" name="customerId" value="<%= customerId %>">
    
</form>

<script type="text/javascript">
$(document).ready(function() {
	var params = "type=";
	params = params.concat("module");
	loadContent("featuresList", $('#formMenu'), $("#feature_tab"), params);
	
	$("#module").click(function(){
		$("#js").removeClass("selected");
		$("#js").addClass("unselected");
		$("#module").addClass("selected");
		 var params = "type=";
		 params = params.concat("module");
		loadContent("featuresList", $('#formMenu'), $("#feature_tab"), params);
	});
	
	$("#js").click(function(){
		$("#module").removeClass("selected");
		$("#module").addClass("unselected");
		$("#js").addClass("selected");
		 var params = "type=";
		 params = params.concat("js");
		loadContent("featuresList", $('#formMenu'), $("#feature_tab"), params);
	});
});
</script>