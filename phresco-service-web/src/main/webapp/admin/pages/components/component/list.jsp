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

<%@ page import="com.photon.phresco.commons.model.Technology" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<% 
	List<Technology> technologies = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
%>
		
<form id="formComponentList" class="form-horizontal customer_list">
	<div class="operation">
		<div class="componentlist_add">
			<input type="button" id="componentAdd" class="btn btn-primary" name="component_add" 
				onclick="loadContent('componentAdd', $('#formComponentList'), $('#subcontainer'));" 
				value="<s:text name='lbl.hdr.comp.component.add'/>"/>
			<input type="button" class="btn" id="del" disabled value="<s:text name='lbl.hdr.comp.delete'/>" 
				onclick="showDeleteConfirmation('<s:text name='del.confirm.component'/>');"/>
		</div>
		
		<div class="componentlist_tech">
			<s:text name='lbl.comp.component.technology'/>
			<select name="techId" id="tech_id">
				<%
					if (technologies != null) {
						for (Technology technology : technologies) {
				%>
							<option value="<%=technology.getId() %>"><%=technology.getName() %></option>
				<%
						}
					}
				%>
			</select>
		</div>
		
		<s:if test="hasActionMessages()">
			<div class="alert alert-success alert-message alert_messagelist" id="successmsg">
				<s:actionmessage />
			</div>
		</s:if>
		
		<s:if test="hasActionErrors()">
			<div class="alert alert-error"  id="errormsg">
				<s:actionerror />
			</div>
		</s:if>
	</div>	
	
	<div class="componentlist_height" id="component_list">
	
	</div>
    
	<!-- Hidden Fields -->
    <input type="hidden" name="customerId" value="<%= customerId %>">
</form>

<script language="JavaScript" type="text/javascript">

	$(document).ready(function() {
		componentlist();
	});
	
	function editComponent(id) {
	    var params = "techId=";
	    params = params.concat(id);
	    loadContent("componentEdit", $('#formComponentList'), $('#subcontainer'), params);
	}
	
	// This method calling from confirm_dialog.jsp
    function continueDeletion() {
    	confirmDialog('none','');
    	loadContent('componentDelete', $('#formComponentList'), $('#subcontainer'));
    }
	
    function componentlist() {
    	loadContent('listComponent', $('#formComponentList'), $('#component_list'));
    	
		$('#tech_id').change(function() {
			loadContent('listComponent', $('#formComponentList'), $('#component_list'));
		});
    	
		$('#type').change(function() {
			loadContent('listComponent', $('#formComponentList'), $('#component_list'));
		});
    }
    
</script>