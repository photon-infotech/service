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
<%@ page import="java.util.ArrayList"%>

<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.ApplicationType"%>
<%@ page import="com.photon.phresco.commons.model.Technology"%>
<%@ page import="com.photon.phresco.commons.model.TechnologyOptions"%>
<%@ page import="com.photon.phresco.commons.model.ArtifactGroup"%>
<%@ page import="com.photon.phresco.commons.model.ArtifactInfo"%>
<%@ page import="com.phresco.pom.site.Reports"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>
<%@ page import="com.photon.phresco.service.admin.actions.util.ServiceActionUtil"%>
<%@ page import="com.photon.phresco.commons.model.TechnologyGroup"%>
<%@ page import="com.photon.phresco.commons.model.FunctionalFramework"%>

<%
	Technology technology = (Technology) request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPE);
	String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
	List<ApplicationType> appTypes = (List<ApplicationType>) request.getAttribute(ServiceUIConstants.REQ_APP_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
	List<TechnologyOptions> options = (List<TechnologyOptions>) request.getAttribute(ServiceUIConstants.REQ_TECHNOLOGY_OPTION);
	List<FunctionalFramework> functionalFrameworks = (List<FunctionalFramework>) request.getAttribute(ServiceUIConstants.REQ_FUNCTIONAL_FRAMEWORKS);
	List<Reports> reports = (List<Reports>)request.getAttribute(ServiceUIConstants.REQ_TECHNOLOGY_REPORTS);
	List<Technology> technologies = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	String title = ServiceActionUtil.getTitle(ServiceUIConstants.ARCHETYPES, fromPage);
	String buttonLbl = ServiceActionUtil.getButtonLabel(fromPage);
	String pageUrl = ServiceActionUtil.getPageUrl(ServiceUIConstants.ARCHETYPES, fromPage);
	String progressTxt = ServiceActionUtil.getProgressTxt(ServiceUIConstants.ARCHETYPES, fromPage);
	String versioning = (String)request.getAttribute(ServiceUIConstants.REQ_VERSIONING);
	String disabledVer ="";
	if (StringUtils.isNotEmpty(versioning)) {
		disabledVer = "disabled";
	}
	
	//For edit
	String name = "";
	String desc = "";
	String version = "";
	String versionComment = "";
	String archArchetypeId = "";
	String archGroupId = "";
	String archVersions = "";
	List<String> techVersions = null;
	boolean isSystem = false;
	String appTypeId = "";
	String techId = "";
	String techVer = "";
	String selectedTech= "";
	List<String> selectedReports = null;
	List<String> selectedFunctionalFrameworks = null;
	if (technology != null) {
		name = technology.getName();		
		desc = technology.getDescription();
		archArchetypeId = technology.getArchetypeInfo().getArtifactId();		
		archGroupId = technology.getArchetypeInfo().getGroupId();		
		archVersions = technology.getArchetypeInfo().getVersions().get(0).getVersion();
		techVersions = technology.getTechVersions();
		if (CollectionUtils.isNotEmpty(techVersions)) {
			techVer  = techVersions.toString().replace("[", "").replace("]", "");
		}
		isSystem = technology.isSystem();
		appTypeId = technology.getAppTypeId();
		techId = technology.getTechGroupId();
		selectedReports = technology.getReports();
		selectedFunctionalFrameworks = (List<String>) request.getAttribute(ServiceUIConstants.REQ_SELECTED_FUNCTIONAL_FRAMEWORKS);
	}
%>

<form id="formArcheTypeAdd" class="form-horizontal customer_list">
	<h4 class="hdr">
		<%= title %>
	</h4>
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.name' />
			</label>
			<div class="controls">
				<input id="archename"  placeholder='<s:text name="place.hldr.archetype.add.name"/>' class="input-xlarge" type="text" 
					name="name" <%= disabledVer %> value="<%= name %>" maxlength="30" title="30 Characters only">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label labelbold"> <s:text
					name='lbl.hdr.comp.desc' /> </label>
			<div class="controls">
				<textarea id="description" class="input-xlarge" placeholder='<s:text name="place.hldr.archetype.add.desc"/>' 
					rows="3" name="description" maxlength="150" title="150 Characters only"><%= desc %></textarea>
			</div>
		</div>

  		<div class="control-group" id="techverControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.technologVersion' />
			</label>
			<div class="controls">
				<input id="techVersion" placeholder='<s:text name="place.hldr.archetype.add.technologyVersion"/>' class="input-xlarge" 
					type="text" name="techVersion" <%= disabledVer %> value="<%= StringUtils.isNotEmpty(techVer) ? techVer : "" %>" maxlength="30" title="30 Characters only">
				<span class="help-inline" id="techvernError"></span>
			</div>
		</div>  

		<div class="control-group" id="versionComment">
			<label class="control-label labelbold"> <s:text
					name='lbl.hdr.com.vercmnt' /> </label>
			<div class="controls">
				<textarea name="versionComment"  placeholder='<s:text name="place.hldr.archetype.add.ver.comment"/>' class="input-xlarge" 
					rows="2" cols="10" maxlength="150" title="150 Characters only"><%= versionComment %></textarea>
			</div>
		</div>

		<div class="control-group apptype" id="appControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.apptype' />
			</label>
			<div class="controls">
				<select id="appTypeLayer" name="apptype">
					<%
						if (CollectionUtils.isNotEmpty(appTypes)) {
							for (ApplicationType appType : appTypes) {
								String selectedStr= "";
								if (appType.getId().equals(appTypeId)) {
									selectedStr = "selected";
								}
					%>
								<option <%= disabledVer %> value="<%= appType.getId() %>" <%= selectedStr %>><%= appType.getName() %></option>
					<%
							}
						}
					%>
				</select> 
				<span class="help-inline" id="appError"></span>
			</div>
		</div>
		
		 <div class="control-group apptype" id="appControl">
			<label class="control-label labelbold"> <span
				class="mandatory"></span>&nbsp;<s:text name='lbl.hdr.archetype.techgroup' />
			</label>
			<div class="controls">
				<select id="techGroup" name="techGroup" <%= disabledVer %>>
					
				</select> 
				<span class="help-inline" id="appError"></span>
			</div>
		</div> 
		
		<!-- POM details starts -->
		<div id="jarDetailsDiv" class="hideContent">
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.groupid'/>
				</label>
				<div class="controls">
					<input id="groupid" name="groupId" class="input-xlarge groupId" maxlength="40" title="40 Characters only" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.groupId'/>">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.artifactid'/>
				</label>
				<div class="controls">
					<input id="artifId" name="artifactId" class="input-xlarge artifactId" maxlength="40" title="40 Characters only" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.artifactId'/>">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.jar.version'/>
				</label>
				<div class="controls">
					<input id="versnId" name="version" maxlength="30" title="30 Characters only" class="input-xlarge jarVersion" type="text"
						placeholder="<s:text name='place.hldr.archetype.add.jar.version'/>">
				</div>
			</div>
		</div>
		<!-- POM details ends -->
		
		<div class="control-group" id="appFileControl">
			<label class="control-label labelbold"> 
			<% if (!fromPage.equals(ServiceUIConstants.EDIT) || StringUtils.isNotEmpty(versioning)) { %>
				<span class="mandatory">*</span>
			<% } %>&nbsp;
				<s:text name='lbl.hdr.comp.archtypejar' />
			</label>
			<div class="controls" style="float: left; margin-left: 3%;">
				<div id="appln-file-uploader" class="file-uploader" title = "<s:text name='title.file.size'/>">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript>
				</div>
			</div>
			<span class="help-inline fileError" id="fileError"></span>
		</div>
		
		<% if (ServiceUIConstants.EDIT.equals(fromPage) && StringUtils.isNotEmpty(archArchetypeId)) { %>
			<div class="control-group" >
				<label class="control-label labelbold"> <s:text name="lbl.hdr.archetype.download" /> </label>
		       	<div class="controls">
					<a href="#" onclick="downloadFile();"><%= archArchetypeId %></a>
				</div>
			</div>
		<% } %>	
		
		<div class="control-group" >
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.plugindependencies'/></label>
			<div class="controls">
				<input id="pluginjarUpload" type="button" class="btn btn-primary" value="Upload PluginJar" 
					onclick="uploadPluginJar();" />
			</div>
		</div>
		
		<div class="control-group" id="applicableControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.applicable'/>
			</label>
			<div class="controls">
					<div class="typeFields" id="typefield">
					<div class="multilist-scroller multiselct" id="applicableToDiv">
						<ul>
							<li>
								<input type="checkbox" value="" <%= disabledVer %> id="checkAllFeatures" name="applicableFeatures" onclick="checkAllEvent(this,$('.applsChk'), false);"
									style="margin: 3px 8px 6px 0;"><s:text name='lbl.all'/>
							</li>
							<%
								if (CollectionUtils.isNotEmpty(options)) {
									String checkedStr = "";
									for (TechnologyOptions option : options) {
										List<String> selectedOptions = new ArrayList<String>();
										if (technology != null) {
											if (CollectionUtils.isNotEmpty(technology.getOptions())) {
												for (String technologyOption : technology.getOptions()) {
													selectedOptions.add(technologyOption);
												}
											}
											if (selectedOptions.contains(option.getId())) {
												checkedStr = "checked";
											} else {
											    checkedStr = "";
											}
										}
							%>
										<li>
											<input type="checkbox" id="appliestoCheckbox" <%= disabledVer %> name="applicable" value="<%= option.getId() %>"
												onclick="checkboxEvent($('#checkAllFeatures'), 'applsChk')" class="check applsChk" <%= checkedStr %>><%= option.getOption() %> 
										</li>
							<%		}
								}
							%>
						</ul>
					</div>
				</div>
          		 <span class="help-inline applyerror" id="applicableError"></span>
			</div>
		</div>
		<div class="control-group" id="reportsControl">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.reports'/>
			</label>
			<div class="controls">
				<div class="typeFields" id="typefield">
					<div class="multilist-scroller multiselct" id="applicableToDiv">
						<ul>
							<li>
								<input type="checkbox" value="" <%= disabledVer %> id="checkAllReports" name="" onclick="checkAllEvent(this,$('.reportsChk'), false);"
								style="margin: 3px 8px 6px 0;"><s:text name='lbl.all'/>
							</li>
							<%
								if (CollectionUtils.isNotEmpty(reports)) {
									String checkedStr = "";
									for (Reports report : reports) {
										if (CollectionUtils.isNotEmpty(selectedReports)) {
											if (selectedReports.contains(report.getId())) {
												checkedStr = "checked";
											} else {
												checkedStr = "";
											}
										}
							%>
										<li> <input type="checkbox" id="reportsCheckbox" <%= disabledVer %> name="applicableReports" value="<%= report.getId() %>"  onclick="checkboxEvent($('#checkAllReports'), 'reportsChk')"
											 class="check reportsChk" <%= checkedStr %>><%= report.getDisplayName() %> 
										</li>
							<%
									}	
								}
							%>
						</ul>
					</div>
				</div>
				<span class="help-inline applyerror" id="applicableError"></span>
			</div>
		</div>
		<%
			if (CollectionUtils.isNotEmpty(technologies)) {
		%>						
			<div class="control-group" id="applyControl">
				<label class="control-label labelbold">
					<s:text name='lbl.comp.featr.archetypefeatures'/>
				</label>
				<div class="controls">
						<div class="typeFields" id="typefield">
						<div class="multilist-scroller multiselct" id="applicableToDiv">
							<ul>
								<li>
									<input type="checkbox" value="" id="checkAllArchetype" name="" onclick="checkAllEvent(this,$('.applicableFeatures'), false);"
										style="margin: 3px 8px 6px 0;" <%= disabledVer %> ><s:text name='lbl.all'/>
								</li>
								<%
								if (CollectionUtils.isNotEmpty(technologies)) {
									String checkedStr = "";
									for (Technology tech : technologies) {
										List<String> selectedOptions = new ArrayList<String>();
										if (technology != null) {
											if (CollectionUtils.isNotEmpty(technology.getArchetypeFeatures())) {
												for (String selectedTechId : technology.getArchetypeFeatures()) {
													selectedOptions.add(selectedTechId);
												}
											}
											if (selectedOptions.contains(tech.getId())) {
												checkedStr = "checked";
											} else {
												checkedStr = "";
											}
										}
								%>
											<li> <input type="checkbox" id="applicableFeatures" <%= disabledVer %> name="applicableAtchetypeFeatures" value='<%= tech.getId() %>'
												onclick="checkboxEvent($('#checkAllArchetype'), 'applicableFeatures')"	class="check applicableFeatures" <%= checkedStr %> ><%= tech.getName() %>
											</li>
								<%		}	
									}
								%>
							</ul>
						</div>
					</div>
				</div>
			</div>
		<%
			}	
		%>
		
		<div class="control-group" id="funcFrameworksControl">
			<label class="control-label labelbold">
				<%-- <span class="mandatory">*</span>&nbsp; --%><s:text name='lbl.hdr.comp.func.test.frameworks'/>
			</label>
			<div class="controls">
				<div class="typeFields" id="typefield">
					<div class="multilist-scroller multiselct" id="funcFrameworksDiv">
						<ul>
							<li>
								<input type="checkbox" value="" id="checkAllFuncFrameworks" name="" onclick="checkAllEvent(this,$('.funcFrameworkChk'), false);"
									style="margin: 3px 8px 6px 0;"><s:text name='lbl.all'/>
							</li>
							<%
								if (CollectionUtils.isNotEmpty(functionalFrameworks)) {
									String checkedStr = "";
									for (FunctionalFramework functionalFramework : functionalFrameworks) {
										if (CollectionUtils.isNotEmpty(selectedFunctionalFrameworks)) {
											if (selectedFunctionalFrameworks.contains(functionalFramework.getId())) {
												checkedStr = "checked";
											} else {
												checkedStr = "";
											}
										}
							%>
										<li>
											<input type="checkbox" id="funcFrameworkCheckbox" name="functionalFrameworks" value="<%= functionalFramework.getName() %>" class="check funcFrameworkChk" 
												<%= checkedStr %> onclick="checkboxEvent($('#checkAllFuncFrameworks'), 'funcFrameworkChk')"><%= functionalFramework.getDisplayName() %> 
										</li>
							<%
									}	
								}
							%>
						</ul>
					</div>
				</div>
				<span class="help-inline applyerror" id="funcFrameworksError"></span>
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
		<input type="button" id="" class="btn <%= disabledClass %>" <%= disabled %> value='<%= buttonLbl %>'
			onclick="validate('<%= pageUrl %>', $('#formArcheTypeAdd'), $('#subcontainer'), '<%= progressTxt %>', $('.content_adder :input'));" />
		
		<input type="button" id="archetypeCancel" class="btn btn-primary" value="<s:text name='lbl.btn.cancel'/>" 
            onclick="loadContent('archetypesList', $('#formArcheTypeAdd'), $('#subcontainer'));"/>
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
	<input type="hidden" name="techId" value="<%= technology != null ? technology.getId() : "" %>"/>
	<input type="hidden" name="oldName" value="<%= technology != null ? technology.getName() : "" %>"/>
	<input type="hidden" name="customerId" value="<%= customerId %>">
	<input type="hidden" name="versioning" value="<%= versioning %>">
	<input type="hidden" name="uploadPlugin" value="uploadPlugin">
	<input type="hidden" name="archArchetypeId" value="<%= technology != null ? archArchetypeId : "" %>"/> 
    <input type="hidden" name="archGroupId" value="<%= technology != null ? archGroupId : "" %>"/>
    <input type="hidden" name="archVersions" value="<%= technology != null ? archVersions : "" %>"/>
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
		$(".multilist-scroller").scrollbars();
	}

    $(document).ready(function() {
    	hideLoadingIcon();
        createUploader();
        getTechGroup();
        checkboxEvent($('#checkAllFeatures'), 'applsChk');
        checkboxEvent($('#checkAllReports'), 'reportsChk');
        // To focus the name textbox by default
        $('#archename').focus();
        
        if (<%= isSystem %>) { 
        	disableUploadButton($("#appln-file-uploader"));
        }

        // To check for the special character in name
        $('#archename').bind('input propertychange', function (e) {
            var name = $(this).val();
            name = checkForSplChr(name);
            $(this).val(name);
        });

     	// To check for the special character in artifactId, groupId, version and jar version
        $('#artifId, #groupid, #versnId, #version').bind('input propertychange', function (e) {
            var artifId = $(this).val();
            artifId = checkForSplChrExceptDot(artifId);
            $(this).val(artifId);
        });
     	
        $("#appTypeLayer").change(function() {
			getTechGroup();
        });  
        
        if ( '<%= versioning %>' != "versioning" ){
			$("#versionComment").hide();
		}
    });

	$(document).keyup(function(e) {
    	if (e.keyCode == 27) {    		  
    		$("#progressBar").hide();
    	} 
    }); 
	   
	function getTechGroup() {
		loadContent('getTechGroup', $('#formArcheTypeAdd'), '', '', true);
		$('#techGroup').empty();
	}
   
	function successEvent(pageUrl, data) {
		if (pageUrl == "getTechGroup") {
			var techGroups = data.appTypeTechGroups;
			for (i in techGroups) {
				var id = techGroups[i].id;
				var name = techGroups[i].name;
				if (id == '<%= techId %>') {
					$('#techGroup').append($("<option selected></option>").attr("value", id).text(name));
				} else {
					$('#techGroup').append($("<option></option>").attr("value", id).text(name));
				}
			}
		}
	}

	function findError(data) {
		if (!isBlank(data.nameError)) {
			showError($("#nameControl"), $("#nameError"), data.nameError);
		} else {
			hideError($("#nameControl"), $("#nameError"));
		}

		if (data.verError != undefined) {
			showError($("#verControl"), $("#verError"), data.verError);
		} else {
			hideError($("#verControl"), $("#verError"));
		}

		if (!isBlank(data.techvernError)) {
			showError($("#techverControl"), $("#techvernError"), data.techvernError);
		} else {
			hideError($("#techverControl"), $("#techvernError"));
		}

		if (!isBlank(data.appError)) {
			showError($("#appControl"), $("#appError"), data.appError);
		} else {
			hideError($("#appControl"), $("#appError"));
		}

		if (!isBlank(data.fileError)) {
			showError($("#appFileControl"), $("#fileError"), data.fileError);
		} else {
			hideError($("#appFileControl"), $("#fileError"));
			if (<%= fromPage.equalsIgnoreCase(ServiceUIConstants.ADD) %>) {
                disableUploadButton($("#appln-file-uploader"));
			}
		}

		if (!isBlank(data.applicableErr)) {
			showError($("#applicableControl"), $("#applicableError"), data.applicableErr);
		} else {
			hideError($("#applicableControl"), $("#applicableError"));
		}
	}

	function jarError(data, type) {
		var controlObj;
		var msgObj;
		if (type == "applnJar") {
			controlObj = $("#appFileControl");
			msgObj = $("#fileError");
		}
		if (data != undefined && !isBlank(data)) {
			showError(controlObj, msgObj, data);
		} else {
			hideError(controlObj, msgObj);
		}
	}

	function createUploader() {
		var applnUploader = new qq.FileUploader({
			element : document.getElementById('appln-file-uploader'),
			action : 'uploadJar',
			multiple : false,
			allowedExtensions : [ "jar" ],
			type : 'applnJar',
			buttonLabel : '<s:label key="lbl.comp.arhtyp.upload" />',
			typeError : '<s:text name="err.invalid.jar.selection" />',
			params : {
				type : 'applnJar',
				archType : true
			},
			debug : true
		});
	}

	function removeUploadedJar(obj, btnId) {
		$('#jarDetailsDiv').hide();
		$(obj).parent().remove();
		var type = $(obj).attr("tempattr");
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
		enableDisableUploads(type, $("#" + btnId));
		jarError('', type);
	}

	function uploadPluginJar() {
		$('#popup_div').show();
		$('#popup_div').empty();
		disableScreen();
		loadContent('uploadPluginJar', $('#formArcheTypeAdd'), $('.modal-body'));
		hideLoadingIcon(); 
	}
	
	function downloadFile() {
		window.location.href="admin/archetypeUrl?" + $('#formArcheTypeAdd').serialize();
	}
</script>