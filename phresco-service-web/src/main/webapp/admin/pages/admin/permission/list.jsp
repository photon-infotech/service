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
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.Permission" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<%
    List<Permission> permissions = (List<Permission>) request.getAttribute(ServiceUIConstants.REQ_PERMISSIONS_LIST);
%>

<form id="formPermissionList" class="form-horizontal customer_list">
	<div class="operation" id="operation">
		<input type="button" id="del" class="btn" disabled value="<s:text name='lbl.btn.del'/>"
			onclick="showDeleteConfirmation('<s:text name='del.confirm.permission'/>');"/>
	</div>
	
	<div class="table_div">
		<div class="fixed-table-container">
			<div class="header-background"> </div>
			<div class="fixed-table-container-inner">
				<table cellspacing="0" class="zebra-striped">
					<thead>
						<tr>
							<th class="first">
								<div class="th-inner">
									<input type="checkbox" id="checkAllAuto" name="checkAllAuto" onclick="checkAllEvent(this,$('.check'),false);">
								</div>
							</th>
							<th class="second">
								<div class="th-inner tablehead">
								<s:label key="lbl.name" theme="simple"/></div>
							</th>
							<th class="third">
								<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.prmlst.prms" theme="simple"/></div>
							</th>
							
					</thead>
		
					<tbody>
						<% 
						 // TODO:Arunprasanna
							if (CollectionUtils.isNotEmpty(permissions)) {
								for ( Permission permission : permissions) {
						%>
								<tr>
									<td class="checkboxwidth">
										<input type="checkbox" class="check" name="permissionId" value="<%= permission.getId() %>" onclick="checkboxEvent($('#checkAllAuto'),'check');">
									</td>
									<td class="namelabel-width"><%= permission.getName()  %></td>
<%-- 								<td><%= permission.getPermission()  %></td> --%>
									<td></td>
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
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".fixed-table-container-inner").scrollbars();  
	}

	$(document).ready(function() {
		hideLoadingIcon();
		toDisableCheckAll($('#checkAllAuto'),'check');
	});
	
	function continueDeletion() {
		hidePopup();
    	loadContent('permissionDelete', $('#formPermissionList'), $('#subcontainer'));
    }
</script>