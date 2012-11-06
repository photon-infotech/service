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

<%@ page import="com.photon.phresco.commons.model.Property"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>
<%@ page import="com.photon.phresco.service.admin.actions.util.ServiceActionUtil"%>

<% 
	Property globalUrl = (Property) request.getAttribute(ServiceUIConstants.REQ_GLOBURL_URL);
    String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
    
    String title = ServiceActionUtil.getTitle(ServiceUIConstants.GLOBALURLS, fromPage);
	String buttonLbl = ServiceActionUtil.getButtonLabel(fromPage);
	String pageUrl = ServiceActionUtil.getPageUrl(ServiceUIConstants.GLOBALURLS, fromPage);
	String progressTxt = ServiceActionUtil.getProgressTxt(ServiceUIConstants.GLOBALURLS, fromPage);
	
    String id = "";
    String name = "";
	String description = "";
	String url  = "";
	if (globalUrl != null) {
		id = globalUrl.getId();
		name = globalUrl.getName();
		url = globalUrl.getValue();
		if (StringUtils.isNotEmpty(globalUrl.getDescription())) {
			description = globalUrl.getDescription();
		}
	}
%>

<form id="formGlobalUrlAdd" class="form-horizontal customer_list">
	<h4 class="hdr">
	  <%= title %>
	</h4>	
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.name'/>
			</label>
			<div class="controls">
				<input id="globalUrlName" placeholder="<s:text name='place.hldr.globalurl.add.name'/>" value= "<%= name %>" maxlength="30" title="30 Characters only"
					class="input-xlarge" type="text" name="name">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.desc'/>
			</label>
			<div class="controls">
				<textarea id="globalUrlDesc" placeholder="<s:text name='place.hldr.globalurl.add.desc'/>" maxlength="150" title="150 Characters only"
					class="input-xlarge" name="description"><%= description %></textarea>
				
			</div>
		</div>
		
		<div class="control-group" id="urlControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.glblurl.url'/>
			</label>
			<div class="controls">
				<input id="globalUrl" placeholder="<s:text name='place.hldr.globalurl.add.url'/>" 
					value="<%= url %>" class="input-xlarge" type="text" name="url">
				<span class="help-inline" id="urlError"></span>
			</div>
		</div>
	</div>
	
	<div class="bottom_button">
			<input type="button" id="" class="btn btn-primary"  value="<%= buttonLbl %>"
				onclick="validate('<%= pageUrl %>', $('#formGlobalUrlAdd'), $('#subcontainer'), '<%= progressTxt %>');" />
    
			<input type="button" id="globalurlCancel" class="btn btn-primary" value="<s:text name='lbl.btn.cancel'/>"
				onclick="loadContent('globalurlList', $('#formGlobalUrlAdd'), $('#subcontainer'));" />
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="fromPage" value="<%= fromPage %>">
	<input type="hidden" name="globalurlId" value="<%= id %>">
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
	}

	$(document).ready(function() {
		enableScreen();
		
		// To check for the special character in name
        $('#globalUrlName').bind('input propertychange', function (e) {
            var name = $(this).val();
            name = checkForSplChr(name);
            $(this).val(name);        
		});
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