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
<%@ taglib uri="/struts-tags" prefix="s" %>

<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.ArtifactInfo" %>
<%@ page import="com.photon.phresco.commons.model.ArtifactGroup" %>
<%@ page import="com.photon.phresco.commons.model.Technology" %>
<%@ page import="com.photon.phresco.commons.model.CoreOption" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.commons.model.RequiredOption"%>

<%
    ArtifactGroup moduleGroup = (ArtifactGroup) request.getAttribute(ServiceUIConstants.REQ_FEATURES_MOD_GRP); 
    List<Technology> technologies = (List<Technology>) request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
    String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
    String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
    
  	//For edit
  	String moduleId = "";
    String name = "";
    String description = "";
    String helpText = "";
    String version = "";
    String groupId = "";
    String artifactId = "";
    boolean isDefaultModule = false;
    boolean isCoreModule = false;
    boolean isSystem = false;
    
    String selectedModuleId = (String) request.getAttribute(ServiceUIConstants.REQ_FEATURES_SELECTED_MODULEID);
    String selectedTechnology = (String) request.getAttribute(ServiceUIConstants.FEATURES_SELECTED_TECHNOLOGY);
    
	if (moduleGroup != null) {
	    name = moduleGroup.getName();
	    
	    List<ArtifactInfo> modules = moduleGroup.getVersions();
	    ArtifactInfo selectedModule = null;
	    if (CollectionUtils.isNotEmpty(modules)) {
	        for (ArtifactInfo module : modules) {
	            if (module.getId().equals(selectedModuleId)) {
	            	selectedModule = module;
	            	break;
	            }
	        }
	        moduleId = selectedModule.getId();
			
	        //To get whether the selected module is default for the currently selected technology
	        List<RequiredOption> requiredOptions = selectedModule.getAppliesTo();
	        if (CollectionUtils.isNotEmpty(requiredOptions)) {
				for(RequiredOption requiredOption : requiredOptions) {
				    if (requiredOption.getTechId().equals(selectedTechnology)) {
				    	isDefaultModule = requiredOption.isRequired();
				    }
				}
	        }
			
			if (selectedModule.getDescription() != null) {
				description = selectedModule.getDescription();
			}
			if (moduleGroup.getHelpText() != null) {
			    helpText = moduleGroup.getHelpText();
			}
	    }
		
	  	//To get whether the selected module is core for the currently selected technology
	    List<CoreOption> coreOptions = moduleGroup.getAppliesTo();
	    if (CollectionUtils.isNotEmpty(coreOptions)) {
			for (CoreOption coreOption : coreOptions) {
		    	if (coreOption.getTechId().equals(selectedTechnology)) {
		    	    isCoreModule = coreOption.isCore();
		    	}
		    }
	    }
		
		isSystem = moduleGroup.isSystem();
	}
%>

<form id="formComponentAdd" class="form-horizontal customer_list" method="post" enctype="multipart/form-data">

	<h4 class="hdr">
		<% if (StringUtils.isNotEmpty(fromPage)) { %>
			<s:label key="lbl.hdr.comp.component.edit"/>
		<% } else { %>
			<s:label key="lbl.hdr.comp.component.add"/>
		<% } %>
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
					if (CollectionUtils.isNotEmpty(technologies)) {
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
		
		<%-- <% if (ServiceUIConstants.REQ_FEATURES_TYPE_MODULE.equals(type)) { %>
			<div class="control-group" id="moduleSelection">
				<label class="control-label labelbold">
					<span class="mandatory">*</span>&nbsp;<s:text name="lbl.comp.featr.module.type" />
				</label>
				<div class="controls">
					<select name="moduleType" id="type">
				        <option value="core"><s:text name="lbl.comp.featr.type.core" /></option>
				        <option value="custom"><s:text name="lbl.comp.featr.type.custom" /></option>
	     		 	</select>
				</div>
			</div>
		<% } %> --%>
		
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
				<div id="feature-file-uploader" class="file-uploader" title="<s:text name='title.file.size'/>">
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
				<input type="button" class="btn btn-primary" value="Select Dependency" onclick="getFeatures();" />
			</div>
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
		%>
		<% if (StringUtils.isNotEmpty(fromPage)) { %>
			<input type="button" id="componentUpdate" class="btn <%= disabledClass %>" <%= disabled %> value="<s:text name='lbl.btn.edit'/>" 
				onclick="validate('componentUpdate', $('#formComponentAdd'), $('#subcontainer'), 'Updating Component', $('#jarDetailsDiv :input'));"/> 
		<% } else { %>
			<input type="button" id="componentSave" class="btn btn-primary" value="<s:text name='lbl.btn.add'/>" 
				onclick="validate('componentSave', $('#formComponentAdd'), $('#subcontainer'), 'Creating Component', $('#jarDetailsDiv :input'));"/> 
		<% } %>
		<input type="button" id="componentCancel" class="btn btn-primary" value="<s:text name='lbl.btn.cancel'/>"
			onclick="loadContent('componentList', $('#formComponentAdd'), $('#subcontainer'));" />
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="customerId" value="<%= customerId %>">
    <input type="hidden" name="moduleGroupId" value="<%= moduleId %>"/>
    <input type="hidden" name="oldName" value="<%= name %>"/>
    <input type="hidden" name="oldVersion" value="<%= version %>"/>
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_feature").scrollbars();  
	}

	$(document).ready(function() {
	    createUploader();
	    hideLoadingIcon();
		
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
            disableCtrl($('input[name=groupId]'));
        }
        
        if (!isBlank(data.artifactIdError)) {
            showError($("#artifactIdControl"), $("#artifactIdError"), data.artifactIdError);
        } else {
            hideError($("#artifactIdControl"), $("#artifactIdError"));
            disableCtrl($('input[name=artifactId]'));
        }
        
       	if (!isBlank(data.verError)) {
            showError($("#verControl"), $("#verError"), data.verError);
        } else {
            hideError($("#verControl"), $("#verError"));
            disableCtrl($('input[name=version]'));
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
			action : 'uploadComponentFile',
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
			url : "removeComponentFile",
			data : params,
			type : "POST",
			success : function(data) {}
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
	
	// to get the features to add the dependencies
	function getFeatures() {
		$('#popup_div').empty();
		$('#popup_div').show();
		disableScreen();
		loadContent('listConponentsDependency', $('#formComponentAdd'), $('#popup_div'));
	}
</script>