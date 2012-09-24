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

<%@ taglib uri="/struts-tags" prefix="s" %>

<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.List" %>


<%@ page import="com.photon.phresco.commons.model.ApplicationInfo"%>
<%@ page import="com.photon.phresco.commons.model.CoreOption"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.commons.model.Technology"%>

<% 
    ApplicationInfo pilotProjectInfo = (ApplicationInfo)request.getAttribute(ServiceUIConstants.REQ_PILOT_PROINFO); 
	String fromPage = (String)request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE); 
	List<Technology> technologys = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);

	//For edit
    String name = "";
	String version = "";
    String description = "";
    boolean isSystem = false;
    if (pilotProjectInfo != null) {
    	if (StringUtils.isNotEmpty(pilotProjectInfo.getName())) {
    		name = pilotProjectInfo.getName();
    	}
    	if (StringUtils.isNotEmpty(pilotProjectInfo.getDescription())) {
    		description = pilotProjectInfo.getDescription();
    	}
    	if (StringUtils.isNotEmpty(pilotProjectInfo.getVersion())) {
    		version = pilotProjectInfo.getVersion();
    	}
    	isSystem = pilotProjectInfo.isSystem();
    }
%>

<form id="formPilotProAdd" class="form-horizontal customer_list">
	<h4 class="hdr">
	<% if (StringUtils.isNotEmpty(fromPage)) { %>
		<s:label key="lbl.hdr.comp.edit.pltprjt.title" theme="simple"/>
    <% } else { %>
		<s:label key="lbl.hdr.comp.add.pltprjt.title" theme="simple"/>
	<% } %> 
	</h4>
	
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.name'/>
			</label>
			<div class="controls">
				<input id="pilotname" placeholder="<s:text name='place.hldr.pilot.add.name'/>" value="<%= name %>" maxlength="30" title="30 Characters only" class="input-xlarge" type="text" name="name">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.desc'/>
			</label>
			<div class="controls">
				<textarea id="pilotDesc" placeholder="<s:text name='place.hldr.pilot.add.desc'/>" value="<%= description %>" 
					maxlength="150" title="150 Characters only" class="input-xlarge" type="text" name="description"></textarea>
			</div>
		</div>
		
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.version'/>
			</label>
			<div class="controls">
				<input id="versionname" placeholder="<s:text name='place.hldr.pilot.add.version'/>" value="<%= version %>" maxlength="30" title="30 Characters only" class="input-xlarge" type="text" name="version">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>

		<div class="control-group" id="applyControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name="Technology" /> </label>
			<div class="controls">
				<select id="multiSelect" name="techId" id="tech">
					
				 	<% if(StringUtils.isNotEmpty(fromPage)) { //for edit 
				 	     
				 	        List<CoreOption> techIds = pilotProjectInfo.getPilotContent().getAppliesTo();
				 	         for(CoreOption techId : techIds){
				 	     
				 	%>
							<option value="<%= pilotProjectInfo.getTechInfo().getVersion() %>"><%= techId.getTechId() %></option>
					<%     }
					     } else { // for add %>		
						<% if (technologys != null) {
								for (Technology technology : technologys) { 
							%>
									<option value="<%=technology.getId() %>"><%=technology.getName() %></option>
							<%
									}
								}
							%>
					<% } %>		
				</select><span class="help-inline applyerror" id="techError"></span>
			</div>
		</div>
        
        <!-- POM details starts -->
		<div id="jarDetailsDiv" class="hideContent">
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.groupid'/>
				</label>
				<div class="controls">
					<input name="groupId" class="input-xlarge" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.groupId'/>">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.artifactid'/>
				</label>
				<div class="controls">
					<input name="artifactId" class="input-xlarge" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.artifactId'/>">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.jar.version'/>
				</label>
				<div class="controls">
					<input name="jarVersion" class="input-xlarge" type="text"
						placeholder="<s:text name='place.hldr.pilot.add.version'/>">
				</div>
			</div>
		</div>
		<!-- POM details ends -->
         
		<div class="control-group" id="pilotProFileControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.projsrc'/>
			</label>
			
			<div class="controls" style="float: left; margin-left: 3%;">
				<div id="pilotPro-file-uploader" class="file-uploader">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript>
				</div>
			</div>
			<span class="help-inline fileError" id="pilotProFileError"></span>
		</div>
	</div>
	
	<div class="bottom_button">
	  <%String disabledClass = "btn-primary";
		String disabled = "";
		if (isSystem) {
			disabledClass = "btn-disabled";
			disabled = "disabled";
		} 
	    if (StringUtils.isNotEmpty(fromPage)) { %>
			<input type="button" id="pilotprojUpdate" class="btn <%= disabledClass %>" <%= disabled %> value="<s:text name='lbl.hdr.comp.update'/>"
				onclick="validate('pilotprojUpdate', $('#formPilotProAdd'), $('#subcontainer'), 'Updating Pilotproject');" />
		<%
			} else {
		%>
			<input type="button" id="pilotprojSave" class="btn btn-primary" value="<s:text name='lbl.hdr.comp.save'/>"
				onclick="validate('pilotprojSave', $('#formPilotProAdd'), $('#subcontainer'), 'Creating Pilotproject');" />
		<% } %>
		<input type="button" id="pilotprojCancel" class="btn btn-primary" onclick="loadContent('pilotprojList', '', $('#subcontainer'));" value="<s:text name='lbl.hdr.comp.cancel'/>"/>
	</div>
	
    <!-- Hidden Fields -->
    <input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
    <input type="hidden" name="projectId" value="<%=  pilotProjectInfo != null ?  pilotProjectInfo.getId() : "" %>"/>
    <input type="hidden" name="oldName" value="<%=  pilotProjectInfo != null ?  pilotProjectInfo.getName() : "" %>"/>  
    <input type="hidden" name="customerId" value="<%= customerId %>"> 
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
	}

	$(document).ready(function() {
		enableScreen();
        createUploader();
        
        <% if (StringUtils.isNotEmpty(fromPage)) { %>
    	$("#tech").attr("disabled","disabled");
    	<% } %>
        
     	// To check for the special character in name
        $('#pilotname').bind('input propertychange', function (e) {
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
		 
		if (data.fileError != undefined) {
			showError($("#pilotProFileControl"), $("#pilotProFileError"), data.fileError);
		} else {
			hideError($("#pilotProFileControl"), $("#pilotProFileError"));
		} 
	}
	
	function jarError(data, type) {
		var controlObj;
		var msgObj;
		if (type == "pilotProZip") {
			controlObj = $("#pilotProFileControl");
			msgObj = $("#pilotProFileError");
		} 
		if (data != undefined && !isBlank(data)) {
			showError(controlObj, msgObj, data);
		} else {
			hideError(controlObj, msgObj);
		}
	}
	
	function createUploader() {
		var fileUploader = new qq.FileUploader({
			element: document.getElementById('pilotPro-file-uploader'),
			action: 'uploadPilotProjZip',
			multiple: false,
			allowedExtensions : ["zip"],
			type: 'pilotProZip',
			buttonLabel: '<s:label key="lbl.hdr.comp.pilotpro.upload" />',
			typeError : '<s:text name="err.invalid.zip.selection" />',
			params: {type: 'pilotProZip'}, 
			debug: true
		});
	} 
	 
	function removeUploadedJar(obj) {
		$('#jarDetailsDiv').hide();
		$(obj).parent().remove();
		var type = $(obj).attr("tempattr"); 
		var params = "uploadedJar=";
		params = params.concat($(obj).attr("id"));
		params = params.concat("&type=");
		params = params.concat(type);
		$.ajax({
			url : "removePilotProjJar",
			data : params,
			type : "POST",
			success : function(data) {
			}
		});
		enableDisableUpload();
		jarError('', type)
	} 
	    
	function enableDisableUpload() {
		if ($('ul[temp="pilotProZip"] > li').length === 1 ) {
			$('#pilotPro-file-uploader').find("input[type='file']").attr('disabled','disabled');
			$('#pilotPro-file-uploader').find($(".qq-upload-button")).removeClass("btn-primary qq-upload-button").addClass("disabled");
		} else {
			$('#pilotPro-file-uploader').find("input[type='file']").attr('disabled', false);
			$('#pilotPro-file-uploader').find($(".btn")).removeClass("disabled").addClass("btn-primary qq-upload-button");
		}
	} 
</script>