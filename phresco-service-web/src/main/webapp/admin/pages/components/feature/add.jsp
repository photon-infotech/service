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

<%@ page import="com.photon.phresco.commons.model.ArtifactInfo" %>
<%@ page import="com.photon.phresco.commons.model.License" %>
<%@ page import="com.photon.phresco.commons.model.ArtifactGroup" %>
<%@ page import="com.photon.phresco.commons.model.Technology" %>
<%@ page import="com.photon.phresco.commons.model.CoreOption" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.service.admin.actions.util.ServiceActionUtil"%>
<%@ page import="com.photon.phresco.commons.model.RequiredOption"%>

<%
    ArtifactGroup moduleGroup = (ArtifactGroup) request.getAttribute(ServiceUIConstants.REQ_FEATURES_MOD_GRP); 
    List<Technology> technologies = (List<Technology>) request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
    List<License> licenses = (List<License>) request.getAttribute(ServiceUIConstants.REQ_FEATURES_LICENSE);
    String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
    String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
    String type = (String) request.getAttribute(ServiceUIConstants.REQ_FEATURES_TYPE);
    String selectedModuleId = (String) request.getAttribute(ServiceUIConstants.REQ_FEATURES_SELECTED_MODULEID);
    String selectedTechnology = (String) request.getAttribute(ServiceUIConstants.FEATURES_SELECTED_TECHNOLOGY);
	String buttonLbl = ServiceActionUtil.getButtonLabel(fromPage);
	String pageUrl = ServiceActionUtil.getPageUrl(ServiceUIConstants.FEATURES, fromPage);
	String progressTxt = ServiceActionUtil.getProgressTxt(ServiceUIConstants.FEATURES, fromPage);
	String versioning = (String)request.getAttribute(ServiceUIConstants.REQ_VERSIONING);
	String disabledVer ="";
	if(StringUtils.isNotEmpty(versioning)) {
		disabledVer = "disabled";
	}
  
  	//For edit
  	String moduleId = "";
    String name = "";
    String description = "";
    String helpText = "";
    String version = "";
    String groupId = "";
    String artifactId = "";
    String moduleGroupId = "";
	String featureID = "";
    boolean isDefaultModule = false;
    boolean isCoreModule = false;
    boolean isSystem = false;
    boolean showArtifactGroup = false;
    boolean editPage = false;
    boolean features = false;
    boolean jslibs = false;
    boolean component = false;
    String title = "";
    //Get Types
	if (ServiceUIConstants.REQ_FEATURES_TYPE_MODULE.equals(type)) {
		features = true;
	}

	if (ServiceUIConstants.REQ_FEATURES_TYPE_JS.equals(type)) {
		jslibs = true;
	}

	if (ServiceUIConstants.REQ_FEATURES_TYPE_COMPONENT.equals(type)) {
		component = true;
	}

	//Get title
	 if (features) {
		title = ServiceActionUtil.getTitle(ServiceUIConstants.FEATURES,fromPage);
	}

	if (jslibs) {
		title = ServiceActionUtil.getTitle(ServiceUIConstants.JSLIBS,fromPage);
	}

	if (component) {
		title = ServiceActionUtil.getTitle(ServiceUIConstants.COMPONENT,fromPage);
	}
 
	if (moduleGroup != null) {
	    name = moduleGroup.getName();
	    moduleGroupId = moduleGroup.getId();
		featureID = moduleGroup.getArtifactId();
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
			if (selectedModule.getHelpText() != null) {
			    helpText = selectedModule.getHelpText();
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

	// Wont allow to update for System generated Features
	String disabledClass = "btn-primary";
	String disabled = "";
	if (isSystem) {
		disabledClass = "btn-disabled";
		disabled = "disabled";
	}
	//from page is not empty
	if (StringUtils.isNotEmpty(fromPage)) {
		editPage = true;
	}
	
%>

<form id="formFeatureAdd" class="form-horizontal customer_list" method="post" enctype="multipart/form-data">

    <h4 class="hdr headerFeat"><%= title %></h4>
	
	<div class="content_feature">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.name'/>
			</label>
			<div class="controls">
				<input id="featureName" placeholder="<s:text name='place.hldr.feature.add.name'/>" 
				     maxlength="40" title="30 Characters only" class="input-xlarge" type="text" name="name" <%= disabledVer %> value="<%= name %>">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.desc'/>
			</label>
			<div class="controls">
				<textarea id="featureDesc" placeholder="<s:text name='place.hldr.feature.add.desc'/>" 
				     maxlength="150" title="150 Characters only" class="input-xlarge" name="description"><%= description %></textarea>
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
					<div class="typeFields" id="typefield">
					<div class="multilist-scroller multiselct" id="applicableToDiv">
						<ul>
							<li>
								<input type="checkbox" value="all" id="checkAllAuto" name="" onclick="checkAllEvent(this,$('.applsChk'), true);"
									style="margin: 3px 8px 6px 0;" <%= disabledVer %> ><s:text name='lbl.all'/>
							</li>
							<%
								if (CollectionUtils.isNotEmpty(technologies)) {
									String checkedStr = "";
									for (Technology technology : technologies) {
										 if (technology.getId().equals(selectedTechnology)) {
												checkedStr = "checked";
											} else {
												checkedStr = "";
											}
							%>
										<li> <input type="checkbox" id="appliestoCheckbox" name="multiTechnology" value='<%= technology.getId() %>'
											class="check applsChk" <%= checkedStr %> <%= disabledVer %> ><%= technology.getName() %>
										</li>
							<%		}	
								}
							%>
						</ul>
					</div>
				</div>
          		 <span class="help-inline" id="techError"></span>
			</div>
		</div>
		
		
		<% if (features) { %>
			<div class="control-group" id="moduleSelection">
				<label class="control-label labelbold">
					<span class="mandatory">*</span>&nbsp;<s:text name="lbl.comp.featr.module.type" />
				</label>
				<div class="controls">
					<select name="moduleType" id="type" <%= disabledVer %>>
				        <option value="core"><s:text name="lbl.comp.featr.type.external" /></option>
				        <option value="custom"><s:text name="lbl.comp.featr.type.custom" /></option>
	     		 	</select>
				</div>
			</div>
		<% } %>
		
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
				<input type="checkbox" name="defaultType" value="true" <%= checkedStr %> <%= disabledVer %>>
			</div>
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
							if (moduleGroup != null) {
								if (license.getId().equals(moduleGroup.getLicenseId())) {
									selectedStr = "selected";
								} else {
									selectedStr = "";
								}
							}
				%>
							<option value="<%= license.getId() %>" <%= selectedStr %> <%= disabledVer %>><%= license.getName() %></option>
				<%
                        }
					}
				%>
				</select>
				<span class="help-inline applyerror" id="licenseError"></span>
			</div>
		</div>
		
		<!-- POM details starts -->
		<div id="jarDetailsDiv" class="hideContent">
		
		<% if (features || component ) { %>
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
			
			<% } %>
			
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
				<% if(fromPage != ServiceUIConstants.EDIT) { %>
				<span class="mandatory">*</span><% } %>&nbsp;<s:text name='lbl.hdr.comp.file'/>
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
		<% 
			 if (ServiceUIConstants.EDIT.equals(fromPage) && StringUtils.isNotEmpty(featureID)) { %>
		   	 <div class="control-group" >
               <label class="control-label labelbold"> <s:text name="lbl.hdr.feature.download" /> </label>
			       <div class="controls">
						<a href="#" onclick="downloadFile();"><%= featureID %></a>
          		   </div>
        	 </div>
		<% } %>		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.dependency'/>
			</label>
			<div class="controls">
				<input type="button" class="btn btn-primary" value="Select Dependency" onclick="getFeatures();" />
			</div>
		</div>
		
		<div class="control-group" id="featureImgControl">
			<label class="control-label labelbold">
				&nbsp;<s:text name='lbl.hdr.comp.icon'/>
			</label>
			
			 <div class="controls" style="float: left; margin-left: 3%;">
				<div id="feature-img-uploader" class="file-uploader">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript>
				</div>
			</div>
			<span class="help-inline fileError" id="featureImgError"></span>
		</div>
	</div>
	
	<div class="bottom_button">
     
     		<input type="button" id="featuresUpdate" class="btn <%= disabledClass %>" <%= disabled %> 
						onclick="validate('<%= pageUrl %>', $('#formFeatureAdd'), $('#featureContainer'), '<%= progressTxt %>', $('.content_feature :input'));"
						value="<%= buttonLbl %>"/>
			<input type="button" class="btn btn-primary" value="<s:text name='lbl.btn.cancel'/>"
				onclick="loadContent('technologies', $('#formFeatureAdd'), $('#featureContainer'));" />
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="customerId" value="<%= customerId %>">
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
    <input type="hidden" name="moduleGroupId" value="<%= moduleGroupId %>"/>
    <input type="hidden" name="oldName" value="<%= name %>"/>
    <input type="hidden" name="oldVersion" value="<%= version %>"/>
    <input type="hidden" name="type" value="<%= type %>">
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_feature").scrollbars(); 
		$(".multilist-scroller").scrollbars();
	}

	$(document).ready(function() {
		createUploader();
		enableScreen();
		hideLoadingIcon();
	
		// To check for the special character in name
        $('#featureName').bind('input propertychange', function (e) {
            var name = $(this).val();
              name = checkForSplChrExceptDot(name);
            $(this).val(name);
        });
	
		// To check for the special character in version
        $('#featureversn').bind('input propertychange', function (e) {
            var version = $(this).val();
            version = checkForSplChrExceptDot(version);
            $(this).val(version);
        });
       
		//To check modulegroup checkbox and show the the selected version when the radio button in clicked
		$("input[type=radio]").change(function() {
	        var name = $(this).attr('name');
	        $("input:checkbox[name='" + name + "']").prop("checked", true);
	        var version = $("input:radio[name='" + name + "']").val();
	        $("p[id='" + name + "']").html(version);
	    });
		
		//To check the first radio button and show the the selected version when the modulegroup checkbox in clicked
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

	//To show the validation error
    function findError(data) {
        if (!isBlank(data.nameError)) {
            showError($("#nameControl"), $("#nameError"), data.nameError);
        } else {
            hideError($("#nameControl"), $("#nameError"));
        }
        
        if (!isBlank(data.licenseError)) {
            showError($("#licenseControl"), $("#licenseError"), data.licenseError);
        } else {
            hideError($("#licenseControl"), $("#licenseError"));
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
        
        if (!isBlank(data.techError)) {
            showError($("#applyControl"), $("#techError"), data.techError);
        } else {
            hideError($("#applyControl"), $("#techError"));
        }
    }
    
	//To upload the file upload validation error
	function jarError(data, type) {
		var controlObj;
		var msgObj;
		if (type == "featureJar") {
			controlObj = $("#featureFileControl");
			msgObj = $("#featureFileError");
		}else if(type == "featureImg" ){
			controlObj = $("#featureImgControl");
			msgObj = $("#featureImgError");
		}
		
		if (data != undefined && !isBlank(data)) {
			showError(controlObj, msgObj, data);
		} else {
			hideError(controlObj, msgObj);
		}
	}

	//To create the file upload control
	function createUploader() {
		var featureUploader = new qq.FileUploader({
			element : document.getElementById('feature-file-uploader'),
			action : 'uploadFeatureFile',
			multiple : false,
			allowedExtensions : ["zip","jar","dll","so"],
			fileType : 'featureJar',
			buttonLabel : '<s:label key="lbl.comp.featr.upload" />',
			typeError : '<s:text name="err.invalid.file.selection" />',
			params : {
				fileType : 'featureJar',
			},
			debug : true
		});
	
		var featureUploader = new qq.FileUploader({
			element : document.getElementById('feature-img-uploader'),
			action : 'uploadIconFile',
			multiple : false,
			allowedExtensions : ["png"],	
			fileType : 'featureImg',
			buttonLabel : '<s:label key="lbl.comp.featricon.upload" />',
			typeError : '<s:text name="err.invalid.img.file" />',
			params : {
				fileType : 'featureImg'
			},
			debug : true
		});
	}
 
	//To remove the uploaded file
	function removeUploadedJar(obj, btnId) {
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
			success : function(data) {}
		});
		enableDisableUploads(type, $("#" + btnId));
		jarError('', type);
	}
	
	// to get the features to add the dependencies
	function getFeatures() {
		$('#popup_div').empty();
		$('#popup_div').show();
		disableScreen();
		loadContent('fetchFeaturesForDependency', $('#formFeatureAdd'), $('#popup_div'));
	}
	
	function downloadFile() {
		window.location.href="admin/featureUrl?" + $('#formFeatureAdd').serialize();
	}
	
</script>