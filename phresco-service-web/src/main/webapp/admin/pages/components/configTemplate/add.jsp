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
	List<Technology> technologies = (List<Technology>) request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
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
		<%
			if (StringUtils.isNotEmpty(fromPage)) {
		%>
			<s:label key="lbl.hdr.comp.cnfigtmplt.edit" theme="simple"/>
		<%
			} else {
		%>
			<s:label key="lbl.hdr.comp.cnfigtmplte.add" theme="simple"/>   
		<%
   			}
   		%>   
	 </h4>	
	
	<div class="content_adder">
		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.name'/>
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
				<select id="multiSelect" multiple="multiple" name="appliesTo">
					<%
						if (CollectionUtils.isNotEmpty(technologies)) {
							for (Technology technology : technologies) {
								String selectedStr = "";
								if (settingsTemplate != null) {
									List<String> appliesTos = settingsTemplate
											.getAppliesTo();
									if (CollectionUtils.isNotEmpty(appliesTos)) {
										if (appliesTos.contains(technology.getId())) {
											selectedStr = "selected";
										} else {
											selectedStr = "";
										}
									}
								}
					%>
					<option value="<%=technology.getId()%>" <%=selectedStr%>><%=technology.getName()%></option>
					<%
						}
						}
					%>
				</select> <span class="help-inline applyerror" id="applyError"></span>
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
									<tr class="1_configdynamiadd">
										<td class="textwidth">
											<input type="text" id="1" value="" placeholder="<s:text name='place.hldr.configTemp.add.key'/>" name="propTempKey"  
												temp="1_key" class="key" onblur="updateRowInputNames(this)" maxlength="30" title="30 Characters only">
										</td>
										<td class="textwidth">
											<select id="1_type" class = "select typewidth">
												<option value="String">String</option>
												<option value="Integer">Integer</option>
												<option value="Password">Password</option>
											</select>
										</td>
										<td class="psblevalue" id="1_psblMulDiv" style="display:none;">
											<select type="text" placeholder="<s:text name='place.hldr.configTemp.add.possible.values'/>" 
												class="propTempTxt psblSelect" id="1_psblMul"></select>
											<input type="hidden" class="1"/>
											<a data-toggle="modal" href="#myModal"><img class="addiconAlign imagealign" temp="1" src="images/add_icon.png"/ 
												onclick="addPsblValPopup(this);"></a>
										</td>
										<td class="psblevalue" id="1_psblSinglDiv">
											<input type="text" placeholder="<s:text name='place.hldr.configTemp.add.possible.values'/>" 
												class="propTempTxt psblSngl" id="1_psblSingl">
											<input type="hidden" class="1"/>
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
								<a href="#" class="btn btn-primary" data-dismiss="modal"><s:label key="lbl.hdr.comp.cancel" theme="simple"/></a>
								<a href="#" id ="ok" class="btn btn-primary" data-dismiss="modal" ><s:label key="lbl.hdr.comp.ok" theme="simple"/></a>
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
		if (StringUtils.isNotEmpty(fromPage)) {
	%>
		<input type="button" id="configtempUpdate" class="btn <%=disabledClass%>" <%=disabled%>
				onclick="validate('configtempUpdate', $('#formConfigTempAdd'), $('#subcontainer'), 'Updating Config Template');" 
		        value="<s:text name='lbl.hdr.comp.update'/>"/>
    <%
    	} else {
    %>		
		<input type="button" id="configtempSave" class="btn btn-primary"
		        onclick="validatePropTempKey();" 
		        value="<s:text name='lbl.hdr.comp.save'/>"/>
	<%
		}
	%> 
		<input type="button" id="configtempCancel" class="btn btn-primary" 
		      onclick="loadContent('configtempList', $('#formConfigTempAdd'), $('#subcontainer'));" 
		      value="<s:text name='lbl.hdr.comp.cancel'/>"/>
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="fromPage" value="<%=StringUtils.isNotEmpty(fromPage) ? fromPage : ""%>"/>
	<input type="hidden" name="oldName" value="<%=name%>"/>
	<input type="hidden" name="customerId" value="<%=customerId%>">
</form>

