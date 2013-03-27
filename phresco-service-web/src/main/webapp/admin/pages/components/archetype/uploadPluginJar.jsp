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
<%@ taglib uri="/struts-tags" prefix="s"%>

<%@ page import="java.util.List"%>

<%@ page import="java.util.Set"%>
<%@ page import="java.util.Map"%>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>
<%@ page import="com.photon.phresco.commons.model.ArtifactGroup"%>
<%@ page import="com.photon.phresco.commons.model.ArtifactInfo"%>

<%
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
  	Map<String, ArtifactGroup> pluginInfos = (Map<String, ArtifactGroup>) request.getAttribute(ServiceUIConstants.REQ_PLUGIN_INFO);
%>
<form id="formPlugin">
	<div class="control-group" id="pluginpopupfileuploader">
		<div id="plugin-popup-file-uploader" class="file-uploader" title="<s:text name='title.file.size'/> style="width: 50%;"">
			<noscript>
				<p>Please enable JavaScript to use file uploader.</p>
				<!-- or put a simple form for upload here -->
			</noscript>
		</div>
		<span class="help-inline pluginErrorMsg" id="popupPluginError" style="width: 33%;"></span>
		<% if (!pluginInfos.isEmpty()) { %>
		<span id="tempText"><b>Selected PluginJars</b></span>
		<% } %>
		<% if (!pluginInfos.isEmpty()) {
			Set<String> keySet = pluginInfos.keySet();
			for (String filename : keySet) {
				ArtifactGroup artifactGroup = pluginInfos.get(filename);
				String artifactId = artifactGroup.getArtifactId();
				List<ArtifactInfo> artifactInfoVersions = artifactGroup.getVersions();
				for (ArtifactInfo artifactInfoVersion : artifactInfoVersions) {
					String version = artifactInfoVersion.getVersion();
		%>
		<ul class="qq-upload-list uploadedPluginsList">
			<li>
				<span class='qq-upload-file'><%= filename %></span>
				<span class='qq-upload-size' style='display: inline;'></span>
				<img class='qq-upload-remove' onclick='removeUploadedJar(this, "plugin-popup-file-uploader");'  tempattr='pluginJar' 
					alt='Remove' title='Remove' src='images/delete.png' filename='<%= filename %>'>
			</li>
		</ul>
		<%		}
			}
		}	
		%>
	</div>
	<div id="jarDetailsDivPopup" style="padding: 0px 10px; float: left; width: 97%;">
		<table class="table table-bordered table-striped jarTable">
			<thead>
				<tr id="plugintable" class="header-background">
					<th class="uploadpluginhead" style="padding: 3px">
						<div class="tableCoordinates">GroupId</div>
			        	<div class="tableCoordinates">ArtifactId</div>
			        	<div class="tableCoordinate">Version</div>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td id="table" class="borderBottom-none">
						<% if(!pluginInfos.isEmpty()) {
							String version = "";
							String artifactId = "";
							String groupId = "";
							Set<String> keySet = pluginInfos.keySet();
							for (String filename : keySet) {
								ArtifactGroup artifactGroup = pluginInfos.get(filename);
									artifactId = artifactGroup.getArtifactId();
									groupId = artifactGroup.getGroupId();
									List<ArtifactInfo> artifactInfoVersions = artifactGroup.getVersions();
									for (ArtifactInfo artifactInfoVersion : artifactInfoVersions) {
										version = artifactInfoVersion.getVersion();
						%>
						<div id='<%= filename %>' class='fileClass'>
							<div style="float: left; margin: 0px 10px 0px 0px;">
								<div class="controls" style="margin-left: 0%;">
									<input style="text-align:center;" id="grouId" class="pluginGroupId" class="input-xlarge" name="<%= filename %>_groupId" maxlength='40' title="40 Characters only" type="text"  value='<%= groupId %>' disabled=true >
								</div>
							</div>
							<div style="float: left; margin: 0px 10px 0px 0px;">
								<div class="controls" style="margin-left: 0%;">
									<input style="text-align:center;" id="artifId" class="pluginArtifactId" class="input-xlarge" name="<%= filename %>_artifactId" maxlength='40' title="40 Characters only" type="text" value='<%= artifactId %>' disabled=true >
				              	</div>
				            </div>
				            <div style="float: left; margin: 0px 10px 0px 0px;">
				            	<div class="controls"  style="margin-left: 0%;">
				            		<input style="text-align:center;" id="versnId" class="pluginJarVersion" class="input-xlarge" name="<%= filename %>_version"  maxlength='30' title="30 Characters only" type='text' value='<%= version %>' disabled=true>
		                   		</div>
		                   	</div>
		                </div>   	
						<% 
									}
								}	
							}	
						%>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<input type="hidden" name="customerId" value="<%= customerId %>">
