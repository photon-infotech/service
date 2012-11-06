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
<%@page import="com.photon.phresco.commons.model.DownloadInfo.Category"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.Technology"%>
<%@ page import="com.photon.phresco.commons.model.DownloadInfo" %>
<%@page import="com.photon.phresco.commons.model.License"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.commons.model.PlatformType" %>
<%@ page import="com.photon.phresco.commons.model.ArtifactGroup"%>
<%@ page import="com.photon.phresco.commons.model.ArtifactInfo"%>
<%@ page import="com.photon.phresco.service.admin.actions.util.ServiceActionUtil"%>

<%
	String checkedStr = "";

    DownloadInfo downloadInfo = (DownloadInfo)request.getAttribute(ServiceUIConstants.REQ_DOWNLOAD_INFO);
    String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
    List<Technology> technologies = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
    List<License> licenses = (List<License>) request.getAttribute(ServiceUIConstants.REQ_FEATURES_LICENSE);
    String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
    List<PlatformType> platforms = (List<PlatformType>) request.getAttribute(ServiceUIConstants.REQ_DOWNLOAD_PLATFORMS);
	
    String title = ServiceActionUtil.getTitle(ServiceUIConstants.DOWNLOADS, fromPage);
	String buttonLbl = ServiceActionUtil.getButtonLabel(fromPage);
	String pageUrl = ServiceActionUtil.getPageUrl(ServiceUIConstants.DOWNLOADS, fromPage);
	String progressTxt = ServiceActionUtil.getProgressTxt(ServiceUIConstants.DOWNLOADS, fromPage);
	String versionPro = (String)request.getAttribute(ServiceUIConstants.REQ_VERSIONING);
	String disabledVer ="";
	if(StringUtils.isNotEmpty(versionPro)) {
		disabledVer = "disabled";
	}
	
    //For edit
    String name = "";
    String description = "";
    String version = "";
    Category category = null;
    List<String> downloadInfoPlatforms = null;
    ArtifactGroup artifactGroup = null;
    if (downloadInfo != null) {
   		name = downloadInfo.getName();
   		description = downloadInfo.getDescription();
   		category = downloadInfo.getCategory();
   		//To get the versions
   		artifactGroup = downloadInfo.getArtifactGroup();
   		List<ArtifactInfo> artifactInfos = artifactGroup.getVersions();
   		if (CollectionUtils.isNotEmpty(artifactInfos)) {
   		    for (ArtifactInfo artifactInfo : artifactInfos) {
   		     	version = artifactInfo.getVersion();
   		    }
   		}
   		
   		downloadInfoPlatforms = downloadInfo.getPlatformTypeIds();
    }
%>

