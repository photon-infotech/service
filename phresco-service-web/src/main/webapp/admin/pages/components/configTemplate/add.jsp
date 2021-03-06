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
<%@ page import="java.util.ArrayList"%>

<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils" %>

<%@ page import="com.photon.phresco.util.ServiceConstants"%>
<%@ page import="com.photon.phresco.commons.model.SettingsTemplate" %>
<%@ page import="com.photon.phresco.commons.model.PropertyTemplate" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.commons.model.Technology" %>
<%@ page import="com.photon.phresco.commons.model.Element" %>
<%@ page import="com.photon.phresco.service.admin.actions.util.ServiceActionUtil"%>

<%
	List<Technology> technologies = (List<Technology>) request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
	String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
	SettingsTemplate settingsTemplate = (SettingsTemplate) request.getAttribute(ServiceUIConstants.REQ_CONFIG_TEMP);
	String title = ServiceActionUtil.getTitle(ServiceUIConstants.CONFIG_TEMPLATES, fromPage);
	String buttonLbl = ServiceActionUtil.getButtonLabel(fromPage);
	String pageUrl = ServiceActionUtil.getPageUrl(ServiceUIConstants.CONFIG_TEMPLATES, fromPage);
	String progressTxt = ServiceActionUtil.getProgressTxt(ServiceUIConstants.CONFIG_TEMPLATES, fromPage);
	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "btn-primary";
	if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_CONFIG_TEMPLATES)) {
		per_disabledStr = "disabled";
		per_disabledClass = "btn-disabled";
	}
	
	//For edit
	String id = "";
	String name = "";
	String dispName = "";
	String desc = "";
	boolean isSystem = false;
	boolean isCustProp = false;
	boolean isFavourite = false;
	boolean isEnvSpecific = false;
	List<PropertyTemplate> properties = null;
	if (settingsTemplate != null) {
		id = settingsTemplate.getId();
		name = settingsTemplate.getName();
		dispName = settingsTemplate.getDisplayName();
		desc = settingsTemplate.getDescription();
		isSystem = settingsTemplate.isSystem();
		isCustProp = settingsTemplate.isCustomProp();
		isFavourite = settingsTemplate.isFavourite();
		isEnvSpecific = settingsTemplate.isEnvSpecific();
		properties = settingsTemplate.getProperties();
		for (PropertyTemplate prop : properties) {
			List<String> values = prop.getPossibleValues();
		}
	}

	String disabledStr = "";
	if (ServiceUIConstants.EDIT.equals(fromPage)) {
		if (ServiceConstants.DEFAULT_CUSTOMER_NAME.equalsIgnoreCase(customerId)) {
			disabledStr = "";
		} else if (isSystem){
			disabledStr = "disabled";
		}
	}
