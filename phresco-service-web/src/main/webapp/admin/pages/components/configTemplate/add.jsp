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

<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils" %>
<%@ page import="java.util.List"%>

<%@ page import="com.photon.phresco.model.SettingsTemplate" %>
<%@ page import="com.photon.phresco.model.PropertyTemplate" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.model.Technology"%>

<%
	List<Technology> technologys = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
    String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
    String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
    SettingsTemplate settingsTemplate = (SettingsTemplate) request.getAttribute(ServiceUIConstants.REQ_CONFIG_TEMP);
    
	//For edit
    String name = "";
    String desc = "";
    boolean isSystem = false;
    if (settingsTemplate != null) {
    	if (StringUtils.isNotEmpty(settingsTemplate.getType())) {
    		name = settingsTemplate.getType();	
    	}
    	if (StringUtils.isNotEmpty(settingsTemplate.getDescription())) {
    		desc = settingsTemplate.getDescription();	
    	}
    	isSystem = settingsTemplate.isSystem();
    }
%>

<form id="formConfigTempAdd" name="configForm" class="form-horizontal customer_list">
	<h4 class="hdr">
		<% if (StringUtils.isNotEmpty(fromPage)) { %>
			<s:label key="lbl.hdr.comp.cnfigtmplt.edit" theme="simple"/>
		<% } else {%>
			<s:label key="lbl.hdr.comp.cnfigtmplte.add" theme="simple"/>   
		<% } %>   
	 </h4>	
	
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.name'/>
			</label>
			
			<div class="controls">
				<input id="configname" placeholder="<s:text name='place.hldr.configTemp.add.name'/>" class="input-xlarge" 
					type="text" name="name"  value="<%= name %>">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.desc'/>
			</label>
			<div class="controls">
				<input id="input01" placeholder="<s:text name='place.hldr.configTemp.add.desc'/>" class="input-xlarge" 
				type="text" name="description" value="<%= desc %>">
			</div>
		</div>
		
		<div class="control-group" id="applyControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.appliesto'/>
			</label>
			<div class="controls">
				<select id="multiSelect" multiple="multiple" name="appliesTo">
					<% 
						if (CollectionUtils.isNotEmpty(technologys)) {
							for (Technology technology : technologys) {
								String selectedStr = "";
								if (settingsTemplate != null) {
									List<String> appliesTos = settingsTemplate.getAppliesTo();
									if (appliesTos.contains(technology.getId())) {
										selectedStr = "selected";
									} else {
										selectedStr = "";
									}
								}
					%>
								<option value="<%= technology.getId() %>" <%= selectedStr %>><%= technology.getName() %></option>
					<%  
							}
						}
					%>
				</select>
				<span class="help-inline applyerror" id="applyError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.help'/>
			</label>
			<div class="controls">
				<input id="input01" placeholder="<s:text name='place.hldr.configTemp.add.help.text'/>" class="input-xlarge" type="text">
			</div>
		</div>
		<fieldset class = "configFieldset">
			<legend class = "configLegend"><s:label key="lbl.hdr.comp.proptemplate" cssClass="labelbold" theme="simple"/></legend>
			<div class = "config_table_div">
				<div class="fixed-table-container prpt-header">
					<div class="config-fixed-table-container-inner">
						<table cellspacing="0" class="zebra-striped tablelegend">
							<div class = "header-background">
								<thead class = "fieldset-tableheader">
									<tr>
										<th class="second">
											<div class="th-inner tablehead fixTableHdr""><s:label key="lbl.hdr.comp.cnfigtmplt.key.title" theme="simple"/></div>
										</th>
										<th class="second">
											<div class="th-inner tablehead fixTableHdr"><s:label key="lbl.hdr.comp.cnfigtmplt.type.title" theme="simple"/></div>
										</th>
										<th class="third">
											<div class="th-inner tablehead fixTableHdr"><s:label key="lbl.hdr.comp.cnfigtmplt.psblvalue.title"  theme="simple"/></div>
										</th>
										<th class="third">
											<div class="th-inner tablehead fixTableHdr"><s:label key="lbl.hdr.comp.cnfigtmplt.mndtry.title"  theme="simple"/></div>
										</th>
										<th class="third">
											<div class="th-inner tablehead fixTableHdr"><s:label key="lbl.hdr.comp.cnfigtmplt.mltpl.title" theme="simple"/></div>
										</th>
										<th class="third">
											<div class="th-inner">
												
											</div>
										</th>
									</tr>
								</thead>
							</div>
							
							<div id="input1" class="clonedInput">
								<tbody>
									<tr class="configdynamiadd">
										<td class="textwidth">
											<input type="text" id = "concate" value="" placeholder="" class="span2">
										</td>
										<td class="textwidth">
											<select id="select01" class = "select typewidth">
												<option>- select -</option>
												<option>String</option>
												<option>Integer</option>
												<option>Password</option>
											</select>
										</td>
										<td class="psblevalue">
											<input type="text" placeholder="" class="propTempTxt">
											<a data-toggle="modal" href="#myModal"><img class="addiconAlign imagealign" src="images/add_icon.png"/></a>
										</td>
										<!-- <td class="hlpText">
											<input type="text" placeholder="" class="propTempTxt">
										</td> -->
										<td class="mandatoryfld">
											<input type="checkbox" value="option1" id="optionsCheckbox">
										</td>
										<td class="multiplefld">
											<input type="checkbox" value="option1" id="optionsCheckbox">
										</td>
										<td class="imagewidth">
											<a ><img class="add imagealign" src="images/add_icon.png" onclick="addconfig();"></a>
										</td>
									</tr>
								</tbody>
							</div>
						</table>
						
						<div id="myModal" class="modal hide fade">
							<div class="modal-header">
								<a class="close" data-dismiss="modal" >&times;</a>
								<h3><s:label key="lbl.hdr.comp.cnfigtmplt.popup.title" theme="simple"/></h3>
							</div>
							<div class="modal-body">
								<div class="control-group">
									<s:label key="lbl.hdr.comp.popup.enter" cssClass="control-label labelbold modallbl-color" theme="simple"/>
									<div class="controls">
										<input type="text" name="txtCombo" id="txtCombo" class="span3"/>
										<button type="button" value="" id="addValues" class="btn btn-primary popupadd">
											<s:label key="lbl.hdr.comp.popup.add" theme="simple"/>
										</button>
									</div>
								</div>
								
								<div class="control-group">
									<div class="controls">
										<select name="valuesCombo" id="valuesCombo" multiple="multiple">
										
										</select>
										<div class="popopimage">
											<img src="images/up_arrow.jpeg" title="Move up" id="up" class="imageupdown"><br>
											<img src="images/remove.jpeg" title="Remove" id="remove" class="imageremove"><br>
											<img src="images/down_arrow.png" title="Move down" id="down" class="imageupdown" >
										</div>
									</div>
								</div>
							</div>
							
							<div class="modal-footer">
								<a href="#" class="btn btn-primary" data-dismiss="modal"><s:label key="lbl.hdr.comp.cancel" theme="simple"/></a>
								<a href="#" class="btn btn-primary" data-dismiss="modal" ><s:label key="lbl.hdr.comp.ok" theme="simple"/></a>
							</div>
						</div>
					</div>	
				</div>
			</div>	
		</fieldset>
	</div>
	 
	<div class="bottom_button">
	<%
	String disabledClass = "btn-primary";
	String disabled = "";
	if (isSystem) {
		disabledClass = "btn-disabled";
		disabled = "disabled";
	}
	if (StringUtils.isNotEmpty(fromPage)) { %>
		<input type="button" id="configtempUpdate" class="btn <%= disabledClass %>" <%= disabled %>
				onclick="validate('configtempUpdate', $('#formConfigTempAdd'), $('#subcontainer'), 'Updating Config Template');" 
		        value="<s:text name='lbl.hdr.comp.update'/>"/>
    <% } else { %>		
		<input type="button" id="configtempSave" class="btn btn-primary"
		        onclick="validate('configtempSave', $('#formConfigTempAdd'), $('#subcontainer'), 'Creating Config Template');" 
		        value="<s:text name='lbl.hdr.comp.save'/>"/>
	<% } %> 
		<input type="button" id="configtempCancel" class="btn btn-primary" 
		      onclick="loadContent('configtempList', $('#formConfigTempAdd'), $('#subcontainer'));" 
		      value="<s:text name='lbl.hdr.comp.cancel'/>"/>
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
	<input type="hidden" name="oldName" value="<%= name %>"/>
	<input type="hidden" name="customerId" value="<%= customerId %>">
