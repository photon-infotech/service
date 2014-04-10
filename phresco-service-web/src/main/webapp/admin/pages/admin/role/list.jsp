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
<%@ taglib uri="/struts-tags" prefix="s" %>

<%@ page import="java.util.List" %>

<%@ page import="org.apache.commons.collections.CollectionUtils" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.commons.model.Role" %>

<% 
   	List<Role> roleLists = (List<Role>)request.getAttribute(ServiceUIConstants.REQ_ROLE_LIST); 
   	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID); 
   	String appliesTo = (String) request.getAttribute(ServiceUIConstants.REQ_APPLIES_TO);
   	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "btn-primary";
	if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_ROLES)) {
		per_disabledStr = "disabled";
		per_disabledClass = "btn-disabled";
	}
%>

<form id="formRoleList"  class="form-horizontal customer_list">
	<div class="operation" id="operation">
		<input type="button" id="roleAdd" class="btn <%= per_disabledClass %>" <%= per_disabledStr %> value="<s:text name='lbl.hdr.adm.rlelst.add'/>"
	        name="role_action" onclick="addRole();" />
		
		<input type="button" id="del" class="btn" disabled value="<s:text 
	        name='lbl.btn.del'/>"  onclick="showDeleteConfirmation('<s:text name='del.confirm.roles'/>');"/>
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
	
	<% if (CollectionUtils.isEmpty(roleLists)) { %>
        <div class="alert alert-block">
            <s:text name='alert.msg.role.not.available'/>
        </div>
    <% } else { %>
	<div class="table_div">
		<div class="fixed-table-container">
			<div class="header-background"> </div>
			<div class="fixed-table-container-inner">
				<table cellspacing="0" class="zebra-striped">
					<thead>
						<tr>
							<th class="first">
								<div class="th-inner">
									<input type="checkbox" value="" <%= per_disabledStr %> id="checkAllAuto" class="checkAllAuto" name="checkAllAuto" onclick="checkAllEvent(this, $('.roles'), false);">
								</div>
							</th>
							<th class="second">
								<div class="th-inner tablehead"><s:label key="lbl.name" theme="simple"/></div>
							</th>
							<th class="third">
								<div class="th-inner tablehead"><s:label key="lbl.desc" theme="simple"/></div>
							</th>
							<th class="third">
								<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.rlelst.perm" theme="simple"/></div>
							</th>
						</tr>
					</thead>
		
					<tbody>
					    <%
							if (CollectionUtils.isNotEmpty(roleLists)) {
								for (Role roleList : roleLists) {
					    %>
								<tr>
									<td class="checkboxwidth">
										<% if (roleList.isSystem()) { %>
											<input type="checkbox" name="roleId" value="<%= roleList.getId() %>"  disabled/>
										<% } else { %>
											<input type="checkbox" class="check roles" name="roleId"  <%=per_disabledStr%> value="<%= roleList.getId() %>" onclick="checkboxEvent($('#checkAllAuto'),'roles');">
										<% } %>
									</td>
									<td  class="namelabel-width">
										<% if (roleList.isSystem()) { %>
											<%= StringUtils.isNotEmpty(roleList.getName()) ? roleList.getName() : "" %>
										<% } else { %>
											<a href="#" onclick="editRole('<%= roleList.getId() %>');"><%= StringUtils.isNotEmpty(roleList.getName()) ? roleList.getName() : "" %></a>
										<% } %>
									</td>
									<td class="namelabel-width"><%= StringUtils.isNotEmpty(roleList.getDescription()) ? roleList.getDescription() : "" %></td>
									<td>
										<input type="button" class="btn btn-primary" value="Assign Permission" 
											onclick="openAssignPerPopup('<%= roleList.getId() %>', '<%= roleList.getName() %>', '<%= roleList.isSystem() %>');">
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
		toDisableCheckAll($('#checkAllAuto'),'roles');
		hideLoadingIcon();
	});
	
	function addRole() {
		showLoadingIcon();
		var params = "appliesTo=";
	    params = params.concat('<%= appliesTo %>');
		loadContent('roleAdd', $('#formRoleList'), $('#subcontainer'), params);
	}
	
	function editRole(id) {
		showLoadingIcon();
	    var params = "roleId=";
	    params = params.concat(id);
	    params = params.concat("&appliesTo=");
	    params = params.concat('<%= appliesTo %>');
	    loadContent("roleEdit", $("#formRoleList"), $('#subcontainer'), params);
	}
	
	var NS4 = (navigator.appName == "Netscape" && parseInt(navigator.appVersion) < 5);

	function addOption(theSel, theText, theValue) {
	    var newOpt = new Option(theText, theValue);
	    var selLength = theSel.length;
	    theSel.options[selLength] = newOpt;
	}
	
	function deleteOption(theSel, theIndex) {
	    var selLength = theSel.length;
	    if(selLength>0) {
	      theSel.options[theIndex] = null;
	    }
	}
	
	function moveOptions(theSelFrom, theSelTo){
		var selLength = theSelFrom.length;
	    var selectedText = new Array();
	    var selectedValues = new Array();
	    var selectedCount = 0;
	    var i;
	  
	   	// Find the selected Options in reverse order
	   	// and delete them from the 'from' Select.
	   	for (i=selLength-1; i>=0; i--) {
	    	if (theSelFrom.options[i].selected) {
				selectedText[selectedCount] = theSelFrom.options[i].text;
	       		selectedValues[selectedCount] = theSelFrom.options[i].value;
	       		deleteOption(theSelFrom, i);
	       		selectedCount++;
    		}
	  	}
	  
		// Add the selected text/values in reverse order.
		// This will add the Options to the 'to' Select
		// in the same order as they were in the 'from' Select.
	  	for (i=selectedCount-1; i>=0; i--) {
			addOption(theSelTo, selectedText[i], selectedValues[i]);
	  	}
	  
	  	if (NS4) {
	  		history.go(0);
	  	}
	}
	
	function moveAllOptions(theSelFrom, theSelTo) {
		var selLength = theSelFrom.length;
		var selectedText = new Array();
		var selectedValues = new Array();
		var selectedCount = 0;
		var i;
	      
		// Find the selected Options in reverse order
		// and delete them from the 'from' Select.
		for (i=selLength-1; i>=0; i--) {
			if (theSelFrom.options[i]) {
				selectedText[selectedCount] = theSelFrom.options[i].text;
	          	selectedValues[selectedCount] = theSelFrom.options[i].value;
	          	deleteOption(theSelFrom, i);
	          	selectedCount++;
	        }
		}
	      
		// Add the selected text/values in reverse order.
		// This will add the Options to the 'to' Select
		// in the same order as they were in the 'from' Select.
		for (i=selectedCount-1; i>=0; i--) {
			addOption(theSelTo, selectedText[i], selectedValues[i]);
		}
	      
		if (NS4) {
			history.go(0);
		}
	}
	
	function continueDeletion() {
    	hidePopup();
    	var params = "appliesTo=";
	    params = params.concat('<%= appliesTo %>');
    	loadContent('roleDelete', $('#formRoleList'), $('#subcontainer'), params);
    }
	
	function openAssignPerPopup(roleId, roleName, isSystem) {
		var params = "roleId=";
		params = params.concat(roleId);
		params = params.concat("&name=");
		params = params.concat(roleName);
		params = params.concat("&appliesTo=");
		params = params.concat('<%= appliesTo %>');
		params = params.concat("&editable=");
		params = params.concat(isSystem);
		yesnoPopup('showAssignPermPopup', "Assign Permission", 'assignPermission', 'OK', '', params);
	}
	
	function popupOnOk(obj) {
		var url = $(obj).attr("id");
		if (url === "assignPermission") {
			$('#selectedPermissions option').prop('selected', 'selected');
			$('#popupPage').modal('hide');
			var params = "appliesTo=";
		    params = params.concat('<%= appliesTo %>');
			loadContent('assignPermission', $('#formAssignPermission'), $('#subcontainer'), params);
		}
	}
</script>