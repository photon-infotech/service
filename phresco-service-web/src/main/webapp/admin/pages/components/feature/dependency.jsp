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

<%@ page import="com.photon.phresco.commons.model.ArtifactInfo"%>
<%@ page import="com.photon.phresco.commons.model.ArtifactGroup"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<% 
	List<ArtifactGroup> moduleGroups = (List<ArtifactGroup>) request.getAttribute(ServiceUIConstants.REQ_FEATURES_MOD_GRP);
	List<String> dependentModuleIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_FEATURES_DEPENDENT_MOD_IDS);
%>

<form id="formDependency">
<div class="modal">
	<div class="modal-header">
		<a class="close" id="close">&times;</a>
	  	<h3><s:label key="lbl.hdr.comp.featr.popup.title" theme="simple"/></h3>
	</div>
	
	<div class="modal-body feat_modal-body">
		<% if (CollectionUtils.isEmpty(moduleGroups)) { %>
			<div class="alert alert-block">
				<s:text name='alert.msg.feature.not.available'/>
			</div>
    	<% } else { %>
			<div class="theme_accordion_container jsLib_accordion_container">
			    <section class="accordion_panel_wid">
			        <div class="accordion_panel_inner">
			            <section class="lft_menus_container">
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
													        if (dependentModuleIds.contains(module.getVersion())) {
													            checkedStr = "checked";
													        } else {
													            checkedStr = "";
													        }
														}
											%>
												<tr>
													<td class="editFeatures_td1">
														<input type="radio" class="module" name="<%= moduleGroup.getId() %>" value="<%= module.getVersion() %>"
															<%= checkedStr %> onchange="selectCheckBox('<%= moduleGroup.getId()%>', this);">
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
			        </div>
			    </section>
			</div>	
		<% } %>
	</div>
	
	<div class="modal-footer">
		<a href="#" class="btn btn-primary" id="cancel"><s:label key="lbl.btn.cancel"/></a>
	  	<a href="#" class="btn btn-primary" id="saveDependency"><s:label key="lbl.btn.ok"/></a>
	</div>
</div>
</form>

<script language="JavaScript" type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".accordion_panel_inner").scrollbars();  
	}
	
	$(document).ready(function() {
		//To close the popup
		$('#cancel, #close').click(function() {
			showParentPage();
		});
		
		//Check box click function to check the first radio button and show the selected version
		$('input[name="dependentModGroupId"]').change(function() {
			var modGrpId = $(this).val();
			var isCheckboxChecked = $(this).is(":checked");
			if (isCheckboxChecked) {
				$("input:radio[name='" + modGrpId + "']:first").prop("checked", true);
				var version = $("input:radio[name='" + modGrpId + "']:first").val();
				$("p[id='" + modGrpId + "version']").html(version);
			} else {
				$("input:radio[name='" + modGrpId + "']").prop("checked", false);
				$("p[id='" + modGrpId + "version']").html("");
			}
		});
		
		//To save the selected dependency module ids
		$('#saveDependency').click(function() {
			showParentPage();
			loadContent('saveDependentFeatures', $('#formDependency'), $('#popup_div'), '', true);
		});
		
		//To check the selected modules group checkbox and show the selected version
		$("input[type=radio]:checked").each(function() {
			var version = $(this).val();
			var moduleGroupId = $(this).attr('name');
			$("p[id='" + moduleGroupId + "version']").html(version);
			$("#" + moduleGroupId + "checkBox").prop("checked", true);
		});
	});
	
	//Radio button click function to check the checkbox and show the selected version
	function selectCheckBox(moduleId, currentElement) {
		var version = currentElement.value;
		$("input[id='" + moduleId + "checkBox']").prop("checked", true);
		$("p[id='" + moduleId + "version']").html(version);
	}
	
	//To enable the page because the page will not be refreshed
	function successEvent(url, data) {
		showParentPage();
	}
</script>