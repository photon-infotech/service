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

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>

<%@ page import="org.apache.commons.collections.MapUtils"%>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ page import="com.photon.phresco.commons.model.User" %> 
<%@ page import="com.photon.phresco.commons.model.Role" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<%
	User user = (User)request.getAttribute(ServiceUIConstants.REQ_USER);
	List<Role> roles = (List<Role>)request.getAttribute(ServiceUIConstants.REQ_ROLE_LIST); 
	Map<String, String> availableRoleMap = (Map<String, String>)request.getAttribute(ServiceUIConstants.REQ_ROLES_MAP); 
	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	
	String userName = "";	
	String userId = "";
	if (user != null) {
		userName = user.getName();
		userId = user.getId();
	}
%>

<form id="userAssignRoleForm">
	<div class="popupbody">
		<div class="popupusr"><s:label key="lbl.login.username" cssClass="popuplabel" theme="simple"/></div> 
		<div class="popupusr-name"><%= userName %></div>
	</div>
	<div class="pouproles">
		<select id="appliesTo" class="input-large" style="margin-left: 3%;">
			<option value="service"><s:text name="lbl.hdr.adm.rles.service"/></option>
			<option value="framework"><s:text name="lbl.hdr.adm.rles.framework"/></option>
		</select>
		<div class="popuprls"><s:label key="lbl.hdr.adm.usrlst.role.popup.lbl.avail.roles" cssClass="popuplabel" theme="simple"/></div> 
		<div class="popuprole-select"><s:label key="lbl.hdr.adm.usrlst.role.popup.lbl.select.roles" cssClass="popuplabel" theme="simple"/></div>
	</div>
	<div class="popuplist">
		<div class="popup-list">
			<select name="" class="sample" id="rolesAvailable" multiple="multiple">
				
			</select> 
		</div>
		
		<div class="popup-button" style="left: 5px;">
			<div class="btnalign"><input type="button" class="btn btn-primary sample" value=">" onclick="moveOptions(this.form.rolesAvailable, this.form.rolesSelected, 'leftToRight');"/></div>
			<div class="btnalign"><input type="button" class="btn btn-primary sample" value=">>" onclick="moveAllOptions(this.form.rolesAvailable, this.form.rolesSelected, 'leftToRight');"/></div>
			<div class="btnalign"><input type="button" class="btn btn-primary sample" value="<" onclick="moveOptions(this.form.rolesSelected, this.form.rolesAvailable, 'rightToLeft');"/></div>
			<div class="btnalign"><input type="button" class="btn btn-primary sample" value="<<" onclick="moveAllOptions(this.form.rolesSelected, this.form.rolesAvailable, 'rightToLeft');"/></div>
		</div>
	
		<div  class="popupselect">
			<select name="" class="sample" id="rolesSelected" multiple="multiple">
				<%
					if (MapUtils.isNotEmpty(availableRoleMap)) { 
						Set<String> keys = availableRoleMap.keySet();
	    				for (String key : keys) {
				%>
							<option value="<%= key %>"><%=  availableRoleMap.get(key) %></option>
				<% 		}
					}
				%>	
			</select> 
		</div>
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="userId" value="<%= userId %>"/>
</form>

<script type="text/javascript">
	getRoles("service");
	
	$(document).ready(function() {
		$('.errMsg').empty();
		
		$("#appliesTo").change(function() {
			getRoles($(this).val());
		});
		
		<% if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_USERS)) { %>
			$("#assignRoles").attr("disabled", true).removeClass("btn-primary").addClass("btn-disabled");
		<% } else { %>
			$("#assignRoles").attr("disabled", false).removeClass("btn-disabled").addClass("btn-primary");
		<% } %>
		
		
	});
	
	function getRoles(appliesTo) {
		var params = "appliesTo=";
		params = params.concat(appliesTo);
		params = params.concat("&userId=");
		params = params.concat('<%= userId %>');
		loadContent("roleListToAssign", "", $("#subcontainer"), params, true);
	}
	
	function moveOptions(theSelFrom, theSelTo, direction) {
		var selLength = theSelFrom.length;
	  	var selectedText = new Array();
	  	var selectedValues = new Array();
	  	var selectedCount = 0;
	  	var i;
	  	
	  	var optionValues = new Array();	  	
	  	if (direction == 'leftToRight') {
	  		$('#rolesSelected > option').each(function() {
	  			optionValues.push($(this).val());
		  	});	  		
	  	} else {
	  		$('#rolesAvailable > option').each(function() {
	  			optionValues.push($(this).val());
		  	});
	  	}
	  		  	
	  	// Find the selected Options in reverse order
	  	// and delete them from the 'from' Select.
	  	for (i=selLength-1; i>=0; i--) {
	  		if (theSelFrom.options[i].selected) {
	  			var exists = $.inArray(theSelFrom.options[i].value, optionValues);
		  		if (exists == -1) {
		  			selectedText[selectedCount] = theSelFrom.options[i].text;
		      		selectedValues[selectedCount] = theSelFrom.options[i].value;
		      		deleteOption(theSelFrom, i);
		      		selectedCount++;
		  		}
	    	}
	  	}
	  
	  	// Add the selected text/values in reverse order.
	  	// This will add the Options to the 'to' Select
	  	// in the same order as they were in the 'from' Select.
	  	for (i = selectedCount-1; i >= 0; i--) {
  			addOption(theSelTo, selectedText[i], selectedValues[i]);
	    	$('.errMsg').empty();
	  	}
	  
	  	if (NS4) {
	  		history.go(0);
	  	}
	}

	function moveAllOptions(theSelFrom, theSelTo, direction) {
		var selLength = theSelFrom.length;
		var selectedText = new Array();
		var selectedValues = new Array();
		var selectedCount = 0;
		var i;
		  
		var optionValues = new Array();	  	
	  	if (direction == 'leftToRight') {
	  		$('#rolesSelected > option').each(function() {
	  			optionValues.push($(this).val());
		  	});	  		
	  	} else {
	  		$('#rolesAvailable > option').each(function() {
	  			optionValues.push($(this).val());
		  	});
	  	}
	  	
		// Find the selected Options in reverse order
		// and delete them from the 'from' Select.
		for (i = selLength-1; i >= 0; i--) {
			if (theSelFrom.options[i]) {
				var exists = $.inArray(theSelFrom.options[i].value, optionValues);
		  		if (exists == -1) {
					selectedText[selectedCount] = theSelFrom.options[i].text;
			      	selectedValues[selectedCount] = theSelFrom.options[i].value;
			      	deleteOption(theSelFrom, i);
			      	selectedCount++;
		  		}   	
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
	
	function popupOnOk(obj) {
		var size = $('#rolesSelected > option').length;
		if (size == 0) {
			$('.errMsg').html('Select atleast one role');
		} else {
			$('.errMsg').empty();
			var action = $(obj).attr("id");
			var params = ""
			if (action == "assignRoles") {
				params = "selectedRoles=";
				var comma = "";
				$('#rolesSelected option').each(function() {
				   params = params.concat(comma);		
				   params = params.concat($(this).val());
				   comma = ",";
				});
				loadContent("assignRoles", $("#userAssignRoleForm"), $("#subcontainer"), params, false);
				$("#popupPage").modal('hide');
			}
		}	
	}
</script>