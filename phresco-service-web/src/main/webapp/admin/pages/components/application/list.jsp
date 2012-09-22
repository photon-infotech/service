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

<%@ page import="com.photon.phresco.commons.model.ApplicationType"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<%
	List<ApplicationType> appTypes = (List<ApplicationType>) request.getAttribute(ServiceUIConstants.REQ_APP_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
%>

<form id="formAppTypeList" class="customer_list">
	<div class="operation" id="operation">
		<input type="button" class="btn btn-primary" name="application_add" id="applicationAdd" 
            onclick="loadContent('applicationAdd', $('#formAppTypeList'), $('#subcontainer'));" value="<s:text name='lbl.hdr.comp.apln.add'/>"/>
		<input type="button" class="btn" id="del" disabled value="<s:text name='lbl.hdr.comp.delete'/>" 
            onclick="showDeleteConfirmation('<s:text name='del.confirm.applicationType'/>');"/>
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
	<% if (CollectionUtils.isEmpty(appTypes)) { %>
		<div class="alert alert-block">
            <s:text name='alert.msg.appType.not.available'/>
        </div>
    <% } else { %>
		<div class="table_div">
			<div class="fixed-table-container">
			  <div class="header-background"></div>
				<div class="fixed-table-container-inner">
					<table cellspacing="0" class="zebra-nonstriped">
						<thead>
							<tr>
								<th class="first nameTd">
									<div class="th-inner">
										<input type="checkbox" value="" id="checkAllAuto" name="checkAllAuto" onclick="checkAllEvent(this,$('.apptype'), false);">
									</div>
								</th>
								<th class="second">
									<div class="th-inner tablehead"><s:label key="lbl.hdr.cmp.name" theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead"><s:label key="lbl.hdr.cmp.desc" theme="simple"/></div>
								</th>
							</tr>
						</thead>
	
						<tbody>
							<%
								if (CollectionUtils.isNotEmpty(appTypes)) {
									for ( ApplicationType appType : appTypes) {
							%>
									<tr>
										<td class="checkboxwidth">
										<% if (appType.isSystem()) { %>
                                               <input type="checkbox" name="apptypeId" value="<%= appType.getId() %>" disabled/>
                                        <% } else { %>
                                               <input type="checkbox" class="check apptype" name="apptypeId" value="<%= appType.getId() %>" 
                                               	onclick="checkboxEvent();"/>
                                        <% } %>
										</td>
										
										<td class="namelabel-width">
                                            <a href="#" onclick="editAppType('<%= appType.getId() %>');"><%= appType.getName() %></a>
										</td>
										<td class="desclabel-width">
                                            <%= StringUtils.isNotEmpty(appType.getDescription()) ? appType.getDescription() : "" %>
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
 		toDisableCheckAll();
		enableScreen();
   	});

	function editAppType(id) {
		var params = "appTypeId=";
		params = params.concat(id);
		loadContent("applicationEdit", $("#formAppTypeList"), $('#subcontainer'), params);
	}
	
	// This method calling from confirm_dialog.jsp
	function continueDeletion() {
    	confirmDialog('none','');
    	loadContent('applicationDelete', $('#formAppTypeList'), $('#subcontainer'));
    }
</script>