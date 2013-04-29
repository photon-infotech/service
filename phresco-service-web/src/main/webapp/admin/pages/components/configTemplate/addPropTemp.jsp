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

<%@ page import="java.util.List" %>

<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<%
 	String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
	String propTempKey = (String) request.getAttribute("propTempKey"); 
	System.out.println("KEY "+propTempKey);
%>
 
 <form id="addPropTemp" name="addPropTempForm" class="form-horizontal customer_list">
	<div class="control-group" id="keyControl">
		<label class="control-label labelbold">
			<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.cnfigtmplt.key.title'/>
		</label>
		
		<div class="controls">
			<input id="key" placeholder="<s:text name='place.hldr.configTemp.add.key'/>" class="input-medium" 
				type="text" name="key" value="" maxlength="30" title="30 Characters only" >
			<span class="help-inline" id="keyError"></span>
		</div>
	</div>
	
	<div class="control-group" id="nameControl">
		<label class="control-label labelbold">
			<span class="mandatory">*</span>&nbsp;<s:text name='lbl.name'/>
		</label>
		
		<div class="controls">
			<input id="name" placeholder="<s:text name='place.hldr.configTemp.add.name'/>" class="input-medium" 
				type="text" name="name"  value="" maxlength="30" title="30 Characters only" >
			<span class="help-inline" id="nameError"></span>
		</div>
	</div>
	
	<div class="control-group" id="selectBoxControl">
		<label class="control-label labelbold">
			<s:text name='lbl.hdr.comp.cnfigtmplt.type.title'/>
		</label>
		
		<div class="controls">
			<select id="type" class = "input-medium propType" onchange="typeChange(this);">
				<option value="String"><s:text name='lbl.hdr.comp.cnfigtmplt.string'/></option>
				<option value="Number"><s:text name='lbl.hdr.comp.cnfigtmplt.number'/></option>
				<option value="Password"><s:text name='lbl.hdr.comp.cnfigtmplt.password'/></option>
				<option value="FileType"><s:text name='lbl.hdr.comp.cnfigtmplt.filetype'/></option>
				<option value="Boolean"><s:text name='lbl.hdr.comp.cnfigtmplt.boolean'/></option>
				<option value="Actions"><s:text name='lbl.hdr.comp.cnfigtmplt.actions'/></option>
				<option value="Scheduler"><s:text name='lbl.hdr.comp.cnfigtmplt.scheduler'/></option>
			</select>
		</div>
	</div>
	
	<div class="control-group" id="helpTextControl">
		<label class="control-label labelbold">
			<s:text name='lbl.hdr.comp.cnfigtmplt.helptext.title'/>
		</label>
		
		<div class="controls">
			<input id="helpText" placeholder="<s:text name='place.hldr.cust.add.help.text'/>" class="input-medium" 
				type="text" name="helpText"  value="" maxlength="30" title="30 Characters only" >
			<span class="help-inline" id="helpTextError"></span>
		</div>
	</div>
	
	<div id="posblDiv">
		
		<div class="control-group" id="addPosblTextControl">
			<label class="control-label labelbold">
				<s:text name='Enter Key Value'/>
			</label>
			
			<div class="controls">
				<input id="addPosblText" placeholder="<s:text name='place.hldr.configTemp.add.possible.values'/>" 
					type="text" name="addPosblText" class="input-medium" value="" maxlength="30" title="30 Characters only" />
				<span class="help-inline" id="addPosblTextError"></span>
				<input type="button" id="add" class="btn btn-primary" value="Add" onclick="addPsblValues();"/>
			</div>
		</div>
	
		<div class="control-group" id="possblValControl">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.cnfigtmplt.psblvalue.title'/>
			</label>
			
			<div class="controls">
				<select id="posblVal" multiple='multiple' >
				</select>
				<div class="popopimage">
					<img src="images/up_arrow.jpeg" title="Move up" id="up" class="imageup"><br>
					<img src="images/remove.jpeg" title="Remove" id="remove" class="imageremove"><br>
					<img src="images/down_arrow.png" title="Move down" id="down" class="imagedown" >
				</div>
			</div>
		</div>
	</div>
	
	<div class="control-group" id="multipleControl">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.cnfigtmplt.mltpl.title'/>
			</label>
			
			<div class="controls">
				<input type="checkbox" class="multiple_chkBox_config" id='multiple' value="false">
			</div>
	</div>
	
	<div class="control-group" id="mandatoryControl">
		<label class="control-label labelbold">
			<s:text name='lbl.hdr.comp.cnfigtmplt.mndtry.title'/>
		</label>
		
		<div class="controls">
			<input type="checkbox" class="multiple_chkBox_config" id='mandatory' value="false">
		</div>
	</div>
	<!-- Hidden Fields -->
	<input type="hidden" name="popupFromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
</form>