<script language="javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	if (!isiPad()) {
		$(".content_adder").scrollbars();  
	}

	$(document).ready(function() {
		enableScreen();
		
		// To check for the special character in configname
        $('#configname').bind('input propertychange', function (e) {
            var configname = $(this).val();
            configname = checkForSplChr(configname);
            $(this).val(configname);
        });
     	
		$("#addValues").click(function() {
			var textComboVal = $("#txtCombo").val();
			var alreadyExists = false;
			if ($("#txtCombo").val().trim() === "") {
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
			
			if (!alreadyExists && textComboVal.trim() !== "") {
				$("#txtCombo").val("");
				$("#valuesCombo").append($('<option name='+ textComboVal +'></option>').attr("value", textComboVal).text(textComboVal));				
			}
		});
		
		$("#ok").click(function() {
			var id = $("#hiddenKey").val();
			var length =  $('#valuesCombo option').length;
			var psblValName ="";
			var psblVal = "";
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
	
	var counter = 2;
	function addconfig() {
		var trId = counter + "_configdynamiadd";
		var keyId = counter;
		var keyTmpName = counter+"_key";
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
	 			"<td class='textwidth'><select id='"+ typeId +"' class = 'select typewidth'><option value='String'>String</option> " + 
	 			"<option value='Integer'>Integer</option><option value='Password'>Password</option></select></td>"  +
	 			"<td class='psblevalue' id='"+ psblMulDivId +"' style='display:none;'><select type='text' placeholder='<s:text name='place.hldr.configTemp.add.possible.values'/>'" + 
	 			" class='propTempTxt psblSelect' id='"+ psblValMultipleId +"'></select>"+
	 			"<input type='hidden' class='"+ keyId +"'/><a data-toggle='modal' href='#myModal'> " + 
	 			"<img class='addIcon imagealign' temp='"+ keyId +"' src='images/add_icon.png' onclick='addPsblValPopup(this);'/></a></td>" +
	 			"<td class='psblevalue' id='"+ psblSinglDivId +"'><input type='text' placeholder='<s:text name='place.hldr.configTemp.add.possible.values'/>' " +
	 			" class='propTempTxt psblSngl' id='"+ psblValSingleId +"'>" + 
	 			"<input type='hidden' class='"+ keyId +"'/><a data-toggle='modal' href='#myModal'><img class='addIcon imagealign' temp='"+ keyId +"' " +
	 			"src='images/add_icon.png' onclick='addPsblValPopup(this);'/></a></td>" +
	 			"<td class='hlpText'><input type='text' id='"+ helpTextId +"' placeholder='<s:text name='place.hldr.configTemp.add.help.text'/>' " + 
	 			"name='helpText' class='propTempTxt hlpTxt'></td> <td class='mandatoryfld'><input type='checkbox' value='true' id='"+ mandChckId +"'></td>" +
	 			"<td class='multiplefld'><input type='checkbox' value='true' id='"+ mulChckId +"'></td>" +
	 			"<td class='imagewidth'><a ><img class='add imagealign' temp='"+ keyId +"' src='images/add_icon.png' onclick='addconfig(this);'></a></td>" + 
	 			"<td><img class = 'del imagealign' src='images/minus_icon.png' onclick='removeTag(this);'></td>")
	 	newPropTempRow.appendTo("#propTempTbody");		
		counter++;
	}
	 
	function addPsblValPopup(obj) {
		$("#hiddenKey").val("");
		$("#txtCombo").val("");
		$("#errMsg").empty();
		$("#valuesCombo").empty();
		$("#hiddenKey").val($(obj).attr("temp"));
	}
	
	function removeTag(currentTag) {
		$(currentTag).parent().parent().remove();
	}
	
	function updateRowInputNames(obj) {
		//to check duplications of entered key value
		var keyClass = $(obj).attr("class");
		$("."+keyClass).each(function(){
			if($(obj).attr("temp") !== $(this).attr("temp") && $(obj).val() === $(this).val()) {
				$(obj).val("");
				showError($("#"+ $(obj).attr("id") +"_configdynamiadd"));
				return false;
			}
		});
		
		//to set names of input fields of current prop template row
		if ($(obj).val().trim() !== "") {
			var id = $(obj).attr("id");
			var typeName =  $(obj).val() + "_type";
		 	var psblVal = $(obj).val() + "_psblVal";
		 	var helpTextName =  $(obj).val() + "_helpText";
		 	var mandChckName =  $(obj).val() + "_propMand";
			var mulChckName =  $(obj).val() + "_propMul";
			
			$("#"+ id +"_type").attr("name", typeName);
			$("."+id).attr("name", psblVal);
			$("#"+ id +"_helpText").attr("name", helpTextName);
			$("#"+ id +"_propMand").attr("name", mandChckName);
			$("#"+ id +"_propMul").attr("name", mulChckName);
		} 
	}
	
	function validatePropTempKey() {
		var redirect = true;
		$(".key").each(function() {
			if ($(this).val().trim() === "") {
				$("#"+$(this).attr("id")).focus();
				redirect = false;
				return false;
			} 
		});
		if ($("#configname").val().trim() === "" ) {
			showError($("#nameControl"), $("#nameError"), '<s:text name='err.msg.name.empty'/>');
		} else {
			hideError($("#nameControl"), $("#nameError"));
		}
		if ($("#multiSelect :selected").length === 0) {
			showError($("#applyControl"), $("#applyError"), '<s:text name='err.msg.applies.empty'/>');
		} else {
			hideError($("#applyControl"), $("#applyError"));
		}
		
		if (redirect) {
			validate('configtempSave', $('#formConfigTempAdd'), $('#subcontainer'), 'Creating Config Template');
		} 
	}
</script>