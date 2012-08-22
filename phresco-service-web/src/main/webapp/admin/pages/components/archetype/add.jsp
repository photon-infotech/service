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
<%@ page import="org.apache.commons.collections.CollectionUtils"%>
<%@ page import="java.util.List"%>

<%@ page import="com.photon.phresco.model.ApplicationType"%>
<%@ page import="com.photon.phresco.model.Technology"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<%
	Technology technology = (Technology)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPE);
	String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
	List<ApplicationType> appTypes = (List<ApplicationType>)request.getAttribute(ServiceUIConstants.REQ_APP_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
	
	//For edit
	String name = "";
	String desc = "";
	List<String> versions = null;
	String versionComment = "";
	boolean isSystem = false;
	if (technology != null) {
		name = technology.getName();
		desc = technology.getDescription();
		versions = technology.getVersions();
		versionComment = technology.getVersionComment();
		isSystem = technology.isSystem();
	}
%>

<form id="formArcheTypeAdd" class="form-horizontal customer_list">
	<h4 class="hdr">
		<%
			if (StringUtils.isNotEmpty(fromPage)) {
		%>
			<s:label key="lbl.hdr.comp.arhtyp.edit.title"/>
		<%
			} else {
		%>
			<s:label key="lbl.hdr.comp.arhtyp.title"/>
		<%
			}
		%>
	</h4>
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.name' />
			</label>
			<div class="controls">
				<input id="name" placeholder='<s:text name="place.hldr.archetype.add.name"/>' class="input-xlarge" type="text" 
					name="name" value="<%= StringUtils.isNotEmpty(name) ? name : "" %>" maxlength="30" title="30 Characters only">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label labelbold"> <s:text
					name='lbl.hdr.comp.desc' /> </label>
			<div class="controls">
				<textarea id="description" class="input-xlarge" placeholder='<s:text name="place.hldr.archetype.add.desc"/>' 
					rows="3" name="description" maxlength="150" title="150 Characters only"><%= StringUtils.isNotEmpty(desc) ? desc : "" %></textarea>
			</div>
		</div>

		<div class="control-group" id="verControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.version' />
			</label>
			<div class="controls">
				<input id="version" placeholder='<s:text name="place.hldr.archetype.add.version"/>' class="input-xlarge" 
					type="text" name="version" value="<%= CollectionUtils.isNotEmpty(versions) ? versions : "" %>" maxlength="30" title="30 Characters only">
				<span class="help-inline" id="verError"></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label labelbold"> <s:text
					name='lbl.hdr.com.vercmnt' /> </label>
			<div class="controls">
				<textarea name="versionComment" placeholder='<s:text name="place.hldr.archetype.add.ver.comment"/>' class="input-xlarge" 
					rows="2" cols="10" maxlength="150" title="150 Characters only"><%= StringUtils.isNotEmpty(versionComment) ? versionComment : "" %></textarea>
			</div>
		</div>

		<div class="control-group apptype" id="appControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.apptype' />
			</label>
			<div class="controls">
				<select id="select01" name="apptype">
					<%
						if (appTypes != null)  {
							for (ApplicationType appType : appTypes) {
					%>
							<option value="<%= appType.getName() %>"><%= appType.getName() %></option>
					<%
							}
						}
					%>
				</select> 
				<span class="help-inline" id="appError"></span>
			</div>
		</div>

		<div class="control-group" id="fileControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.applnjar' />
			</label>
			<div class="controls" style="float: left; margin-left: 3%;">
				<div id="appln-file-uploader" class="file-uploader">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript>
				</div>
			</div>
			<span class="help-inline fileError" id="fileError"></span>
		</div>

		<div class="control-group" id="pluginControl">
			<label class="control-label labelbold"> <s:text
					name='lbl.hdr.comp.pluginjar' /> </label>
			<div class="controls" style="float: left; margin-left: 3%;">
				<div id="plugin-file-uploader" class="file-uploader">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript> 
				</div>
			</div>
			<span class="help-inline pluginError" id="pluginError"></span>
		</div>
	</div>

	<div class="bottom_button">
		<%
			String disabledClass = "btn-primary";
			String disabled = "";
			if (isSystem) {
				disabledClass = "btn-disabled";
				disabled = "disabled";
			}

			if (StringUtils.isNotEmpty(fromPage)) {
		%>
				<input type="button" id="archetypeUpdate" class="btn <%= disabledClass %>" <%= disabled %>
					onclick="validate('archetypeUpdate', $('#formArcheTypeAdd'), $('#subcontainer'), '<s:text name='lbl.prog.arche.update'/>');"
					value="<s:text name='lbl.hdr.comp.update'/>" />
		
		<% } else { %>
				<input type="button" id="archetypeSave" class="btn btn-primary"
					onclick="validate('archetypeSave', $('#formArcheTypeAdd'), $('#subcontainer'), '<s:text name='lbl.prog.arche.save'/>');"
					value="<s:text name='lbl.hdr.comp.save'/>" />
		<% } %>
		
		<input type="button" id="archetypeCancel" class="btn btn-primary" value="<s:text name='lbl.hdr.comp.cancel'/>" 
            onclick="loadContent('archetypesList', $('#formArcheTypeAdd'), $('#subcontainer'));"/>
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
	<input type="hidden" name="techId" value="<%= technology != null ? technology.getId() : "" %>"/>
	<input type="hidden" name="oldName" value="<%= technology != null ? technology.getName() : "" %>"/>
	<input type="hidden" name="customerId" value="<%= customerId %>"> 
</form>

<script type="text/javascript">
    $(document).ready(function() {
        enableScreen();
        
        createUploader();
        
        // To focus the name textbox by default
        $('#name').focus();

        // To check for the special character in name
        $('#name').bind('input propertychange', function (e) {
            var name = $(this).val();
            name = checkForSplChr(name);
            $(this).val(name);
        });

        // To check for the special character in version
        $('#version').bind('input propertychange', function (e) {
            var version = $(this).val();
            version = checkForSplChrExceptDot(version);
            $(this).val(version);
        });

        // To remove the plugin jar file field
        $('.del').live('click', function() {
            $(this).parent().parent().remove();
        });
    });

    function findError(data) {
        if (data.nameError != undefined) {
            showError($("#nameControl"), $("#nameError"), data.nameError);
        } else {
            hideError($("#nameControl"), $("#nameError"));
        }

        if (data.verError != undefined) {
            showError($("#verControl"), $("#verError"), data.verError);
        } else {
            hideError($("#verControl"), $("#verError"));
        }
        if (data.appError != undefined) {
            showError($("#appControl"), $("#appError"), data.appError);
        } else {
            hideError($("#appControl"), $("#appError"));
        }

        if (data.fileError != undefined) {
            showError($("#fileControl"), $("#fileError"), data.fileError);
        } else {
            hideError($("#fileControl"), $("#fileError"));
        }
    }
    
    function pluginJarError(data) {
		if (data != undefined ) {
			showError($("#pluginControl"), $("#pluginError"), data);
		} else {
			hideError($("#pluginControl"), $("#pluginError"));
		}
	}
	
	function applnJarError(data) {
		if (data != undefined) {
			showError($("#fileControl"), $("#fileError"), data);
		} else {
			hideError($("#fileControl"), $("#fileError"));
		}
	}
    
	function createUploader() {
		var applnUploader = new qq.FileUploader({
            element: document.getElementById('appln-file-uploader'),
            action: 'uploadJar',
            multiple: false,
            type: 'applnJar',
            buttonLabel: '<s:label key="lbl.comp.arhtyp.upload" />',
            typeError : '<s:text name="err.invalid.file.selection" />',
            params: {type: 'applnJar'}, 
            debug: true
        });
		 
		var pluginUploader = new qq.FileUploader({
           	element: document.getElementById('plugin-file-uploader'),
           	action: 'uploadJar',
           	multiple: true,
           	type: 'pluginJar',
           	buttonLabel: '<s:text name="lbl.comp.arhtyp.upload" />',
           	typeError : '<s:text name="err.invalid.file.selection" />',
			params: {type: 'pluginJar'}, 
           	debug: true
		});
   	}
   
	function removeUploadedJar(obj) {
		$(obj).parent().remove();
		var params = "uploadedJar=";
		params = params.concat($(obj).attr("id"));
		params = params.concat("&type=");
		params = params.concat($(obj).attr("tempattr"));
		$.ajax({
			url : "removeUploadedJar",
			data : params,
			type : "POST",
			success : function(data) {
			}
		});
		enableDisableUpload();
		pluginJarError();
		applnJarError();
	}
	
	function enableDisableUpload() {
		if ($('ul[temp="applnJar"] > li').length === 1 ) {
			$('#appln-file-uploader').find("input[type='file']").attr('disabled','disabled');
			$('#appln-file-uploader').find($(".qq-upload-button")).removeClass("btn-primary qq-upload-button").addClass("disabled");
		} else {
			$('#appln-file-uploader').find("input[type='file']").attr('disabled', false);
			$('#appln-file-uploader').find($(".btn")).removeClass("disabled").addClass("btn-primary qq-upload-button");
		}
	} 
</script>