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
<%@ page import="com.photon.phresco.commons.model.FunctionalFrameworkGroup"%>
<%@ page import="com.photon.phresco.commons.model.FunctionalFramework"%>
<%@ page import="com.photon.phresco.commons.model.FunctionalFrameworkInfo"%>



<%
	Technology technology = (Technology) request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPE);
	String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
	List<ApplicationType> appTypes = (List<ApplicationType>) request.getAttribute(ServiceUIConstants.REQ_APP_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
	List<TechnologyOptions> options = (List<TechnologyOptions>) request.getAttribute(ServiceUIConstants.REQ_TECHNOLOGY_OPTION);
	List<FunctionalFrameworkGroup> functionalFrameworksGroups = (List<FunctionalFrameworkGroup>) request.getAttribute(ServiceUIConstants.REQ_FUNCTIONAL_FRAMEWORKS);
	List<Reports> reports = (List<Reports>)request.getAttribute(ServiceUIConstants.REQ_TECHNOLOGY_REPORTS);
	List<Technology> technologies = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	String title = ServiceActionUtil.getTitle(ServiceUIConstants.ARCHETYPES, fromPage);
	String buttonLbl = ServiceActionUtil.getButtonLabel(fromPage);
	String pageUrl = ServiceActionUtil.getPageUrl(ServiceUIConstants.ARCHETYPES, fromPage);
	String progressTxt = ServiceActionUtil.getProgressTxt(ServiceUIConstants.ARCHETYPES, fromPage);
	String versioning = (String)request.getAttribute(ServiceUIConstants.REQ_VERSIONING);
	String checkedStr = "";
	String disabledVer ="";
	if (StringUtils.isNotEmpty(versioning)) {
		disabledVer = "disabled";
	}
	
	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "btn-primary";
	if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_ARCHETYPES)) {
		per_disabledStr = "disabled";
		per_disabledClass = "btn-disabled";
	}
	
	
	List<String> ffIds = new ArrayList<String>();
	if (technology != null) {
		List<FunctionalFrameworkInfo> ffis = technology
				.getFunctionalFrameworksInfo();
		if (CollectionUtils.isNotEmpty(ffis)) {
			for (FunctionalFrameworkInfo ffi : ffis) {
				List<String> ffids = ffi.getFunctionalFrameworkIds();
				String grpId = ffi.getFrameworkGroupId();
				for (String ffid : ffids) {
					String addedString = grpId + "#" + ffid;
					ffIds.add(addedString);
				}
			}
		}
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
	String selectedTech = "";
	List<String> selectedReports = null;
	List<String> selectedFunctionalFrameworks = null;
	List<String> subModules = null;
	boolean isMultiModule = false;
	if (technology != null) {
		name = technology.getName();
		desc = technology.getDescription();
		archArchetypeId = technology.getArchetypeInfo().getArtifactId();		
		archGroupId = technology.getArchetypeInfo().getGroupId();
		isMultiModule = technology.isMultiModule();
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
		subModules = technology.getSubModules();
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
		
		 <div class="control-group apptype" id="techGroupControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.archetype.techgroup' />
			</label>
			<div class="controls">
				<select id="techGroup" name="techGroup" <%= disabledVer %>>
					
				</select> 
				<span class="help-inline" id="techGroupError"></span>
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
					<a href="#" onclick="downloadFile();"><%= name %>-<%= archVersions %></a>
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
		
		<% if (CollectionUtils.isNotEmpty(technologies)) { %>
			<div class="control-group" >
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.multi.module'/></label>
				<div class="controls">
					<%
						if (isMultiModule) {
							checkedStr = "checked";
						}
					%>
					<input name="multiModule" type="checkbox" value="true" <%= checkedStr %>/>
				</div>
			</div>
			
			<div class="control-group hideContent" id="subModulesControl">
				<label class="control-label labelbold">
					<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.sub.modules'/>
				</label>
				<div class="controls">
						<div class="typeFields" id="typefield">
						<div class="multilist-scroller multiselct" id="subModulesDiv">
							<ul>
								<li>
									<input type="checkbox" value="" <%= disabledVer %> id="checkAllSubModules" onclick="checkAllEvent(this,$('.subModules'), false);"
										style="margin: 3px 8px 6px 0;"><s:text name='lbl.all'/>
								</li>
								<%
									if (CollectionUtils.isNotEmpty(technologies)) {
										checkedStr = "";
										for (Technology tech : technologies) {
											if (CollectionUtils.isNotEmpty(subModules)) {
												if (subModules.contains(tech.getId())) {
													checkedStr = "checked";
												} else {
													checkedStr = "";
												}
											}
								%>
											<li> <input type="checkbox" id="subModules" name="subModules" value='<%= tech.getId() %>'
												onclick="checkboxEvent($('#checkAllSubModules'), 'subModules')"	class="check subModules" <%= checkedStr %> ><%= tech.getName() %>
											</li>
								<%		}	
									}
								%>
							</ul>
						</div>
					</div>
	          		 <span class="help-inline applyerror" id="subModulesError"></span>
				</div>
			</div>
		<% } %>
		
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
									checkedStr = "";
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
		<!-- functional framework starts -->
		<div id="func_frmwrk">
			<%
			if (CollectionUtils.isNotEmpty(functionalFrameworksGroups)) {
		%>						
			<div class="control-group" id="ffControl">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.ff'/>
				</label>
				<div class="controls">
						<div class="ffFields" id="fffield">
						<div class="multilist-scroller multiselct" id="applicableToFF">
							<ul>
								<%
									for (FunctionalFrameworkGroup functionalFrameworksGroup : functionalFrameworksGroups) {
										if (functionalFrameworksGroup != null) {
								%>
											<li style="font-weight:900;"> <%= functionalFrameworksGroup.getName() %></li>
								<%		
											List<FunctionalFramework> functionalFrameworks = functionalFrameworksGroup.getFunctionalFrameworks();
											String checked = "";
											if (CollectionUtils.isNotEmpty(functionalFrameworks)) {
												for (FunctionalFramework functionalFramework : functionalFrameworks) {
													if (CollectionUtils.isNotEmpty(ffIds)) {
														checked = "";
														for (String ffId : ffIds) {
															String[] appended = ffId.split("#");
															if (appended != null) {
																if (appended[0].equals(functionalFrameworksGroup.getId()) && appended[1].equals(functionalFramework.getId())) {
																	checked = "checked";
																} 
															}
														}
													}
								%>
													<li> <input type="checkbox" id="applicableFF" <%= disabledVer %> name="functionalFrameworkInfo" value='<%= functionalFrameworksGroup.getId() %>#<%= functionalFramework.getId() %>'
															 class="check applicableFF"  <%= checked %>><%= functionalFramework.getName() %>
													</li>
								<%
												}
											}
										}	
									}
								%>
							</ul>
						</div>
					</div>
					<span class="help-inline applyerror" id="ffError"></span>
				</div>
				
			</div>
		<%
			}	
		%>
		</div>
		<!--  functional framework ends -->
		
		<!--  embed Technology  starts-->
		<div id="embed_tech">
		<%
			if (CollectionUtils.isNotEmpty(technologies)) {
		%>						
			<div class="control-group" id="techControl">
				<label class="control-label labelbold">
					<s:text name='lbl.hdr.comp.tchngy'/>
				</label>
				<div class="controls">
						<div class="techFields" id="techfield">
						<div class="multilist-scroller multiselct" id="applicableToTech">
							<ul>
								<li>
									<input type="checkbox" value="" id="checkAllTechnology" name="" onclick="checkAllEvent(this,$('.applicableTechnology'), false);"
										style="margin: 3px 8px 6px 0;" <%= disabledVer %> ><s:text name='lbl.all'/>
								</li>
							<%
									if (CollectionUtils.isNotEmpty(technologies)) {
										checkedStr = "";
										for (Technology tech : technologies) {
											List<String> selectedOptions = new ArrayList<String>();
											if (technology != null) {
												if (CollectionUtils.isNotEmpty(technology.getApplicableEmbedTechnology())) {
													for (String embedTechId : technology.getApplicableEmbedTechnology()) {
														selectedOptions.add(embedTechId);
													}
												}
												if (selectedOptions.contains(tech.getId())) {
													checkedStr = "checked";
												} else {
													checkedStr = "";
												}
											}
							%>
												<li> <input type="checkbox" id="applicableTechnology" <%= disabledVer %> name="applicableEmbedTechnology" value='<%= tech.getId() %>'
													onclick="checkboxEvent($('#checkAllTechnology'), 'applicableTechnology')"	class="check applicableTechnology" <%= checkedStr %> ><%= tech.getName() %>
												</li>
			 			   <%			}	
									}
							%>
							</ul>
						</div>
					</div>
					<span class="help-inline applyerror" id="techError"></span>
				</div>
				
			</div>
		<%
			}	
		%>
		</div>
		<!--  embed Technology  ends-->
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
									checkedStr = "";
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
									checkedStr = "";
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
	<div class="bottom_button">
		<%
			String disabledClass = "btn-primary";
			String disabled = "";
			if (isSystem) {
// 				disabledClass = "btn-disabled";
// 				disabled = "disabled";
			}
		%>
		<input type="button" id="" class="btn <%= disabledClass %> <%= per_disabledClass %>" <%= disabled %> <%= per_disabledStr %> value='<%= buttonLbl %>'
			onclick="validate('<%= pageUrl %>', $('#formArcheTypeAdd'), $('#subcontainer'), '<%= progressTxt %>', $('.content_adder :input'));" />
		
		<input type="button" id="archetypeCancel" class="btn btn-primary" value="<s:text name='lbl.btn.cancel'/>" 
            onclick="getArchetypes();"/>
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
        hideShowTechs();
        checkboxEvent($('#checkAllFeatures'), 'applsChk');
        checkboxEvent($('#checkAllReports'), 'reportsChk');
        checkboxEvent($('#checkAllTechnology'), 'applicableTechnology');
        checkboxEvent($('#checkAllSubModules'), 'subModules');
        $("#funcFrameworksControl").hide();
    	var functionalStatus = $("input[value='Functional_Test']").attr("checked");
    	if (functionalStatus === "checked") {
    		 $("#funcFrameworksControl").show();
    	}
        // To focus the name textbox by default
        $('#archename').focus();
        
        if (<%= isSystem %>) { 
        	disableUploadButton($("#appln-file-uploader"));
        }
        
        if (<%= isMultiModule %>) {
       		$('#subModulesControl').show();        		
        } else {
        	$('#subModulesControl').hide();
        }
        
        $('#checkAllFeatures').click(function() {
        	hideShowTechs();
        });

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
        
        $('.applsChk[value=Embed_Application]').change(function() {
        	hideShowTechs();
        });
        
        $('.applsChk[value=Functional_Test]').change(function() {
        	hideShowTechs();
        });
        
       	$("input[value='Functional_Test']").change(function() {
        	var status = $(this).attr("checked");
        	showOrHideFunctionalFramework(status);
        });
       	
       	$("input[name='applicableFeatures']").change(function() {
       		var status = $(this).attr("checked");
       		showOrHideFunctionalFramework(status);
       	});
        if ( '<%= versioning %>' != "versioning" ){
			$("#versionComment").hide();
		}
        
        $('input[name=multiModule]').click(function() {
        	if ($('input[name=multiModule]').is(':checked')) {
        		$('#subModulesControl').show();        		
        	} else {
        		$('#subModulesControl').hide();
        	}
        });
    });

	$(document).keyup(function(e) {
    	if (e.keyCode == 27) {    		  
    		$("#progressBar").hide();
    	} 
    });
	
	function hideShowTechs() {
		if($('.applsChk[value=Embed_Application]').is(':checked')) {
    		$("#embed_tech :input").attr("disabled", false);
    		$("#embed_tech").show();
    	} else {
    		$("#embed_tech :input").attr("disabled", true);
    		$("#embed_tech").hide();
    	}
		
		if($('.applsChk[value=Functional_Test]').is(':checked')) {
    		$("#func_frmwrk :input").attr("disabled", false);
    		$("#func_frmwrk").show();
    	} else {
    		$("#func_frmwrk :input").attr("disabled", true);
    		$("#func_frmwrk").hide();
    	}
	}
	
	function showOrHideFunctionalFramework(status){
		if (status === "checked") {
    		$("#funcFrameworksControl").show();
    	} else {
    		$("#funcFrameworksControl").hide();
    	}
	}
	function getArchetypes() {
		showLoadingIcon();
		loadContent('archetypesList', $('#formArcheTypeAdd'), $('#subcontainer'));
	}
	   
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
		
		if (!isBlank(data.techGroupError)) {
			showError($("#techGroupControl"), $("#techGroupError"), data.techGroupError);
		} else {
			hideError($("#techGroupControl"), $("#techGroupError"));
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
		
		if (!isBlank(data.funcFrameworksError)) {
			showError($("#funcFrameworksControl"), $("#funcFrameworksError"), data.funcFrameworksError);
		} else {
			hideError($("#funcFrameworksControl"), $("#funcFrameworksError"));
		}
		
		if (!isBlank(data.techErr)) {
			showError($("#techControl"), $("#techError"), data.techErr);
		} else {
			hideError($("#techControl"), $("#techError"));
		}
		
		if (!isBlank(data.funcFrameworksError)) {
			showError($("#ffControl"), $("#ffError"), data.funcFrameworksError);
		} else {
			hideError($("#ffControl"), $("#ffError"));
		}
		
		if (!isBlank(data.subModulesError)) {
			showError($("#subModulesControl"), $("#subModulesError"), data.subModulesError);
		} else {
			hideError($("#subModulesControl"), $("#subModulesError"));
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