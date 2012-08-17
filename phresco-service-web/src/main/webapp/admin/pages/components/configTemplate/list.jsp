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
<%@ page import="org.apache.commons.collections.CollectionUtils" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.model.SettingsTemplate" %>
<%@ page import="com.photon.phresco.model.PropertyTemplate" %>

<%
	List<SettingsTemplate> configTemplates = (List<SettingsTemplate>) request.getAttribute(ServiceUIConstants.REQ_CONFIG_TEMPLATES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
%>

<form id="formConfigTempList" class="customer_list">
	<div class="operation">
		<input type="button" class="btn btn-primary" name="configTemplate_add" id="configtempAdd" 
		         onclick="loadContent('configtempAdd', $('#formConfigTempList'), $('#subcontainer'));" 
		         value="<s:text name='lbl.hdr.comp.cnfigtmplte.add'/>"/>
		<input type="button" class="btn" id="del" disabled 
		        onclick="loadContent('configtempDelete', $('#formConfigTempList'), $('#subcontainer'));" 
		        value="<s:text name='lbl.hdr.comp.delete'/>"/>
		<s:if test="hasActionMessages()">
			<div class="alert alert-success alert-message" id="successmsg" >
				<s:actionmessage />
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert alert-error"  id="errormsg">
				<s:actionerror />
			</div>
		</s:if>
	</div>
		
	<% if (CollectionUtils.isEmpty(configTemplates)) { %>
		<div class="alert alert-block">
			<s:text name='alert.msg.configTemp.not.available'/>
		</div>
    <% } else { %>	
		<div class="table_div">
			<div class="fixed-table-container">
				<div class="fixed-table-container-inner">
					<table cellspacing="0" class="zebra-striped">
						<thead>
							<div class="header-background">
								<tr>
									<th class="first">
										<div class="th-inner tablehead">
											<input type="checkbox" value="" id="checkAllAuto" name="checkAllAuto" onclick="checkAllEvent(this);">
										</div>
									</th>
									<th class="second">
										<div class="th-inner tablehead"><s:label key="lbl.hdr.cmp.name" theme="simple"/></div>
									</th>
									<th class="third">
										<div class="th-inner tablehead"><s:label key="lbl.hdr.cmp.desc" theme="simple"/></div>
									</th>
									<th class="third">
										<div class="th-inner tablehead"><s:label key="lbl.hdr.comp.aplesto" theme="simple"/></div>
									</th>
								</tr>
							</div>
						</thead>
						
						<tbody>
						  	<%
								if (CollectionUtils.isNotEmpty(configTemplates)) {
									for ( SettingsTemplate configTemplate : configTemplates) {
							%>
									<tr>
										<td class="checkboxwidth">
											<input type="checkbox" class="check" name="configId" value="<%= configTemplate.getId() %>" onclick="checkboxEvent();">
										</td>
										
										<% 
											List<PropertyTemplate> propTemps = configTemplate.getProperties();
											if (CollectionUtils.isNotEmpty(propTemps)) {
												for (PropertyTemplate propTemp : propTemps) {
							            %>
												<td>
													<a href="#" onclick="editConfigTemp('<%= configTemplate.getId() %>');" name="edit" id="" >
														<%= StringUtils.isNotEmpty(propTemp.getIName()) ? propTemp.getIName() : "" %>
													</a>
												</td>
												<td><%= StringUtils.isNotEmpty(propTemp.getIDesc()) ? propTemp.getIDesc() : "" %></td>
										<%
												}
											}
										%>
										
										<td><%= CollectionUtils.isNotEmpty(configTemplate.getAppliesTo()) ? configTemplate.getAppliesTo() : "" %></td>
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
    function editConfigTemp(id) {
		var params = "configId=";
		params = params.concat(id);
		params = params.concat("&fromPage=");
		params = params.concat("edit");
	    params = params.concat("&customerId=");
	    params = params.concat("<%= customerId %>");
		loadContentParam("configtempEdit", params, $('#subcontainer'));
	}
</script>