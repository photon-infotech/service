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
<%@ page import="java.util.ArrayList"%>

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
	String dispName = "";
	String desc = "";
	boolean isSystem = false;
	boolean isCustProp = false;
	if (settingsTemplate != null) {
		name = settingsTemplate.getName();
		dispName = settingsTemplate.getDisplayName();
		desc = settingsTemplate.getDescription();
		isSystem = settingsTemplate.isSystem();
		isCustProp = settingsTemplate.isCustomProp();
	}
	
	String disabledStr = "";
	String addImgFunction = "";
	String removeImgFunction = "";
	String possibleValFunction = "";
	String possibleValueHref = ""; 
	if ("edit".equals(fromPage)) {
		if ("photon".equalsIgnoreCase(customerId)) {
			disabledStr = "";
			addImgFunction = "addconfig(this);";
			removeImgFunction = "removeTag(this);";
			possibleValFunction = "addPsblValPopup(this);";
			possibleValueHref = "#myModal";
		} else {
			disabledStr = "disabled";
			addImgFunction = "";
			removeImgFunction = "";
			possibleValFunction = "";
			possibleValueHref = "";
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
				<textarea placeholder="<s:text name='place.hldr.configTemp.add.desc'/>" class="input-xlarge" 
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
		
		<div class="control-group" id="keyControl">
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
				<input type="checkbox" name="defaultCustProp" value="true" <%= checkedStr %>>
			</div>
			 <span class="help-inline padd" id="keyvalueError"></span>
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
											<div class="th-inner tablehead fixTableHdr"><span class="mandatory">*</span>&nbsp;
												<s:label key="lbl.hdr.comp.cnfigtmplt.name.title" cssClass="keyMandtory" theme="simple"/></div>
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
											<div class="th-inner"></div>
										</th>
										<th class="third">
											<div class="th-inner"></div>
										</th>
									</tr>
								</thead>
							</div>
							<div id="input1" class="clonedInput">
								<tbody id="propTempTbody">
									<!-- For add -->
									<% if (ServiceUIConstants.ADD.equals(fromPage)) { %>
										<tr class="1_configdynamiadd">
											<td class="tdWidth">
												<input type="text" id="1" value="" placeholder="<s:text name='place.hldr.configTemp.add.key'/>" name="propTempKey"  
													temp="1_key" class="keywidth" onblur="updateRowInputNames(this)" maxlength="30" title="30 Characters only">
											</td>
											<td class="tdWidth">
												<input type="text" id = "1_propTempName" value="" placeholder="<s:text name='place.hldr.configTemp.add.name'/>" 
													onblur="checkNameUnique(this);" maxlength="30" title="30 Characters only" tempAttr="configNameField" class="textWidth" >
											</td>
											<td class="tdWidth">
												<select id="1_type" class = "textWidth" onchange="typeChange(this);">
													<option value="String"><s:text name='lbl.hdr.comp.cnfigtmplt.string'/></option>
													<option value="Number"><s:text name='lbl.hdr.comp.cnfigtmplt.number'/></option>
													<option value="Password"><s:text name='lbl.hdr.comp.cnfigtmplt.password'/></option>
													<option value="FileType"><s:text name='lbl.hdr.comp.cnfigtmplt.filetype'/></option>
													<option value="Boolean"><s:text name='lbl.hdr.comp.cnfigtmplt.boolean'/></option>
													<option value="Actions"><s:text name='lbl.hdr.comp.cnfigtmplt.actions'/></option>
												</select>
											</td>
											 <td class="tdWidth" id="1_psblMulDiv" style="display:none;">
												<select type="text" placeholder="<s:text name='place.hldr.configTemp.add.possible.values'/>" 
													class="psblselectwidth" id="1_psblMul"></select>
												<a data-toggle="modal" class="togglePopup" href="#myModal"><img class="addiconAlign imagealign" temp="1" src="images/add_icon.png"/ 
													onclick="addPsblValPopup(this);"></a>
											</td>
											<input type="hidden" class="1"/>
											<td class="psblbtnwidth" id="1_psblSinglDiv">
												<input type="text" placeholder="<s:text name='place.hldr.configTemp.add.possible.values'/>" 
													class="psbltextwidth" id="1_psblSingl" disabled>
												<a data-toggle="modal" class="togglePopup" href="#myModal"><img class="addiconAlign imagealign" temp="1" 
													src="images/add_icon.png"/ onclick="addPsblValPopup(this);"/></a>
											</td>
											<td class="tdWidth">
												<input type="text" placeholder="<s:text name='place.hldr.configTemp.add.help.text'/>" 
													id="1_helpText" class="textWidth" maxlength="150" title="150 Characters only">
											</td>
											<td class="buttonwidth">
												<input class="chkBox_config" type="checkbox" value="true" id="1_propMand">
											</td>
											<td class="buttonwidth">
												<input class="multiple_chkBox_config" type="checkbox" value="true" id="1_propMul">
											</td>
											<td class="buttonwidth">
												<a><img class="add imagealign" temp="1" src="images/add_icon.png" onclick="addconfig(this);"></a>
											</td>
											<td class="buttonwidth">
											<img class = 'del imagealign' id='deleteIcon' src='images/minus_icon.png' onclick='removeTag(this);'>
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
													<td class="tdWidth">
														<input type="text" id='<%= dynamicId %>' value='<%= propertyTemplate.getKey()%>' 
															placeholder="<s:text name='place.hldr.configTemp.add.key'/>" name="propTempKey"  
															temp='<%= dynamicId + "_key" %>' class="keywidth" onblur="updateRowInputNames(this)" 
															maxlength="30" title="30 Characters only" <%= disabledStr %>>
													</td>
													<td class="tdWidth">
														<input type="text" placeholder="<s:text name='place.hldr.configTemp.add.name'/>" 
															id=<%= dynamicId + "_propTempName"%> class="textWidth" maxlength="30" 
															title="30 Characters only" onblur='checkNameUnique(this);' tempAttr='configNameField' 
															value='<%= propName %>' <%= disabledStr %>>
													</td>
													<td class="tdWidth">
														<select id='<%= dynamicId + "_type" %>' class = "textWidth" <%= disabledStr %> onchange="typeChange(this);">
															<option value="String"><s:text name='lbl.hdr.comp.cnfigtmplt.string'/></option>
															<option value="Number"><s:text name='lbl.hdr.comp.cnfigtmplt.number'/></option>
															<option value="Password"><s:text name='lbl.hdr.comp.cnfigtmplt.password'/></option>
															<option value="FileType"><s:text name='lbl.hdr.comp.cnfigtmplt.filetype'/></option>
															<option value="Boolean"><s:text name='lbl.hdr.comp.cnfigtmplt.boolean'/></option>
															<option value="Actions"><s:text name='lbl.hdr.comp.cnfigtmplt.actions'/></option>
														</select>
													</td>
													<td class="psblbtnwidth" id='<%= dynamicId + "_psblSinglDiv" %>' style="display:none">
														<input type="text" placeholder="<s:text name='place.hldr.configTemp.add.possible.values'/>" 
															class="psbltextwidth" id='<%= dynamicId + "_psblSingl" %>' disabled>
														<a data-toggle="modal" class='togglePopup' href='<%= possibleValueHref %>'><img class="addiconAlign imagealign" temp='<%= dynamicId %>' 
															src="images/add_icon.png"/ onclick='<%=possibleValFunction%>'/></a>
													</td>
													<input type="hidden" class='<%= dynamicId %>'>
													<td class="psblbtnwidth" id='<%= dynamicId + "_psblMulDiv" %>' style="display:none">
														<select type="text" placeholder="<s:text name='place.hldr.configTemp.add.possible.values'/>" 
															class="psblselectwidth" id='<%= dynamicId + "_psblMul" %>' <%= disabledStr %>>
														</select>
														<a data-toggle="modal" class='togglePopup' href='<%= possibleValueHref %>'><img class="addiconAlign imagealign" temp='<%= dynamicId %>' 
															src="images/add_icon.png" onclick='<%=possibleValFunction%>'></a>
													</td>
													<td class="tdWidth">
														<input type="text" placeholder="<s:text name='place.hldr.configTemp.add.help.text'/>" 
															id=<%= dynamicId + "_helpText"%> class="textWidth" maxlength="150" 
															title="150 Characters only" value='<%= StringUtils.isNotEmpty(propertyTemplate.getHelpText()) ? propertyTemplate.getHelpText() : ""  %>' <%= disabledStr %>>
													</td>
													<td class="buttonwidth">
														<input type="checkbox" class="chkBox_config" value="true" id='<%= dynamicId + "_propMand" %>' <%= mndtryChck %> <%= disabledStr %>>
													</td>
													<td class="buttonwidth">
														<input type="checkbox" class="multiple_chkBox_config" value="true" id='<%= dynamicId + "_propMul" %>' <%= mulChck %> <%= disabledStr %>>
													</td>
													<td class="buttonwidth">
														<a><img class="add imagealign" temp='<%= dynamicId %>' src="images/add_icon.png" onclick='<%= addImgFunction %>'></a>
													</td class="buttonwidth">
														<td class="buttonwidth">
															<img onclick='<%=removeImgFunction %>' id="deleteIcon" src="images/minus_icon.png" class="del imagealign">
														</td>
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
		checkboxEvent($('#checkAllAuto'), 'applsChk');
		chkCount();
		
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
				$("#errMsg").html('<s:text name='err.msg.enter.key'/>');
				alreadyExists = true;
			}
			
			$('#valuesCombo option').each( function() {
				if ($(this).val() == textComboVal) {
					$("#errMsg").html('<s:text name='err.msg.key.exist.already'/>');
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
			
			//To clear value of text box if no key is added
			if ($('#valuesCombo option').size() == 0) {
				$("#"+ id +"_psblSingl").val('');
				$("#"+ id +"_psblMulDiv").hide();
				$("#"+ id +"_psblSinglDiv").show();
			}
			if (length !== 1) {
				$("."+id).val(psblVal.substring(0, psblVal.length-1));
				$("."+id).attr("name", psblValName);
			}
		});
	
		$('#remove').click(function() {
			if ($('#valuesCombo option:selected').size() == 0) {
				$("#errMsg").text('<s:text name='err.msg.select.key'/>');
			} else {
				$("#errMsg").empty();
				$('#valuesCombo option:selected').each( function() {
					$(this).remove();
				});
			}	
		});
	});
	
	
	var counter = 2;
	if ("edit" == '<%= fromPage %>') {
		counter = $('.keywidth').size() + 1;
	} 
	
	function addconfig() {
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
	 	newPropTempRow.html("<td class='tdWidth'><input type='text' id='"+ keyId +"' class='keywidth' name='propTempKey' value='' "+
	 			" temp='"+ keyTmpName +"' placeholder='<s:text name='place.hldr.configTemp.add.key'/>' onblur='updateRowInputNames(this)'></td>" + 
				"<td class='tdWidth'> <input type='text' id = '"+ nameId +"' placeholder='<s:text name='place.hldr.configTemp.add.name'/>' " + 
				" value='' placeholder='' maxlength='30' onblur='checkNameUnique(this);' tempAttr='configNameField' class='textWidth'></td><td class='tdWidth'><select id='"+ typeId +"' " + 
				"class = 'textWidth'  onchange='typeChange(this);'><option value='String'>String</option><option value='Number'>Number</option><option value='Password'>" + 
				"Password</option><option value='FileType'>FileType</option><option value='Boolean'>Boolean</option><option value='Actions'>Actions</option></select></td><td class='tdWidth' id='"+ psblMulDivId +"' style='display:none;'><select type='text' " + 
				"placeholder='<s:text name='place.hldr.configTemp.add.possible.values'/>'class='psblselectwidth' id='"+ psblValMultipleId +"'>" + 
				"</select><a data-toggle='modal' class='togglePopup' href='#myModal'><img class='addIcon imagealign' temp='"+ keyId +"' src='images/add_icon.png'" + 
				"onclick='addPsblValPopup(this);'/></a></td><input type='hidden' class='"+ keyId +"'/><td class='psblbtnwidth' id='"+ psblSinglDivId +"'>" + 
				"<input type='text' placeholder='<s:text name='place.hldr.configTemp.add.possible.values'/>'class='psbltextwidth' " + 
				"id='"+ psblValSingleId +"' disabled><a data-toggle='modal' class='togglePopup' href='#myModal'><img class='addIcon imagealign' temp='"+ keyId +"' " +
	 			"src='images/add_icon.png' onclick='addPsblValPopup(this);'/></a></td><td class='tdWidth'><input type='text' id='"+ helpTextId +"' " + 
	 			"placeholder='<s:text name='place.hldr.configTemp.add.help.text'/>' name='helpText' class='textWidth'></td>" + 
	 			"<td class='buttonwidth'><input type='checkbox' class='chkBox_config' value='true' id='"+ mandChckId +"'></td><td class='buttonwidth'> " + 
	 			"<input type='checkbox' class='multiple_chkBox_config' value='true' id='"+ mulChckId +"'></td><td class='buttonwidth'><a ><img class='add imagealign' " + 
	 			" temp='"+ keyId +"' src='images/add_icon.png' onclick='addconfig(this);'></a></td><td class='buttonwidth'><img class = 'del imagealign'" + 
	 			"src='images/minus_icon.png' id='deleteIcon' onclick='removeTag(this);'></td>")
	 	newPropTempRow.appendTo("#propTempTbody");		
		counter++;
		chkCount();
	}
	
	function removeTag(currentTag) {
		$(currentTag).parent().parent().remove();
		chkCount();
	}
	
	function chkCount(){
		var noOfRows =  $('input[class="keywidth"]').size();
		if(noOfRows >1){
			$("#deleteIcon").show();
		}else{
			$("#deleteIcon").hide();
		}
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
			if ($(obj).val() != "" && !isBlank($(obj).val())) {
				if ($(obj).attr("temp") !== $(this).attr("temp")
						&& $(obj).val() === $(this).val()) {
					$(obj).val("");
					showError($("#" + $(this).attr("id")), $("#keyvalueError"),'<s:text name='err.msg.key.unique'/>');
					return false;
				} else {
					$("#keyvalueError").empty();
				}				
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
	
	//to check for duplicate property template name field
	function checkNameUnique(obj) {
		$('input[tempAttr=configNameField]').each(function() {
			if ($(obj).val() != "" && !isBlank($(obj).val())) {
				if ($(obj).attr("id") !== $(this).attr("id")
						&& $(obj).val() === $(this).val()) {
					$(obj).val("");
					showError($("#" + $(this).attr("id")), $("#keyvalueError"),'<s:text name='err.msg.name.unique'/>');
					return false;
				} else {
					$("#keyvalueError").empty();
				}	
			}
		});
	}
	
	//for edit -- to change names of property template input fields
	function updateRowInputNamesInEdit() {
		$(".keywidth").each(function() {
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
		//To disable multiple checkbox and adding possible value while edit if the customer is photon
		if ("photon" == '<%= customerId %>') {
			if (type == "String" || type == "Number") {
				$("#" + id + "_type").parent().parent().find($('input[class*=multiple_chkBox_config]')).attr("disabled", false);				
				$("#" + id + "_type").parent().parent().find($('a[class*=togglePopup]')).attr("href","#myModal");
			} else {
				$("#" + id + "_type").parent().parent().find($('input[class*=multiple_chkBox_config]')).attr("disabled", true);
				$("#" + id + "_type").parent().parent().find($('input[class*=multiple_chkBox_config]')).attr("checked", false);
				$("#" + id + "_type").parent().parent().find($('a[class*=togglePopup]')).removeAttr("href");
			}
		}	
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
		var checkName = true;
		var showNameMissingError = false;
		$(".keywidth").each(function() {
			if ($(this).val().replace(/\s/g, "") === "") {
				//$("#" + $(this).attr("id")).focus();
				$(this).focus();
				redirect = false;
				checkName = false;
				return false;
			}
		});
		
		if (checkName) {
			$('input[tempAttr=configNameField]').each(function() {
				if ($(this).val().replace(/\s/g, "") === "") {
					//$("#" + $(this).attr("id")).focus();
					$(this).focus();
					showNameMissingError = true;
					redirect = false;
					return false;
				}
			});			
		}
		
		var count = 0;
		$(".keywidth").each(function() {
			if($(this).val() === ""){
				count++;
			}
		});
		
		if(count > 0){
			showError($("#" + $(this).attr("id")), $("#keyvalueError"),'<s:text name='err.msg.key.missing'/>');				
			count = 0;
		}else{
			hideError($("#" + $(this).attr("id")), $("#keyvalueError"));
			count = 0;
		}
		
		if (showNameMissingError) {
			showError($("#" + $(this).attr("id")), $("#keyvalueError"),'<s:text name='err.msg.name.propTemp.missing'/>');
		} 
		
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
		
		var value = $('#configname').val().charAt(0).replace(/[0-9]+/, '.');
		if (value.startsWith("-") || value.startsWith(".")) {
			showError($("#nameControl"), $("#nameError"), '<s:text name='err.msg.name.invalid'/>');
		} 
		
		$('.keywidth').each( function() {
			var val = $(this).val().charAt(0).replace(/[0-9]+/, '.');
			if (val.startsWith("-") || val.startsWith(".")) {
				showError($("#keyControl"), $("#keyvalueError"), '<s:text name='err.msg.key.invalid'/>');
			} 
		});
		
		if (redirect) {
			validate(pageUrl, $('#formConfigTempAdd'), $('#subcontainer'),
					progressText, $('#appliesToDiv :input'));
		}
	}
	

	function findError(data) {
		if (!isBlank(data.nameError)) {
			showError($("#nameControl"), $("#nameError"), data.nameError);
		} else {
			hideError($("#nameControl"), $("#nameError"));
		}
		
		if (!isBlank(data.dispError)) {
			showError($("#dispNameControl"), $("#dispNameError"), data.dispError);
		} else {
			hideError($("#dispNameControl"), $("#dispNameError"));
		}
	}
	
	//To check for the special character in Key Value
	$('input[name=propTempKey]').on('input propertychange', function(e) {
		var propTempKey = $(this).val();
		propTempKey = checkForSplChrExceptDot(propTempKey);
		propTempKey = stripSpace(propTempKey);
		$(this).val(propTempKey);
	});
	
	// To check for the special character in configname
	$('#configname').bind('input propertychange', function(e) {
		var configname = $(this).val();
		configname = checkForSplChrExceptDot(configname);
	    configname = stripSpace(configname);
		$(this).val(configname);
	});
	
	// To check for the special character in PossibleValues
	$('#txtCombo').bind('input propertychange', function(e) {
		var configname = $(this).val();
		configname = allowAlphaNum(configname);
		$(this).val(configname);
	});
	
	//to disable multiple checkbox if we select password
	function typeChange(obj) {
		if ($(obj).val() == "String" || $(obj).val() == "Number") {
			$(obj).parent().parent().find($('input[class*=multiple_chkBox_config]')).attr("disabled", false);
			$(obj).parent().parent().find($('a[class*=togglePopup]')).attr("href","#myModal");
		} else {
			$(obj).parent().parent().find($('input[class*=multiple_chkBox_config]')).attr("disabled", true);//To make multiple checkbox disable
			$(obj).parent().parent().find($('input[class*=multiple_chkBox_config]')).attr("checked", false);//To make multiple checkbox unchecked
			$(obj).parent().parent().find($('a[class*=togglePopup]')).removeAttr("href");//To disable adding possible values
			$(obj).parent().parent().find($('input[type=hidden]')).val("");//To empty already added possible values in hidden field
			$(obj).parent().parent().find($('.psblselectwidth option')).remove();//To empty already added possible values
			$(obj).parent().parent().find($('.psbltextwidth')).val('');//To empty already added possible values
		}
	}
</script>