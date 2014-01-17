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
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="changePassword">
	<div id="change_password" >
		<form id="changePasswordForm" class="form-horizontal">
			<div class="control-group">
				<label class="control-label labelbold modallbl-color"> <span
					class="mandatory">*</span>&nbsp;Old Password </label>
				<div class="controls">
					<input type="password" name="oldPassword" id="old_password" class="span3">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label labelbold modallbl-color"> <span
					class="mandatory">*</span>&nbsp;New Password </label>
				<div class="controls">
					<input type="password" name="newPassword" id="new_password" class="span3">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label labelbold modallbl-color"> <span
					class="mandatory">*</span>&nbsp;Renter New Password </label>
				<div class="controls">
					<input type="password" name="retypePassword" id="new_password_reenter" class="span3">
				</div>
			</div>
	</div>

<script type="text/javascript">
	$(document).ready(function() {
	$("#popupPage_modal-body").css('height', '100px');	
	});
	

	function successEvent(pageUrl, data) {
		if (pageUrl == "changePassword") {
			$(".errMsg" ).hide();
			$(".popupOk").hide();			
			$("#popupPage_modal-body").html("<div>"+data.msg+"</div>");
		}
		setTimeout(function(){
			window.location.href = "<s:url action='admin/logout'/>";
		},3000);
	}
	
	function popupOnOk(thisObj) {
	//	$(".errMsg" ).remove();
		var url = $(thisObj).attr("id");
		if($("#new_password").val()== $("#new_password_reenter").val()) {
			loadContent(url, $('#changePasswordForm'), $('#popupPage'),'',true);
		} else {
			alert("ddddd");
			$(".errMsg" ).show();
			$('.errMsg').html("New password and reentered password should be same");
		}
	}
</script>