<form id="formDownloadAdd" class="form-horizontal customer_list">
	<h4>
		<%= title %> 
	</h4>
	 
	<div class="content_adder" id="downloadInputDiv">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.name'/>
			</label>
			<div class="controls">
				<input id="downloadName" <%= disabledVer %> placeholder="<s:text name='place.hldr.download.add.name'/>" 
					value="<%= name %>" maxlength="30" title="30 Characters only" class="input-xlarge" type="text" name="name">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
			
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.desc'/>
			</label>
			<div class="controls">
				<textarea id="downloadDesc"  placeholder="<s:text name='place.hldr.download.add.desc'/>" class="input-xlarge"
					maxlength="150" title="150 Characters only" name="description"><%= description %></textarea>
			</div>
		</div>
		
		<div class="control-group" id="techControl">
            <label class="control-label labelbold">
                <span class="mandatory">*</span>&nbsp;<s:text name="lbl.hdr.comp.dwnld.technology"/>
            </label>
            <div class="controls">
            	<div class="typeFields" id="typefield">
	                <div class="multilist-scroller multiselct" style="height: 95px; width:300px;">
		                <ul>
							<li>
								<input type="checkbox" <%= disabledVer %> id="checkAllAuto" onclick="checkAllEvent(this, $('.techCheck'), true);"
									style="margin: 3px 8px 6px 0;">All
							</li>
		                    <%
		                        if (CollectionUtils.isNotEmpty(technologies)) {
   									for (Technology technology : technologies) {
   										if (downloadInfo != null) {
   											List<String> appliesTos = downloadInfo.getAppliesToTechIds();
   											if (appliesTos.contains(technology.getId())) {
   												checkedStr = "checked";
   											} else {
   												checkedStr = "";
   											}
   										}
		                    %>
	                   			<li>
									<input type="checkbox" name="technology" <%= disabledVer %>  value="<%= technology.getId() %>"  <%= checkedStr %>
										class="check techCheck"><%= technology.getName() %>
								</li>
							<% 	 
									}
								}
							%> 
						</ul>
	                </div>
				</div>
			</div>
                <span class="help-inline applyerror" id="techError"></span>
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
		
		<div class="control-group" id="licenseControl">
			<label class="control-label labelbold"> 
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.comp.featr.license'/>
			 </label>
			<div class="controls">
				<select name="license">
				<option value=""><s:text name='lbl.comp.featr.license.select'/></option>
				<%	
					if (CollectionUtils.isNotEmpty(licenses)) {
					    String selectedStr = "";
						for (License license : licenses) {
							if (artifactGroup != null) {
								if (license.getId().equals(artifactGroup.getLicenseId())) {
									selectedStr = "selected";
								} else {
									selectedStr = "";
								}
							}
				%>
							<option value="<%= license.getId() %>" <%= selectedStr %>><%= license.getName() %></option>
				<%
                        }
					}
				%>
				</select>
				<span class="help-inline applyerror" id="licenseError"></span>
			</div>
		</div>
		
		<div class="control-group" id="platformControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.dwnld.appltfrm'/>
			</label>
			<div class="controls">
				<div class="typeFields" >
				<div class="multilist-scroller multiselct" style="height: 95px; width:300px;">
					<ul>
						<li>
							<input type="checkbox" <%= disabledVer %> id="checkAll" onclick="checkAllEvent(this, $('.platFormCheck'), true);" 
								style="margin: 3px 8px 6px 0;">All
						</li>
						<%
							if (CollectionUtils.isNotEmpty(platforms)) {
							    for (PlatformType platform : platforms) {
							        if (CollectionUtils.isNotEmpty(downloadInfoPlatforms)) {
											if (downloadInfoPlatforms.contains(platform.getId())) {
												checkedStr = "checked";
											} else {
												checkedStr = "";
											}
										}
						%>
								<li>
									<input type="checkbox" <%= disabledVer %> name="platform" class="check platFormCheck" value="<%= platform.getId() %>" <%= checkedStr %>>
									<%= platform.getType() + platform.getBit() %>
								</li>
						<% 
								}
	    					}
	    				%>
					</ul>
				</div>
				</div>
				<span class="help-inline applyerror" id="platformError"></span>
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
				<input id="dwnVersn" <%= disabledVer %> placeholder="<s:text name='place.hldr.download.add.version'/>" value="<%= version %>" 
					maxlength="30" title="30 Characters only" class="input-xlarge" type="text" name="version">
				<span class="help-inline" id="verError"></span>
			</div>
		</div>
			
		<div class="control-group" id="groupControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.dwnld.group'/>
			</label>	
			<div class="controls">
				<select id="category" name="category" <%= disabledVer %>>
					<option value="">- select -</option>
					<option value="Server">Server</option>
					<option value="Database">Database</option>
					<option value="Editor">Editor</option>
					<option value="Tools">Tools</option>
                    <option value="Others">Others</option>
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
		<input type="button" id="" class="btn btn-primary" value='<%= buttonLbl %>' 
			onclick="validate('<%= pageUrl %>', $('#formDownloadAdd'), $('#subcontainer'), '<%= progressTxt %>', $('.content_adder :input'));" />	
		<input type="button" id="downloadCancel" class="btn btn-primary" value="<s:text name='lbl.btn.cancel'/>"
			onclick="loadContent('downloadList', $('#formDownloadAdd'), $('#subcontainer'));"/>
	</div>
	
	<!-- Hidden Fields -->
    <input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
    <input type="hidden" name="downloadId" value="<%= downloadInfo != null ? downloadInfo.getId() : "" %>"/>
    <input type="hidden" name="oldName" value="<%= name %>"/>
    <input type="hidden" name="customerId" value="<%= customerId %>">
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars(); 
		$(".multilist-scroller").scrollbars();
	}
	
	//For Editable Combo Box
	$(function () {
	    $('#category').jec();
	});
	
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
       		 $("#category option[value='<%= category %>']").attr('selected', 'selected'); 
      
	});

	function findError(data) {
		if (!isBlank()) {
			showError($("#nameControl"), $("#nameError"), data.nameError);
		} else {
			hideError($("#nameControl"), $("#nameError"));
		}
		
		if (!isBlank(data.verError)) {
			showError($("#verControl"), $("#verError"), data.verError);
		} else {
			hideError($("#verControl"), $("#verError"));
		}
		
		if (!isBlank(data.licenseError)) {
            showError($("#licenseControl"), $("#licenseError"), data.licenseError);
        } else {
            hideError($("#licenseControl"), $("#licenseError"));
        }
		
		if (!isBlank(data.platformTypeError)) {
			showError($("#platformControl"), $("#platformError"), data.platformTypeError);
		} else {
			hideError($("#platformControl"), $("#platformError"));
		}
		
		if (!isBlank(data.groupError)) {
			showError($("#groupControl"), $("#groupError"), data.groupError);
		} else {
			hideError($("#groupControl"), $("#groupError"));
		}
		
		if (!isBlank( data.techError)) {
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