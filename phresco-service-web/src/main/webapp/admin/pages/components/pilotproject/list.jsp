<%--

    Service Web Archive

    Copyright (C) 1999-2014 Photon Infotech Inc.

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
<%@ page import="org.apache.commons.collections.CollectionUtils"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ page import="com.photon.phresco.commons.model.Technology" %>
<%@ page import="com.photon.phresco.commons.model.CoreOption" %>
<%@ page import="com.photon.phresco.commons.model.ApplicationInfo"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>
<%@ page import="com.photon.phresco.commons.model.TechnologyInfo"%>

<%
	List<ApplicationInfo> pilotProjectInfo = (List<ApplicationInfo>) request.getAttribute(ServiceUIConstants.REQ_PILOT_PROJECTS);
    List<Technology> technologies = (List<Technology>) request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
	
	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "btn-primary";
	if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_PILOT_PROJECTS)) {
		per_disabledStr = "disabled";
		per_disabledClass = "btn-disabled";
	}
%>

<form id="formPilotProjList" class="customer_list">
	<div class="operation">
		<input type="button" class="btn <%= per_disabledClass %>" name="pilotproj_add" id="pilotprojAdd" <%= per_disabledStr %> 
			onclick="addPilotProject();" value="<s:text name='lbl.hdr.comp.pltprjt.add'/>" /> 
		<input type="button" class="btn" id="del" disabled value="<s:text name='lbl.btn.del'/>" 
			onclick="showDeleteConfirmation('<s:text name='del.confirm.pilotes'/>');"/>
		<s:if test="hasActionMessages()">
			<div class="alert alert-success alert-message" id="successmsg">
				<s:actionmessage />
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert alert-error" id="errormsg">
				<s:actionerror />
			</div>
		</s:if>
	</div>
     <% if (CollectionUtils.isEmpty(pilotProjectInfo)) { %>
		<div class="alert alert-block">
		    <s:text name='alert.msg.pilotpro.not.available'/>
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
										<input type="checkbox" value="" <%= per_disabledStr %> class=checkAll id="checkAllAuto" name="checkAllAuto" onclick="checkAllEvent(this,$('.pilotprojt'), false);">
									</div>
								</th>
								<th class="second">
									<div class="th-inner tablehead">
										<s:label key="lbl.name" theme="simple" />
									</div>
								</th>
								<th class="third">
									<div class="th-inner tablehead">
										<s:label key="lbl.desc" theme="simple" />
									</div>
								</th>
								<th class="third">
									<div class="th-inner tablehead">
										<s:label key="lbl.comp.featr.technology" theme="simple" />
									</div>
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
							if (CollectionUtils.isNotEmpty(pilotProjectInfo)) {
								for (ApplicationInfo proInfo : pilotProjectInfo) {
						%>
								<tr>
									<td class="checkboxwidth">
			 					        <% if (proInfo.isSystem()) { %>
											<input type="checkbox" name="projectId" value="<%=proInfo.getId() %>"  disabled/>	
										<% } else { %>
											<input type="checkbox" <%= per_disabledStr %> class="check pilotprojt" name="projectId" value="<%=proInfo.getId() %>" onclick="checkboxEvent($('#checkAllAuto'),'pilotprojt');" />
										<% } %>
									</td>
									<td class="namelabel-width">
										<a href="#" onclick="editPilotProject('<%=proInfo.getId() %>');" name="edit" id=""><%= proInfo.getName()%></a>
									</td>
									<td class="desclabel-width"><%= StringUtils.isNotEmpty(proInfo.getDescription()) ? proInfo.getDescription() : ""%></td>
									<%  
										TechnologyInfo techId = proInfo.getTechInfo();
									      if(StringUtils.isNotEmpty(techId.getId())) {
									        for (Technology technology : technologies ){ 
									    	   if(techId.getId().equalsIgnoreCase(technology.getId())) { 
									%> 
									   	<td class="namelabel-width"><%= techId.getName() %></td> 
										<td class="psblevalue" id="1_psblSinglDiv">
										<a href="#" onclick="versioningPilotPro('<%=proInfo.getId() %>');" name="edit" id=""><img class="addiconAlign imagealign" temp="1" 
													src="images/versioning.png
													"/></a>
										
									  </td>
								</tr>
						<%			
							       	}
						 		  }
								}
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
		toDisableCheckAll($('#checkAllAuto'),'pilotprojt'); 
		hideLoadingIcon();
	});
	
	function addPilotProject() {
		showLoadingIcon();
		loadContent('pilotprojAdd', $('#formPilotProjList'), $('#subcontainer'));
	}
	
	function versioningPilotPro(id) {
		showLoadingIcon();
		var customerId = $('input[name = customerId]').val();
		var params = "projectId=";
		params = params.concat(id);
		params = params.concat("&customerId=");
        params = params.concat(customerId);
		params = params.concat("&versioning=")
		params = params.concat("versioning");
		loadContent("pilotprojEdit",'', $('#subcontainer'), params);
	}
	
    /** To edit the pilot project **/
    function editPilotProject(id) {
    	showLoadingIcon();
    	var customerId = $('input[name = customerId]').val();
    	var params = "projectId=";
        params = params.concat(id);
        params = params.concat("&customerId=");
        params = params.concat(customerId);
        loadContent("pilotprojEdit", '', $('#subcontainer'), params);
    }
    
 	// This method calling from confirm_dialog.jsp
    function continueDeletion() {
    	hidePopup();
    	loadContent('pilotprojDelete', $('#formPilotProjList'), $('#subcontainer'), "", "", "", "<s:text name='lbl.prog.txt.pilot.delete'/>");
    }
</script>