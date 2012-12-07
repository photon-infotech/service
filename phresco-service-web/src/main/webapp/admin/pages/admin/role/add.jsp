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

<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.commons.model.Role" %>
<% 
   Role role = (Role)request.getAttribute(ServiceUIConstants.REQ_ROLE_ROLE);  
   String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
   
   //For edit
   String name = "";
   String description = "";
   if (role != null) {
	   if (StringUtils.isNotEmpty(role.getName())) {
		   name = role.getName();
	   }
	   if (StringUtils.isNotEmpty(role.getDescription())) {
		   description = role.getDescription();
	   }
   }
%>

<form id="formRoleAdd" class="form-horizontal customer_list">
	<h4><s:label key="lbl.hdr.adm.rle.add" theme="simple"/></h4>
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.name'/>
			</label>
			<div class="controls">
				<input id="input01" placeholder="<s:text name='place.hldr.role.add.name'/>" class="input-xlarge" type="text" 
					name="name" value="<%= name %>" maxlength="30" title="30 Characters only">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.desc'/>
			</label>
			<div class="controls">
					<textarea id="input01" class="input-xlarge" placeholder='<s:text name="place.hldr.role.add.desc"/>' 
					rows="3" name="description" maxlength="150" title="150 Characters only"><%= description %></textarea>
			</div>
		</div>
	</div>
	
	<div class="bottom_button">
		<% if(StringUtils.isNotEmpty(fromPage)) { %>
			<input type="button" id="roleUpdate" class="btn btn-primary" value="<s:text name='lbl.btn.edit'/>"
		 		onclick="validate('roleUpdate',$('#formRoleAdd'),$('#subcontainer'), '<s:text name='lbl.prog.role.update'/>');" />
		<% } else { %> 
			<input type="button" id="roleSave" class="btn btn-primary" value="<s:text name='lbl.btn.add'/>"
				onclick="validate('roleSave',$('#formRoleAdd'),$('#subcontainer'), '<s:text name='lbl.prog.role.save'/>');" />
		<% } %>
		<input type="button" id="roleCancel" class="btn btn-primary" value="<s:text name='lbl.btn.cancel'/>" 
			onclick="loadContent('roleList', $('#formRoleAdd'), $('#subcontainer'));" />
	</div>
	
	<!-- Hidden Fields -->
    <input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
    <input type="hidden" name="roleId" value="<%= role != null ? role.getId() : "" %>"/>
    <input type="hidden" name="oldName" value="<%= role != null ? role.getName() : "" %>"/>
</form>

<script type="text/javascript">
	$(document).ready(function() {
		hideLoadingIcon();
	});

	function findError(data) {
	    if (data.nameError != undefined) {
	        showError($("#nameControl"), $("#nameError"), data.nameError);
	    } else {
	        hideError($("#nameControl"), $("#nameError"));
	    }
	}
</script>