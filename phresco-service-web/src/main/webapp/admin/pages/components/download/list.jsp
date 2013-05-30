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
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.apache.commons.collections.CollectionUtils" %>

<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.commons.model.DownloadInfo" %>
<%@ page import="com.photon.phresco.commons.model.ArtifactInfo" %>

<% 
	List<DownloadInfo> downloadInfos = (List<DownloadInfo>)request.getAttribute(ServiceUIConstants.REQ_DOWNLOAD_INFO); 
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
	
	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "btn-primary";
	if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_DOWNLOADS)) {
		per_disabledStr = "disabled";
		per_disabledClass = "btn-disabled";
	}
%>

<form id="formDownloadList" class="customer_list">
	<div class="operation" id="operation">
		<input type="button" id="downloadAdd" class="btn <%= per_disabledClass %>" <%= per_disabledStr %> name="download_add" 
			onclick="addDownload();" value="<s:text name='lbl.hdr.download.add'/>"/>
		<input type="button" id="del" class="btn" disabled value="<s:text name='lbl.btn.del'/>"
			onclick="showDeleteConfirmation('<s:text name='del.confirm.download'/>');"/>
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
	
	<% if (CollectionUtils.isEmpty(downloadInfos)) { %>
		<div class="alert alert-block">
		    <s:text name='alert.msg.download.not.available'/>
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
										<input type="checkbox" value="" class="checkAll" <%= per_disabledStr %> id="checkAllAuto" name="checkAllAuto" onclick="checkAllEvent(this,$('.dwnloadInfo'), false);">
									</div>
								</th>
								<th class="second">
									<div class="th-inner tablehead"><s:label key="lbl.name" theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead"><s:label key="lbl.desc" theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.dwnldlst.appltfrm" theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead" ><s:label key="lbl.hdr.adm.dwnldlst.ver"  theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead">
										<s:label key="lbl.hdr.pilot.version" theme="simple" />
									</div>
								</th>
							</tr>
						</thead>
			
						<tbody>
				       	<% 
							if (CollectionUtils.isNotEmpty(downloadInfos)) {
								for (DownloadInfo download : downloadInfos) {
				       	%>
								<tr>
									<td class="checkboxwidth">
									<% if (download.isSystem()) { %>
										<input type="checkbox"  name="downloadId" value="<%= download.getId() %>" disabled >
									<% } else { %>
									  	<input type="checkbox" <%= per_disabledStr %> class="check dwnloadInfo" name="downloadId" value="<%= download.getId() %>" 
									  		onclick="checkboxEvent($('#checkAllAuto'),'dwnloadInfo');" >
									<% } %>								
									</td>
									<td class="namelabel-width">
										<a href="#" onclick="editDownload('<%= download.getId() %>');" name="edit" id="" ><%= download.getName() %></a>
									</td>
									<td class="namelabel-width">
										<%= StringUtils.isNotEmpty(download.getDescription()) ? download.getDescription() : "" %>
									</td>
									<td>
										<%= CollectionUtils.isNotEmpty(download.getPlatformTypeIds()) ? download.getPlatformTypeIds() : "" %>
									</td>
									<%
										List<ArtifactInfo> versions = download.getArtifactGroup().getVersions();
									%>
										<td><%= StringUtils.isNotEmpty(versions.get(0).getVersion()) ? versions.get(0).getVersion() : "" %></td> 
	                                 	<td class="psblevalue" id="1_psblSinglDiv">
											<a href="#" onclick="versioningDownload('<%= download.getId() %>');" name="edit" id="">
												<img class="addiconAlign imagealign" temp="1" src="images/versioning.png"/>
											</a>
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
		toDisableCheckAll($('#checkAllAuto'),'dwnloadInfo'); 
		hideLoadingIcon();
	});
	
	function addDownload() {
		showLoadingIcon();
		loadContent('downloadAdd', $('#formDownloadList'), $('#subcontainer'));
	}
     
	function versioningDownload(id) {
		showLoadingIcon();
		var params = "downloadId=";
	    params = params.concat(id);
	    params = params.concat("&versioning=")
	    params = params.concat("versioning");
	    loadContent("downloadEdit", $('#formDownloadList'), $('#subcontainer'), params);
	}
	
    /** To edit the download **/
    function editDownload(id) {
    	showLoadingIcon();
		var customerId = $('input[name=customerId]').val();
        var params = "downloadId=";
        params = params.concat(id);
        params = params.concat("&customerId=");
        params = params.concat(customerId);
        loadContent("downloadEdit", '', $('#subcontainer'), params);
    }
    
    // This method calling from confirm_dialog.jsp
    function continueDeletion() {
    	hidePopup();
    	loadContent('downloadDelete', $('#formDownloadList'), $('#subcontainer'), "", "", "", "<s:text name='lbl.prog.txt.download.delete'/>");
    }
</script>