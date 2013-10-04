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

<%@ page import="com.photon.phresco.commons.model.ArtifactInfo"%>
<%@ page import="com.photon.phresco.commons.model.ArtifactGroup"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<% 
	List<ArtifactGroup> moduleGroups = (List<ArtifactGroup>) request.getAttribute(ServiceUIConstants.REQ_FEATURES_MOD_GRP);
	List<ArtifactGroup> features = (List<ArtifactGroup>) request.getAttribute(ServiceUIConstants.REQ_FEATURES_TYPE_MODULE);
	List<ArtifactGroup> jsLibrary = (List<ArtifactGroup>) request.getAttribute(ServiceUIConstants.REQ_FEATURES_TYPE_JS);
	List<String> dependentModuleIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_FEATURES_DEPENDENT_MOD_IDS);
	List<String> artifactGroups = (List<String>) request.getAttribute(ServiceUIConstants.REQ_SELECTED_DEPEDENCY_IDS);
%>

<form id="formDependency">
	<% if (CollectionUtils.isEmpty(moduleGroups) && CollectionUtils.isEmpty(features) && CollectionUtils.isEmpty(jsLibrary)) { %>
		<div class="alert alert-block">
			<s:text name='alert.msg.feature.not.available'/>
		</div>
   	<% } else { %>
		<div class="theme_accordion_container jsLib_accordion_container">
		    <section class="accordion_panel_wid">
		        <div class="accordion_panel_inner">
		            <section class="lft_menus_container">
	            	<div style="background-color:#DE522F; color:white; width:100%;font-weight:bold;"><%= moduleGroups.get(0).getType().name() %></div>
		            <%
						for (ArtifactGroup moduleGroup : moduleGroups) {
					%>
		                <span class="siteaccordion closereg">
		                	<span class="dependencySpan">
		                		<input type="checkbox" name="dependentModGroupId" value="<%= moduleGroup.getId()%>" 
		                			id="<%= moduleGroup.getId()%>checkBox" class="floatLeft">
		                		&nbsp;&nbsp;<%= moduleGroup.getName() %>&nbsp;&nbsp;
		                		<p id="<%= moduleGroup.getId()%>version" class="version floatRight"></p>
		                	</span>
		                </span>
		                <div class="mfbox siteinnertooltiptxt hideContent">
		                    <div class="scrollpanel">
		                        <section class="scrollpanel_inner">
		                        	<table class="download_tbl">
			                            <tbody>
			                            <% 
			                            	List<ArtifactInfo> versions = moduleGroup.getVersions();
									    	if (CollectionUtils.isNotEmpty(versions)) {
												for (ArtifactInfo module : versions) {
												    String descContent = "";
													if (StringUtils.isNotEmpty(module.getDescription())) {
													  	descContent = module.getDescription();
													}
													
													String helpTextContent = "";
													if (StringUtils.isNotEmpty(module.getHelpText())) { 
													  	helpTextContent = module.getHelpText();
													}
													
													//To check the already selected dependent modules
													String checkedStr = "";
													if (CollectionUtils.isNotEmpty(dependentModuleIds)) {
												        if (dependentModuleIds.contains(module.getId())) {
												            checkedStr = "checked";
												        } else {
												            checkedStr = "";
												        }
													}
													if (CollectionUtils.isNotEmpty(artifactGroups)) {
												        if (artifactGroups.contains(module.getId())) {
												            checkedStr = "checked";
												        } else {
												            checkedStr = "";
												        }
													}
										%>
											<tr>
												<td class="editFeatures_td1">
													<input type="radio" class="module" name="<%= moduleGroup.getId() %>" value="<%= module.getId() %>"
														<%= checkedStr %> version="<%= module.getVersion() %>" onchange="selectCheckBox('<%= moduleGroup.getId()%>', this);">
												</td>
												<td class="fontColor"><%= moduleGroup.getName() %></td>
												<td class="fontColor"><%= module.getVersion() %></td>
											</tr>
										<%	
												}
								    		}
										%>
			                            </tbody>
		                        	</table>
		                        </section>
		                    </div>
		                </div>
		                <% 		
							}
						%>	
		            </section>  
		            
		            
		            <section class="lft_menus_container">
		            <%
		            if (CollectionUtils.isNotEmpty(features)) {
		            	%>
		            	<div style="background-color:#DE522F; color:white; width:100%;font-weight:bold;"><%= features.get(0).getType().name() %></div>
		            	<%
						for (ArtifactGroup moduleGroup : features) {
					%>
		                <span class="siteaccordion closereg">
		                	<span class="dependencySpan">
		                		<input type="checkbox" name="dependentModGroupId" value="<%= moduleGroup.getId()%>" 
		                			id="<%= moduleGroup.getId()%>checkBox" class="floatLeft">
		                		&nbsp;&nbsp;<%= moduleGroup.getName() %>&nbsp;&nbsp;
		                		<p id="<%= moduleGroup.getId()%>version" class="version floatRight"></p>
		                	</span>
		                </span>
		                <div class="mfbox siteinnertooltiptxt hideContent">
		                    <div class="scrollpanel">
		                        <section class="scrollpanel_inner">
		                        	<table class="download_tbl">
			                            <tbody>
			                            <% 
			                            	List<ArtifactInfo> versions = moduleGroup.getVersions();
									    	if (CollectionUtils.isNotEmpty(versions)) {
												for (ArtifactInfo module : versions) {
												    String descContent = "";
													if (StringUtils.isNotEmpty(module.getDescription())) {
													  	descContent = module.getDescription();
													}
													
													String helpTextContent = "";
													if (StringUtils.isNotEmpty(module.getHelpText())) { 
													  	helpTextContent = module.getHelpText();
													}
													
													//To check the already selected dependent modules
													String checkedStr = "";
													if (CollectionUtils.isNotEmpty(dependentModuleIds)) {
												        if (dependentModuleIds.contains(module.getId())) {
												            checkedStr = "checked";
												        } else {
												            checkedStr = "";
												        }
													}
													if (CollectionUtils.isNotEmpty(artifactGroups)) {
												        if (artifactGroups.contains(module.getId())) {
												            checkedStr = "checked";
												        } else {
												            checkedStr = "";
												        }
													}
										%>
											<tr>
												<td class="editFeatures_td1">
													<input type="radio" class="module" name="<%= moduleGroup.getId() %>" value="<%= module.getId() %>"
														<%= checkedStr %> version="<%= module.getVersion() %>" onchange="selectCheckBox('<%= moduleGroup.getId()%>', this);">
												</td>
												<td class="fontColor"><%= moduleGroup.getName() %></td>
												<td class="fontColor"><%= module.getVersion() %></td>
											</tr>
										<%	
												}
								    		}
										%>
			                            </tbody>
		                        	</table>
		                        </section>
		                    </div>
		                </div>
		                <% 		
							}
   						}
						%>	
		            </section> 
		            
		            
		            <section class="lft_menus_container">
		            <%
		            if (CollectionUtils.isNotEmpty(jsLibrary)) {
		            	%>
		            	<div style="background-color:#DE522F; color:white; width:100%;font-weight:bold;"><%= jsLibrary.get(0).getType().name() %></div>
		            	<%
						for (ArtifactGroup moduleGroup : jsLibrary) {
					%>
		                <span class="siteaccordion closereg">
		                	<span class="dependencySpan">
		                		<input type="checkbox" name="dependentModGroupId" value="<%= moduleGroup.getId()%>" 
		                			id="<%= moduleGroup.getId()%>checkBox" class="floatLeft">
		                		&nbsp;&nbsp;<%= moduleGroup.getName() %>&nbsp;&nbsp;
		                		<p id="<%= moduleGroup.getId()%>version" class="version floatRight"></p>
		                	</span>
		                </span>
		                <div class="mfbox siteinnertooltiptxt hideContent">
		                    <div class="scrollpanel">
		                        <section class="scrollpanel_inner">
		                        	<table class="download_tbl">
			                            <tbody>
			                            <% 
			                            	List<ArtifactInfo> versions = moduleGroup.getVersions();
									    	if (CollectionUtils.isNotEmpty(versions)) {
												for (ArtifactInfo module : versions) {
												    String descContent = "";
													if (StringUtils.isNotEmpty(module.getDescription())) {
													  	descContent = module.getDescription();
													}
													
													String helpTextContent = "";
													if (StringUtils.isNotEmpty(module.getHelpText())) { 
													  	helpTextContent = module.getHelpText();
													}
													
													//To check the already selected dependent modules
													String checkedStr = "";
													if (CollectionUtils.isNotEmpty(dependentModuleIds)) {
												        if (dependentModuleIds.contains(module.getId())) {
												            checkedStr = "checked";
												        } else {
												            checkedStr = "";
												        }
													}
													if (CollectionUtils.isNotEmpty(artifactGroups)) {
												        if (artifactGroups.contains(module.getId())) {
												            checkedStr = "checked";
												        } else {
												            checkedStr = "";
												        }
													}
										%>
											<tr>
												<td class="editFeatures_td1">
													<input type="radio" class="module" name="<%= moduleGroup.getId() %>" value="<%= module.getId() %>"
														<%= checkedStr %> version="<%= module.getVersion() %>" onchange="selectCheckBox('<%= moduleGroup.getId()%>', this);">
												</td>
												<td class="fontColor"><%= moduleGroup.getName() %></td>
												<td class="fontColor"><%= module.getVersion() %></td>
											</tr>
										<%	
												}
								    		}
										%>
			                            </tbody>
		                        	</table>
		                        </section>
		                    </div>
		                </div>
		                <% 		
							}
		            	}
						%>	
		            </section> 
		            
		            
		        </div>
		    </section>
		</div>	
	<% } %>
