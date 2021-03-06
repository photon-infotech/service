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
  
<%@ page import="java.util.List"%>

<%@ page import="org.apache.commons.collections.CollectionUtils"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ page import="com.photon.phresco.commons.model.User" %> 
<%@	page import="com.photon.phresco.commons.model.User.AuthType"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<%
   	List<User> userList = (List<User>)request.getAttribute(ServiceUIConstants.REQ_USER_LIST);
    User userInfo = (User) session.getAttribute(ServiceUIConstants.SESSION_USER_INFO);
   	
   	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "";
	String disableChk = "disabled";
	String chkBxClass = "";
%>

<form class="form-horizontal customer_list" id="userListForm">
   <div class="operation">
		<%
			if (permissionIds.contains(ServiceUIConstants.PER_MANAGE_USERS)){
				per_disabledStr = "";
				per_disabledClass = "btn-primary";
			}else{
				per_disabledStr = "disabled";
				per_disabledClass = "btn-disabled";
			}				
		%>
      <input type="button"  class="btn <%= per_disabledClass %>" <%= per_disabledStr %> name="user_add" id="userAdd" onclick="addUser();" 
			value="<s:text name='lbl.hdr.comp.pltprjt.add'/>" />
		
      <input type="button"  id="del"  class="btn del" class="btn btn-primary" disabled value="<s:text name='lbl.btn.del'/>" 
		      onclick="showDeleteConfirmation('<s:text name='del.confirm.users'/>');"/>   	
   </div>
	<%
		if (CollectionUtils.isEmpty(userList)) {
	%>
	<div class="alert alert-block">
		<s:text name='alert.msg.users.not.available' />
	</div>
	<%
		} else {
	%>
	<div class="table_div" >
		<div class="fixed-table-container">
			<div class="header-background"> </div>
			<div class="fixed-table-container-inner">
				<table cellspacing="0" class="zebra-striped">
						<thead>
							<tr>
							    <th class="first">
									<div class="th-inner">
										<input <%= per_disabledStr %> type="checkbox" id="checkAllAuto" name="checkAllAuto" onclick="checkAllEvent(this, $('.userChk'), false);">
									</div>
								</th> 
								<th class="second">
									<div class="th-inner tablehead" style="top: 153px;"><s:label key="lbl.name" theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead" style="top: 153px;"><s:label key="lbl.hdr.adm.usrlst.mail" theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead" style="top: 153px;"><s:label key="lbl.hdr.adm.usrlst.status" theme="simple"/></div>
								</th>
								<th class="third">
									<div class="th-inner tablehead" style="top: 153px;"><s:label key="lbl.hdr.adm.usrlst.asignrole" theme="simple"/></div>
								</th>
							</tr>
						</thead>
			            <%
			            	if (CollectionUtils.isNotEmpty(userList)) {
			            %>	
			            <tbody>	
			            <% 		for (User user : userList) {
			            			if (CollectionUtils.isNotEmpty(permissionIds)) {
				            			if (!permissionIds.contains(ServiceUIConstants.PER_MANAGE_USERS) && !user.getId().equals(userInfo.getId())) {
				            				per_disabledStr = "disabled";
											per_disabledClass = "btn-disabled";
										} else {
											per_disabledStr = "";
											per_disabledClass = "btn-primary";
										}
			            			}
									if (permissionIds.contains(ServiceUIConstants.PER_MANAGE_USERS) && user.getAuthType().equals(AuthType.LOCAL)){
										disableChk = "";
										chkBxClass = "userChk";
			            		  	}else{
									if(user.getAuthType().equals(AuthType.AUTHSERVICE))
										disableChk = "disabled";
										chkBxClass = "";
									}
			            %>
						
						<tr>
						    <td class="checkboxwidth">
						    <input type="checkbox" <%= per_disabledStr %>  class="check <%= chkBxClass %>" name="userId" value="<%= user.getId() %>" 
									   onclick="checkboxEvent($('#checkAllAuto'),'<%= chkBxClass %>');"  <%= disableChk %>/>
						    </td>			   
						
						    <% if (StringUtils.isNotEmpty(user.getDisplayName())) { %>
						    <td>
								<a href="#" onclick="editUser('<%= user.getId() %>');"><%= StringUtils.isNotEmpty(user.getDisplayName()) ? user.getDisplayName() :"" %></a>
							</td>
						    <% } else {%>
							<td>
								<a href="#" onclick="editUser('<%= user.getId() %>');"><%= StringUtils.isNotEmpty(user.getFirstName()+ user.getLastName()) ? user.getFirstName()+user.getLastName() :"" %></a>
							</td>
							<% } %>
							<td class="emailalign"><%= StringUtils.isNotEmpty(user.getEmail()) ? user.getEmail() : "" %></td>
							<td class="userwidth"><%= user.getStatus()!= null ? user.getStatus() : "" %></td>
							<td  class = "tablealign">
								<input type="button" class="btn <%= per_disabledClass %> addiconAlign" <%= per_disabledStr %> value="Roles" onclick="showAssignRolesPopup('<%= user.getId() %>');">
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
	<%	
		}
	%>
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".fixed-table-container-inner").scrollbars();  
	}
	
	hideLoadingIcon();
	toDisableCheckAll($('#checkAllAuto'), '<%= chkBxClass %>');
	
	$(document).ready(function() {
		$("#addValues").click(function() {
			var val = $("#txtCombo").val();
			$("#valuesCombo").append($("<option></option>").attr("value", val).text(val));
			$("#txtCombo").val("");
		});
																					
		$('.add').click(function() {
			var appendRow =  '<tr class="configdynamiadd">' + $('.configdynamiadd').html() + '</tr>';
			appendRow = appendRow.replace('class="add" src="images/add_icon.png"', 'class = "del" src="images/minus_icon.png" onclick="removeTag(this);"');
			$("tr:last").after(appendRow);			
		}); 
		
		$('#remove').click(function() {
			$('#valuesCombo option:selected').each( function() {
				$(this).remove();
			});
		});
		
		//To move up the values
		$('#up').bind('click', function() {
			$('#valuesCombo option:selected').each( function() {
				var newPos = $('#valuesCombo  option').index(this) - 1;
				if (newPos > -1) {
					$('#valuesCombo  option').eq(newPos).before("<option value='"+$(this).val()+"' selected='selected'>"+$(this).text()+"</option>");
					$(this).remove();
				}
			});
		});
		
		//To move down the values
		$('#down').bind('click', function() {
			var countOptions = $('#valuesCombo option').size();
			$('#valuesCombo option:selected').each( function() {
				var newPos = $('#valuesCombo  option').index(this) + 1;
				if (newPos < countOptions) {
					$('#valuesCombo  option').eq(newPos).after("<option value='"+$(this).val()+"' selected='selected'>"+$(this).text()+"</option>");
					$(this).remove();
				}
			});
		});
	});
	
	function removeTag(currentTag) {
		$(currentTag).parent().parent().parent().remove();
	}
	
	var NS4 = (navigator.appName == "Netscape" && parseInt(navigator.appVersion) < 5);

	function addOption(theSel, theText, theValue) {
		var newOpt = new Option(theText, theValue);
		var selLength = theSel.length;
		theSel.options[selLength] = newOpt;
	}

	function deleteOption(theSel, theIndex) { 
		var selLength = theSel.length;
		if (selLength>0) {
	    	theSel.options[theIndex] = null;
	  	}
	}

	function showAssignRolesPopup(userId) {
		var params = "userId=";
		params = params.concat(userId);
		yesnoPopup("showAssignRoles", '<s:text name="lbl.hdr.adm.usrlst.role.popup.title"/>', 'assignRoles' , '<s:text name="lbl.btn.ok"/>', '', params);
	}
	
	function addUser() {
		 showLoadingIcon();
		 loadContent('userAdd', $('#userListForm'), $('#subcontainer'));
		 hideLoadingIcon();
	}
	
	function editUser(id) {
		showLoadingIcon();
		var params = "id=";
		params = params.concat(id);
		params = params.concat("&fromPage=");
		params = params.concat("edit");
		loadContent("userEdit", '', $('#subcontainer'), params);
	}
	
	function successEvent(pageUrl, data) {
		if (pageUrl === "roleListToAssign") {
// 			$("#rolesSelected").empty()
			$("#rolesAvailable").empty()
			if (data != undefined && !isBlank(data)) {
				/* if (data.availableRoles != undefined && !isBlank(data.availableRoles)) {
					for (i in data.availableRoles) {
						var role = data.availableRoles[i];
						var id = role.id;
						var name = role.name;
						if ($.inArray(id.toString(), selectedRoleIds) == -1) {
							var option = "<option value='"+ id +"'>"+ name +"</option>";
							$("#rolesSelected").append(option);
						}
					}
				} */
				
				var selectedRoleIds = [];
				$("#rolesSelected option").each(function() {
					selectedRoleIds.push($(this).val());
				});
				
				for (i in data.roles) {
					var role = data.roles[i];
					var id = role.id;
					var name = role.name;
					if ($.inArray(id.toString(), selectedRoleIds) == -1) {
						var option = "<option value='"+ id +"'>"+ name +"</option>";
						$("#rolesAvailable").append(option);
					}
				}
			}
		}
	}
	
	function continueDeletion() {
    	hidePopup();
    	loadContent('userDelete', $('#userListForm'), $('#subcontainer'), "", "", "", "<s:text name='lbl.prog.txt.user.delete'/>");
    }
	
</script>