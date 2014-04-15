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

<%@ page import="java.util.List"%>

<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.Permission"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<%
	String roleName = (String) request.getAttribute(ServiceUIConstants.REQ_ROLE_NAME);
	String roleId = (String) request.getAttribute(ServiceUIConstants.REQ_ROLE_ID);
	List<Permission> permissions = (List<Permission>) request.getAttribute(ServiceUIConstants.REQ_PERMISSIONS_LIST);
	List<String> permissionIds = (List<String>) request.getAttribute(ServiceUIConstants.REQ_SELECTED_PERMISSION_IDS);
	boolean isEditable = (Boolean) request.getAttribute(ServiceUIConstants.REQ_ROLE_EDITABLE);
	List<String> permIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
%>

<form id="formAssignPermission">
	<div class="popupbody">
		<div class="popupusr"><s:label key="lbl.hdr.adm.rolename" cssClass="popuplabel" theme="simple"/></div>
		<div class="popupusr-name"><%= roleName %></div>
	</div>
	<div class="pouproles">
		<div class="popuprls"><s:label key="lbl.hdr.adm.availperm" cssClass="popuplabel" theme="simple"/></div> 
		<div class="popuprole-select"><s:label key="lbl.hdr.adm.selperm" cssClass="popuplabel" theme="simple"/></div>
	</div>
	<div class="popuplist">
		<div class="popup-list">
			<select name="permAvailable" class="sample" id="permAvailable" multiple="multiple">
				<%
					if (CollectionUtils.isNotEmpty(permissionIds) && CollectionUtils.isNotEmpty(permissions)) {
						for (Permission permission : permissions) {
							if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(permission.getId())) {
				%>
								<option value="<%= permission.getId() %>"><%= permission.getName() %></option>
				<%
							}
						}
					} else if (CollectionUtils.isNotEmpty(permissions)) {
						for (Permission permission : permissions) {
				%>
							<option value="<%= permission.getId() %>"><%= permission.getName() %></option>
				<%		
						}
					}
				%>
			</select> 
		</div>
		
		<div class="popup-button">
			<div class="btnalign"><input type="button" class="btn btn-primary sample" value=">" onclick="moveOptions(this.form.permAvailable, this.form.selectedPermissions);"/></div>
			<div class="btnalign"><input type="button" class="btn btn-primary sample" value=">>" onclick="moveAllOptions(this.form.permAvailable, this.form.selectedPermissions);"/></div>
			<div class="btnalign"><input type="button" class="btn btn-primary sample" value="<" onclick="moveOptions(this.form.selectedPermissions, this.form.permAvailable);"/></div>
			<div class="btnalign"><input type="button" class="btn btn-primary sample" value="<<" onclick="moveAllOptions(this.form.selectedPermissions, this.form.permAvailable);"/></div>
		</div>
	
		<div  class="popupselect">
			<select name="selectedPermissions" class="sample" id="selectedPermissions" multiple="multiple">
				<%
					if (CollectionUtils.isNotEmpty(permissionIds) && CollectionUtils.isNotEmpty(permissions)) {
						for (Permission permission : permissions) {
							if (permissionIds.contains(permission.getId())) {
				%>
							<option value="<%= permission.getId() %>"><%= permission.getName() %></option>
				<%
							}
						}
					}
				%>
			</select> 
		</div>
	</div>
	
	<!-- Hidden Filelds -->
	<input type="hidden" name="roleId" value="<%= roleId %>">
</form>

<script type="text/javascript">
	$(document).ready(function () {
		//To restrict the user in updating the systems roles
	 	<% if (isEditable) { %>
			$("#assignPermission").attr("disabled", true).removeClass("btn-primary").addClass("btn-disabled");
		<% } else { %>
			$("#assignPermission").attr("disabled", false).removeClass("btn-disabled").addClass("btn-primary");
		<% } %>
		
		<% if (CollectionUtils.isNotEmpty(permIds) && !permIds.contains(ServiceUIConstants.PER_MANAGE_ROLES)) { %>
			$("#assignPermission").attr("disabled", true).removeClass("btn-primary").addClass("btn-disabled");
		<% } %>
		
	});
</script>