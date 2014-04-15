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

<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ page
	import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>
<%@ page
	import="com.photon.phresco.service.admin.actions.util.ServiceActionUtil"%>
<%@ page import="com.photon.phresco.commons.model.User"%>

<%
	User user = (User) request.getAttribute(ServiceUIConstants.USERS);
	String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
	String title = ServiceActionUtil.getTitle(ServiceUIConstants.USERS, fromPage);
	String pageUrl = ServiceActionUtil.getPageUrl(ServiceUIConstants.USERS, fromPage);
	String progressTxt = ServiceActionUtil.getProgressTxt(ServiceUIConstants.CUSTOMERS, fromPage);
	String buttonLbl = ServiceActionUtil.getButtonLabel(fromPage);
   
	String id = "";
	String firstName = "";
	String lastName = "";
	String loginid = "";
	String passwd = "";
	String confirmPwd = "";
	String email = "";
	
	if (user != null) {
		id = user.getId();
		firstName = user.getFirstName();
		lastName = user.getLastName();
		loginid = user.getLoginId();
		passwd = user.getPassword();
		confirmPwd = user.getPassword();
		email = user.getEmail();
	}
%>

<form id="formUserAdd" class="form-horizontal customer_list">
	<h4 class="hdr"><%= title %></h4>
	<div class="content_adder">
		<div class="control-group" id="firstNameControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.user.first.name' />
			</label>
			<div class="controls">
				<input id="input01" class="input-xlarge" type="text"
					name="firstname" value="<%= firstName %>" maxlength="30"
					title="30 Characters only"> <span class="help-inline"
					id="firstNameError"></span>
			</div>
		</div>

		<div class="control-group" id="lastNameControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.user.last.name' />
			</label>
			<div class="controls">
				<input id="input01" class="input-xlarge" type="text" name="lastname"
					value="<%= lastName %>" maxlength="30" title="30 Characters only">
				<span class="help-inline" id="lastNameError"></span>
			</div>
		</div>

		<div class="control-group" id="loginidControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.user.loginid' />
			</label>
			<div class="controls">
				<input id="input01" class="input-xlarge" type="text" name="loginid"
					value="<%= loginid %>" maxlength="30" title="30 Characters only">
				<span class="help-inline" id="loginidError"></span>
			</div>
		</div>

		<div class="control-group repoMndatory" id="userpwdControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.user.passwd' />
			</label>
			<div class="controls">
				<input id="userPwd" class="input-xlarge" name="userPwd"
					type="password" value="" maxlength="30" title="30 Characters only">
				<span class="help-inline" id="userPwdErr"></span>
			</div>
		</div>

		<div class="control-group repoMndatory" id="userretypepwdControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text
					name='lbl.user.retype.passwd' />
			</label>
			<div class="controls">
				<input id="userrePwd" class="input-xlarge" name="userrePwd"
					type="password" value="" maxlength="30" title="30 Characters only">
				<span class="help-inline" id="userRePwdErr"></span>
			</div>
		</div>

		<div class="control-group" id="mailControl">
			<label class="control-label labelbold"> <span
				class="mandatory">*</span>&nbsp;<s:text name='lbl.user.email' />
			</label>
			<div class="controls">
				<div class="input-prepend">
					<span class="add-on"> <i class="icon-envelope"></i></span> <input
						id="inputIcon" class="span2" type="text" name="email"
						value="<%= email %>"> <span class="help-inline"
						id="mailError"></span>
				</div>
			</div>
		</div>
	</div>

	<div class="bottom_button">
		<% if(StringUtils.isNotEmpty(fromPage)) { %>
		<input type="button" id="" class="btn btn-primary"
			value="<%= buttonLbl %>"
			onclick="validate('<%= pageUrl %>',$('#formUserAdd'),$('#subcontainer'), '<%= progressTxt %>');" />
		<% } %>
		<input type="button" id="userCancel" class="btn btn-primary"
			value="<s:text name='lbl.btn.cancel'/>" />
	</div>

	<!-- Hidden Fields -->
	<input type="hidden" name="fromPage"
		value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>" /> <input
		type="hidden" name="oldName" value="<%= loginid %>" /> <input
		type="hidden" name="id" value="<%= id %>" />

</form>
<script type="text/javascript">
	$(document).ready(function() {
		hideLoadingIcon();
		hidePasswd();
		
		$("#userCancel").click(function() {
		
			showLoadingIcon();
			loadContent('userList', $('#formUserAdd'), $('#subcontainer'));
		});
	});
	
	function findError(data) {
		if (!isBlank(data.firstNameError)) {
			showError($("#firstNameControl"), $("#firstNameError"), data.firstNameError);
		} else {
			hideError($("#firstNameControl"), $("#firstNameError"));
		}
		
		if (!isBlank(data.lastNameError)) {
			showError($("#lastNameControl"), $("#lastNameError"), data.lastNameError);
		} else {
			hideError($("#lastNameControl"), $("#lastNameError"));
		}
		
		if (!isBlank(data.loginidError)) {
			showError($("#loginidControl"), $("#loginidError"), data.loginidError);
		} else {
			hideError($("#loginidControl"), $("#loginidError"));
		}
		
		if (!isBlank(data.userPwdErr)) {
			showError($("#userpwdControl"), $("#userPwdErr"), data.userPwdErr);
		} else {
			hideError($("#userpwdControl"), $("#userPwdErr"));
		}
		
		if (!isBlank(data.userRePwdErr)) {
			showError($("#userretypepwdControl"), $("#userRePwdErr"), data.userRePwdErr);
		} else {
			hideError($("#userretypepwdControl"), $("#userRePwdErr"));
		}
		
		if (!isBlank(data.mailError)) {
			showError($("#mailControl"), $("#mailError"), data.mailError);
		} else {
			hideError($("#mailControl"), $("#mailError"));
		}
	}
	
	function hidePasswd() {
		if ('<%= fromPage %>
	' === 'edit') {
			$('#userpwdControl').hide();
			$('#userretypepwdControl').hide();
		}
	}
</script>
