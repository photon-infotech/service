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

<%@ page import="com.photon.phresco.model.Technology" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<% 
	List<Technology> technologies = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
%>
		
<form id="formFeaturesList" class="form-horizontal customer_list">
	<div class="operation">
		<div class="featurelist_add">
			<input type="button" id="featuresAdd" class="btn btn-primary" name="features_add" 
				onclick="loadContent('featuresAdd', $('#formFeaturesList'), $('#subcontainer'));" 
				value="<s:text name='lbl.hdr.comp.featrs.add'/>"/>
			<input type="button" class="btn" id="del" disabled value="<s:text name='lbl.hdr.comp.delete'/>" 
				onclick="showDeleteConfirmation('<s:text name='del.confirm.feature'/>');"/>
		</div>
		
		<div class="featurelist_tech">
			<s:text name='lbl.comp.featr.technology'/>
			<select name="technology" id="tech_id">
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
        
		<div class="featurelist_type"><s:text name="lbl.comp.featr.type" />
			<select class="select_type" name="type" id="type">
				<option value="module">Modules</option>
				<option value="js">JS Libraries</option>
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
	
	<div class="featurelist_height"; id="feature_list">
	
	</div>
    
	<!-- Hidden Fields -->
    <input type="hidden" name="customerId" value="<%= customerId %>">
</form>

<script language="JavaScript" type="text/javascript">

	$(document).ready(function() {
		featurelist();
	});
	
	function editFeature(id) {
	    var params = "techId=";
	    params = params.concat(id);
	    loadContent("featuresEdit", $('#formFeaturesList'), $('#subcontainer'), params);
	}
	
	// This method calling from confirm_dialog.jsp
    function continueDeletion() {
    	confirmDialog('none','');
    	loadContent('featuresDelete', $('#formFeaturesList'), $('#subcontainer'));
    }
	
    function featurelist() {
    	loadContent('listFeatures', $('#formFeaturesList'), $('#feature_list'));
    	
		$('#tech_id').change(function() {
			loadContent('listFeatures', $('#formFeaturesList'), $('#feature_list'));
		});
    	
		$('#type').change(function() {
			loadContent('listFeatures', $('#formFeaturesList'), $('#feature_list'));
		});
    }
    
</script>