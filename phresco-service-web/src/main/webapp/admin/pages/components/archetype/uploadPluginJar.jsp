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

<form id="formPlugin">
<div class="modal pluginpopup" id="pluginJarUpload" style="width: 900px; margin-left: -400px; overflow: hidden;" >
	<div class="modal-header">
     	<a class="close" id="close">&times;</a>
		<h3>
			<s:label key="lbl.hdr.featr.plugin.popup.title" theme="simple" />
		</h3>
	</div>
    <div class="model-body"> 
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
		
		<div id="jarDetailsDivPopup"  class="hideContent" style="padding: 0px 10px; float: left; width: 97%;">
		</div>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn btn-primary" id="cancelPluginUpload"><s:label key="lbl.btn.cancel"/></a>
	  	<a href="#" class="btn btn-primary" id="pluginUpload"><s:label key="lbl.btn.ok"/></a>
	</div>	
</div>
</form>
<script language="JavaScript" type="text/javascript">
 	
	$(document).ready(function() {
		createPluginUploader();
		$('#cancelPluginUpload, #close').click(function() {
			$('#popup_div').empty();
			showParentPage();
		});

		$('#pluginUpload').click(function() {
			$('#pluginJarUpload').hide();
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
		var controlpluginObj;
		var msgpluginObj;
		if (type == "pluginJar") {
			controlpluginObj = $("#popupPluginControl");
			msgpluginObj = $("#popupPluginError");
		}
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
		params = params.concat($(obj).attr("id"));
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
				archType : false
			},
			debug : true
		});
	}
	
	function successEvent(url, data) {
		showParentPage();
	}
</script>
