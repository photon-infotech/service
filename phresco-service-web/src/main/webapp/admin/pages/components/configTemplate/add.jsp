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
	//For edit
	String id = "";
	String name = "";
	String dispName = "";
	String desc = "";
	boolean isSystem = false;
	boolean isCustProp = false;
	if (settingsTemplate != null) {
		id = settingsTemplate.getId();
		name = settingsTemplate.getName();
		dispName = settingsTemplate.getDisplayName();
		desc = settingsTemplate.getDescription();
		isSystem = settingsTemplate.isSystem();
		isCustProp = settingsTemplate.isCustomProp();
		List<PropertyTemplate> properties = settingsTemplate.getProperties();
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
				<s:text name="lbl.hdr.comp.system.properties" />
			</label>
			<div class="controls">
				<%
					String checkedStr = "";
					if (isCustProp) {
					    checkedStr = "checked";
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
		<input type="button" id="" class="btn btn-primary" 
			onclick="validatePropTempKey('<%= pageUrl %>', '<%= progressTxt %>');" 
			value='<%= buttonLbl %>'/>
		<input type="button" id="configtempCancel" class="btn btn-primary" 
			onclick="loadContent('configtempList', $('#formCustomerId'), $('#subcontainer'));" value="<s:text name='lbl.btn.cancel'/>"/>
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
	<input type="hidden" name="oldName" value="<%= name %>"/>
	<input type="hidden" name="customerId" value="<%= customerId %>">
	<input type="hidden" name="configId" value="<%= id %>">
</form>

<script language="javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
		$(".multilist-scroller").scrollbars();
	}
	
	$(document).ready(function() {
		hideLoadingIcon();
		checkboxEvent($('#checkAllAuto'), 'applsChk');
		tableHide();
		
		$('#defaultCustProp').click(function(){
			changeChckBoxValue(this);
		});
		
		<%
			if (settingsTemplate != null) {
				id = settingsTemplate.getId();
				name = settingsTemplate.getName();
				dispName = settingsTemplate.getDisplayName();
				desc = settingsTemplate.getDescription();
				isSystem = settingsTemplate.isSystem();
				isCustProp = settingsTemplate.isCustomProp();
				List<PropertyTemplate> properties = settingsTemplate.getProperties();
				for(PropertyTemplate prop : properties) {
 		%>	
		 			var values = [];
					var json = {};
					json.name = '<%= prop.getName() %>';
					json.key = '<%= prop.getKey() %>';
					json.type = '<%= prop.getType() %>';
					json.helpText =  '<%= StringUtils.isNotEmpty(prop.getHelpText()) ? prop.getHelpText() : "" %>';
			
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
		%>
					json.possibleValues = values;
					json.multiple =  '<%= prop.isMultiple() %>';
					json.required =  '<%= prop.isRequired() %>';
					constructDiv(json);
		<%		
				}
		 	}
		%>
		trimValues();
	});
	
	var customer = $('input[name=customerId]').val();
	var fromPage = '<%= fromPage %>';
	var system = '<%= isSystem %>';
	
	//Add propTemp
	function openConfigTempPopup() {
		var Params = 'fromPage=';
		Params = Params.concat("Add");
		if ((fromPage == 'add') || (fromPage == 'edit' && system == 'false')) {
			yesnoPopup('showPropTempPopup', "Add Property Templates", 'saveTemplate', 'OK', '', Params);
		} else if (fromPage == 'edit' && system == 'true' && customer == 'photon') {
			yesnoPopup('showPropTempPopup', "Add Property Templates", 'saveTemplate', 'OK', '', Params);
		}
	}
	
	//edit propTemp
	function editPopup(key) {
		var params = "propTempKey=";
		params=params.concat(key);
		params = params.concat("&fromPage=");
		params = params.concat("Edit");
		if (fromPage == 'add' || (fromPage == 'edit'&& system == 'false')) {
			yesnoPopup('showPropTempPopup', "Edit Property Templates", 'saveTemplate', 'Update','', params);
		} else if (fromPage == 'edit' && system == 'true' && customer == 'photon') {
			yesnoPopup('showPropTempPopup', "Edit Property Templates", 'saveTemplate', 'Update','', params);
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
		jsonObj.defaultCustProp = $('#defaultCustProp').val();
		jsonObj.customerId = "<%= customerId %>";
		jsonObj.configId = "<%= id %>";
		jsonObj.oldName = "<%= name %>";
		jsonObj.fromPage = "<%= fromPage %>";
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
	  	tr.id = "tr-"+jsonObj.key;
	  
	 	var key = document.createElement('td');
	  	key.innerHTML = "<a href='#' onclick='editPopup("+JSON.stringify(jsonObj.key)+");'>"+jsonObj.key+"</a>";
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
	  	if (status == "true") {
			img = "<img src='images/success.png' title='Failure'>"; 
	  	} else if (status == "false") {
 			img = "<img src='images/smalldelete.png' title='Failure'>";
		}
	  	var mandatory = document.createElement('td');
	  	mandatory.innerHTML = img;
	  	tr.appendChild (mandatory);
	  
	  	var icon = document.createElement('td');
	  	icon.innerHTML = "<img class = 'del imagealign' id='deleteIcon' src='images/minus_icon.png' onclick='removeRow(this);' value='"+JSON.stringify(jsonObj)+"'>";
	  	tr.appendChild (icon);
	  
	  	var hiddenField = document.createElement("input");
	  	hiddenField.type = "hidden";
	  	hiddenField.name = "propTemps";
	  	hiddenField.id = jsonObj.key;
	  	hiddenField.value = JSON.stringify(jsonObj);
	  	tr.appendChild(hiddenField);
	  
	  	body.appendChild(tr);
	  	tableHide();
	}

	//Modify Divs dynamically
	function modifyDiv(jsonObj, fieldId) {
		var table = document.getElementById('dataTable');
		var body = table.getElementsByTagName('tbody')[0];
		var trId = $('#' + fieldId).parent().attr("id");
		$('#'+trId).find("td").remove();
		$('#'+trId).find("input").remove();
		
		$('#'+trId).append("<td><a href='#' onclick='editPopup("+JSON.stringify(jsonObj.key)+");'>"+jsonObj.key+"</td>");
		$('#'+trId).append("<td>"+jsonObj.name+"</td>");
		$('#'+trId).append("<td>"+jsonObj.type+"</td>");
		$('#'+trId).append("<td id='posbl-td'>"+jsonObj.possibleValues+"</td>");
		var status = jsonObj.required;
		var img = "";
		if (status == "true") {
			img = "<img src='images/success.png' title='Failure'>"; 
	  	} else if (status == "false") {
			img = "<img src='images/smalldelete.png' title='Failure'>";
	  	}
		$('#'+trId).append("<td>"+img+"</td>");
		$('#'+trId).append("<td><img class = 'del imagealign' id='deleteIcon' src='images/minus_icon.png' onclick='removeRow(this);' value="+JSON.stringify(jsonObj)+"></td>");
		$('#'+trId).append("<input type='hidden' name='propTemps' id='"+jsonObj.key+"' value='"+JSON.stringify(jsonObj)+"'>");
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
			var key = $('#key').val();
			var name = $('#name').val();
			var type = $('.propType').val();
			var helpText = $('#helpText').val();
			var multiple = "false";
			if ($('#multiple').is(':checked')) {
				multiple = "true"; 
			} 
			var mandatory = "false";
			if ($('#mandatory').is(':checked')) {
				mandatory = "true";
			} 
			var value = {};
			value.key = key;
			value.name = name;
			value.type = type;
			value.helpText = helpText;
			value.possibleValues = array;
			value.multiple = multiple;
			value.required = mandatory;
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