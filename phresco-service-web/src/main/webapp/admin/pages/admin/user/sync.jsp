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

<form id="syncForm" class="form-horizontal customer_list">
	<div class="operation" id="operation">
		<input type="button" id="userSync" class="btn btn-primary" style="display: none;" name="users_sync"  value="<s:text name='lbl.hdr.adm.usrlst'/>"/>
		<s:if test="hasActionMessages()">
			<div class="alert alert-success alert-message assignRolesSuccMsg"  id="successmsg">
				<s:actionmessage />
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert alert-error"  id="errormsg">
				<s:actionerror />
			</div>
		</s:if>
	    <div id="userListContainer" class="syncTable">
	
		</div>
	</div>
</form>
<script type="text/javascript">
	$(document).ready(function() {
		loadContent("fetchUsersFromDB", $("#syncForm"), $('#userListContainer'));
	});
</script>