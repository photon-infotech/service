<%--

    Service Web Archive

    Copyright (C) 1999-2014 Photon Infotech Inc.

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

<%@ page import="org.apache.commons.collections.CollectionUtils"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.List"%>
<%@ page import="com.photon.phresco.commons.model.VideoInfo"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<%
	List<VideoInfo> videoInfos = (List<VideoInfo>) request.getAttribute(ServiceUIConstants.REQ_VIDEO_INFO);
	
	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "btn-primary";
	if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_VIDEOS)) {
		per_disabledStr = "disabled";
		per_disabledClass = "btn-disabled";
	}
%>

<form id="formVideoList" class="form-horizontal customer_list">
	<div class="operation" id="operation">
		<input type="button" id="videoAdd" <%= per_disabledStr %> class="btn <%= per_disabledClass %>" name="video_add"
			onclick="addVideo();" value="<s:text name='lbl.hdr.video.add'/>"/>
		<input type="button" id="del" class="btn" disabled value="<s:text name='lbl.btn.del'/>" 
			onclick="showDeleteConfirmation('<s:text name='del.confirm.videos'/>');"/>
			
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
	
	<% if (CollectionUtils.isEmpty(videoInfos)) { %>
		<div class="alert alert-block">
		    <s:text name='alert.msg.video.not.available'/>
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
										<input type="checkbox" <%= per_disabledStr %> value="" id="checkAllAuto" name="checkAllAuto" 
											onclick="checkAllEvent(this, $('.videoChk'), false);">
									</div>
								</th>
								<th class="second">
									<div class="th-inner tablehead"><s:label key="lbl.name" theme="simple"/></div>
								</th>
								<th class="second">
									<div class="th-inner tablehead"><s:label key="lbl.desc" theme="simple"/></div>
								</th>
								<%-- <th class="second">
									<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.video.thumbnail" theme="simple"/></div>
								</th> --%>
							</tr>
						</thead>
			
						<tbody>
						<% for (VideoInfo videoInfo : videoInfos) { %>
							<tr>
								<td class="checkboxwidth">
									<% if (videoInfo.isSystem()) { %>
										<input type="checkbox" name="videoId" value="<%= videoInfo.getId() %>"  disabled /> 
								   	<% } else { %>
										<input type="checkbox" <%= per_disabledStr %> class="check videoChk" name="videoId" value="<%= videoInfo.getId() %>" 
									   		onclick="checkboxEvent($('#checkAllAuto'),'videoChk');" />
								    <% } %>	   
								</td>
								<td>
									<a href="#" onclick="editVideo('<%= videoInfo.getId() %>');"><%= videoInfo.getName() %></a>
								</td>
								<td>
									<%= StringUtils.isNotEmpty(videoInfo.getDescription()) ? videoInfo.getDescription() : "" %>
								</td>
								<!-- <td>
									Sample Thumbnail
								</td> -->
								<td class="emailalign"></td>
							</tr>
						<% } %>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<% } %>
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".fixed-table-container-inner").scrollbars();  
	}
	
	$(document).ready(function() {
		toDisableCheckAll($('#checkAllAuto'),'videoChk');
		hideLoadingIcon();
	});
	
	function addVideo() {
		showLoadingIcon();
		loadContent('videoAdd', $('#formVideoList'), $('#subcontainer'));
	}
	
	/** To edit the pilot project **/
    function editVideo(id) {
    	showLoadingIcon();
        var params = "videoId=";
        params = params.concat(id);
        loadContent("videoEdit", '', $('#subcontainer'), params);
    }
	 
 // This method calling from confirm_dialog.jsp
    function continueDeletion() {
    	hidePopup();
    	loadContent('videoDelete', $('#formVideoList'), $('#subcontainer'));
    } 
</script>