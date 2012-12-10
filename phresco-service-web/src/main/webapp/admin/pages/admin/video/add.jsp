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
<%@ page import="com.photon.phresco.service.admin.actions.util.ServiceActionUtil"%>

<%
	VideoInfo videoInfo = (VideoInfo) request.getAttribute(ServiceUIConstants.REQ_VIDEO_INFO);
	String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
	
	String title = ServiceActionUtil.getTitle(ServiceUIConstants.VIDEOS, fromPage);
	String buttonLbl = ServiceActionUtil.getButtonLabel(fromPage);
	String pageUrl = ServiceActionUtil.getPageUrl(ServiceUIConstants.VIDEOS, fromPage);
	String progressTxt = ServiceActionUtil.getProgressTxt(ServiceUIConstants.VIDEOS, fromPage);

	String name = "";
	String description = "";
	if (videoInfo != null) {
		name = videoInfo.getName();
		description = videoInfo.getDescription();
	}
%>

<form id="formVideoAdd" class="form-horizontal customer_list">
	<h4 class="hdr">
		<%= title %>
	</h4>
	
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.name'/>
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
		
		<div class="control-group" id="videoControl">
			<label class="control-label labelbold"> 
			   <% if(fromPage != ServiceUIConstants.EDIT) { %>
			<span class="mandatory">*</span><% } %>&nbsp;<s:text name='lbl.hdr.adm.video' />
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
			<label class="control-label labelbold"> 
			   <% if(fromPage != ServiceUIConstants.EDIT) { %>
			<span class="mandatory">*</span><% } %>&nbsp;<s:text name='lbl.hdr.adm.img' />
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
       	<input type="button" id="" class="btn btn-primary"  value='<%= buttonLbl %>'
           onclick="validate('<%= pageUrl %>', $('#formVideoAdd'), $('#subcontainer'), '<%= progressTxt %>');" />
  
		<input type="button" id="videoCancel" class="btn btn-primary" value="<s:text name='lbl.btn.cancel'/>" 
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
		hideLoadingIcon();
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
            allowedExtensions : ["mp4","ogg","ogv","webm"],
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
            allowedExtensions : ["png"],
            type: 'imageFile',
            buttonLabel: '<s:label key="lbl.adm.upload.img" />',
            typeError : '<s:text name="err.invalid.img.file" />',
            params: {type: 'imageFile'}, 
            debug: true
        });
   	}
	
	function removeUploadedJar(obj, btnId) {
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
		}
		enableDisableUploads(type, $("#" + btnId));
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
</script>