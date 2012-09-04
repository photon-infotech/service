<%--
  ###
  Service Web Archive
  
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
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.List"%>

<%@ page import="com.photon.phresco.model.GlobalURL"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<% 
	GlobalURL globalUrls = (GlobalURL) request.getAttribute(ServiceUIConstants.REQ_GLOBURL_URL);
    String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
    String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
    String name = "";
	String description = "";
	String url  = "";
	if(globalUrls != null) {
		if (StringUtils.isNotEmpty(globalUrls.getDescription())) {
			description = globalUrls.getDescription();
		}
	}
%>

<form id="formGlobalUrlAdd" class="form-horizontal customer_list">
	<h4>
	<%
			if (StringUtils.isNotEmpty(fromPage)) {
		%>
			<s:label key="lbl.hdr.adm.url.edit.title"/>
		<%
			} else {
		%>
			<s:label key="lbl.hdr.adm.url.tiltle" theme="simple"/>
		<%
			}
		%>
	</h4>	
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.name'/>
			</label>
			<div class="controls">
				<input id="input01" placeholder="<s:text name='place.hldr.globalurl.add.name'/>" value= "<%= name %>" 
				                          class="input-xlarge" type="text" name="name">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.adm.desc'/>
			</label>
			<div class="controls">
				<input id="input01" placeholder="<s:text name='place.hldr.globalurl.add.desc'/>" value="<%= description %>" 
				                         class="input-xlarge" type="text" name="description">
				
			</div>
		</div>
		
		<div class="control-group" id="urlControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.glblurl.url'/>
			</label>
			<div class="controls">
				<input id="input01" placeholder="<s:text name='place.hldr.globalurl.add.url'/>" value="<%= url %>" class="input-xlarge" type="text" name="url">
				<span class="help-inline" id="urlError"></span>
			</div>
		</div>
	</div>
	
	<div class="bottom_button">
	  	<% if (StringUtils.isNotEmpty(fromPage)) { %>
		<input type="button" id="globalurlUpdate" class="btn btn-primary"
				onclick="validate('globalurlUpdate', $('#formGlobalUrlAdd'), $('#subcontainer'), 'Updating GlobalURL');" 
		        value="<s:text name='lbl.hdr.comp.update'/>"/>
    <% } else { %>		
		<input type="button" id="globalurlSave" class="btn btn-primary"
		        onclick="validate('globalurlSave', $('#formGlobalUrlAdd'), $('#subcontainer'), 'Creating GlobalURL');" 
		        value="<s:text name='lbl.hdr.comp.save'/>"/>
	<% } %> 
		<input type="button" id="globalurlCancel" class="btn btn-primary" 
		                   onclick="loadContent('globalurlList', $('#formGlobalUrlAdd'), $('#subcontainer'));" 
		                         value="<s:text name='lbl.hdr.comp.cancel'/>"/>
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="customerId" value="<%= customerId %>">
	
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
	}

	$(document).ready(function() {
		enableScreen();
	});
	
	function findError(data) {
		if (data.nameError != undefined) {
			showError($("#nameControl"), $("#nameError"), data.nameError);
		} else {
			hideError($("#nameControl"), $("#nameError"));
		}
		
		if (data.urlError != undefined) {
			showError($("#urlControl"), $("#urlError"), data.urlError);
		} else {
			hideError($("#urlControl"), $("#urlError"));
		}
	}
</script>