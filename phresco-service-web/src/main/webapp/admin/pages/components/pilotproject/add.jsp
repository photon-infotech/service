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

<%@ taglib uri="/struts-tags" prefix="s" %>

<%@ page import="java.util.List" %>

<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.ApplicationInfo"%>
<%@ page import="com.photon.phresco.commons.model.CoreOption"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.commons.model.Technology"%>
<%@ page import="com.photon.phresco.service.admin.actions.util.ServiceActionUtil"%>
<%@ page import="com.photon.phresco.commons.model.ArtifactGroup"%>
<%
	ApplicationInfo pilotProjectInfo = (ApplicationInfo)request.getAttribute(ServiceUIConstants.REQ_PILOT_PROINFO); 
	String fromPage = (String)request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE); 
	List<Technology> technologies = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
	
	String title = ServiceActionUtil.getTitle(ServiceUIConstants.PILOT_PROJECTS, fromPage);
	String buttonLbl = ServiceActionUtil.getButtonLabel(fromPage);
	String pageUrl = ServiceActionUtil.getPageUrl(ServiceUIConstants.PILOT_PROJECTS, fromPage);
	String progressTxt = ServiceActionUtil.getProgressTxt(ServiceUIConstants.PILOT_PROJECTS, fromPage);
	String versioning = (String)request.getAttribute(ServiceUIConstants.REQ_VERSIONING);
	String disabledVer ="";
	if (StringUtils.isNotEmpty(versioning)) {
		disabledVer = "disabled";
	}
	
	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "btn-primary";
	if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_PILOT_PROJECTS)) {
		per_disabledStr = "disabled";
		per_disabledClass = "btn-disabled";
	}
	
	//For edit
    String name = "";
	String version = "";
    String description = "";
    String groupId = "";
    String artifactId = "";
    String pilotProjectName = "";
    String jarVersion = "";
    String pilotProjectVersion = "";
    boolean isSystem = false;
    ArtifactGroup pilotContent = null;
    if (pilotProjectInfo != null) {
   		name = pilotProjectInfo.getName();
   		pilotProjectName = pilotProjectInfo.getName();
   		description = pilotProjectInfo.getDescription();
   		version = pilotProjectInfo.getVersion();
   		pilotProjectVersion = pilotProjectInfo.getPilotContent().getVersions().get(0).getVersion();
    	isSystem = pilotProjectInfo.isSystem();
    	pilotContent = pilotProjectInfo.getPilotContent();
    }
%>

