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

<%@ page import="com.photon.phresco.model.Module" %>
<%@ page import="com.photon.phresco.model.ModuleGroup" %>
<%@ page import="com.photon.phresco.model.Technology" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.model.Documentation.DocumentationType"%>

<%
	ModuleGroup moduleGroup = (ModuleGroup)request.getAttribute(ServiceUIConstants.REQ_FEATURES_MOD_GRP); 
    List<Technology> technologies = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
    String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
    String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
    String type = (String) request.getAttribute(ServiceUIConstants.REQ_FEATURES_TYPE);
    String header = (String) request.getAttribute(ServiceUIConstants.REQ_FEATURES_HEADER);
    String selectedModuleId = (String) request.getAttribute(ServiceUIConstants.REQ_FEATURES_SELECTED_MODULEID);
    
  	//For edit
  	String moduleId = "";
    String name = "";
    String description = "";
    String helpText = "";
    String version = "";
    String groupId = "";
    String artifactId = "";
    String selectedTechnology = "";
    boolean isDefaultModule = false; 
	if (moduleGroup != null) {
	    List<Module> modules = moduleGroup.getVersions();
	    Module selectedModule = null;
	    if (CollectionUtils.isNotEmpty(modules)) {
	        for (Module module : modules) {
	            if (module.getId().equals(selectedModuleId)) {
	            	selectedModule = module;
	            	break;
	            }
	        }
	        moduleId = selectedModule.getId();
			name = selectedModule.getName();
			isDefaultModule = selectedModule.getRequired();
			if (selectedModule.getDoc(DocumentationType.DESCRIPTION) != null) {
				description = selectedModule.getDoc(DocumentationType.DESCRIPTION).getContent();
			}
			if (selectedModule.getDoc(DocumentationType.HELP_TEXT) != null) {
			    helpText = selectedModule.getDoc(DocumentationType.HELP_TEXT).getContent();
			}
	    }
		selectedTechnology = moduleGroup.getTechId();
	}
%>

