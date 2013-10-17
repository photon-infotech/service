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
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
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
	String versioning = (String) request.getAttribute(ServiceUIConstants.REQ_VERSIONING);
	List<String> dependencyIds = (List<String>) request.getAttribute(ServiceUIConstants.REQ_SELECTED_DEPEDENCY_IDS);
	
	
	String disabledVer ="";
	if(StringUtils.isNotEmpty(versioning)) {
		disabledVer = "disabled";
	}
	
	List<RequiredOption> selectedTech = new ArrayList<RequiredOption>();
	 if (moduleGroup != null){
		List<ArtifactInfo> versionIds= moduleGroup.getVersions();
   		 for (ArtifactInfo versionId : versionIds ){
    		selectedTech = versionId.getAppliesTo();
   	 	}
    }
	 
	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "btn-primary";
	if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_REUSABLE_COMPONENTS)) {
		per_disabledStr = "disabled";
		per_disabledClass = "btn-disabled";
	}
  
  	//For edit
  	String moduleId = "";
    String name = "";
    String displayName = "";
    String description = "";
    String helpText = "";
    String version = "";
    String groupId = "";
    String artifactId = "";
    String moduleGroupId = "";
	String featureArtifactId = "";
	String featureGroupId = "";
	String featureVersions = "";
    boolean isDefaultModule = false;
    boolean isCoreModule = false;
    boolean isSystem = false;
    boolean showArtifactGroup = false;
    boolean editPage = false;
    boolean features = false;
    boolean jslibs = false;
    boolean component = false;
    String title = "";
    String packaging = "";
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
	    displayName = moduleGroup.getDisplayName(); 
	    moduleGroupId = moduleGroup.getId();
	    featureArtifactId = moduleGroup.getArtifactId();
		featureGroupId = moduleGroup.getGroupId();
		featureVersions = moduleGroup.getVersions().get(0).getVersion();
	    List<ArtifactInfo> modules = moduleGroup.getVersions();
	    ArtifactInfo selectedModule = null;
	    packaging = moduleGroup.getPackaging();
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

	// Wont allow to update for System generated Features
	String disabledClass = "btn-primary";
	String disabled = "";
	/* if (isSystem) {
		disabledClass = "btn-disabled";
		disabled = "disabled";
	} */
	if ("edit".equals(fromPage)) {
		if ("photon".equalsIgnoreCase(customerId)) {
			disabled = "";
			disabledClass = "btn-primary";
		} else {
			if(isSystem) {
				disabled = "disabled";
				disabledClass = "btn-disabled";
			}
		}
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
				     maxlength="40" title="30 Characters only" class="input-xlarge" type="text" name="name" <%= disabledVer %> value="<%= name %>"  <%= disabled %>>
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group" id="dispNameControl">
			<label class="control-label labelbold">
				<s:text name='lbl.disp.name'/>
			</label>
			<div class="controls">
				<input id="featureDisplayName" placeholder="<s:text name='place.hldr.feature.add.dispname'/>" 
				     maxlength="40" title="30 Characters only" class="input-xlarge" type="text" name="displayName" <%= disabledVer %> value="<%= displayName %>"  <%= disabled %>>
				<span class="help-inline" id="dispNameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.desc'/>
			</label>
			<div class="controls">
				<textarea <%= disabled %> id="featureDesc" placeholder="<s:text name='place.hldr.feature.add.desc'/>" 
				     maxlength="150" title="150 Characters only" class="input-xlarge" name="description"><%= description %> </textarea>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.help'/>
			</label>
			<div class="controls">
				<textarea <%= disabled %> name="helpText" id="hlptext" placeholder="<s:text name='place.hldr.feature.add.help.text'/>" 
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
								<input type="checkbox" value="" id="checkAllAuto" name="" onclick="checkAllEvent(this,$('.applsChk'), false);"
									style="margin: 3px 8px 6px 0;" <%= disabledVer %> ><s:text name='lbl.all'/>
							</li>
							 <%
								if (CollectionUtils.isNotEmpty(technologies)) {
									String checkedStr = "";
									for (Technology technology : technologies) {
										if (CollectionUtils.isNotEmpty(selectedTech)) {
											checkedStr = "";
											for (RequiredOption selectedTechId : selectedTech) {
										 		if (technology.getId().equals(selectedTechId.getTechId())) {
													checkedStr = "checked";
													break;
												} 
											}
										}
							%>
										<li> <input type="checkbox" id="appliestoCheckbox" name="multiTechnology" value='<%= technology.getId() %>'
											onclick="checkboxEvent($('#checkAllAuto'), 'applsChk')"	class="check applsChk" <%= checkedStr %> <%= disabledVer %> ><%= technology.getName() %>
										</li>
							<%		
									}
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
					<select name="moduleType" id="type" <%= disabledVer %> <%= disabled %>>
				        <option value="true"><s:text name="lbl.comp.featr.type.external" /></option>
				        <option value="false"><s:text name="lbl.comp.featr.type.custom" /></option>
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
				<input type="checkbox" name="defaultType" value="true" <%= checkedStr %> <%= disabledVer %> <%= disabled %>>
			</div>
		</div>
		
		<div class="control-group" id="licenseControl">
			<label class="control-label labelbold"> 
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.comp.featr.license'/>
			 </label>
			<div class="controls">
				<select name="license" <%= disabled %>>
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
				<% if(fromPage != ServiceUIConstants.EDIT || StringUtils.isNotEmpty(versioning)) { %>
				<span class="mandatory">*</span><% } %>&nbsp;<s:text name='lbl.hdr.comp.file'/>
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
		<% 
			 if (ServiceUIConstants.EDIT.equals(fromPage) && StringUtils.isNotEmpty(featureArtifactId)) { %>
		   	 <div class="control-group" >
               <label class="control-label labelbold"> <s:text name="lbl.hdr.feature.download" /> </label>
			       <div class="controls">
						<a href="#" onclick="downloadFile();"><%= displayName %>-<%= featureVersions %></a>
          		   </div>
        	 </div>
		<% } %>		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.dependency'/>
			</label>
			<div class="controls">
				<input type="button" class="btn <%= disabledClass %>" <%= disabled %> value="Select Dependency" onclick="getFeatures();" />
				<span id="totalSize"></span>
			</div>
		</div>
		
		<div class="control-group" id="featureImgControl">
			<label class="control-label labelbold">
				&nbsp;<s:text name='lbl.hdr.comp.icon'/>
			</label>
			
			 <div class="controls" style="float: left; margin-left: 3%;">
				<div id="feature-img-uploader" class="file-uploader" title="<s:text name='title.icon.size'/>">
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
   		<input type="button" id="featuresUpdate" class="btn <%= per_disabledClass %>" 
			onclick="validate('<%= pageUrl %>', $('#formFeatureAdd'), $('#subcontainer'), '<%= progressTxt %>', $('.content_feature :input'));"
			value="<%= buttonLbl %>" <%= per_disabledStr %>/>
		<input type="button" class="btn btn-primary" value="<s:text name='lbl.btn.cancel'/>"
			onclick="loadTechnologies();" />
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="customerId" value="<%= customerId %>">
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
    <input type="hidden" name="moduleGroupId" value="<%= moduleGroupId %>"/>
    <input type="hidden" name="oldName" value="<%= name %>"/>
    <input type="hidden" name="oldVersion" value="<%= version %>"/>
    <input type="hidden" name="type" value="<%= type %>">
    <input type="hidden" name="featureArtifactId" value="<%= moduleGroup != null ? featureArtifactId : "" %>"/> 
    <input type="hidden" name="featureGroupId" value="<%= moduleGroup != null ? featureGroupId : "" %>"/> 
    <input type="hidden" name="featureVersions" value="<%= moduleGroup != null ? featureVersions : "" %>"/>
    <input type="hidden" name="moduleId" value="<%= StringUtils.isNotEmpty(selectedModuleId) ? selectedModuleId : "" %>">    
   	<input type="hidden" name="packaging" value="<%= packaging %>">
   	<input type="hidden" name="versioning" value="<%= versioning %>">
   	
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_feature").scrollbars(); 
		$(".multilist-scroller").scrollbars();
	}

	$(document).ready(function() {
		
		var allowedFiles = "";
		var fileErr = "";
		if (<%= ServiceUIConstants.REQ_FEATURES_TYPE_MODULE.equals(type)%>) {			
			allowedFiles = ["jar","zip","war","apklib"];
			fileErr = "<s:text name='err.invalid.module.selection'/>";
		} else if (<%= ServiceUIConstants.REQ_FEATURES_TYPE_JS.equals(type)%>) {			
			allowedFiles = ["js","zip","war"];
			fileErr = "<s:text name='err.invalid.js.selection'/>";
		}else if (<%= ServiceUIConstants.REQ_FEATURES_TYPE_COMPONENT.equals(type)%>) {			
			allowedFiles = ["jar","zip","apklib"];
			fileErr = "<s:text name='err.invalid.file.selection'/>";
		}
		createUploader(allowedFiles,fileErr);
		enableScreen();
		hideLoadingIcon();
		
		 <%
		   if(CollectionUtils.isNotEmpty(dependencyIds)) {
		%>
		   $('#totalSize').html("Added Dependencies : " +'<%= dependencyIds %>');
		<% } %> 
		
		$("#totalSize").text(function(index) {
	        return depenTrim($(this));
	    }); 
	
		// To check for the special character in name
        $('#featureName').bind('input propertychange', function (e) {
            var name = $(this).val();
              name = checkForSplChrExceptDot(name);
            $(this).val(name);
        });
		
        if ( '<%= fromPage %>' === "edit" &&  '<%= customerId %>' != "photon" ) { 
            disableUploadButton($("#feature-file-uploader"));
            disableUploadButton($("#feature-img-uploader"))
        }
        
        if ('<%= fromPage %>' === "edit") {
        	var checkedLen = $("input:checked").length;
    		if ($('.applsChk').length == checkedLen){
    		  $('#checkAllAuto').prop('checked', true);
    		} 
    	} 
	
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
		
		//To make the module type selected during edit
		$('#type option').each(function() {
			var value = $(this).val();
			if (value === '<%= isCoreModule %>') {
				$(this).attr("selected", true);
				return false;
			}
		});
		
	});
	
	function loadTechnologies() {
		showLoadingIcon();
		loadContent('technologies', $('#formFeatureAdd'), $('#subcontainer'));
	}

	//To show the validation error
    function findError(data) {
        if (!isBlank(data.nameError)) {
            showError($("#nameControl"), $("#nameError"), data.nameError);
        } else {
            hideError($("#nameControl"), $("#nameError"));
        }

        if (!isBlank(data.dispNameError)) {
            showError($("#dispNameControl"), $("#dispNameError"), data.dispNameError);
        } else {
            hideError($("#dispNameControl"), $("#dispNameError"));
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
            if (<%= fromPage.equalsIgnoreCase(ServiceUIConstants.ADD)%> ) {
                disableUploadButton($("#feature-file-uploader"));
        	}
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
	function createUploader(allowedFiles,fileErr) {
		var featureUploader = new qq.FileUploader({
			element : document.getElementById('feature-file-uploader'),
			action : 'uploadFeatureFile',
			multiple : false,
			uploadId: 'featureUploadId',
			allowedExtensions : allowedFiles,
			fileType : 'featureJar',
			buttonLabel : '<s:label key="lbl.comp.featr.upload" />',
			typeError : fileErr,
			params : {
				fileType : 'featureJar',
				type : '<%= type %>'
			},
			debug : true
		});
	
		var featureUploader = new qq.FileUploader({
			element : document.getElementById('feature-img-uploader'),
			action : 'uploadIconFile',
			multiple : false,
			uploadId: 'featureUploadImg',
			allowedExtensions : ["png"],
			fileType : 'featureImg',
			buttonLabel : '<s:label key="lbl.comp.featricon.upload" />',
			typeError : '<s:text name="err.invalid.img.file" />',
			params : {
				fileType : 'featureImg',
				type : '<%= type %>'
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
	
	//To get the features to add the dependencies
	function getFeatures() {
		var length = $('input[name=multiTechnology]:checked').length;	
		if (length > 0) {
			enableDivCtrls($('#applicableToDiv :input'));
			hideError($("#applyControl"), $("#techError"));
	 		yesnoPopup("fetchFeaturesForDependency", '<s:text name="lbl.hdr.comp.featr.popup.title"/>', 'saveDependentFeatures', '<s:text name="lbl.btn.ok"/>', $('#formFeatureAdd'));
	 		if ('<%= versioning%>' === 'versioning') {
				$('#applicableToDiv :input').attr("disabled", true);
			}
		} else {
		 	showError($("#applyControl"), $("#techError"), "Select atleast one technology");
		}
	}
	
	function downloadFile() {
		window.location.href="admin/featureUrl?" + $('#formFeatureAdd').serialize();
	}
	
</script>