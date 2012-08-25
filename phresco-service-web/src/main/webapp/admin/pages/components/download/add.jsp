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

<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.model.Technology"%>
<%@ page import="com.photon.phresco.model.DownloadInfo" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %> 

<%
    DownloadInfo downloadInfo = (DownloadInfo)request.getAttribute(ServiceUIConstants.REQ_DOWNLOAD_INFO);
    String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
    List<Technology> technologys = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
    String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
  
    //For edit
    String name = "";
    String description = "";
    String version = "";
    if (downloadInfo != null) {
    	if (StringUtils.isNotEmpty(downloadInfo.getName())) {
    		name = downloadInfo.getName();
    	}
    	if (StringUtils.isNotEmpty(downloadInfo.getDescription())) {
    		description = downloadInfo.getDescription();
    	}
    	if (StringUtils.isNotEmpty(downloadInfo.getVersion())) {
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
				<input id="input01" placeholder="<s:text name='place.hldr.download.add.name'/>" 
					value="<%= name %>" class="input-xlarge" type="text" name="name">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
			
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.adm.desc'/>
			</label>
			<div class="controls">
				<input id="input01"  placeholder="<s:text name='place.hldr.download.add.desc'/>" class="input-xlarge" type="text"
					value="<%= description %>" name="description">
			</div>
		</div>
		
		<div class="control-group" id="applyControl">
            <label class="control-label labelbold">
                <span class="mandatory">*</span>&nbsp;<s:text name="Technology"/>
            </label>
            <div class="controls">
                <select id="multiSelect" multiple="multiple" name="technology">
                    <% 
                    	if (technologys != null) {
							for (Technology technology : technologys) { 
					%>
                    			<option value="<%=technology.getName() %>"><%=technology.getName() %></option> 
					<% 	 
							}
						}
					%> 
                </select>
                <span class="help-inline applyerror" id="techError"></span>
            </div>
        </div>
		
		<div class="control-group">
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
					<option  value="WN">Windows</option>
					<option  value="LN">Linux</option>
					<option  value="MC">Mac</option>
					<option  value="SL">Solaris</option>
				</select>
				<span class="help-inline applyerror" id="appltError"></span>
			</div>
		</div>
			
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.adm.dwnld.icon'/>
			</label>
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
			
		<div class="control-group" id="verControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.dwnld.ver'/>
			</label>
			<div class="controls">
				<input id="input01" placeholder="<s:text name='place.hldr.download.add.version'/>" value="<%= version %>" 
					class="input-xlarge" type="text" name="version">
				<span class="help-inline" id="verError"></span>
			</div>
		</div>
			
		<div class="control-group" id="groupControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.dwnld.group'/>
			</label>	
			<div class="controls">
				<select id="select01" name="group">
					<option value="" onclick="javascript:hideDiv();">- select -</option>
					<option value="DB" onclick="javascript:hideDiv();">Database</option>
					<option value="SR" onclick="javascript:hideDiv();">Servers</option>
					<option value="ED" onclick="javascript:hideDiv();">Editors</option>
                    <option value="OT" onclick="javascript:showDiv();">Others</option>
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
</form>

<script type="text/javascript">
	$(document).ready(function() {
		enableScreen();
        createUploader(); 
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
	}
	 
	function applnJarError(data) {
		if (data != undefined) {
			showError($("#fileControl"), $("#fileError"), data);
		} else {
			hideError($("#fileControl"), $("#fileError"));
		}
	}

	function pluginJarError(data) {
		if (data != undefined) {
			showError($("#pluginControl"), $("#pluginError"), data);
		} else {
			hideError($("#pluginControl"), $("#pluginError"));
		}
	}

	function showDiv() {
		$('#othersDiv').show();
	}

	function hideDiv() {
		$('#othersDiv').hide();
	}

	function createUploader() {
		var applnUploader = new qq.FileUploader({
			element : document.getElementById('download-file-uploader'),
			action : 'uploadJar',
			multiple : false,
			type : 'applnJar',
			buttonLabel : '<s:label key="lbl.comp.featr.upload" />',
			typeError : '<s:text name="err.invalid.file.selection" />',
			params : {
				type : 'applnJar'
			},
			debug : true
		});

		var pluginUploader = new qq.FileUploader({
			element : document.getElementById('plugin-file-uploader'),
			action : 'uploadJar',
			multiple : false,
			type : 'pluginJar',
			buttonLabel : '<s:text name="lbl.comp.featr.upload" />',
			typeError : '<s:text name="err.invalid.file.selection" />',
			params : {
				type : 'pluginJar'
			},
			debug : true
		});
	}

	function removeUploadedJar(obj) {
		$(obj).parent().remove();
		var params = "uploadedJar=";
		params = params.concat($(obj).attr("id"));
		params = params.concat("&type=");
		params = params.concat($(obj).attr("tempattr"));
		$.ajax({
			url : "removeDownloadZip",
			data : params,
			type : "POST",
			success : function(data) {
			}
		});
		enableDisableUpload();
		enablePluginDisableUpload();
		pluginJarError();
		applnJarError();
	}

	function enableDisableUpload() {
		if ($('ul[temp="applnJar"] > li').length === 1) {
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

	function enablePluginDisableUpload() {
		if ($('ul[temp="pluginJar"] > li').length === 1) {
			$('#plugin-file-uploader').find("input[type='file']").attr(
					'disabled', 'disabled');
			$('#plugin-file-uploader').find($(".qq-upload-button"))
					.removeClass("btn-primary qq-upload-button").addClass(
							"disabled");
		} else {
			$('#plugin-file-uploader').find("input[type='file']").attr(
					'disabled', false);
			$('#plugin-file-uploader').find($(".btn")).removeClass("disabled")
					.addClass("btn-primary qq-upload-button");
		}
	}
</script>