<form id="formFeatureAdd" class="form-horizontal customer_list" method="post" enctype="multipart/form-data">
	<h4 class="hdr">
		<%= header %>
	</h4>	
	<div class="content_feature">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.name'/>
			</label>
			<div class="controls">
				<input id="featureName" placeholder="<s:text name='place.hldr.feature.add.name'/>" 
				     maxlength="30" title="30 Characters only" class="input-xlarge" type="text" name="name" value="<%= name %>">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.desc'/>
			</label>
			<div class="controls">
				<textarea id="featureDesc" placeholder="<s:text name='place.hldr.feature.add.desc'/>" 
				     maxlength="150" title="150 Characters only" class="input-xlarge" type="text" 
				     name="description"><%= description %></textarea>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.help'/>
			</label>
			<div class="controls">
				<textarea name="helpText" id="hlptext" placeholder="<s:text name='place.hldr.feature.add.help.text'/>" 
					maxlength="150" title="150 Characters only" class="input-xlarge" 
					rows="2" cols="10" ><%= helpText %></textarea>
			</div>
		</div>
		
		<div class="control-group" id="applyControl">
			<label class="control-label labelbold"> 
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.comp.featr.technology'/>
			 </label>
			<div class="controls">
				<select name="technology">
				<%
					if (technologies != null) {
					    String selectedStr = "";
						for (Technology technology : technologies) {
						    if (technology.getId().equals(selectedTechnology)) {
						        selectedStr = "selected";
						    } else {
						        selectedStr = "";
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
		
		<div class="control-group" id="moduleSelection">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name="lbl.comp.featr.module.type" />
			</label>
			<div class="controls">
				<select name="moduleType" id="type">
			        <option value="core">Core Module</option>
			        <option value="custom">Custom Module</option>
     		 	</select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name="lbl.comp.featr.default.module" />
			</label>
			<div class="controls">
				<%
					String checkedStr = "";
					if (isDefaultModule) {
					    checkedStr = "checked";
					}
				%>
				<input type="checkbox" name="defaultType" value="true" <%= checkedStr %>>
			</div>
		</div>
		
		<!-- POM details starts -->
		<div id="jarDetailsDiv" class="hideContent">
			<div class="control-group" id="groupIdControl">
				<label class="control-label labelbold">
					<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.groupid'/>
				</label>
				<div class="controls">
					<input name="groupId" class="groupId" class="input-xlarge" maxlength="40" title="40 Characters only" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.groupId'/>">
						<span class="help-inline" id="groupIdError"></span>
				</div>
			</div>
			
			<div class="control-group" id="artifactIdControl">
				<label class="control-label labelbold">
					<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.artifactid'/>
				</label>
				<div class="controls">
					<input name="artifactId" class="artifactId" class="input-xlarge" maxlength="40" title="40 Characters only" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.artifactId'/>">
						<span class="help-inline" id="artifactIdError"></span>
				</div>
			</div>
			
			<div class="control-group" id="verControl">
				<label class="control-label labelbold">
					<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.jar.version'/>
				</label>
				<div class="controls">
					<input name="version" class="jarVersion" maxlength="30" title="30 Characters only" class="input-xlarge" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.version'/>">
						<span class="help-inline" id="verError"></span>
				</div>
			</div>
		</div>
		<!-- POM details ends -->
		
		<div class="control-group" id="featureFileControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.file'/>
			</label>
			
			 <div class="controls" style="float: left; margin-left: 3%;">
				<div id="feature-file-uploader" class="file-uploader">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript>
				</div>
			</div>
			<span class="help-inline fileError" id="featureFileError"></span>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.dependency'/>
			</label>
			<div class="controls">
				<input type="button" class="btn btn-primary" value="Select Dependency" 
					onclick="getFeatures();" />
			</div>
		</div>
	</div>
	
	<div class="bottom_button">
     	<% if (StringUtils.isNotEmpty(fromPage)) { %>
	     	<input type="button" id="featuresUpdate" class="btn btn-primary" value="<s:text name='lbl.hdr.comp.update'/>" 
				onclick="validate('featuresUpdate', $('#formFeatureAdd'), $('#feature_tab'), 'Updating Feature');"/> 
     	<% } else { %>
	     	<input type="button" id="featuresSave" class="btn btn-primary" value="<s:text name='lbl.hdr.comp.save'/>" 
				onclick="validate('featuresSave', $('#formFeatureAdd'), $('#feature_tab'), 'Creating Feature');"/> 
		<% } %>
		<input type="button" id="featuresCancel" class="btn btn-primary" value="<s:text name='lbl.hdr.comp.cancel'/>"
		onclick="loadContent('featuresMenu', $('#formFeatureAdd'), $('#feature_tab'));" />
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="customerId" value="<%= customerId %>">
	<input type="hidden" name="from" value="dependency">
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
    <input type="hidden" name="moduleGroupId" value="<%= moduleId %>"/>
    <input type="hidden" name="oldName" value="<%= name %>"/>
    <input type="hidden" name="oldVersion" value="<%= version %>"/>
    <input type="hidden" name="type" value="<%= type %>">
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_feature").scrollbars();  
	}

	$(document).ready(function() {
	    createUploader();
		enableScreen();
		
		// To check for the special character in name
        $('#featureName').bind('input propertychange', function (e) {
            var name = $(this).val();
            name = checkForSplChr(name);
            $(this).val(name);
        });
	
		// To check for the special character in version
        $('#featureversn').bind('input propertychange', function (e) {
            var version = $(this).val();
            version = checkForSplChrExceptDot(version);
            $(this).val(version);
        });
       
		$("input[type=radio]").change(function() {
	        var name = $(this).attr('name');
	        $("input:checkbox[name='" + name + "']").prop("checked", true);
	        var version = $("input:radio[name='" + name + "']").val();
	        $("p[id='" + name + "']").html(version);
	    });

	    $("input[type=checkbox]").change(function() {
	        var checkboxChecked = $(this).is(":checked");
	        var name = $(this).attr('name');
	        if (!checkboxChecked) {
	            $("input:radio[name='" + name + "']").prop("checked", false);
	            $("p[id='" + name + "']").empty();
	        } else {
	            $("input:radio[name='" + name + "']:first").prop("checked", true);
	            var version = $("input:radio[name='" + name + "']").val();
	            $("p[id='" + name + "']").html(version);
	        }
	    });
	    
	    $('#type').change(function() {
			var selectVal = $('#type :selected').val();
			if (selectVal == 'module') {
				$('#moduleSelection').show();
			} else {
				$('#moduleSelection').hide();
			}
		});
	    
	    $("input[type=radio]").change(function() {
			var name = $(this).attr('name');
			$("input:checkbox[name='" + name + "']").prop("checked", true);
			var version = $("input:radio[name='" + name + "']").val();
			$("p[id='" + name + "']").html(version);
		});

		$("input[type=checkbox]").change(function() {
			var checkboxChecked = $(this).is(":checked");
			var name = $(this).attr('name');
			if (!checkboxChecked) {
				$("input:radio[name='" + name + "']").prop("checked", false);
				$("p[id='" + name + "']").empty();
			} else {
				$("input:radio[name='" + name + "']:first").prop("checked", true);
				var version = $("input:radio[name='" + name + "']").val();
				$("p[id='" + name + "']").html(version);
			}
		});
	});

    function findError(data) {
        if (!isBlank(data.nameError)) {
            showError($("#nameControl"), $("#nameError"), data.nameError);
        } else {
            hideError($("#nameControl"), $("#nameError"));
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
        
       	if (!isBlank(data.verError)) {
            showError($("#verControl"), $("#verError"), data.verError);
        } else {
            hideError($("#verControl"), $("#verError"));
        }
        
        if (!isBlank(data.fileError)) {
            showError($("#featureFileControl"), $("#featureFileError"), data.fileError);
        } else {
            hideError($("#featureFileControl"), $("#featureFileError"));
        }
    }
    
	function jarError(data, type) {
		var controlObj;
		var msgObj;
		if (type == "featureJar") {
			controlObj = $("#featureFileControl");
			msgObj = $("#featureFileError");
		} 
		if (data != undefined && !isBlank(data)) {
			showError(controlObj, msgObj, data);
		} else {
			hideError(controlObj, msgObj);
		}
	}

	 function createUploader() {
		var featureUploader = new qq.FileUploader({
			element : document.getElementById('feature-file-uploader'),
			action : 'uploadFeatureFile',
			multiple : false,
			allowedExtensions : ["zip","jar","dll","so"],
			type : 'featureJar',
			buttonLabel : '<s:label key="lbl.comp.featr.upload" />',
			typeError : '<s:text name="err.invalid.file.selection" />',
			params : {
				type : 'featureJar'
			},
			debug : true
		});
	}
 
	function removeUploadedJar(obj) {
		$('#jarDetailsDiv').hide();
		$(obj).parent().remove();
		var type = $(obj).attr("tempattr");
		var params = "uploadedJar=";
		params = params.concat($(obj).attr("id"));
		params = params.concat("&type=");
		params = params.concat(type);
		$.ajax({
			url : "removeFeatureJar",
			data : params,
			type : "POST",
			success : function(data) {
			}
		});
		jarError('', type);
		enableDisableUpload();
	}

	function enableDisableUpload() {
		if ($('ul[temp="featureJar"] > li').length === 1) {
			$('#feature-file-uploader').find("input[type='file']").attr(
					'disabled', 'disabled');
			$('#feature-file-uploader').find($(".qq-upload-button")).removeClass(
					"btn-primary qq-upload-button").addClass("disabled");
		} else {
			$('#feature-file-uploader').find("input[type='file']").attr(
					'disabled', false);
			$('#feature-file-uploader').find($(".btn")).removeClass("disabled")
					.addClass("btn-primary qq-upload-button");
		}
	} 
	
	function getFeatures() {
		$('#popup_div').empty();
		$('#popup_div').show();
		disableScreen();
		loadContent('listFeatures', $('#formFeatureAdd'), $('#popup_div'));
	}
</script>