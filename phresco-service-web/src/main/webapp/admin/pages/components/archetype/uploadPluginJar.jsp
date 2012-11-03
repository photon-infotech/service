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
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>
<%
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
%>
<form id="formPlugin">
<div class="modal pluginpopup" id="pluginJarUpload" style="width: 826px; margin-left: -400px; overflow: hidden;" >
	<div class="modal-header" style="padding: 3px 15px";>
     	<a class="close" id="close">&times;</a>
		<h3>
			<s:label key="lbl.hdr.featr.plugin.popup.title" theme="simple" />
		</h3>
	</div>
    <div class="model-body" style="max-height: 300px;overflow-y:auto;overflow-x:hidden "> 
		<div class="control-group" id="popupPluginControl" style="float: left; width: 100%; margin-top: 16px;">
			<%-- <label class="control-label labelbold"> <s:text
				name='lbl.hdr.comp.pluginjar' /> </label> --%>
					<div class="controls" style="float: left; margin-left: 37%;">
						<div id="plugin-popup-file-uploader" class="file-uploader">
							<noscript>
								<p>Please enable JavaScript to use file uploader.</p>
								<!-- or put a simple form for upload here -->
							</noscript>
						</div>
					</div>
					 <span class="help-inline pluginError" id="popupPluginError"></span>
		</div>
		
		
		<!-- <div id="outerDiv" class="hideContent" style="padding: 0px 10px; float: left; width: 100%;border:1px solid red">
			<div id="1stdiv" class="headerColor" style="background: RED">
				<div class=""></div>
				<div class=""></div>
				<div class=""></div>
			</div>
			<div id="jarDetailsDivPopup" class="hideContent" style="padding: 0px 10px; width: 97%;border:1px solid red">
			</div>
		</div> -->
	
		<div id="jarDetailsDivPopup"  class="hideContent" style="padding: 0px 10px; float: left; width: 97%;">
			 <table class="table table-bordered table-striped" style=" width: 708px;margin-left: 30px;border: 1px solid #630A0A;">
		        <thead>
		          <tr class="header-background">
		            <th class="uploadpluginhead" style="padding: 3px">
		            	<div class="" style="height: 20px;width: 230px;float: left;text-align: center;">GroupId</div>
		            	<div class="" style="height: 20px;width: 230px;float: left;text-align: center;">ArtifactId</div>
		            	<div class="" style="height: 20px;width: 220px;float: left;text-align: center;">Version</div>
		            </th>
		           </tr>
		         </thead>
				<tbody>
					<tr>
						<td id="table" class="borderBottom-none"></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class="modal-footer" style="float: right; width: 97%">
		<a href="#" class="btn btn-primary" id="cancelPluginUpload"><s:label key="lbl.btn.cancel"/></a>
	  	<a href="#" class="btn btn-primary" id="pluginUpload"><s:label key="lbl.btn.ok"/></a>
	</div>
	<input type="hidden" name="customerId" value="<%= customerId %>">
</form>
<script language="JavaScript" type="text/javascript">
 	
	$(document).ready(function() {
		createPluginUploader();
		$('#cancelPluginUpload, #close').click(function() {
			$('#popup_div').empty();
			showParentPage();
		});

		$('#pluginUpload').click(function() {
			$('#formPlugin').hide();
			$('#popup_div').hide();
			enableScreen();
			loadContent('technology', $('#formPlugin'), $('#popup_div'), '', true);
		});

	});

	function findError(data) {
		if (data.fileError != undefined) {
			showError($("#popupPluginControl"), $("#popupPluginError"),
					data.fileError);
		} else {
			hideError($("#popupPluginControl"), $("#popupPluginError"));
		}
	}

	function jarPopupError(data, type) {
		var	controlpluginObj = $("#popupPluginControl");
		var	msgpluginObj = $("#popupPluginError");
		if (data != undefined && !isBlank(data)) {
			showError(controlpluginObj, msgpluginObj, data);
		} else {
			hideError(controlpluginObj, msgpluginObj);
		}
	}

	function removeUploadedJar(obj) {
		$(obj).parent().remove();
		
		var type = $(obj).attr("tempattr");
		var tempFile = $(obj).attr("filename");

		$(".fileClass").each(function() {
			if ($(this).attr("id") == tempFile) {
				$(this).remove();
				return false;
			}
		});
		var params = "uploadedJar=";
		params = params.concat(tempFile);
		params = params.concat("&type=");
		params = params.concat(type);
		$.ajax({
			url : "removeArchetypeJar",
			data : params,
			type : "POST",
			success : function(data) {
			}
		});
		enableDisableUpload();
		jarPopupError('', type);
	}

	function createPluginUploader() {
		var pluginUploader = new qq.FileUploader({
			element : document.getElementById('plugin-popup-file-uploader'),
			action : 'uploadJar',
			multiple : true,
			allowedExtensions : [ "jar" ],
			type : 'pluginJar',
			buttonLabel : '<s:text name="lbl.comp.arhtyp.upload" />',
			typeError : '<s:text name="err.invalid.jar.selection" />',
			params : {
				type : 'pluginJar',
				customerId: '<%= customerId %>',
				archType : false
			},
			debug : true
		});
	}
	
	function successEvent(url, data) {
		showParentPage();
	}
</script>
