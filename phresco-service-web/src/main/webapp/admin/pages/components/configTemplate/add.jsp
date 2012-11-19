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
<%@page import="java.util.ArrayList"%>

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
	String name = "";
	String desc = "";
	boolean isSystem = false;
	if (settingsTemplate != null) {
		name = settingsTemplate.getName();
		desc = settingsTemplate.getDescription();
		isSystem = settingsTemplate.isSystem();
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
					type="text" name="name"  value="<%=name%>" maxlength="30" title="30 Characters only">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.desc'/>
			</label>
			<div class="controls">
				<textarea placeholder="<s:text name='place.hldr.configTemp.add.desc'/>" class="input-xlarge" 
					name="description" maxlength="150" title="150 Characters only"><%=desc%></textarea>
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
							<input type="checkbox" value="all" id="checkAllAuto" name="" onclick="checkAllEvent(this,$('.applsChk'), true);" style="margin: 3px 8px 6px 0;">All
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
									<input type="checkbox" id="appliestoCheckbox" name="appliesTo" value="<%= technology.getId() %>"  <%= checkedStr %>
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
											<div class="th-inner tablehead fixTableHdr"><span class="mandatory">*</span>&nbsp;
												<s:label key="lbl.hdr.comp.cnfigtmplt.key.title" cssClass="keyMandtory" theme="simple"/></div>
										</th>
										<th class="second">
											<div class="th-inner tablehead fixTableHdr"><s:label key="lbl.hdr.comp.cnfigtmplt.name.title" theme="simple"/></div>
										</th>
										<th class="second">
											<div class="th-inner tablehead fixTableHdr"><s:label key="lbl.hdr.comp.cnfigtmplt.type.title" theme="simple"/></div>
										</th>
										<th class="third">
											<div class="th-inner tablehead fixTableHdr"><s:label key="lbl.hdr.comp.cnfigtmplt.psblvalue.title"  theme="simple"/></div>
										</th>
										<th class="third">
											<div class="th-inner tablehead fixTableHdr"><s:label key="lbl.hdr.comp.cnfigtmplt.helptext.title"  theme="simple"/></div>
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
								<tbody id="propTempTbody">
									<!-- For add -->
									<% if (ServiceUIConstants.ADD.equals(fromPage)) { %>
										<tr class="1_configdynamiadd">
											<td class="textwidth">
												<input type="text" id="1" value="" placeholder="<s:text name='place.hldr.configTemp.add.key'/>" name="propTempKey"  
													temp="1_key" class="key" onblur="updateRowInputNames(this)" maxlength="30" title="30 Characters only">
											</td>
											<td class="textwidth">
												<input type="text" id = "1_propTempName" value="" placeholder="<s:text name='place.hldr.configTemp.add.name'/>" maxlength="30" title="30 Characters only" class="propyName" >
											</td>
											<td class="textwidth">
												<select id="1_type" class = "select typewidth">
													<option value="String"><s:text name='lbl.hdr.comp.cnfigtmplt.string'/></option>
													<option value="Number"><s:text name='lbl.hdr.comp.cnfigtmplt.number'/></option>
													<option value="Password"><s:text name='lbl.hdr.comp.cnfigtmplt.password'/></option>
													<option value="FileType"><s:text name='lbl.hdr.comp.cnfigtmplt.filetype'/></option>
													<option value="Boolean"><s:text name='lbl.hdr.comp.cnfigtmplt.boolean'/></option>
												</select>
											</td>
											<td class="psblevalue" id="1_psblMulDiv" style="display:none;">
												<select type="text" placeholder="<s:text name='place.hldr.configTemp.add.possible.values'/>" 
													class="propTempTxt psblSelect" id="1_psblMul"></select>
												<a data-toggle="modal" href="#myModal"><img class="addiconAlign imagealign" temp="1" src="images/add_icon.png"/ 
													onclick="addPsblValPopup(this);"></a>
											</td>
											<input type="hidden" class="1"/>
											<td class="psblevalue" id="1_psblSinglDiv">
												<input type="text" placeholder="<s:text name='place.hldr.configTemp.add.possible.values'/>" 
													class="propTempTxt psblSngl" id="1_psblSingl">
												<a data-toggle="modal" href="#myModal"><img class="addiconAlign imagealign" temp="1" 
													src="images/add_icon.png"/ onclick="addPsblValPopup(this);"/></a>
											</td>
											<td class="hlpText">
												<input type="text" placeholder="<s:text name='place.hldr.configTemp.add.help.text'/>" 
													id="1_helpText" class="propTempTxt hlpTxt" maxlength="150" title="150 Characters only">
											</td>
											<td class="mandatoryfld">
												<input type="checkbox" value="true" id="1_propMand">
											</td>
											<td class="multiplefld">
												<input type="checkbox" value="true" id="1_propMul">
											</td>
											<td class="imagewidth">
												<a><img class="add imagealign" temp="1" src="images/add_icon.png" onclick="addconfig(this);"></a>
											</td>
											<td>
											</td>
										</tr>
									<% 
										} else { // for edit
											String key = "";
											String helpTxt = "";
											String mndtryChck = "";
											String mulChck = "";
											int dynamicId = 1;
											List<PropertyTemplate> propertyTemplates = settingsTemplate.getProperties();
											if (CollectionUtils.isNotEmpty(propertyTemplates)) {
												for (PropertyTemplate propertyTemplate : propertyTemplates) {
													List<String> possibleValues = propertyTemplate.getPossibleValues();
													if (propertyTemplate.isRequired()) {
														mndtryChck = "checked";
													} else {
														mndtryChck = "";
													}
													if (propertyTemplate.isMultiple()) {
														mulChck = "checked";
													} else {
														mulChck = "";
													}
													String propName = "";
													if (StringUtils.isNotEmpty(propertyTemplate.getName())) {
														propName = propertyTemplate.getName();
													}
									%>
												<tr class='<%= dynamicId + "_configdynamiadd" %>'>
													<td class="textwidth">
														<input type="text" id='<%= dynamicId %>' value='<%= propertyTemplate.getKey()%>' 
															placeholder="<s:text name='place.hldr.configTemp.add.key'/>" name="propTempKey"  
															temp='<%= dynamicId + "_key" %>' class="key" onblur="updateRowInputNames(this)" 
															maxlength="30" title="30 Characters only">
													</td>
													<td class="textwidth">
														<input type="text" placeholder="<s:text name='place.hldr.configTemp.add.name'/>" 
															id=<%= dynamicId + "_propTempName"%> class="propyName" maxlength="30" 
															title="30 Characters only" value='<%= propName %>'>
													</td>
													<td class="textwidth">
														<select id='<%= dynamicId + "_type" %>' class = "select typewidth">
															<option value="String"><s:text name='lbl.hdr.comp.cnfigtmplt.string'/></option>
															<option value="Number"><s:text name='lbl.hdr.comp.cnfigtmplt.number'/></option>
															<option value="Password"><s:text name='lbl.hdr.comp.cnfigtmplt.password'/></option>
															<option value="FileType"><s:text name='lbl.hdr.comp.cnfigtmplt.filetype'/></option>
															<option value="Boolean"><s:text name='lbl.hdr.comp.cnfigtmplt.boolean'/></option>
														</select>
													</td>
													<td class="psblevalue" id='<%= dynamicId + "_psblSinglDiv" %>' style="display:none">
														<input type="text" placeholder="<s:text name='place.hldr.configTemp.add.possible.values'/>" 
															class="propTempTxt psblSngl" id='<%= dynamicId + "_psblSingl" %>'>
														<a data-toggle="modal" href="#myModal"><img class="addiconAlign imagealign" temp='<%= dynamicId %>' 
															src="images/add_icon.png"/ onclick="addPsblValPopup(this);"/></a>
													</td>
													<input type="hidden" class='<%= dynamicId %>'>
													<td class="psblevalue" id='<%= dynamicId + "_psblMulDiv" %>' style="display:none">
														<select type="text" placeholder="<s:text name='place.hldr.configTemp.add.possible.values'/>" 
															class="propTempTxt psblSelect" id='<%= dynamicId + "_psblMul" %>'>
														</select>
														<a data-toggle="modal" href="#myModal"><img class="addiconAlign imagealign" temp='<%= dynamicId %>' 
															src="images/add_icon.png" onclick="addPsblValPopup(this);"></a>
													</td>
													<td class="hlpText">
														<input type="text" placeholder="<s:text name='place.hldr.configTemp.add.help.text'/>" 
															id=<%= dynamicId + "_helpText"%> class="propTempTxt hlpTxt" maxlength="150" 
															title="150 Characters only" value='<%= StringUtils.isNotEmpty(propertyTemplate.getHelpText()) ? propertyTemplate.getHelpText() : ""  %>'>
													</td>
													<td class="mandatoryfld">
														<input type="checkbox" value="true" id='<%= dynamicId + "_propMand" %>' <%= mndtryChck %>>
													</td>
													<td class="multiplefld">
														<input type="checkbox" value="true" id='<%= dynamicId + "_propMul" %>' <%= mulChck %>>
													</td>
													<td class="imagewidth">
														<a><img class="add imagealign" temp='<%= dynamicId %>' src="images/add_icon.png" onclick="addconfig(this);"></a>
													</td>
													<% if (dynamicId != 1) { %>
														<td>
															<img onclick="removeTag(this);" src="images/minus_icon.png" class="del imagealign">
														</td>
													<% } %>	
												</tr>
									<%
												dynamicId++;
												}
											}
										}	
									%>	
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
								<input type="hidden" id="hiddenKey"/>
								<div class="errMsg" id="errMsg"></div>
								<a href="#" class="btn btn-primary" data-dismiss="modal"><s:label key="lbl.btn.cancel" theme="simple"/></a>
								<a href="#" id ="ok" class="btn btn-primary" data-dismiss="modal" ><s:label key="lbl.btn.ok" theme="simple"/></a>
							</div>
						</div>
					</div>	
				</div>
			</div>	
		</fieldset>
	</div>
	 
	<div class="bottom_config">
		<input type="button" id="" class="btn btn-primary" 
				onclick="validatePropTempKey('<%= pageUrl %>', '<%= progressTxt %>');" 
		        value='<%= buttonLbl %>'/>
		<input type="button" id="configtempCancel" class="btn btn-primary" 
		      onclick="loadContent('configtempList', $('#formConfigTempAdd'), $('#subcontainer'));" 
		      value="<s:text name='lbl.btn.cancel'/>"/>
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
	<input type="hidden" name="oldName" value="<%= name %>"/>
	<input type="hidden" name="customerId" value="<%= customerId %>">
	<input type="hidden" name="configId" value="<%= settingsTemplate != null ? settingsTemplate.getId(): "" %>">
</form>

<script language="javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
		$(".multilist-scroller").scrollbars();
	}

	$(document).ready(function() {
		hideLoadingIcon();
		
		//for edit -- to dynamically populate possible values in property template fieldset 
		<% 
			if (ServiceUIConstants.EDIT.equals(fromPage)) {
				int dynamicId = 1;
				List<PropertyTemplate> propertyTemplates = settingsTemplate.getProperties();
				if (CollectionUtils.isNotEmpty(propertyTemplates)) {
					for (PropertyTemplate propertyTemplate : propertyTemplates) {
						List<String> possibleValues = propertyTemplate.getPossibleValues(); 
						if (CollectionUtils.isNotEmpty(possibleValues) && possibleValues.size() == 1) {
							for(String possibleValue : possibleValues) { 
		%>
							$("#" + '<%= dynamicId %>' + "_psblSinglDiv").show();
							$("#" + '<%= dynamicId %>' + "_psblMulDiv").hide();
							$("#" + '<%= dynamicId %>' + "_psblSingl").val('<%= possibleValue %>');
							$("#" + '<%= dynamicId %>' + "_psblMulDiv").hide();
							$("." + '<%= dynamicId %>').val('<%= possibleValue %>');
		<% 
							}
						} else {
							StringBuilder csvPsblVal = new StringBuilder();
							if (CollectionUtils.isNotEmpty(possibleValues)) {
				            	for (String possibleValue : possibleValues) {
		%>
			            			$("#"+ '<%= dynamicId %>' +"_psblMul").append($("<option></option>").attr("value", '<%= possibleValue %>').text('<%= possibleValue %>'));
			            	
		<% 
					            	if (csvPsblVal.length() != 0) {
										csvPsblVal.append(",");
						            }
									csvPsblVal.append(possibleValue);
		%>
									$("#" + '<%= dynamicId %>' + "_psblSinglDiv").hide();
									$("#" + '<%= dynamicId %>' + "_psblMulDiv").show();
									$("." + '<%= dynamicId %>').val('<%= csvPsblVal %>');
		<% 
								}
							}
						}
						if (CollectionUtils.isEmpty(possibleValues)) {
		%>
							$("#" + '<%= dynamicId %>' + "_psblSinglDiv").show();
		<%
						}	
		%>
						selectType('<%= propertyTemplate.getType() %>', '<%= dynamicId %>');
		<%
						dynamicId ++; 
					}
				}
		%>
				updateRowInputNamesInEdit();
		<% } %>

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
		
		$("#addValues").click(function() {
			var textComboVal = $("#txtCombo").val();
			var alreadyExists = false;
			if ($("#txtCombo").val().replace(/\s/g, "") === "") {
				$("#errMsg").html("Please enter Key");
				alreadyExists = true;
			}
			
			$('#valuesCombo option').each( function() {
				if ($(this).val() == textComboVal) {
					$("#errMsg").html("Key value already exists");
					alreadyExists = true;
					return false;
				}
			});
			
			if (!alreadyExists && textComboVal.replace(/\s/g, "") !== "") {
				$("#txtCombo").val("");
				$("#valuesCombo").append($('<option name='+ textComboVal +'></option>').attr("value", textComboVal).text(textComboVal));				
			}
		});
		
		$("#ok").click(function() {
			var id = $("#hiddenKey").val();
			var length =  $('#valuesCombo option').length;
			var psblValName ="";
			var psblVal = "";
			$("#"+ id +"_psblMul").empty();// to clear select options before populating
			$('#valuesCombo option').each( function() {
				var length =  $('#valuesCombo option').length;
				var value = $(this).val();
				var keyVal = $("#"+id).val();
				psblValName = keyVal + "_psblVal";
				if (length === 1) {
					$("#"+ id +"_psblMulDiv").hide();
					$("#"+ id +"_psblSinglDiv").show();
					$("#"+ id +"_psblSingl").val(value);
					$("."+id).val(value);
					$("."+id).attr("name", psblValName);
				} else {
					$("#"+ id +"_psblSinglDiv").hide();
					$("#"+ id +"_psblMulDiv").show();
					$("#"+ id +"_psblMul").append($("<option></option>").attr("value", value).text(value));
					psblVal = psblVal.concat(value);
					psblVal = psblVal.concat(",");
				}
			});
			if (length !== 1) {
				$("."+id).val(psblVal.substring(0, psblVal.length-1));
				$("."+id).attr("name", psblValName);
			}
		});
	
		$('#remove').click(function() {
			$('#valuesCombo option:selected').each( function() {
				$(this).remove();
			});
		});
	});
	
	var counter = "";
	function addconfig() {
		counter = $(".key").size() + 1;
		var trId = counter + "_configdynamiadd";
		var keyId = counter;
		var keyTmpName = counter+"_key";
		var nameId = counter + "_propTempName";
	 	var typeId = counter + "_type";
	 	var psblMulDivId = counter + "_psblMulDiv";
	 	var psblValMultipleId = counter + "_psblMul";
	 	var psblSinglDivId = counter + "_psblSinglDiv";
	 	var psblValSingleId = counter + "_psblSingl";
	 	var helpTextId = counter + "_helpText";
	 	var mandChckId = counter + "_propMand";
		var mulChckId = counter + "_propMul";
	 	
	 	var newPropTempRow = $(document.createElement('tr')).attr("id", trId);
	 	newPropTempRow.html("<td class='textwidth'><input type='text' id='"+ keyId +"' class='key' name='propTempKey' value='' "+
	 			" temp='"+ keyTmpName +"' placeholder='<s:text name='place.hldr.configTemp.add.key'/>' onblur='updateRowInputNames(this)'></td>" + 
				"<td class='textwidth'> <input type='text' id = '"+ nameId +"' placeholder='<s:text name='place.hldr.configTemp.add.name'/>' " + 
				" value='' placeholder='' maxlength='30' class='propyName'></td><td class='textwidth'><select id='"+ typeId +"' " + 
				"class = 'select typewidth'><option value='String'>String</option><option value='Integer'>Integer</option><option value='Password'>" + 
				"Password</option></select></td><td class='psblevalue' id='"+ psblMulDivId +"' style='display:none;'><select type='text' " + 
				"placeholder='<s:text name='place.hldr.configTemp.add.possible.values'/>'class='propTempTxt psblSelect' id='"+ psblValMultipleId +"'>" + 
				"</select><a data-toggle='modal' href='#myModal'><img class='addIcon imagealign' temp='"+ keyId +"' src='images/add_icon.png'" + 
				"onclick='addPsblValPopup(this);'/></a></td><input type='hidden' class='"+ keyId +"'/><td class='psblevalue' id='"+ psblSinglDivId +"'>" + 
				"<input type='text' placeholder='<s:text name='place.hldr.configTemp.add.possible.values'/>'class='propTempTxt psblSngl' " + 
				"id='"+ psblValSingleId +"'><a data-toggle='modal' href='#myModal'><img class='addIcon imagealign' temp='"+ keyId +"' " +
	 			"src='images/add_icon.png' onclick='addPsblValPopup(this);'/></a></td><td class='hlpText'><input type='text' id='"+ helpTextId +"' " + 
	 			"placeholder='<s:text name='place.hldr.configTemp.add.help.text'/>' name='helpText' class='propTempTxt hlpTxt'></td>" + 
	 			"<td class='mandatoryfld'><input type='checkbox' value='true' id='"+ mandChckId +"'></td><td class='multiplefld'> " + 
	 			"<input type='checkbox' value='true' id='"+ mulChckId +"'></td><td class='imagewidth'><a ><img class='add imagealign' " + 
	 			" temp='"+ keyId +"' src='images/add_icon.png' onclick='addconfig(this);'></a></td><td><img class = 'del imagealign'" + 
	 			"src='images/minus_icon.png' onclick='removeTag(this);'></td>")
	 	newPropTempRow.appendTo("#propTempTbody");		
		counter++;
	}
	 
	function removeTag(currentTag) {
		$(currentTag).parent().parent().remove();
	}
	
	function addPsblValPopup(obj) {
		$("#hiddenKey").val("");
		$("#txtCombo").val("");
		$("#errMsg").empty();
		$("#valuesCombo").empty();
		$("#hiddenKey").val($(obj).attr("temp"));

		//to populate already added possible values in popup
		var keyClass = $(obj).attr("temp");
		var psblValSep = new Array();
		var values = $("." + keyClass).val();
		if (values !== undefined && values !== "") {
			psblValSep = values.split(",");
			for ( var i = 0; i < psblValSep.length; i++) {
				$("#valuesCombo").append(
						$('<option name='+ psblValSep[i] +'></option>').attr(
								"value", psblValSep[i]).text(psblValSep[i]));
			}
		}
	}

	function updateRowInputNames(obj) {
		//to check duplications of entered key value
		var keyClass = $(obj).attr("class");
		$("." + keyClass).each(function() {
			if ($(obj).attr("temp") !== $(this).attr("temp")
					&& $(obj).val() === $(this).val()) {
				$(obj).val("");
				showError($("#" + $(obj).attr("id")
						+ "_configdynamiadd"));
				return false;
			}
		});

		//to set names of input fields of current prop template row
		if ($(obj).val().replace(/\s/g, "") !== "") {
			var id = $(obj).attr("id");
			var proptempName = $(obj).val() + "_propTempName";
			var typeName = $(obj).val() + "_type";
			var psblVal = $(obj).val() + "_psblVal";
			var helpTextName = $(obj).val() + "_helpText";
			var mandChckName = $(obj).val() + "_propMand";
			var mulChckName = $(obj).val() + "_propMul";

			$("#" + id + "_propTempName").attr("name", proptempName);
			$("#" + id + "_type").attr("name", typeName);
			$("." + id).attr("name", psblVal);
			$("#" + id + "_helpText").attr("name", helpTextName);
			$("#" + id + "_propMand").attr("name", mandChckName);
			$("#" + id + "_propMul").attr("name", mulChckName);
		}
	}
	
	//for edit -- to change names of property template input fields
	function updateRowInputNamesInEdit() {
		$(".key").each(function() {
			var id = $(this).attr("id");
			var proptempName = $(this).val() + "_propTempName";
			var typeName = $(this).val() + "_type";
			var psblVal = $(this).val() + "_psblVal";
			var helpTextName = $(this).val() + "_helpText";
			var mandChckName = $(this).val() + "_propMand";
			var mulChckName = $(this).val() + "_propMul";

			$("#" + id + "_propTempName").attr("name", proptempName);
			$("#" + id + "_type").attr("name", typeName);
			$("." + id).attr("name", psblVal);
			$("#" + id + "_helpText").attr("name", helpTextName);
			$("#" + id + "_propMand").attr("name", mandChckName);
			$("#" + id + "_propMul").attr("name", mulChckName);
		});
	}

	//for edit -- to preselect type select box value in property template 
	function selectType(type, id) {
		$("#" + id + "_type > option").each(function() {
			if ($(this).val() === type) {
				$("#" + id + "_type  option[value='" + type + "']")
						.attr("selected", "selected");
				return false;
			}
		});
	}

	function validatePropTempKey(pageUrl, progressText) {
		var redirect = true;
		$(".key").each(function() {
			if ($(this).val().replace(/\s/g, "") === "") {
				$("#" + $(this).attr("id")).focus();
				redirect = false;
				return false;
			}
		});
		if ($("#configname").val().replace(/\s/g, "") === "") {
			showError($("#nameControl"), $("#nameError"),
					'<s:text name='err.msg.name.empty'/>');
		} else {
			hideError($("#nameControl"), $("#nameError"));
		}
		if ($("input[name='appliesTo']:checked").length === 0) {
			showError($("#applyControl"), $("#applyError"),
					'<s:text name='err.msg.applies.empty'/>');
		} else {
			hideError($("#applyControl"), $("#applyError"));
		}

		if (redirect) {
			validate(pageUrl, $('#formConfigTempAdd'), $('#subcontainer'),
					progressText, $('#appliesToDiv :input'));
		}
	}

	// To check for the special character in configname
	$('#configname').bind('input propertychange', function(e) {
		var configname = $(this).val();
		configname = checkForSplChr(configname);
		$(this).val(configname);
	});
</script>