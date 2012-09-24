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
<%@ page import=" com.photon.phresco.commons.model.ArtifactGroup" %>

<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<% 
	List<ArtifactGroup> moduleGroups = (List<ArtifactGroup>)request.getAttribute(ServiceUIConstants.REQ_FEATURES_MOD_GRP);
%>
    
   <div class="featuresScrollDiv">
   	<% 
		if (CollectionUtils.isEmpty(moduleGroups)) {
	%>
		<div class="alert alert-block">
			<s:text name='alert.msg.feature.not.available'/>
		</div>
	<% } else { %>	
		<div class="header-background accor_head">
			<table class="border_collapse">
				<tbody>
					<tr>
						<td>
							<input type="checkbox" id="checkAllAuto" name="moduleGroup" 
								onclick="checkAllEvent(this, $('.technology'), false);">
						</td>
						<td class="labelbold"><s:text name='lbl.hdr.comp.name'/></td>
					</tr>
				</tbody>
			</table>
		</div>	
		
		<div class="theme_accordion_container jsLib_accordion_container">
		    <section class="accordion_panel_wid">
		        <div class="accordion_panel_inner">
		            <section class="lft_menus_container">
		            <%
						for (ArtifactGroup moduleGroup : moduleGroups) {
					%>
		                <span class="siteaccordion closereg">
		                	<span>
	                			<% if (moduleGroup.isSystem()) { %>
									<input type="checkbox" name="moduleGroup" value="<%= moduleGroup.getId() %>" disabled/>&nbsp;&nbsp;<%= moduleGroup.getName() %>&nbsp;&nbsp;
								<% } else { %>
			                		<input type="checkbox" class="check technology" name="moduleGroup" value="<%= moduleGroup.getId()%>" id="<%= moduleGroup.getId()%>checkBox" onclick="checkboxEvent();">
			                		&nbsp;&nbsp;<%= moduleGroup.getName() %>&nbsp;&nbsp;
	                			<% } %>
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
													if (module.getDescription() != null) { 
													  	descContent = module.getDescription();
													}
													
													String helpTextContent = "";
													if (module.getHelpText() != null) { 
													  	helpTextContent = module.getHelpText();
													}
										%>
											<tr>
												<td>
													<input type="radio" name="<%= module.getId() %>" value="<%= module.getVersion() %>" >
												</td>
												<td>
													<a href="#" name="ModuleDesc" onclick="editFeature('<%= moduleGroup.getId() %>', '<%= module.getId() %>');" >
														<%= module.getName() %>
													</a>
												</td>
												<td><%= module.getVersion() %></td>
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

<script language="JavaScript" type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".accordion_panel_inner").scrollbars();  
	}
	
	$(document).ready(function() {
		toDisableCheckAll();
		enableScreen();
	});
	
	function editFeature(moduleGroupId, moduleId) {
		var params = "moduleGroupId=";
	    params = params.concat(moduleGroupId);
	    params = params.concat("&moduleId=");
	    params = params.concat(moduleId);
	    loadContent("featuresEdit", $('#formFeaturesList'), $('#feature_tab'), params);
	}
</script>