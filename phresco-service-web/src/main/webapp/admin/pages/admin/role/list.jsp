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
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>
<%@ page import="com.photon.phresco.commons.model.Role" %>

<% 
   List<Role> roleLists = (List<Role>)request.getAttribute(ServiceUIConstants.REQ_ROLE_LIST); 
   String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID); 
%>

<form id="formRoleList"  class="form-horizontal customer_list">
	<div class="operation" id="operation">
		<%-- <input type="button" id="roleAdd" class="btn btn-primary" name="role_action" onclick="loadContent('roleAdd', '', $('#subcontainer'));" value="<s:text name='lbl.hdr.adm.rlelst.add'/>"/> --%>
		<input type="button" id="roleAdd" class="btn btn-primary" 
		        name="role_action" onclick="loadContent('roleAdd', $('#formRoleList'), $('#subcontainer'));" value="<s:text name='lbl.hdr.adm.rlelst.add'/>"/>
		
		<input type="button" id="del" class="btn" disabled value="<s:text name='lbl.hdr.adm.delete'/>"  onclick="loadContent('roleDelete', $('#formRoleList'), $('#subcontainer'));"/>
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
									<input type="checkbox" value="" id="checkAllAuto" class="checkAllAuto" name="checkAllAuto" onclick="checkAllEvent(this);">
								</div>
							</th>
							<th class="second">
								<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.rlelst.name" theme="simple"/></div>
							</th>
							<th class="third">
								<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.rlelst.desc" theme="simple"/></div>
							</th>
							<th class="third">
								<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.rlelst.perm" theme="simple"/></div>
							</th>
						</tr>
					</thead>
		
					<tbody>
					    <% if(CollectionUtils.isNotEmpty(roleLists)) { 
					          for(Role roleList : roleLists) {
					    %>
					 
						<tr>
							<td class="checkboxwidth">
								<input type="checkbox" class="check" name="roleId"  value="<%= roleList.getId() %>" onclick="checkboxEvent();">
							</td>
							<td  class="namelabel-width">
								<a href="#" onclick="editRole('<%= roleList.getId() %>');"><%= roleList.getName() %></a>
							</td>
							<td class="namelabel-width"><%= roleList.getDescription()%></td>
							<td>
								<a data-toggle="modal" href="#myModal"><input type="button" class="btn btn-primary addiconAlign" value="Assign Permission"></a>
							</td>
						</tr>
						<%
							}
								}
						%>
					</tbody>
				</table>
				<div id="myModal" class="modal hide fade">
					<div class="modal-header">
					  <a class="close" data-dismiss="modal" >&times;</a>
					  <h3><s:label key="lbl.hdr.adm.rlelst.asgnprm" theme="simple"/></h3>
					</div>
					<div class="modal-body">
						<div class="popupbody">
							<div class="popupusr"><s:label key="lbl.hdr.adm.rolename" cssClass="popuplabel" theme="simple"/></div> 
							<div class="popupusr-name">Phresco Admin</div>
						</div>
						<div class="pouproles">
							<div class="popuprls"><s:label key="lbl.hdr.adm.availperm" cssClass="popuplabel" theme="simple"/></div> 
							<div class="popuprole-select"><s:label key="lbl.hdr.adm.selperm" cssClass="popuplabel" theme="simple"/></div>
						</div>
						<div class="popuplist">
							<div class="popup-list">
								<select names="permAvailable" class="sample" id="permAvailable" multiple="multiple">
									<option value="Developer">View</option>
									<option value="Testing">Update</option>
									<option value="Macys">Create</option>
								</select> 
							</div>
							
							<div class="popup-button">
								<div class="btnalign"><input type="button" class="btn sample" value=">" onclick="moveOptions(this.form.permAvailable, this.form.rolesSelected);"/></div>
								<div class="btnalign"><input type="button" class="btn sample" value=">>" onclick="moveAllOptions(this.form.permAvailable, this.form.rolesSelected);"/></div>
								<div class="btnalign"><input type="button" class="btn sample" value="<" onclick="moveOptions(this.form.rolesSelected, this.form.permAvailable);"/></div>
								<div class="btnalign"><input type="button" class="btn sample" value="<<" onclick="moveAllOptions(this.form.rolesSelected, this.form.permAvailable);"/></div>
							</div>
 
							<div  class="popupselect">
								<select name="rolesSelected" class="sample" id="rolesSelected" multiple="multiple">
								</select> 
							</div>
						</div>
					</div>
					<div class="modal-footer">
					  <a href="#" class="btn btn-primary" data-dismiss="modal"><s:label key="lbl.hdr.comp.cancel" theme="simple"/></a>
					  <a href="#" class="btn btn-primary" data-dismiss="modal" ><s:label key="lbl.hdr.comp.ok" theme="simple"/></a>
					</div>
				</div>
				
			</div>
		</div>
	</div>
	<% } %>
	
	<!-- Hidden Fields -->
    <input type="hidden" name="customerId" value="<%= customerId %>">
</form>

<script type="text/javascript">
	$(document).ready(function() {
		enableScreen();
	});
	
	function editRole(id) {
	    var params = "roleId=";
	    params = params.concat(id);
	    params = params.concat("&fromPage=");
	    params = params.concat("edit");
	    loadContentParam("roleEdit", params, $('#subcontainer'));
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
</script>