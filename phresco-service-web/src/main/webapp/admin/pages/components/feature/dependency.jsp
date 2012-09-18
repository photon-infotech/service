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
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.model.Documentation.DocumentationType"%>

<% 
	List<ModuleGroup> moduleGroups = (List<ModuleGroup>)request.getAttribute(ServiceUIConstants.REQ_FEATURES_MOD_GRP);
%>
		
<div class="modal" style="height: 375px; overflow: hidden;">
	<div class="modal-header">
		<a class="close" id="close">&times;</a>
	  	<h3><s:label key="lbl.hdr.comp.featr.popup.title" theme="simple"/></h3>
	</div>
	
	<div class="modal-body">
		<% if (CollectionUtils.isEmpty(moduleGroups)) { %>
			<div class="alert alert-block">
				<s:text name='alert.msg.feature.not.available'/>
			</div>
    	<% } else { %>
			<div class="content_adder">
				<div class="control-group">
					<div class="external_features_wrapper" style="margin-left: 4px;">
						<div class="theme_accordion_container" id="coremodule_accordion_container" style="width: 98%;">
							<section class="accordion_panel_wid">
								<% for(ModuleGroup moduleGroup : moduleGroups) { %>
								<div class="accordion_panel_inner">
									<section class="lft_menus_container">
										<span class="siteaccordion">
											<span>
											   <input type="checkbox" class="check" name="techId" value="<%= moduleGroup.getId() %>" onclick="checkboxEvent();" >
												<%= moduleGroup.getName()  %>
											</span>
										</span>
										<div class="mfbox siteinnertooltiptxt">
											<div class="scrollpanel" style="overflow: hidden;">
												<section class="scrollpanel_inner">
													<table class="download_tbl">
														<thead>
															<tr>
																<th></th>
																<th class="accordiantable"><s:label key="lbl.hdr.cmp.name"/></th>
																<th class="accordiantable"><s:label key="lbl.hdr.cmp.desc"/></th>
																<th class="accordiantable"><s:label key="lbl.hdr.comp.ver"/></th>
															</tr>
														</thead>
															
														<tbody>
														<% 
															List<Module> versions = moduleGroup.getVersions();
														    if (CollectionUtils.isNotEmpty(versions)) {
																for (Module module : versions) {
																    String name = module.getName();
																    String desc = "";
																    if (module.getDoc(DocumentationType.DESCRIPTION) != null) {
																    	desc = module.getDoc(DocumentationType.DESCRIPTION).getContent();
																    }
																    String version = module.getVersion();
														%>
																<tr>
																	<td class="editFeatures_td1">
																		<input type="radio" name="" value="">
																	</td>
																	<td class="editFeatures_td2">
																		<div class="accordalign"></div>
																		<a href="#" name="ModuleDesc" onclick="editFeature('<%= moduleGroup.getId() %>');" style="color: #000">
																			<%= name %>
																		</a>
																	</td>
																	<td class="editFeatures_td4" style="color: #000"><%= desc %></td>
																	<td class="editFeatures_td4" style="color: #000">
																		<%= StringUtils.isNotEmpty(version) ? version : "" %>
																	</td>
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
									</section>  
								</div>
								<% } %>
							</section>		
						</div>
					</div>
				</div>
			</div>	
		<% } %>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn btn-primary" id="cancel"><s:label key="lbl.hdr.comp.cancel"/></a>
	  	<a href="#" class="btn btn-primary"><s:label key="lbl.hdr.comp.ok"/></a>
	</div>
</div>

<script language="JavaScript" type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$("#coremodule_accordion_container").scrollbars();  
	}
	
	$(document).ready(function() {
		$('#cancel, #close').click(function() {
			showParentPage();
		});
	});
</script>