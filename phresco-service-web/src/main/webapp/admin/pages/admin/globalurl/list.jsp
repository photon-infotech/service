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

<%@ page import="java.util.List"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.Property"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<% 
 	List<Property> globalUrls = (List<Property>) request.getAttribute(ServiceUIConstants.REQ_GLOBURL_URL);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
%>

<form id="formGlobalUrlList" class="customer_list">
	<div class="operation" id="operation">
		<input type="button" id="globalurlAdd" class="btn btn-primary" name="url_add" 
            onclick="loadContent('globalurlAdd', $('#formGlobalUrlList'), $('#subcontainer'));" 
            value="<s:text name='lbl.hdr.adm.urllst.title'/>"/>
		<input type="button" class="btn" id="del" disabled value="<s:text name='lbl.btn.del'/>"
			onclick="showDeleteConfirmation('<s:text name='del.confirm.globalURL'/>');"/>              
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
	
	<% if (CollectionUtils.isEmpty(globalUrls)) { %>
		<div class="alert alert-block">
			<s:text name='alert.msg.globalUrl.not.available'/>
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
									<input type="checkbox" value="" id="checkAllAuto" name="checkAllAuto" onclick="checkAllEvent(this,$('.check'),false);">
								</div>
							</th>
							<th class="second">
								<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.lurllst.name" theme="simple"/></div>
							</th>
							<th class="third">
								<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.urllst.desc" theme="simple"/></div>
							</th>
							<th class="third">
								<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.urllst.url" theme="simple"/></div>
							</th>
						</tr>
					</thead>
		
					<tbody>
					<% 
						if (CollectionUtils.isNotEmpty(globalUrls)) {
							for(Property globalUrl : globalUrls) {
					%>
							<tr>
								<td class="checkboxwidth">
									<% if (globalUrl.isSystem()) { %>
										<input type="checkbox" disabled>
									<% } else { %>
										<input type="checkbox" class="check" name="globalurlId" value="<%= globalUrl.getId() %>" onclick="checkboxEvent($('#checkAllAuto'),'check');">
									<% } %>
								</td>
								<td class="namelabel-width">
									<a href="#" name="edit" id="" onclick="editGlobalUrl('<%= globalUrl.getId() %>');">
										<%= StringUtils.isNotEmpty(globalUrl.getName()) ? globalUrl.getName() : "" %>
									</a>
								</td>
								<td><%= StringUtils.isNotEmpty(globalUrl.getDescription()) ? globalUrl.getDescription() : "" %></td>
								<td><%= StringUtils.isNotEmpty(globalUrl.getValue()) ? globalUrl.getValue() : "" %></td>
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
	<%
      }
	%>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="customerId" value="<%= customerId %>">
	
</form>	

<script type="text/javascript">
	clickMenu($("a[name='url_add']"),$("#subcontainer"));
	
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".fixed-table-container-inner").scrollbars();  
	}

	$(document).ready(function() {
		hideLoadingIcon();
		toDisableCheckAll($('#checkAllAuto'),'check');
	});
	
	function editGlobalUrl(id) {		
		var params = "globalurlId=";
		params = params.concat(id);
		loadContent("globalurlEdit",'', $('#subcontainer'), params);
	}
	
	// This method calling from confirm_dialog.jsp
	function continueDeletion() {
    	hidePopup();
    	loadContent('globalurlDelete', $('#formGlobalUrlList'), $('#subcontainer'));
    }
</script>