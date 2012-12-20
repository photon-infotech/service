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
<%@ page import="com.photon.phresco.commons.model.Element" %>
<%@ page import="com.photon.phresco.commons.model.SettingsTemplate" %>

<%
	List<SettingsTemplate> configTemplates = (List<SettingsTemplate>) request.getAttribute(ServiceUIConstants.REQ_CONFIG_TEMPLATES);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
%>

<form id="formConfigTempList" class="customer_list">
	<div class="operation">
		<input type="button" class="btn btn-primary" name="configTemplate_add" id="configtempAdd" 
	         onclick="loadContent('configtempAdd', $('#formConfigTempList'), $('#subcontainer'));" 
		         value="<s:text name='lbl.hdr.configtemp.add'/>"/>
		<input type="button" class="btn" id="del" disabled value="<s:text name='lbl.btn.del'/>"
			onclick="showDeleteConfirmation('<s:text name='del.confirm.configurationTemplate'/>');"/>
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
				<div class="header-background"> </div>
				<div class="fixed-table-container-inner">
					<table cellspacing="0" class="zebra-striped">
						<thead>
								<tr>
									<th class="first">
										<div class="th-inner tablehead">
											<input type="checkbox" value="" class=checkAll id="checkAllAuto" name="checkAllAuto" onclick="checkAllEvent(this,$('.configtempltes'), false);">
										</div>
									</th>
									<th class="second">
										<div class="th-inner tablehead"><s:label key="lbl.name" theme="simple"/></div>
									</th>
									<th class="third">
										<div class="th-inner tablehead"><s:label key="lbl.desc" theme="simple"/></div>
									</th>
									<th class="third">
										<div class="th-inner tablehead"><s:label key="lbl.hdr.comp.aplesto" theme="simple"/></div>
									</th>
								</tr>
						</thead>
						
						<tbody>
						  	<%

								if (CollectionUtils.isNotEmpty(configTemplates)) {
									for ( SettingsTemplate configTemplate : configTemplates) {
										List<Element> applsTos = configTemplate.getAppliesToTechs();
										String appliesTos = "";
										for(Element applsTo : applsTos) {
											appliesTos = appliesTos + applsTo.getName() + ",";
										}
										appliesTos  = appliesTos.substring(0, appliesTos.length() - 1);
							%>
									<tr>
										<td class="checkboxwidth">
								 		<% 
								 		if (configTemplate.isSystem()) { %>
											<input type="checkbox" name="configId" value="<%= configTemplate.getId() %>" 
											             onclick="checkboxEvent();" disabled/>
										<% } else { %>
										<input type="checkbox" class="check configtempltes" name="configId" value="<%= configTemplate.getId() %>" 
										                onclick="checkboxEvent($('#checkAllAuto'),'configtempltes');" />
										<% } %>
										</td>
										<td class="nameConfig">
											<a href="#" onclick="editConfigTemp('<%= configTemplate.getId() %>');" name="edit" id="" >
												<%= StringUtils.isNotEmpty(configTemplate.getName()) ? configTemplate.getName() : "" %>
											</a>
										</td>
										<td class="descConfig">
											<%= StringUtils.isNotEmpty(configTemplate.getDescription()) ? configTemplate.getDescription() : "" %>
										</td>
										<td id="hoverAppliesTo">
											<%= StringUtils.isNotEmpty(appliesTos) ? appliesTos : "" %>
										</td>
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
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".fixed-table-container-inner").scrollbars();  
	}

	$(document).ready(function() {
		toDisableCheckAll($('#checkAllAuto'),'configtempltes');
		hideLoadingIcon();
		
		$("td[id = 'hoverAppliesTo']").text(function(index) {
	        return textTrim($(this));
	    });
		
   	});
	
    function editConfigTemp(id) {
		var params = "configId=";
		params = params.concat(id);
		loadContent("configtempEdit", $("#formConfigTempList"), $('#subcontainer'), params);
	}
    
 	// This method calling from confirm_dialog.jsp
    function continueDeletion() {
    	confirmDialog('none','');
    	loadContent('configtempDelete', $('#formConfigTempList'), $('#subcontainer'));
    }
 	
</script>