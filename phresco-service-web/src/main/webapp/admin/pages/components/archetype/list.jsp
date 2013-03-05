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

<%@ page import="org.apache.commons.collections.CollectionUtils"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Collections"%>

<%@ page import="com.photon.phresco.commons.model.Technology" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>
<%@ page import="com.photon.phresco.commons.model.ApplicationType"%>

<%
	List<Technology> technologies = (List<Technology>) request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	List<ApplicationType> appTypes = (List<ApplicationType>)request.getAttribute(ServiceUIConstants.REQ_APP_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
%>

<form id="formArchetypeList" class="customer_list" >
	<div class="operation" id="operation">
		<input type="button" id="archetypeAdd" class="btn btn-primary" name="archetype_add" 
			onclick="loadContent('archetypeAdd', $('#formArchetypeList'), $('#subcontainer'));" 
			value="<s:text name='lbl.hdr.archetype.add'/>"/>
		<input type="button" id="del" class="btn" disabled value="<s:text name='lbl.btn.del'/>"
		 	onclick="showDeleteConfirmation('<s:text name='del.confirm.archetype'/>');"/>
		<input type="button" id="techGroup" class="btn btn-primary" name="tech_group" onclick="addTechGroup();" value="<s:text name='lbl.hdr.archetype.techgroup'/>"/> 	
		<s:if test="hasActionMessages()">
			<div class="alert alert-success alert-message"  id="successmsg">
				<s:actionmessage />
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert alert-error"  id="errormsg">
				<s:actionerror />
			</div>
		</s:if>
	</div>
	
	<% if (CollectionUtils.isEmpty(technologies)) { %>
		<div class="alert alert-block">
			<s:text name='alert.msg.archetype.not.available'/>
		</div>
    <% } else { %>
		<div class="table_div">
			<div class="fixed-table-container">
				<div class="header-background"></div>
				<div class="fixed-table-container-inner">
					<table cellspacing="0" class="zebra-nonstriped">
						<thead>
							<tr>
								<th class="first">
									<div class="th-inner">
										<input type="checkbox" value="" class=checkAll id="checkAllFeature" name="checkAllAuto" onclick="checkAllEvent(this, $('.technolgies'), false);">
									</div>
								</th>
								<th class="second">
									<div class="th-inner tablehead"><s:label key="lbl.name"  theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead"><s:label key="lbl.desc"  theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead"><s:label key="lbl.version"  theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead"><s:label key="lbl.hdr.comp.apptype"  theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead">
										<s:label key="lbl.hdr.pilot.version" theme="simple" />
									</div>
								</th>
							</tr>
						</thead>
			
						<tbody>
						<%
							if (CollectionUtils.isNotEmpty(technologies)) {
								for ( Technology technology : technologies) {
						%>
								<tr>
									<td class="checkboxwidth">
									<% if (technology.isSystem()) { %>
										<input type="checkbox" name="techId" value="<%= technology.getId() %>" disabled/>
									<% } else { %>
										<input type="checkbox" class="checkAll technolgies" name="techId" value="<%= technology.getId() %>" onclick="checkboxEvent($('#checkAllFeature'), 'technolgies');" />
									<% } %>
									</td>
									
									<td class="namelabel-width">
										<a href="#" onclick="editTech('<%= technology.getId() %>');">
											<%= StringUtils.isNotEmpty(technology.getName()) ? technology.getName() : "" %>
                                        </a>
									</td>
									<td id="desc" class="desclabel-width">
										<%= StringUtils.isNotEmpty(technology.getDescription()) ? technology.getDescription() : "" %>
									</td>	
									
										<% 
											List<String> techVer = technology.getTechVersions(); 
											if (CollectionUtils.isNotEmpty(techVer)) {
												Collections.sort(techVer);
												Collections.reverse(techVer);
											}
										%>
											
									<td class="namelabel-width">
										<%= CollectionUtils.isNotEmpty(techVer) ? StringUtils.join(techVer, ", ") : ""  %>
									</td>
									
									<td class="namelabel-width">
										<% 
											String appTypeName = "";
											if (CollectionUtils.isNotEmpty(appTypes)) {
												for (ApplicationType appType : appTypes) {
													if (appType.getId().equals(technology.getAppTypeId())) {
														appTypeName = appType.getName();
														break;
													}
												}
											}
										%>
										<%= appTypeName %>
									</td>
									
									<td class="psblevalue" id="1_psblSinglDiv">
									      <a href="#" onclick="versioningTech('<%= technology.getId() %>');" name="edit" id=""><img class="addiconAlign imagealign" temp="1" 
													src="images/versioning.png"/></a>
							   		</td>		
								</tr>	
						<%		
								}
							}
						%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	<% } %>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="customerId" value="<%= customerId %>">
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".fixed-table-container-inner").scrollbars();  
	}
	
	$(document).ready(function() {
		toDisableCheckAll($('#checkAllFeature') , 'technolgies');
		hideLoadingIcon();
	 	 $("td[id = 'desc']").text(function(index) {
	        return textTrim($(this));
	    }); 
	});
 
	function versioningTech(id) {
		var params = "techId=";
		params = params.concat(id);
		params = params.concat("&versioning=")
	    params = params.concat("versioning");
		loadContent("archetypeEdit", $('#formArchetypeList'), $('#subcontainer'), params);
	}
	
    function editTech(id) {   
    	var customerId = $('input[name=customerId]').val();
		var params = "techId=";
		params = params.concat(id);
		params = params.concat("&customerId=");
        params = params.concat(customerId);
		loadContent("archetypeEdit", '', $('#subcontainer'), params);
	}    
   
    // This method calling from confirm_dialog.jsp
    function continueDeletion() {
    	confirmDialog('none','');
    	loadContent('archetypeDelete', $('#formArchetypeList'), $('#subcontainer'));
    }
    
    function addTechGroup() {
		$('#popup_div').show();
		$('#popup_div').empty();
		loadContent('openTechGroupPopup', $('#formArchetypeList'), $('.modal-body'));
		$("#loadingIconDiv").hide();
	}
    
	function popupOnOk(self) {
		if ($(self).attr("id") == "techGroupOk") {
			var customerId = $('select[name=customerId]').val();
			var params = '{"techGroups" : [' + techGroupToAdd.join(',') + '], "customerId" : "' + customerId + '"}';
			loadJsonContent('newTechGroup',params,$("#subcontainer"));
		}
	}
</script>