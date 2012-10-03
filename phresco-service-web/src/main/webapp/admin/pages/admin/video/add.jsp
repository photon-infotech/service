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

<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.List"%>
<%@ page import="com.photon.phresco.commons.model.VideoInfo"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<%
	VideoInfo videoInfo = (VideoInfo) request.getAttribute(ServiceUIConstants.REQ_VIDEO_INFO);
	String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
	
	String name = "";
	String description = "";
	if (videoInfo != null) {
		if (StringUtils.isNotEmpty(videoInfo.getName())) {
			name = videoInfo.getName();
		}
		if (StringUtils.isNotEmpty(videoInfo.getDescription())) {
			description = videoInfo.getDescription();
		}
	}
	
%>

<form id="formVideoAdd" class="form-horizontal customer_list">
	<h4 class="hdr">
   <% if (StringUtils.isNotEmpty(fromPage)) { %> 
		<s:label key="lbl.hdr.adm.vdoedit" theme="simple" />
	<% } else { %>
		<s:label key="lbl.hdr.adm.vdeoadd" theme="simple"/>	
   <% } %>
	</h4>
	
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.name'/>
			</label>
			<div class="controls">
				<input id="name" class="input-xlarge" placeholder='<s:text name="place.hldr.vdeo.add.name"/>'
					type="text" name="name" value='<%= name %>' maxlength="30" title="30 Characters only">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.desc'/>
			</label>
			<div class="controls">
				<textarea id="description" class="input-xlarge" placeholder='<s:text name="place.hldr.appType.add.desc"/>' 
				    rows="3" name="description" maxlength="150" title="150 Characters only"><%= description %></textarea>
			</div>
		</div>
		
		<!-- POM details starts -->
		<div id="videoDetailsDiv" class="hideContent">
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.groupid'/>
				</label>
				<div class="controls">
					<input id="groupid" name="groupId" class="input-xlarge groupId" maxlength="40" title="40 Characters only" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.groupId'/>">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.artifactid'/>
				</label>
				<div class="controls">
					<input id="artifId" name="artifactId" class="input-xlarge artifactId" maxlength="40" title="40 Characters only" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.artifactId'/>">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.jar.version'/>
				</label>
				<div class="controls">
					<input id="versnId" name="version" maxlength="30" title="30 Characters only" class="input-xlarge jarVersion" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.version'/>">
				</div>
			</div>
		</div>
		<!-- POM details ends -->
		
		<div class="control-group" id="videoControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.vdeo' />
			</label>
			<div class="controls" style="float: left; margin-left: 3%;">
				<div id="video-file-uploader" class="file-uploader">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript>
				</div>
			</div>
			<span class="help-inline fileError" id="videoError"></span>
		</div>
		
		<div class="control-group" id="imageControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.img' />
			</label>
			<div class="controls" style="float: left; margin-left: 3%;">
				<div id="image-file-uploader" class="file-uploader">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript>
				</div>
			</div>
			<span class="help-inline fileError" id="imageError"></span>
		</div>
	</div>
	
	
	
	<div class="bottom_button">
        <%
            if (StringUtils.isNotEmpty(fromPage)) {
        %>
	            <input type="button" id="videoUpdate" class="btn btn-primary"  value="<s:text name='lbl.hdr.comp.update'/>"
	                onclick="validate('videoUpdate', $('#formVideoAdd'), $('#subcontainer'), '<s:text name='lbl.prog.vdeo.update'/>');" />
        <% } else { %>
                <input type="button" id="videoSave" class="btn btn-primary" value="<s:text name='lbl.hdr.comp.save'/>"
                    onclick="validate('videoSave', $('#formVideoAdd'), $('#subcontainer'), '<s:text name='lbl.prog.vdeo.save'/>');" />
        <% } %>
        
		<input type="button" id="videoCancel" class="btn btn-primary" value="<s:text name='lbl.hdr.comp.cancel'/>" 
            onclick="loadContent('videoList', $('#formVideoAdd'), $('#subcontainer'));" />
    </div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
	<input type="hidden" name="videoId" value="<%=  videoInfo != null ?  videoInfo.getId() : "" %>"/>
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
	}

	$(document).ready(function() {
		enableScreen();
		createUploader();
		//To focus the name textbox by default
		$('#name').focus();
		
		// To check for the special character in name
		$('#name').bind('input propertychange', function (e) {
			var name = $(this).val();
			name = checkForSplChr(name);
	    	$(this).val(name);
		});	
	});
	
	function findError(data) {
		if (!isBlank(data.nameError)) {
			showError($("#nameControl"), $("#nameError"), data.nameError);
		} else {
			hideError($("#nameControl"), $("#nameError"));
		}
		if (!isBlank(data.videoError)) {
			showError($("#videoControl"), $("#videoError"), data.videoError);
		} else {
			hideError($("#videoControl"), $("#videoError"));
		}
		if (!isBlank(data.imgError)) {
			showError($("#imageControl"), $("#imageError"), data.imgError);
		} else {
			hideError($("#imageControl"), $("#imageError"));
		} 
	}
	
	function createUploader() {
		var videoUploader = new qq.FileUploader ({
            element: document.getElementById('video-file-uploader'),
            action: 'uploadVideo',
            multiple: false,
            allowedExtensions : ["mp4,ogg,ogv,webm"],
            type: 'videoFile',
            buttonLabel: '<s:label key="lbl.adm.upload.vdeo" />',
            typeError : '<s:text name="err.invalid.vdeo.file" />',
            params: {type: 'videoFile'}, 
            debug: true
        });
		
		var imgUploader = new qq.FileUploader ({
            element: document.getElementById('image-file-uploader'),
            action: 'uploadVideo',
            multiple: false,
            allowedExtensions : ["jpg"],
            type: 'imageFile',
            buttonLabel: '<s:label key="lbl.adm.upload.img" />',
            typeError : '<s:text name="err.invalid.img.file" />',
            params: {type: 'imageFile'}, 
            debug: true
        });
   	}
	
	function removeUploadedJar(obj) {
		$(obj).parent().remove();
		var type = $(obj).attr("tempattr"); 
		var params = "uploadedJar=";
		params = params.concat($(obj).attr("id"));
		params = params.concat("&type=");
		params = params.concat(type);
		$.ajax({
			url : "removeVideo",
			data : params,
			type : "POST",
			success : function(data) {
			}
		});
		if (type === "videoFile") {
			$('#videoDetailsDiv').hide();
			enableDisableUpload("videoFile", "video-file-uploader");
		} else if (type === "imageFile"){
			enableDisableUpload("imageFile", "image-file-uploader");
		} 
	} 
	
	function jarError(data, type) {
		var controlObj;
		var msgObj;
		if (type == "videoFile") {
			controlObj = $("#videoControl");
			msgObj = $("#videoError");
		} else if (type == "imageFile") {
			controlObj = $("#imageControl");
			msgObj = $("#imageError");
		}
		if (data != undefined && !isBlank(data)) {
			showError(controlObj, msgObj, data);
		} else {
			hideError(controlObj, msgObj);
		}
	}
	
	function enableDisableUpload(tempattr, divId) {
		if ($('ul[temp="'+ tempattr +'"] > li').length === 1 ) {
			$('#'+divId).find("input[type='file']").attr('disabled','disabled');
			$('#'+divId).find($(".qq-upload-button")).removeClass("btn-primary qq-upload-button").addClass("disabled");
		} else {
			$('#'+divId).find("input[type='file']").attr('disabled', false);
			$('#'+divId).find($(".btn")).removeClass("disabled").addClass("btn-primary qq-upload-button");
		}
	}
</script>