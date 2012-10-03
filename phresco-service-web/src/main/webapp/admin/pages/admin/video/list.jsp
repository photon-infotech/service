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

<%@ page import="org.apache.commons.collections.CollectionUtils"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.List"%>
<%@ page import="com.photon.phresco.commons.model.VideoInfo"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<%
	List<VideoInfo> videoInfos = (List<VideoInfo>) request.getAttribute(ServiceUIConstants.REQ_VIDEO_INFO);
%>

<form id="formVideoList" class="form-horizontal customer_list">
	<div class="operation" id="operation">
		<input type="button" id="videoAdd" class="btn btn-primary" name="video_add"
			onclick="loadContent('videoAdd', $('#formVideoList'), $('#subcontainer'));" 
					value="<s:text name='lbl.hdr.adm.vdeoadd'/>"/>
		<input type="button" id="del" class="btn" disabled value="<s:text name='lbl.hdr.adm.delete'/>" 
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
										<input type="checkbox" id="checkAllAuto" name="checkAllAuto" onclick="checkAllEvent(this);">
									</div>
								</th>
								<th class="second">
									<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.vdeo.name" theme="simple"/></div>
								</th>
								<th class="second">
									<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.vdeo.desc" theme="simple"/></div>
								</th>
								<%-- <th class="second">
									<div class="th-inner tablehead"><s:label key="lbl.hdr.adm.vdeo.thumbnail" theme="simple"/></div>
								</th> --%>
							</tr>
						</thead>
			
						<tbody>
						<% for (VideoInfo videoInfo : videoInfos) { %>
							<tr>
								<td class="checkboxwidth">
									<input type="checkbox" class="check" name="videoId" value="<%= videoInfo.getId() %>" 
									   onclick="checkboxEvent();" />
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
		enableScreen();
	});
	
	 /** To edit the pilot project **/
    function editVideo(id) {
        var params = "videoId=";
        params = params.concat(id);
        loadContent("videoEdit", $("#formVideoList"), $('#subcontainer'), params);
    }
	 
 // This method calling from confirm_dialog.jsp
    function continueDeletion() {
    	confirmDialog('none','');
    	loadContent('videoDelete', $('#formVideoList'), $('#subcontainer'));
    } 
</script>