<form id="formPilotProAdd" class="form-horizontal customer_list">
	<h4 class="hdr">
		<%= title %> 
	</h4>
	
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.name'/>
			</label>
			<div id="pilotProName" class="controls">
				<input id="pilotname" placeholder="<s:text name='place.hldr.pilot.add.name'/>" <%= disabledVer %> value="<%= name %>" maxlength="30" title="30 Characters only" class="input-xlarge" type="text" name="name">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.desc'/>
			</label>
			<div class="controls">
				<textarea id="pilotDesc" placeholder="<s:text name='place.hldr.pilot.add.desc'/>"  
					maxlength="150" title="150 Characters only" class="input-xlarge" type="text" name="description"><%= description %></textarea>
			</div>
		</div>
		
		<div class="control-group" id="applyControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name="Technology" /> </label>
			<div class="controls">
				<select id="multiSelect" name="techId">
				 	<%
				 	String techId = "";
				 	if(ServiceUIConstants.EDIT.equals(fromPage)) { 
				 		techId = pilotProjectInfo.getTechInfo().getVersion();
				   	}
				 	if (technologies != null) {
				 		String selectedStr = "";
						for (Technology technology : technologies) { 
							if (techId.equals(technology.getId())) {
								selectedStr = "selected";
							} else {
								selectedStr = "";
							}
					%>
						<option <%= disabledVer %> value="<%=technology.getId() %>" <%= selectedStr %>><%=technology.getName() %></option>
					<%
							}
						}
					%>
				</select><span class="help-inline applyerror" id="techError"></span>
			</div>
		</div>
        
        <!-- POM details starts -->
		<div id="jarDetailsDiv" class="hideContent">
			<div class="control-group" id="groupIdControl">
				<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.groupid'/>
				</label>
				<div class="controls">
					<input name="groupId" class="input-xlarge" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.groupId'/>">
					<span class="help-inline" id="groupIdError"></span>
				</div>
			</div>
			<div class="control-group" id="artifactIdControl">
				<label class="control-label labelbold">
					<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.artifactid'/>
				</label>
				<div class="controls">
					<input name="artifactId" class="input-xlarge" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.artifactId'/>">
					<span class="help-inline" id="artifactIdError"></span>
				</div>
			</div>
			
			<div class="control-group" id="verControl">
				<label class="control-label labelbold">
					<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.jar.version'/>
				</label>
				<div class="controls">
					<input name="jarVersion" class="input-xlarge" type="text"
						placeholder="<s:text name='place.hldr.pilot.add.version'/>">
					<span class="help-inline" id="jarversionError"></span>
				</div>
			</div>
		</div>
		<!-- POM details ends -->
         
		<div class="control-group" id="pilotProFileControl">
			<label class="control-label labelbold">
				<% if(fromPage != ServiceUIConstants.EDIT || StringUtils.isNotEmpty(versioning)) { %>
				<span class="mandatory">*</span><% } %>&nbsp;<s:text name='lbl.hdr.comp.projsrc'/>
			</label>
			
			<div class="controls" style="float: left; margin-left: 3%;">
				<div id="pilotPro-file-uploader" class="file-uploader" title = "<s:text name='title.file.size'/>">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript>
				</div>
			</div>
			<span class="help-inline fileError" id="pilotProFileError"></span>
		</div>
		
		<% 
			 if (ServiceUIConstants.EDIT.equals(fromPage) && StringUtils.isNotEmpty(pilotProjectName)) { %>
		   	 <div class="control-group" >
                <label class="control-label labelbold"> <s:text name="lbl.hdr.pilotproject.download" /> </label>
			      <div class="controls">
						<a href="#" onclick="downloadFile();"><%= pilotProjectName %>-<%= pilotProjectVersion %></a>
          		   </div>
			       
          		   </div>
			 </div>
		<% } %>	
	</div>
	
	<div class="bottom_button">
		<%
			String disabledClass = "btn-primary";
			String disabled = "";
			if (isSystem) {
				disabledClass = "btn-disabled";
				disabled = "disabled";
			} 
		%>	
		<input type="button" id="" class="btn <%= disabledClass %> <%= per_disabledClass %>" <%= per_disabledStr %> <%= disabled %> value='<%= buttonLbl %>'
			onclick="validate('<%= pageUrl %>', $('#formPilotProAdd'), $('#subcontainer'), '<%= progressTxt %>',$('.content_adder: input'));"/>
		<input type="button" id="pilotprojCancel" class="btn btn-primary" onclick="getPilotProjects();" value="<s:text name='lbl.btn.cancel'/>"/>
	</div>
	
    <!-- Hidden Fields -->
    <input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
    <input type="hidden" name="projectId" value="<%=  pilotProjectInfo != null ?  pilotProjectInfo.getId() : "" %>"/>
    <input type="hidden" name="oldName" value="<%=  pilotProjectInfo != null ?  pilotProjectInfo.getName() : "" %>"/> 
    <input type="hidden" name="customerId" value="<%= customerId %>"> 
    <input type="hidden" name="pilotContentId" value="<%= pilotContent != null ? pilotContent.getId() : "" %>"/> 
    <input type="hidden" name="pilotContentArtiId" value="<%= pilotContent != null ? pilotContent.getArtifactId() : "" %>"/> 
    <input type="hidden" name="pilotContentGroId" value="<%= pilotContent != null ? pilotContent.getGroupId() : "" %>"/> 
    <input type="hidden" name="pilotContentVersion" value="<%= pilotContent != null ? pilotContent.getVersions().get(0).getVersion() : "" %>"/>
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
	}

	$(document).ready(function() {
		hideLoadingIcon();
        createUploader();
    
        <% if (ServiceUIConstants.EDIT.equals(fromPage)) { %>
    		//$("#multiSelect").attr("disabled","disabled");
    	<% } %>
    	
    	if (<%=isSystem%>) { 
    		disableUploadButton($("#pilotPro-file-uploader"));
        }
        
     	// To check for the special character in name
        $('#pilotname').bind('input propertychange', function (e) {
            var name = $(this).val();
            name = checkForSplChr(name);
            $(this).val(name);
        });
	});
	
	function getPilotProjects() {
		showLoadingIcon();
		loadContent('pilotprojList', $('#formPilotProAdd'), $('#subcontainer'));
	}

	function findError(data) {
		if (!isBlank(data.nameError)) {
			showError($("#nameControl"), $("#nameError"), data.nameError);
		} else {
			hideError($("#nameControl"), $("#nameError"));
		}
		
		if (!isBlank(data.technologyError)) {
			showError($("#applyControl"), $("#techError"), data.technologyError);
		} else {
			hideError($("#applyControl"), $("#techError"));
		}

		if (!isBlank(data.fileError)) {
			showError($("#pilotProFileControl"), $("#pilotProFileError"), data.fileError);
		} else {
			hideError($("#pilotProFileControl"), $("#pilotProFileError"));
			if ( <%= fromPage.equalsIgnoreCase(ServiceUIConstants.ADD) %> ) {
                disableUploadButton($("#pilotPro-file-uploader"));
        }
		}
		
		if (!isBlank(data.groupIdError)) {
			showError($("#groupIdControl"), $("#groupIdError"), data.groupIdError);
		} else {
			hideError($("#groupIdControl"), $("#groupIdError"));
		}
		
		if (!isBlank(data.artifactIdError)) {
			showError($("#artifactIdControl"), $("#artifactIdError"), data.artifactIdError);
		} else {
			hideError($("#artifactIdControl"), $("#artifactIdError"));
		}
		
		if (!isBlank(data.jarVerError)) {
			showError($("#verControl"), $("#jarversionError"), data.jarVerError);
		} else {
			hideError($("#verControl"), $("#jarversionError"));
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
			uploadId: 'pilotProjUploadId',
			allowedExtensions : ["zip"],
			type: 'pilotProZip',
			buttonLabel: '<s:label key="lbl.hdr.comp.pilotpro.upload" />',
			typeError : '<s:text name="err.invalid.zip.selection" />',
			params: {type: 'pilotProZip'}, 
			debug: true
		});
	}
	
	function removeUploadedJar(obj, btnId) {
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
		enableDisableUploads(type, $("#" + btnId));
		jarError('', type)
	} 
	 
	function downloadFile() {
		window.location.href="admin/pilotUrl?" + $('#formPilotProAdd').serialize();
	}
</script>