%>
<form id="formConfigTempAdd" name="configForm" class="form-horizontal customer_list">
	<h4 class="hdr">
		<%= title %>   
	</h4>	
	
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.name'/>
			</label>

			<div class="controls">
				<input id="configname" placeholder="<s:text name='place.hldr.configTemp.add.name'/>" class="input-xlarge" 
					type="text" name="name"  value="<%=name%>" maxlength="30" title="30 Characters only" <%= disabledStr %>>
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>

		<div class="control-group" id="dispNameControl">
			<label class="control-label labelbold">
				<s:text name='lbl.disp.name'/>
			</label>

			<div class="controls">
				<input id="dispName" placeholder="<s:text name='place.hldr.configTemp.add.disp.name'/>" class="input-xlarge" 
					type="text" name="dispName"  value="<%= StringUtils.isEmpty(dispName) ? "" : dispName %>" maxlength="30" title="30 Characters only" <%= disabledStr %>>
				<span class="help-inline" id="dispNameError"></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.desc'/>
			</label>
			<div class="controls">
				<textarea placeholder="<s:text name='place.hldr.configTemp.add.desc'/>" class="input-xlarge" id="description"
					name="description" maxlength="150" title="150 Characters only" <%= disabledStr %>><%=desc%></textarea>
			</div>
		</div>

		<div class="control-group" id="applyControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.appliesto'/>
			</label>
			<div class="controls">
					<div class="typeFields" id="typefield">
					<div class="multilist-scroller multiselct" id="appliesToDiv">
					<ul>
						<li>
							<input type="checkbox" value="" id="checkAllAuto" name="" onclick="checkAllEvent(this,$('.applsChk'), false);" style="margin: 3px 8px 6px 0;">All
						</li>
						<%
						    if (CollectionUtils.isNotEmpty(technologies)) {
								for (Technology technology : technologies) {
									String checkedStr = "";
									if (settingsTemplate != null) {
										List<Element> appliesTos = settingsTemplate.getAppliesToTechs();
										if (CollectionUtils.isNotEmpty(appliesTos)) {
											List<String> techIds = new ArrayList<String>();
											for (Element appliesTo : appliesTos) {
												techIds.add(appliesTo.getId());
											}
											if (techIds.contains(technology.getId())) {
												checkedStr = "checked";
											} else {
												checkedStr = "";
											}
										}
									}

						%>		
								<li>
									<input type="checkbox" id="appliestoCheckbox" name="appliesTo" onclick= "checkboxEvent($('#checkAllAuto'),'applsChk')" value="<%= technology.getId() %>"  <%= checkedStr %>
										class="check applsChk"><%= technology.getName() %>
								</li>
						<%  
								}
							}
						%>
						</ul>
					</div>
				</div>
          		 <span class="help-inline applyerror" id="applyError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name="lbl.hdr.comp.cnfigtmplt.env.specific" />
			</label>
			<div class="controls">
				<%
					String checkedStr = "";
					if (isEnvSpecific) {
					    checkedStr = "checked";
					} else {
						checkedStr = "";	
					}
				%>
				<input type="checkbox" name="envSpecific" id="envSpecific" value="false" <%= checkedStr %> <%= disabledStr %>>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name="lbl.hdr.comp.cnfigtmplt.favourite" />
			</label>
			<div class="controls">
				<%
					String disabled = "";
					if (isFavourite) {
					    checkedStr = "checked";
					} else {
						if (!ServiceUIConstants.EDIT.equals(fromPage)) {
							checkedStr = "checked";
							disabled = "disabled";
						} else {
							checkedStr = "";
						}
					}
					
					if (!isEnvSpecific) {
						disabled = "disabled";
					}
				%>
				<input type="checkbox" name="favourite" id="favourite" value="false" <%= checkedStr %> <%= disabledStr %> <%= disabled %>>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name="lbl.hdr.comp.system.properties" />
			</label>
			<div class="controls">
				<%
					if (isCustProp) {
					    checkedStr = "checked";
					} else {
						checkedStr = "";	
					}
				%> 
				<input type="checkbox" name="defaultCustProp" id="defaultCustProp" value="false" <%= checkedStr %> <%= disabledStr %>>
			</div>
		</div>
		
		<div class="control-group" id="propTempControl">
			<label class="control-label labelbold"><span class="mandatory">*</span>&nbsp;
				<s:text name='lbl.hdr.comp.proptemplate'/>
			</label>
			
			<div class="controls">
				<input type="button" onClick="openConfigTempPopup();" value = "Add Templates" class="btn btn-primary" />
				<span class="help-inline" id="propTempError"></span>
			</div>
		</div>

		<div class="config_table_div">
			<div class="fixed-table-container prpt-header">
				<div class="config-fixed-table-container-inner">
					<table id='dataTable' cellspacing="0" class="zebra-striped tablelegend">
						<div class="header-background">
							<thead class="fieldset-tableheader">
								<tr>
									<th class="second" style="width: 20%">
										<div class="th-inner tablehead fixTableHdr">
											<span class="mandatory">*</span>&nbsp;
											<s:label key="lbl.hdr.comp.cnfigtmplt.key.title"
												cssClass="keyMandtory" theme="simple" />
										</div>
									</th>
									<th class="second" style="width: 20%">
										<div class="th-inner tablehead fixTableHdr">
											<span class="mandatory">*</span>&nbsp;
											<s:label key="lbl.hdr.comp.cnfigtmplt.name.title"
												cssClass="keyMandtory" theme="simple" />
										</div>
									</th>
									<th class="second" style="width: 20%">
										<div class="th-inner tablehead fixTableHdr">
											<s:label key="lbl.hdr.comp.cnfigtmplt.type.title"
												theme="simple" />
										</div>
									</th>
									<th class="third" style="width: 20%">
										<div class="th-inner tablehead fixTableHdr">
											<s:label key="lbl.hdr.comp.cnfigtmplt.psblvalue.title"
												theme="simple" />
										</div>
									</th>
									<th class="third" style="width: 10%">
										<div class="th-inner tablehead fixTableHdr">
											<s:label key="lbl.hdr.comp.cnfigtmplt.mndtry.title"
												theme="simple" />
										</div>
									</th>
									<th class="third" style="width: 10%">
										<div class="th-inner tablehead fixTableHdr">
											<s:label key="lbl.btn.del" theme="simple" />
										</div>
									</th>
								</tr>
							</thead>
						</div>
						<tbody id="propTempTbody">

						</tbody>
					</table>
				</div>
			</div>
		</div>

	</div>	
	 
	<div class="bottom_config">
		<input type="button" id="" class="btn <%= per_disabledClass %>" <%= per_disabledStr %> 
			onclick="validatePropTempKey('<%= pageUrl %>', '<%= progressTxt %>');" value='<%= buttonLbl %>'/>
		<input type="button" id="configtempCancel" class="btn btn-primary" onclick="getConfigTemplates()" 
			value="<s:text name='lbl.btn.cancel'/>"/>
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
	<input type="hidden" name="oldName" value="<%= name %>"/>
	<input type="hidden" name="customerId" value="<%= customerId %>">
	<input type="hidden" name="configId" value="<%= id %>">
	<input type="hidden" id="csvAppliesTo" value="">