</form>

<script language="javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
	}

	$(document).ready(function() {
		enableScreen();
		
		$("#addValues").click(function() {
			var val = $("#txtCombo").val();
			$("#valuesCombo").append($("<option></option>").attr("value", val).text(val));
			$("#txtCombo").val("");
		});
	
		/* $('.add').live('click', function() {
			alert("sd");
			var appendRow =  '<tr class="configdynamiadd">' + $('.configdynamiadd').html() + '</tr>';
			$("tr:last").after(appendRow);			
			$("td:last").append('<img class = "del imagealign" src="images/minus_icon.png" onclick="removeTag(this);">');		
		});  */
	
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
	
	function addconfig() {
		var appendRow =  '<tr class="configdynamiadd">' + $('.configdynamiadd').html() + '</tr>';
		$("tr:last").after(appendRow);			
		$("td:last").append('<img class = "del imagealign" src="images/minus_icon.png" onclick="removeTag(this);">');		
	}
	 
	function removeTag(currentTag) {
		$(currentTag).parent().parent().remove();
	}
	
	function findError(data) {
		if (data.nameError != undefined) {
			showError($("#nameControl"), $("#nameError"), data.nameError);
		} else {
			hideError($("#nameControl"), $("#nameError"));
		}
		
		if (data.applyError != undefined) {
			showError($("#applyControl"), $("#applyError"), data.applyError);
		} else {
			hideError($("#applyControl"), $("#applyError"));
		}
	}
</script>