<script language="javascript">
	var fieldId = '<%= propTempKey %>';
	var value = $('#' + fieldId).val();
	var oldKey="";
	var oldName="";
	var selectedType = $('#type').find('option:selected').val();
	loadDiv(selectedType);
	
	$(document).ready(function() {
		$('#clipboard').hide();
		var popupFromPage = '<%= fromPage %>';
		hidePopuploadingIcon();
		$('.errMsg').html("");
		
		if (value != null) {
			var jsonObj = JSON.parse(value);
			if (jsonObj != null) {
				$('#key').val(jsonObj.key);
				$('#name').val(jsonObj.name);
				$('.propType').val(jsonObj.type);
				$('#helpText').val(jsonObj.helpText);
				var psblValues = jsonObj.possibleValues;
				for ( var i = 0; i < psblValues.length; i++) {
					$('#posblVal').append($('<option>', {
						value : psblValues[i],
						text : psblValues[i]
					}));
				}
				if (jsonObj.multiple == "true") {
					$('#multiple').prop('checked', true);
				} else {
					$('#multiple').prop('checked', false);
				}
				if (jsonObj.required == "true") {
					$('#mandatory').prop('checked', true);
				} else {
					$('#mandatory').prop('checked', false);
				}
			}
			oldKey = jsonObj.key;
			oldName = jsonObj.name;
		}
	
		//remove possible value entry
		$('.imageremove').click(function() {
			if ($('#posblVal option:selected').size() == 0) {
				$(".errMsg").text('<s:text name='err.msg.select.key'/>');
			} else {
				$(".errMsg").empty();
				$('#posblVal option:selected').each( function() {
					$(this).remove();
				});
			}	
			showHideMultiple();
		});
			
		//To move up the values
		$('.imageup').bind('click', function() {
			$('#posblVal option:selected').each( function() {
				var newPos = $('#posblVal  option').index(this) - 1;
				if (newPos > -1) {
					$('#posblVal  option').eq(newPos).before("<option value='"+$(this).val()+"' selected='selected'>"+$(this).text()+"</option>");
					$(this).remove();
				}
			});
		});
		
		//To move down the values
		$('.imagedown').bind('click', function() {
			var countOptions = $('#posblVal option').size();
			$('#posblVal option:selected').each( function() {
				var newPos = $('#posblVal  option').index(this) + 1;
				if (newPos < countOptions) {
					$('#posblVal  option').eq(newPos).after("<option value='"+$(this).val()+"' selected='selected'>"+$(this).text()+"</option>");
					$(this).remove();
				}
			});
		});
			
		//change value of chkboxs
		$('#multiple').click(function(){
			changeChckBoxValue(this);
		});
		
		$('#mandatory').click(function(){
			changeChckBoxValue(this);
		});
			
		//To check for the special character in Key Value
		$('input[name=key]').live('input propertychange', function(e) {
			var propTempKey = $(this).val();
			propTempKey = checkForSplChrExceptDot(propTempKey);
			propTempKey = stripSpace(propTempKey);
			$(this).val(propTempKey);
		});
			
		// To check for the special character in PossibleValues
		$('#addPosblText').bind('input propertychange', function(e) {
			var configname = $(this).val();
			configname = allowAlphaNum(configname);
			$(this).val(configname);
		});
			
		//to empty possblevalues while changing type
		$('#type').change(function() {
			$('#addPosblText').val("");
			$('#mySelect').find('option').remove();
			$('#posblVal option').each( function() {
				$(this).remove();
			});
			$('#multiple').attr("checked", false);
			showHideMultiple();
		});
		$('#key').focus();
		showHideMultiple();
	});
		

	//Show Divs as per type
	function typeChange(obj) {
		var selectedType = $('#type').find('option:selected').val();
		loadDiv(selectedType);
	}
	
	//show/hide multiple chkbox
	function showHideMultiple() {
		var size = $('#posblVal option').size();
		if(size > 1) {
			$('#multipleControl').show();
		} else {
			$('#multipleControl').hide();
		}
	}
	
	//load appropriate div
	function loadDiv(type) {
		if (type === 'String' || type === 'Number') {
			$('#posblDiv').show();
			$('#multipleControl').hide();
		} else if (type === 'FileType') {
			$('#posblDiv').hide();
			$('#multipleControl').hide();
		} else {
			$('#posblDiv').hide();
			$('#multipleControl').hide();
		}
	}

	//add possible values
	function addPsblValues() {
		var newOption = $('#addPosblText').val();
		var flag = false;
		if (newOption === "") {
			$('.errMsg').html("Please Enter Valid Key Value");
			flag = true;
		} else {
			$('#posblVal').find('option').each(function() {
				var opts = $(this).val();
				if (opts === newOption) {
					flag = true;
					$('.errMsg').html("Key Value already Exists");
				}
			});
		}
		if (flag == false) {
			$('#posblVal').append($('<option>', {
				value : newOption,
				text : newOption
			}));
			$('#addPosblText').val("");
			$('.errMsg').html("");
		}
		showHideMultiple();
	}
	
	//key and name validation @ ok
	function validatePropTemplates() {
		var key = $('#key').val();
		var val = key.charAt(0).replace(/[0-9]+/, '.');
		if (val === "-" || val === "." || key === "") {
			$('.errMsg').html('<s:text name='err.msg.key.invalid'/>');
			return false;
		} 
		var name = $('#name').val();
		if(name === "") {
			$('.errMsg').html('<s:text name='err.msg.name.empty'/>');
			return false;
		}
		
		return true;
	}
</script>  