</form>
<script language="JavaScript" type="text/javascript">
 	
	$(document).ready(function() {
		
		<% if (pluginInfos.isEmpty()) { %>
			$("#jarDetailsDivPopup").addClass("hideContent");
			$("#tempText").addClass("hideContent");
		<% } %>
		createPluginUploader();
		$('#popup_div').hide();
		$('#popupTitle').html("Upload Plugin Jar"); 
		$('#popupClose').hide();
		$('.popupOk').attr("onclick","");
		$('#popupPage').css({"width":"780px","position":"relative","left":"40%"});
		$('#plugin-popup-file-uploader').css("margin-left","300px");
		$('.modal-body').css("height","200px");
		$('#clipboard').hide();
		$(".popupOk").html("<s:text name="Ok"/>");
		$('.popupOk, #popupCancel').show(); // show ok & cancel button
		$('.modal-body').html($("#formPlugin"));
		$('.popupOk').attr('id', "pluginUpload");
		$('.borderBottom-none').attr('id', "tableAdd");
		$('#popupPage').modal({
			show: true
		});

		function popupOnOk(obj) {
			if ($(obj).attr("id") == "pluginUpload") {
				
			}
		}
		$('#pluginUpload').click(function() {
			$(".pluginGroupId").attr("disabled", false); 
			$(".pluginArtifactId").attr("disabled", false); 
			$(".pluginJarVersion").attr("disabled", false); 
			var createPluginInfo = validationForArtifactId();
			if (createPluginInfo) {
				$('#popupPage').modal('hide');
				$('#formPlugin').hide();
				$('#popup_div').hide();
				loadContent('technology', $('#formPlugin'), $('#popupPage'), '', true);
				hideLoadingIcon();
			}
		});

		$('#popupCancel').click(function() {
			$('#popup_div').empty();
			showParentPage();
		});
		
		$('.close').click(function() {
			$('#popup_div').empty();
			showParentPage();
		});
		
	});

	function validationForArtifactId() {
		var redirect = true;
		$(".pluginJarVersion").each(function() {
			var value = $(this).val();
			if(value == undefined || isBlank(value)) {
				$(".errMsg").html("<s:text name='err.msg.ver.empty'/>");
				setTimeOut();
				$(this).focus();
				redirect = false;
				return false;
			}
		});
		
		$(".pluginArtifactId").each(function() {
			var value = $(this).val();
			if(value == undefined || isBlank(value)) {
				$(".errMsg").html("<s:text name='err.msg.artfid.empty'/>"); 
				setTimeOut();
				$(this).focus();
				redirect = false;
				return false;
			}
		});
		

		$(".pluginGroupId").each(function() {
			var value = $(this).val();
			if(value == undefined || isBlank(value)) {
				$(".errMsg").html("<s:text name='err.msg.grpid.empty'/>");
				setTimeOut();
				$(this).focus();
				redirect = false;
				return false;
			}
		});
		
		return redirect;
	}
	
	function jarPopupError(data, type) {
		var	controlpluginObj = $("#pluginpopupfileuploader");
		var	msgpluginObj = $("#popupPluginError");
		if (data != undefined && !isBlank(data)) {
			showError(controlpluginObj, msgpluginObj, data);
		} else {
			hideError(controlpluginObj, msgpluginObj);
		}
	}

	function removeUploadedJar(obj, btnId) {
		$('#jarDetailsDiv').hide();
		$(obj).parent().remove();
		
		var type = $(obj).attr("tempattr");
		var size = $('.qq-upload-file').size();
		if (size == 0) {
			$("#jarDetailsDivPopup").hide();
			$("#tempText").addClass("hideContent");
		}
		var tempFile = $(obj).attr("filename");
		if(btnId != "appln-file-uploader"){	
		$(".fileClass").each(function() {
			if ($(this).attr("id") == tempFile) {
				$(this).remove();
				return false;
			}
		});
		arrayPushPop(tempFile, false);
		}
		var params = "uploadedJar=";
		params = params.concat($(obj).attr("filename"));
		params = params.concat("&type=");
		params = params.concat(type);
		$.ajax({
			url : "removeArchetypeJar",
			data : params,
			type : "POST",
			success : function(data) {
			}
		});
		
		if(btnId == "appln-file-uploader"){	
			enableDisableUploads(type, $("#" + btnId));
			jarError('', type);
		}
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