</form>

<script language="javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
		$(".multilist-scroller").scrollbars();
	}
	var customer = $('input[name=customerId]').val();
	var fromPage = '<%= fromPage %>';
	var system = <%= isSystem %>;
	$(document).ready(function() {
		hideLoadingIcon();
		checkboxEvent($('#checkAllAuto'), 'applsChk');
		tableHide();
		
		<%
			if (settingsTemplate != null && properties != null) {
				for (PropertyTemplate prop : properties) {
					String propTempCustId = prop.getCustomerIds().get(0);
					if((propTempCustId.equals(customerId)||propTempCustId.equals("photon"))){
 		%>	
		 			var values = [];
		 			var appliesTo = [];
		 			var customerIds=[];
		 			var json = {};
		 			customerIds.push('<%= propTempCustId %>');
		 			json.id = '<%= prop.getId() %>';
					json.name = '<%= prop.getName() %>';
					json.key = '<%= prop.getKey() %>';
					json.type = '<%= prop.getType() %>';
					json.system = <%= prop.isSystem() %>;
					json.helpText =  '<%= StringUtils.isNotEmpty(prop.getHelpText()) ? prop.getHelpText() : "" %>';
					json.defaultValue =  '<%= StringUtils.isNotEmpty(prop.getDefaultValue()) ? prop.getDefaultValue() : "" %>';
			
		<% 
					List<String> possibleValues = null;
					if (prop.getPossibleValues() != null) {
						List<String> values = prop.getPossibleValues();
						for (String psblValue : values) { 
		%>
							values.push('<%= psblValue %>');
		<%	
						}
					}
					
					List<String> appliesTo = prop.getAppliesTo();
					if (CollectionUtils.isNotEmpty(appliesTo)) {
						for (String applyTo : appliesTo) {
		%>
							appliesTo.push('<%= applyTo %>');				
		<%
						}
					}
		%>			
					json.customerIds= customerIds;
					json.possibleValues = values;
					json.appliesTo = appliesTo;
					json.multiple =  <%= prop.isMultiple() %>;
					json.required =  <%= prop.isRequired() %>;
					constructDiv(json);
		<%		
					}
				}
		 	}
				
		%>
		trimValues();
		
		$('#envSpecific').click(function() {
			if ($(this).is(":checked")) {
				$("#favourite").attr("disabled", false).attr("checked", false);				
			} else {
				$("#favourite").attr("disabled", true).attr("checked", true);
			}
			
		});
	});
	
	function getConfigTemplates() {
		showLoadingIcon();
		loadContent('configtempList', $('#formCustomerId'), $('#subcontainer'));
	}
	
	//Add propTemp
	function openConfigTempPopup() {
		var appliesTo = [];
		$("input[name=appliesTo]:checked").each(function() {
			var techId = $(this).val();
			var techName = $(this).parent().text().trim();
			var selected = techId + "#" + techName;
			appliesTo.push(selected);
		});
		var csvAppliesTo = appliesTo.join(",");
		$("#csvAppliesTo").val(csvAppliesTo);
		if (appliesTo.length <= 0) {
			showError($("#applyControl"), $("#applyError"), 'Select atleast one technology');
		} else {
			var params = 'fromPage=';
			params = params.concat("Add");
			yesnoPopup('showPropTempPopup', "Add Property Templates", 'saveTemplate', 'OK', '', params);
		}
	}
	
	//edit propTemp
	function editPopup(key, isSystem) {
		var appliesTo = [];
		$("input[name=appliesTo]:checked").each(function() {
			var techId = $(this).val();
			var techName = $(this).parent().text().trim();
			var selected = techId + "#" + techName;
			appliesTo.push(selected);
		});
		var csvAppliesTo = appliesTo.join(",");
		$("#csvAppliesTo").val(csvAppliesTo);
		
		if (appliesTo.length <= 0) {
			showError($("#applyControl"), $("#applyError"), 'Select atleast one technology');
		} else {
			var params = "propTempKey=";
			params = params.concat(key);
			params = params.concat("&fromPage=");
			params = params.concat("Edit");
			if (fromPage == 'add' || (fromPage == 'edit'&& !isSystem)) {
				yesnoPopup('showPropTempPopup', "Edit Property Templates", 'saveTemplate', 'Update','', params);
			} else if (fromPage == 'edit' && isSystem && customer == 'photon') {
				yesnoPopup('showPropTempPopup', "Edit Property Templates", 'saveTemplate', 'Update','', params);
			}
		}
	}
	
	function trimValues() {
		$("td[id = 'posbl-td']").text(function(index) {
	        return textTrim($(this));
	    });
	}

	function validatePropTempKey(pageUrl, progressText) {
		
		var nameStat = true;
		if (($('#configname').val()) == "") {
			showError($("#nameControl"), $("#nameError"), 'Name is missing');
			nameStat = false;
		} else {
			hideError($("#nameControl"), $("#nameError"));
			nameStat = true;
		}
		
		var techStat = true;
		if ($('input[name=appliesTo]:checked').length < 1) {
			showError($("#applyControl"), $("#applyError"), 'Select atleast one technology');
			techStat = false;
		} else {
			hideError($("#applyControl"), $("#applyError"));
			techStat = true;
			var techs = [];
			$('input[name=appliesTo]:checked').each( function() {
				techs.push($(this).val());
			});
		}
		
		var propTemps = [];
		$('input[name=propTemps]').each( function() {
			propTemps.push(jQuery.parseJSON($(this).val()));
		});
		var jsonObj = {};
		jsonObj.dispName = $('#dispName').val();
		jsonObj.name = $('#configname').val();
		jsonObj.propTemps = propTemps;
		jsonObj.appliesTo = techs;
		jsonObj.description = $('#description').val();
		jsonObj.envSpecific = $('#envSpecific').prop("checked");
		jsonObj.favourite = $('#favourite').prop("checked");
		jsonObj.defaultCustProp = $('#defaultCustProp').prop("checked");
		jsonObj.customerId = "<%= customerId %>";
		jsonObj.configId = "<%= id %>";
		jsonObj.oldName = "<%= name %>";
		jsonObj.fromPage = "<%= fromPage %>";
		jsonObj.system = <%= isSystem %>;
		var jsonParam = JSON.stringify(jsonObj);
		
		var tableStat = tableHide();
		if (!tableStat) {
			showError($("#propTempControl"), $("#propTempError"), 'Add Property Template(s)');
		} else {
			hideError($("#propTempControl"), $("#propTempError"));
		}
		
		if (tableStat && nameStat && techStat) {
			validateJson(pageUrl, $('#formConfigTempAdd'), $('#subcontainer'), jsonParam, progressText, $('#appliesToDiv :input'));
		}
	}
	
	// To check for the special character in configname
	$('#configname').bind('input propertychange', function(e) {
		var configname = $(this).val();
		configname = checkForSplChrExceptDot(configname);
	    configname = stripSpace(configname);
		$(this).val(configname);
	});
	
	//construct Divs dynamically
	function constructDiv(jsonObj) {
		var table = document.getElementById('dataTable');
	  	var body = table.getElementsByTagName('tbody')[0];
	  	var tr = document.createElement('tr');
	  	tr.id = "tr-"+jsonObj.key.replace(/\./g, '-');
	  	var key = document.createElement('td');
	 	key.innerHTML = "<a href='#' onclick='editPopup("+JSON.stringify(jsonObj.key)+", "+jsonObj.system+");'>"+jsonObj.key+"</a>";
	  	tr.appendChild (key);
	  	var name = document.createElement('td');
	  	name.innerHTML = jsonObj.name;  
	  	tr.appendChild (name);
	  
	  	var type = document.createElement('td');
	  	type.innerHTML = jsonObj.type;
	  	tr.appendChild (type);
	  
	  	var posblValues = document.createElement('td');
	  	posblValues.innerHTML = jsonObj.possibleValues;
	  	posblValues.id = "posbl-td"; 
	  	tr.appendChild (posblValues);
	  
	  	var status = jsonObj.required;
	  	var img = "";
	  	status ? img = "<img src='images/success.png' title=''>" : img = "<img src='images/smalldelete.png' title=''>";
	  	var mandatory = document.createElement('td');
	  	mandatory.innerHTML = img;
	  	tr.appendChild (mandatory);
	  	
	  	var customerId = $('input[name=customerId]').val();
	  	if (fromPage == 'add' || (fromPage == 'edit'&& !system)) {
	  		var icon = document.createElement('td');
		  	icon.innerHTML = "<img class = 'del imagealign' id='deleteIcon' src='images/minus_icon.png' onclick='removeRow(this);' value='"+JSON.stringify(jsonObj)+"'>";
		  	tr.appendChild (icon);
		} else if (fromPage == 'edit' && system && customerId == 'photon') {
			var icon = document.createElement('td');
		  	icon.innerHTML = "<img class = 'del imagealign' id='deleteIcon' src='images/minus_icon.png' onclick='removeRow(this);' value='"+JSON.stringify(jsonObj)+"'>";
		  	tr.appendChild (icon);
		} else if (fromPage == 'edit' && !system && customerId != 'photon') {
			var icon = document.createElement('td');
		  	icon.innerHTML = "<img class = 'del imagealign' id='deleteIcon' src='images/minus_icon.png' onclick='removeRow(this);' value='"+JSON.stringify(jsonObj)+"'>";
		  	tr.appendChild (icon);
		} else {
			var icon = document.createElement('td');
		  	icon.innerHTML = "<img class = 'del imagealign' id='deleteIcon' src='images/minus_icon.png' value='"+JSON.stringify(jsonObj)+"'>";
		  	tr.appendChild (icon);
		}
	 	var hiddenField = document.createElement("input");
	  	hiddenField.type = "hidden";
	  	hiddenField.name = "propTemps";
	  	hiddenField.id = jsonObj.key.replace(/\./g, '-');
	  	hiddenField.value = JSON.stringify(jsonObj);
	  	tr.appendChild(hiddenField);
	   	body.appendChild(tr);
	  	tableHide();
	}

	//Modify Divs dynamically
	function modifyDiv(jsonObj, fieldId) {
		var table = document.getElementById('dataTable');
		var body = table.getElementsByTagName('tbody')[0];
		var trId = $('#' + fieldId.replace(/\./g, '-')).parent().attr("id");
		$('#'+trId).find("td").remove();
		$('#'+trId).find("input").remove();
		
		$('#'+trId).append("<td><a href='#' onclick='editPopup("+JSON.stringify(jsonObj.key)+", "+jsonObj.system+");'>"+jsonObj.key+"</td>");
		$('#'+trId).append("<td>"+jsonObj.name+"</td>");
		$('#'+trId).append("<td>"+jsonObj.type+"</td>");
		$('#'+trId).append("<td id='posbl-td'>"+jsonObj.possibleValues+"</td>");
		var status = jsonObj.required;
		var img = "";
		status ? img = "<img src='images/success.png' title=''>" : img = "<img src='images/smalldelete.png' title=''>";
		$('#'+trId).append("<td>"+img+"</td>");
		$('#'+trId).append("<td><img class = 'del imagealign' id='deleteIcon' src='images/minus_icon.png' onclick='removeRow(this);' value="+JSON.stringify(jsonObj)+"></td>");
		$('#'+trId).append("<input type='hidden' name='propTemps' id='"+jsonObj.key.replace(/\./g, '-')+"' value='"+JSON.stringify(jsonObj)+"'>");
	}
	
	//Hide table if no propTemp is available
	function tableHide() {
		var numberOfRows = document.getElementById('dataTable').getElementsByTagName('tbody')[0].getElementsByTagName('tr').length;
		if (numberOfRows < 1) {
			$('.config_table_div').hide();
			return false;
		} else {
			$('.config_table_div').show();
			return true;
		}
	}
	
	function removeRow(currentTag) {
		$(currentTag).parent().parent().remove();
		tableHide();
	}
	
	//To find duplicates of key and name in propTemps
	function duplicateFinder(newKey, newName, oldKey, oldName) {
		var hasError = false;
		$("[name=propTemps]").each(function(){  
			var jsonObj = JSON.parse($(this).val());
			var key = jsonObj.key;
			var name = jsonObj.name;
			if (oldKey != newKey) {
				if (key === newKey) {
					$('.errMsg').html("Key Already Exists");
					hasError = true;
					return false;
				}
			}
			if (oldName != newName) {
				if (name === newName) {
					$('.errMsg').html("Name Already Exists");
					hasError = true;
					return false;
				} 
			}
		});
		
		return hasError;
	}
	
	function popupOnOk(self) {
		var fromPage = $('input[name="popupFromPage"]').val();
		var oldname = oldName;
		var oldkey = oldKey;
		if (validatePropTemplates()) {
			var array = [];
			$('#posblVal option').each( function() {
				array.push($(this).val());
			});
			
			var appliesTo = [];
			$("input[name=propAppliesTo]:checked").each( function() {
				appliesTo.push($(this).val());
			});
			
			var customerIdArray = [];
			var system = false;
			<%if(customerId.equals("photon")) %>
				system = true;
			customerIdArray.push('<%= customerId %>');
			var id = $('#id').val();
			var key = $('#key').val();
			var name = $('#name').val();
			var type = $('.propType').val();
			var helpText = $('#helpText').val();
			var defaultValue = $('#defaultValue').val();
			var multiple = $('#multiple').is(':checked');
			var mandatory = $('#mandatory').is(':checked');
			
			var value = {};
			if (!isBlank(id)) {
				value.id = id;				
			}
			value.key = key;
			value.name = name;
			value.type = type;
			value.helpText = helpText;
			value.possibleValues = array;
			value.multiple = multiple;
			value.required = mandatory;
			value.defaultValue = defaultValue;
			value.appliesTo = appliesTo;
			value.system = system;
			value.customerIds = customerIdArray;
			hideError($("#propTempControl"), $("#propTempError"));
			if (!duplicateFinder(key, name, oldkey, oldname)) {
				$('#popupPage').modal('hide');
				if (fromPage === 'Add') {
					constructDiv(value);
				} else if (fromPage === "Edit") {
					modifyDiv(value,fieldId);
				}
			}
			trimValues();
		}
	}
	
	function findError(data) {
		if (!isBlank(data.nameError)) {
			showError($("#nameControl"), $("#nameError"), data.nameError);
		} else {
			hideError($("#nameControl"), $("#nameError"));
		}
		
		if (!isBlank(data.applyError)) {
			showError($("#applyControl"), $("#applyError"), data.applyError);
		} else {
			hideError($("#applyControl"), $("#applyError"));
		}
		
		if (!isBlank(data.dispError)) {
			showError($("#dispNameControl"), $("#dispNameError"), data.dispError);
		} else {
			hideError($("#dispNameControl"), $("#dispNameError"));
		}
	}
	
</script>