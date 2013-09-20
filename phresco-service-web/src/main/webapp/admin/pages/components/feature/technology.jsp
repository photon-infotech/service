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

<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.Technology" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<%
	List<Technology> technologies = (List<Technology>) request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
	String type = (String) request.getAttribute(ServiceUIConstants.REQ_FEATURES_TYPE);
	boolean moduleGro = false;
	boolean jslibs = false;
	boolean component = false;
	
	//Get Types
	if (ServiceUIConstants.REQ_FEATURES_TYPE_MODULE.equals(type)) {
		moduleGro = true;
	}
	
	if (ServiceUIConstants.REQ_FEATURES_TYPE_JS.equals(type)) {
		jslibs = true;
	}
	
	if (ServiceUIConstants.REQ_FEATURES_TYPE_COMPONENT.equals(type)) {
		component = true;
	}
	
	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "btn-primary";
	if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_REUSABLE_COMPONENTS)) {
		per_disabledStr = "disabled";
		per_disabledClass = "btn-disabled";
	}
%>

<%
	if (CollectionUtils.isEmpty(technologies)) {
%>
		<div class="alert alert-block">
			<s:text name='alert.msg.archetype.not.available'/>
		</div>
		
		<script type="text/javascript">
			$(document).ready(function() {
				hideLoadingIcon();
			});
		</script>
<%
	} else {
%>
		<form id="formFeaturesList" class="form-horizontal customer_list">
			<div class="operation">
				<div class="featurelist_add">
				<% if (moduleGro || jslibs || component ) { %>	
					<input type="button" class="btn <%= per_disabledClass %>" <%= per_disabledStr %> name="features_add" 
						onclick="addFeatures();" value="<s:text name='lbl.hdr.comp.featrs.mod.add'/>"/>
				<% } %>		
					<input type="button" class="btn" id="del" disabled value="<s:text name='lbl.btn.del'/>" 
						onclick="showDeleteConfirmation('<s:text name='del.confirm.feature'/>');"/>
				</div>
				
				<div class="featurelist_tech">
					<span><s:text name='lbl.comp.featr.technology'/></span>
					<select name="" id="tech_id">
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
					<div class="alert alert-error lert-message alert_messagelist"  id="errormsg">
						<s:actionerror />
					</div>
				</s:if>
				<div class="clear"></div>
			</div>	
			<div class="featurelist_height" id="feature_list">
			
			</div>
		    
			<!-- Hidden Fields -->
			<input type="hidden" id="" name="technology" value=""/>
		    <input type="hidden" name="customerId" value="<%= customerId %>">
		    <input type="hidden" name="type" value="<%= type %>">
		</form>
		
		<script language="JavaScript" type="text/javascript">
			var selectedTechId = "";
			$(document).ready(function() {
				$('#tech_id').ddslick({
		        	onSelected: function(data) {
		        		selectedTechId = data.selectedData.value;
		        		$("tech_id").val(selectedTechId);
		        		$("input[name=technology]").val(selectedTechId);
		        		featurelist();
		        	}
		        });
			});
			
			function addFeatures() {
				showLoadingIcon();
				loadContent('addFeatures', $('#formFeaturesList'), $('#subcontainer'));
			}
			
			// This method calling from confirm_dialog.jsp
		    function continueDeletion() {
		    	hidePopup();
		    	loadContent('featuresDelete', $('#formFeaturesList'), $("#subcontainer"), "", "", "", "<s:text name='lbl.prog.txt.features.delete'/>");
		    }
			
			//To list the features based on the type
		    function featurelist() {
		    	showLoadingIcon();
				loadContent('listFeatures', $('#formFeaturesList'), $('#feature_list'), '', '', true);
		    }
		</script>
<%
	}
%>