</form>

<script language="JavaScript" type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".accordion_panel_inner").scrollbars();  
	}
	
	$(document).ready(function() {
		accordion();
		$('.popupOk').attr("onClick", "popupOnOk(this);");
		//Check box click function to check the first radio button and show the selected version
		$('input[name="dependentModGroupId"]').change(function() {
			var modGrpId = $(this).val();
			var isCheckboxChecked = $(this).is(":checked");
			if (isCheckboxChecked) {
				$("input:radio[name='" + modGrpId + "']:first").prop("checked", true);
				var version = $("input:radio[name='" + modGrpId + "']:first").attr("version");
				$("p[id='" + modGrpId + "version']").html(version);
			} else {
				$("input:radio[name='" + modGrpId + "']").prop("checked", false);
				$("p[id='" + modGrpId + "version']").html("");
			}
		});
		
		//To check the selected modules group checkbox and show the selected version
		$("input[type=radio]:checked").each(function() {
			var version = $(this).attr('version');
			var moduleGroupId = $(this).attr('name');
			$("p[id='" + moduleGroupId + "version']").html(version);
			$("#" + moduleGroupId + "checkBox").prop("checked", true);
		});
	});
	
	//To save the selected dependency module ids
	function popupOnOk(self) {
		var size = $('input[name="dependentModGroupId"]:checked').size();
		if(size > 0) {
			$('#totalSize').html(size+" Dependencies Added");
		} else {
			$('#totalSize').empty();
		}
		loadContent('saveDependentFeatures', $('#formDependency'), '', '', true);
	}
	
	//Radio button click function to check the checkbox and show the selected version
	function selectCheckBox(moduleId, currentElement) {
		var version = $(currentElement).attr("version");
		$("input[id='" + moduleId + "checkBox']").prop("checked", true);
		$("p[id='" + moduleId + "version']").html(version);
	}
	
	//To enable the page because the page will not be refreshed
	function successEvent(url, data) {
		showParentPage();
	}
</script>