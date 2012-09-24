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
<%@ taglib uri="/struts-tags" prefix="s"%>

<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.commons.model.LogInfo" %>

<% 
	LogInfo log = (LogInfo)request.getAttribute(ServiceUIConstants.REQ_LOG_REPORT); 
%>
	
<!-- Error dialog starts -->	
<div id="errorDialog" class="modal exceptionErrors">
	<div id="versionInfo">
		<div class="modal-header">
			<h3><s:text name="label.phresco.alert"/><span id="version"></span></h3>
			<a id="closeAboutDialog" class="close" href="#" id="close">&times;</a>
		</div>
		
		<div class="modal-body abt_modal-body errorReportContainer">
			<div class="abt_logo errLogo">
				<img src="images/crashReport.png" alt="logo" class="abt_err_img">
			</div>
			<div class="abt_content errorMsgDisplay" id="errorMsgDisplay">
				<%= log.getMessage() %>
			</div>
			<div class="clipboardDiv">
				<img src="images/clipboard-copy.png" alt="clipboard" class="clipboard" 
					id="clipboard" " title="Copy to clipboard">
			</div>
		</div>
		
		<div class="modal-body abt_modal-body errorReportTrace">
			<div id="trackTrace" id="trackTrace"> <%= log.getTrace() %> </div>
		</div>
		
		<div class="modal-footer">
			<div class="errMsg" id="reportMsg"></div>
			<div class="action abt_action">
				<input type="button" class="btn btn-primary" value="<s:text name="label.sent.report"/>" id="submitReport">
				<input type="button" class="btn btn-primary" value="<s:text name="label.cancel"/>" id="submitReportCancel">
			</div>
		</div>
	</div>
</div>
<!-- Error dialog ends -->
	
<script>
	$(document).ready(function() {
		$('.modal-backdrop').show();
		$('#closeAboutDialog').click(function(){
			errorReportEnable();
		});
		
		$('#clipboard').click(function(){
			copyToClipboard($('#trackTrace').text());
		});
		
		<%-- $('#submitReport').click(function(){
			var errorOrigin = $('.errorMessage li span').html();
			var errorMessage = $('#message').html();
			var errorTrace =  $('#trace').html();
			$.ajax({
				url : 'sendReport',
	            data : {
	                'message' : errorMessage,
	                'trace' : errorTrace,
	                'action' : "<%= log.getAction() %>",
	                'userid' : "<%= log.getUserId() %>",
	            },
	            type : "POST",
	            success : function(data) {
	            	$('#reportMsg').html(data.reportStatus);
	            	$("#submitReport").attr("class", "btn disabled");
	                $("#submitReport").attr("disabled", true);
	            }
			});
		}); --%>
		 
		$('#submitReportCancel').click(function() {
			errorReportEnable();
		});
	});

	function errorReportEnable() {
		$("#errorDialog").hide();
		$('.modal-backdrop').hide();
	}
</script>