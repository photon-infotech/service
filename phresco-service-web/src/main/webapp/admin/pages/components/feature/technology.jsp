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
	List<Technology> technologies = (List<Technology>) request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
	String type = (String) request.getAttribute(ServiceUIConstants.REQ_FEATURES_TYPE);
%>
		
<form id="formFeaturesList" class="form-horizontal customer_list">
	<div class="operation">
		<div class="featurelist_add">
		<% if (ServiceUIConstants.REQ_FEATURES_TYPE_MODULE.equals(type)) { %>	
			<input type="button" class="btn btn-primary" name="features_add" 
				onclick="loadContent('moduleAdd', $('#formFeaturesList'), $('#featureContainer'));" 
				value="<s:text name='lbl.hdr.comp.featrs.mod.add'/>"/>
		<% } else if (ServiceUIConstants.REQ_FEATURES_TYPE_JS.equals(type)) { %>
			<input type="button" class="btn btn-primary" name="features_add" 
				onclick="loadContent('jsAdd', $('#formFeaturesList'), $('#featureContainer'));" 
				value="<s:text name='lbl.hdr.comp.featrs.js.add'/>"/>
		<% } %>
			<input type="button" class="btn" id="del" disabled value="<s:text name='lbl.hdr.comp.delete'/>" 
				onclick="showDeleteConfirmation('<s:text name='del.confirm.feature'/>');"/>
		</div>
		
		<div class="featurelist_tech">
			<s:text name='lbl.comp.featr.technology'/>
			<select name="technology" id="tech_id">
				<%
					if (CollectionUtils.isNotEmpty(technologies)) {
						for (Technology technology : technologies) {
				%>
							<option value="<%= technology.getId() %>"><%= technology.getName() %></option>
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
	
	<div class="featurelist_height" id="feature_list">
	
	</div>
    
	<!-- Hidden Fields -->
    <input type="hidden" name="customerId" value="<%= customerId %>">
    <input type="hidden" name="type" value="<%= type %>">
</form>

<script language="JavaScript" type="text/javascript">
	$(document).ready(function() {
		featurelist();
		
		//To get the list based on the selected technology
		$('#tech_id').change(function() {
			featurelist();
		});
	});
	
	// This method calling from confirm_dialog.jsp
    function continueDeletion() {
    	confirmDialog('none', '');
    	loadContent('featuresDelete', $('#formFeaturesList'), $('#feature_tab'));
    }
	
	//To list the features based on the type
    function featurelist() {
		loadContent('<%= type %>List', $('#formFeaturesList'), $('#feature_list'));
    }
</script>