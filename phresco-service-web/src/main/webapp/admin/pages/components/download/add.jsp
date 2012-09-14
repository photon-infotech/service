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

<%@page import="java.util.ArrayList"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.model.Technology"%>
<%@ page import="com.photon.phresco.model.DownloadInfo" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %> 

<%
    DownloadInfo downloadInfo = (DownloadInfo)request.getAttribute(ServiceUIConstants.REQ_DOWNLOAD_INFO);
    String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
    List<Technology> technologies = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
    String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);

    //For edit
    String name = "";
    String description = "";
    List<String> version = new ArrayList<String>();
    if (downloadInfo != null) {
    	if (StringUtils.isNotEmpty(downloadInfo.getName())) {
    		name = downloadInfo.getName();
    	}
    	if (StringUtils.isNotEmpty(downloadInfo.getDescription())) {
    		description = downloadInfo.getDescription();
    	}
    	if (CollectionUtils.isNotEmpty(downloadInfo.getVersion())) {
    		version = downloadInfo.getVersion();
    	}
    }
%>

<form id="formDownloadAdd" class="form-horizontal customer_list">
	<h4>
	<% if (StringUtils.isNotEmpty(fromPage)) { %>
		<s:label key="lbl.hdr.adm.dwnlad.edit.title" theme="simple"/>
	<% } else { %>	
		<s:label key="lbl.hdr.adm.dwnlad.add.title" theme="simple"/>
	<% } %> 
	</h4>
	 
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.name'/>
			</label>
			<div class="controls">
				<input id="downloadName" placeholder="<s:text name='place.hldr.download.add.name'/>" 
					value="<%= name %>" maxlength="30" title="30 Characters only" class="input-xlarge" type="text" name="name">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
			
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.adm.desc'/>
			</label>
			<div class="controls">
				<textarea id="downloadDesc"  placeholder="<s:text name='place.hldr.download.add.desc'/>" class="input-xlarge" type="text"
					maxlength="150" title="150 Characters only" value="<%= description %>" name="description"></textarea>
			</div>
		</div>
		
		<div class="control-group" id="techControl">
            <label class="control-label labelbold">
                <span class="mandatory">*</span>&nbsp;<s:text name="Technology"/>
            </label>
            <div class="controls">
                <select id="multiSelect" multiple="multiple" name="technology">
                    <% 	
                    	if (technologies != null) {
							for (Technology technology : technologies) { 
								String selectedStr = "";
								if (downloadInfo != null) {
									List<String> appliesTos = downloadInfo.getAppliesTo();
									if (appliesTos.contains(technology.getId())) {
										selectedStr = "selected";
									} else {
										selectedStr = "";
									}
								}
					%>
                    			<option value="<%= technology.getId() %>" <%= selectedStr %>><%= technology.getName() %></option> 
					<% 	 
							}
						}
					%> 
                </select>
                <span class="help-inline applyerror" id="techError"></span>
            </div>
        </div>
        
        <!-- POM details starts -->
		<div id="jarDetailsDiv" class="hideContent">
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.groupid'/>
				</label>
				<div class="controls">
					<input id="grpId" name="groupId" class="input-xlarge" type="text"
						maxlength="40" title="40 Characters only" placeholder="<s:text name='place.hldr.archetype.add.groupId'/>">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.artifactid'/>
				</label>
				<div class="controls">
					<input id="arftId" name="artifactId" class="input-xlarge" type="text"
						maxlength="40" title="40 Characters only" placeholder="<s:text name='place.hldr.archetype.add.artifactId'/>">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.jar.version'/>
				</label>
				<div class="controls">
					<input id="jarVersn" class="jarVersion" class="input-xlarge" maxlength="30" title="30 Characters only" type="text"
						placeholder="<s:text name='place.hldr.download.add.version'/>">
				</div>
			</div>
		</div>
		<!-- POM details ends -->
		
		<div class="control-group" id="downloadFileControl">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.adm.dwnld.fle'/>
			</label>
			<div class="controls" style="float: left; margin-left: 3%;">
				<div id="download-file-uploader" class="file-uploader">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript>
				</div>
			</div>
			 <span class="help-inline fileError" id="fileError"></span>
		</div>
		
		<div class="control-group" id="appltControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.dwnld.appltfrm'/>
			</label>
			<div class="controls">
				<select id="multiSelect" multiple="multiple" name="application">
					<option  value="Windows">Windows</option>
					<option  value="Linux">Linux</option>
					<option  value="Mac">Mac</option>
					<option  value="Solaris">Solaris</option>
				</select>
				<span class="help-inline applyerror" id="appltError"></span>
			</div>
		</div>
			
		<div class="control-group"  id="iconControl">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.adm.dwnld.icon'/>
			</label>
			<div class="controls" style="float: left; margin-left: 3%;">
				<div id="icon-file-uploader" class="file-uploader">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript> 
				</div>
			</div>
			<span class="help-inline iconError" id="iconError"></span>
		</div>
			
		<div class="control-group" id="verControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.dwnld.ver'/>
			</label>
			<div class="controls">
				<input id="dwnVersn" placeholder="<s:text name='place.hldr.download.add.version'/>" value="<%= version %>" 
					maxlength="30" title="30 Characters only" class="input-xlarge" type="text" name="version">
				<span class="help-inline" id="verError"></span>
			</div>
		</div>
			
		<div class="control-group" id="groupControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.dwnld.group'/>
			</label>	
			<div class="controls">
				<select id="group" name="group">
					<option value="" onclick="javascript:hideDiv();">- select -</option>
					<option value="Database" onclick="javascript:hideDiv();">Database</option>
					<option value="Server" onclick="javascript:hideDiv();">Server</option>
					<option value="Editor" onclick="javascript:hideDiv();">Editor</option>
                    <option value="Others" onclick="javascript:showDiv();">Others</option>
				</select>
				<span class="help-inline" id="groupError"></span>
			</div>
		</div>
        
        <div class="control-group popupalign" id="othersDiv">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>
			</label>
			<div class="controls">
				<input id="input01" placeholder="<s:text name='place.hldr.download.add.others'/>" class="input-xlarge" 
					type="text" name="others">
				<span class="help-inline" id="othersError"></span>
			</div>
		</div>
	</div>

	<div class="bottom_button">
		<% if (StringUtils.isNotEmpty(fromPage)) { %>
			<input type="button" id="downloadUpdate" class="btn btn-primary" value="<s:text name='lbl.hdr.comp.update'/>" 
				onclick="validate('downloadUpdate', $('#formDownloadAdd'), $('#subcontainer'), 'Updating Download');" />	
        <% } else { %>
			<input type="button" id="downloadSave" class="btn btn-primary" value="<s:text name='lbl.hdr.comp.save'/>"
				onclick="validate('downloadSave', $('#formDownloadAdd'), $('#subcontainer'), 'Creating Download');" />
		<% } %>
		<input type="button" id="downloadCancel" class="btn btn-primary" onclick="loadContent('downloadList', $('#formDownloadAdd'), $('#subcontainer'));" value="<s:text name='lbl.hdr.comp.cancel'/>"/>
	</div>
	
	<!-- Hidden Fields -->
    <input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
    <input type="hidden" name="id" value="<%= downloadInfo != null ? downloadInfo.getId() : "" %>"/>
    <input type="hidden" name="oldName" value="<%= downloadInfo != null ? downloadInfo.getName() : "" %>"/>
    <input type="hidden" name="customerId" value="<%= customerId %>"> 
    <input type="hidden" name="oldVersion" value="<%= downloadInfo != null ? downloadInfo.getVersion() : "" %>"/>
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
	}
	
	$(document).ready(function() {
		enableScreen();
        createUploader(); 
        
     	// To check for the special character in name
        $('#downloadName').bind('input propertychange', function (e) {
            var name = $(this).val();
            name = checkForSplChr(name);
            $(this).val(name);
        });
	
		// To check for the special character in version ,groupId, artifactId
        $('#jarVersn, #dwnVersn, #arftId, #grpId').bind('input propertychange', function (e) {
            var version = $(this).val();
            version = checkForSplChrExceptDot(version);
            $(this).val(version);
        });
        
        // for edit - to show selected group while page loads 
        <% if (downloadInfo != null)  {%>
       		 $("#group option[value='<%= downloadInfo.getType() %>']").attr('selected', 'selected');
       	<% } %>
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
		
		if (data.appltError != undefined) {
			showError($("#appltControl"), $("#appltError"), data.appltError);
		} else {
			hideError($("#appltControl"), $("#appltError"));
		}
		
		if (data.groupError != undefined) {
			showError($("#groupControl"), $("#groupError"), data.groupError);
		} else {
			hideError($("#groupControl"), $("#groupError"));
		}
		
		if (data.techError != undefined) {
			showError($("#techControl"), $("#techError"), data.techError);
		} else {
			hideError($("#techControl"), $("#techError"));
		}
	}
	 
	function showDiv() {
		$('#othersDiv').show();
	}

	function hideDiv() {
		$('#othersDiv').hide();
	}
	
	function jarError(data, type) {
		var controlObj;
		var msgObj;
		if (type == "uploadIcon") {
			controlObj = $("#iconControl");
			msgObj = $("#iconError");
		} else if (type == "uploadFile") {
			controlObj = $("#downloadFileControl");
			msgObj = $("#fileError");
		}
		if (data != undefined && !isBlank(data)) {
			showError(controlObj, msgObj, data);
		} else {
			hideError(controlObj, msgObj);
		}
	}

	function createUploader() {
		var fileUploader = new qq.FileUploader({
			element : document.getElementById('download-file-uploader'),
			action : 'uploadFile',
			multiple : false,
			allowedExtensions : ["jar","zip","gz","exe","dll"],
			type : 'uploadFile',
			buttonLabel : '<s:label key="lbl.comp.featr.upload" />',
			typeError : '<s:text name="err.invalid.dwnloadfile.selection" />',
			params : {
				type : 'uploadFile'
			},
			debug : true
		});

		var iconUploader = new qq.FileUploader({
			element : document.getElementById('icon-file-uploader'),
			action : 'uploadImage',
			multiple : false,
			allowedExtensions : ["jpg","jpeg","png"],
			type : 'uploadIcon',
			buttonLabel : '<s:text name="lbl.hdr.comp.dwnload.icon" />',
			typeError : '<s:text name="err.invalid.image.selection" />',
			params : {
				type : 'uploadIcon'
			},
			debug : true
		});
	}

	function removeUploadedJar(obj) {
		$('#jarDetailsDiv').hide();
		$(obj).parent().remove();
		var type = $(obj).attr("tempattr");
		var params = "uploadedFile=";
		params = params.concat($(obj).attr("id"));
		params = params.concat("&type=");
		params = params.concat(type);
		$.ajax({
			url : "removeDownloadZip",
			data : params,
			type : "POST",
			success : function(data) {
			}
		});
		jarError('', type);
		enableDisableUpload();
		enableIconDisableUpload();
	}

	function enableDisableUpload() {
		if ($('ul[temp="uploadFile"] > li').length === 1) {
			$('#download-file-uploader').find("input[type='file']").attr(
					'disabled', 'disabled');
			$('#download-file-uploader').find($(".qq-upload-button")).removeClass(
					"btn-primary qq-upload-button").addClass("disabled");
		} else {
			$('#download-file-uploader').find("input[type='file']").attr(
					'disabled', false);
			$('#download-file-uploader').find($(".btn")).removeClass("disabled")
					.addClass("btn-primary qq-upload-button");
		}
	}

	function enableIconDisableUpload() {
		if ($('ul[temp="uploadIcon"] > li').length === 1) {
			$('#icon-file-uploader').find("input[type='file']").attr(
					'disabled', 'disabled');
			$('#icon-file-uploader').find($(".qq-upload-button"))
					.removeClass("btn-primary qq-upload-button").addClass(
							"disabled");
		} else {
			$('#icon-file-uploader').find("input[type='file']").attr(
					'disabled', false);
			$('#icon-file-uploader').find($(".btn")).removeClass("disabled")
					.addClass("btn-primary qq-upload-button");
		}
	}
</script>