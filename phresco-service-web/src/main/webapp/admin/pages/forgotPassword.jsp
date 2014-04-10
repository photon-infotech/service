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
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="forgotPassword">
	<div id="forgot_password" >
		<form id="forgotPasswordForm" class="form-horizontal">
			<div class="control-group">
				<label class="control-label labelbold modallbl-color"> <span
					class="mandatory">*</span>&nbsp;User name </label>
				<div class="controls">
					<input type="text" name="userId" id="user_id" class="span3">
				</div>
			</div>
			</form>
	</div>
	</div>

<script type="text/javascript">
	$(document).ready(function() {
	$("#popupPage_modal-body").css('height', '50px');	
	});
	
	
	function successEvent(pageUrl, data) {
		 if (pageUrl == "forgotPassword") {
			$(".errMsg" ).remove()
			$(".popupOk").hide();	
			hidePopuploadingIcon();
			$("#popupPage_modal-body").html("<div>"+data.msg+"</div>");
		} 
	}
	
	function popupOnOk(thisObj) {
		$('.popuploadingIcon').show();
		$('.popuploadingIcon').activity({segments: 10, color: '#3c3c3c', speed: 1.8});
		 var url = $(thisObj).attr("id");
		 if($("#user_id").val()!= '') {
			loadContent(url, $('#forgotPasswordForm'), $('#popupPage'),'',true);
		} else {
			$('.errMsg').html("Enter user name");
		}
	}
</script>