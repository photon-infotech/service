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
<%@page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>
<%@page import="com.photon.phresco.util.ServiceConstants"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.TechnologyGroup"%>
<%@ page import="com.photon.phresco.commons.model.ApplicationType"%>
<%@ page import="com.photon.phresco.commons.model.Technology"%>

<%
	List<ApplicationType> appTypes = (List<ApplicationType>)request.getAttribute(ServiceUIConstants.REQ_APP_TYPES);
    Gson gson = new Gson();
   
    String versioning = (String)request.getAttribute(ServiceUIConstants.REQ_VERSIONING);
	String disabledVer ="";
	if (StringUtils.isNotEmpty(versioning)) {
		disabledVer = "disabled";
	}
%>


<form id="formTechgroup" class="form-horizontal">
	<div class="control-group">
		<label class="control-label labelbold modallbl-color">
			<span class="mandatory">*</span>&nbsp;<s:text name='lbl.name' />
		</label>
		<div class="controls">
			<input type="text" name="techGroupName" id="techGroupName" class="span3"  placeholder="<s:text name='place.hldr.TechGroup.name'/>" 
			maxlength="30" title="<s:text name='title.30.chars'/>" />
		</div>
	</div>
	
	<div class="control-group" id="techappControl">
			<label class="control-label labelbold modallbl-color"> 
			<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.apptype' />
			</label>
			<div class="controls">
				<select id="appTypeLayer" name="apptype">
					<%
						if (CollectionUtils.isNotEmpty(appTypes)) {
							for (ApplicationType appType : appTypes) {
								String selectedStr= "";
					%>
							<option <%= disabledVer %> value="<%= appType.getId() %>" <%= selectedStr %>><%= appType.getName() %></option>
					<%
							}
						}
					%>
				</select> 
				<span class="help-inline" id="appError"></span>
			</div>
		</div>
		
	<div class="control-group">
		<label class="control-label labelbold modallbl-color">
			<s:text name='lbl.desc'/>
		</label>
		<div class="controls">
			<textarea name="techDesc" id="techGroDesc" class="input-xlarge" 
				 maxlength="150" title="<s:text name='title.150.chars'/>" placeholder="<s:text name='place.hldr.techgroup.desc'/>"></textarea>
			<input type="button" value="<s:text name='lbl.hdr.component.add'/>" tabindex=3 id="add" class="btn btn-primary addButton">
		</div>
	</div>

	<fieldset class="popup-fieldset">
		<legend class="fieldSetLegend" ><s:text name="lbl.added.technologygroup"/></legend>
		<div class="popupTypeFields" id="typefield">
            <div class="multilist-scroller multiselect" id='multiTechGroup'>
                <ul>
	       			<li>
					</li>
				</ul>
            </div>
		</div>
		<div class="deleteButton">
			<input type="button" value="<s:text name='lbl.btn.del' />" tabindex=5 id="removeTechGroup" class="btn btn-primary">
		</div>
	</fieldset>
</form>


<script language="JavaScript" type="text/javascript">
	var techGroupToAdd = [];
	$(document).ready(function() {
		getTechGroup();
		$('#popup_div').hide();
		$('#popupTitle').html("Technology Group"); 
		$('#popupClose').hide();
		$('.popupOk').attr("onclick","popupOnOk(this)");
		$('#popupPage').css({"width":"590px","position":"relative","left":"49%"});
		$('.modal-body').css("height","270px");
		$('#clipboard').hide();
		$('.popupOk, #popupCancel').show(); // show ok & cancel button
		$('.modal-body').html($("#formTechgroup"));
		$('.popupOk').attr('id', "techGroupOk");
		$('.borderBottom-none').attr('id', "tableAdd");
		$('#reportMsg').html("");
		$('#popupPage').modal({
			show: true
		}); 
		
		$('#techGroupOk').click(function() {
			$('#formTechgroupn').hide();
			$('#popup_div').hide();
			hideLoadingIcon();
		});
		
		$("#appTypeLayer").change(function() {
			techGroupToAdd = [];
			getTechGroup();
        });  
		
		$('#popupCancel').click(function() {
			$('#popup_div').empty();
			showParentPage();
		});
		
		$('#add').click(function() {
			$('#reportMsg').html("");
			var returnValue = true;
			name = $('#techGroupName').val();
			appTypeId = $('#techappControl :selected').text();
			desc = $("#techGroDesc").val();
			if (name == "") {
				$("#reportMsg").html("<s:text name='enter.technology.name'/>");
				$("#techGroupName").focus();
				$("#techGroupName").val("");
				returnValue = false;
			} else {
				$('#multiTechGroup ul li input[type=checkbox]').each(function() {
					var jsonData = $(this).val();
					var techGrou = $.parseJSON(jsonData);
					var techName = techGrou.name;
					var techType = techGrou.appTypeId;
					if (name.trim().toLowerCase() == techName.trim().toLowerCase()) {
						$("#reportMsg").html("<s:text name='technology.name.already.exists'/>");
						returnValue = false;
						return false;
					} 
				});
			}
			if (returnValue) {
				addRow();
				$("#techGroupName").val("");
				$("#techGroDesc").val("");
			}
		});
	});
	
	//To remove the added Technology value
    $('#removeTechGroup').click(function() {
    	 selectTech();
		//To remove the Technologies from the list box which is not in the XML
        $('#multiTechGroup ul li input[type=checkbox]:checked').each( function() {
			removeItem(techGroupToAdd, $(this).val())
			var params = "removeTechGroup=";
			params = params.concat($.parseJSON($(this).val()).id);
			loadContent('deleteTechGroup', $('#formTechgroup'), '', params, false);
			$('#multiTechGroup ul li input[type=checkbox]:checked').parent().remove();
        });
    });
	
    function removeItem(array, item) {
        for (var i in array) {
            if (array[i]==item) {
                array.splice(i,1);
                break;
			}
        }
    }
    
    function getTechGroup() {
		loadContent('getTechGroup', $('#formTechgroup'), '', '', true);
		$("#multiTechGroup ul").empty();
    }
  
	function successEvent(pageUrl, data) {
		if (pageUrl == "getTechGroup") {
			var techGroups = data.appTypeTechGroups;
			for (i in techGroups) {
				var id = techGroups[i].id;
				var name = techGroups[i].name;
				var system = techGroups[i].system;
				var description = techGroups[i].description;
				var appTypeId = techGroups[i].appTypeId;
				var checkValue = '{"name": "' + name + '", "id": "' + id + '", "description": "' + description + '", "appTypeId": "' + appTypeId + '", "system": "' + system + '"}';
				if (system) {
					var checkbox = '<input type="checkbox" name="groupTech" class="techCheck" value=\'' + checkValue + '\' title="' + description + '"  disabled/>' + name;
				}else {
					var checkbox = '<input type="checkbox" name="groupTech" class="techCheck" value=\'' + checkValue + '\' title="' + description + '" />' + name;
				}
				$("#multiTechGroup ul").append('<li>' + checkbox + '</li>');
			}
		}
	}
	
	function addRow() {
		var value = $('#techGroupName').val();
		var desc = $('#techGroDesc').val();
		var appTypeId = $('#techappControl :selected').val();
		var checkValue = '{"name": "' + value + '", "description": "' + desc + '", "appTypeId": "' + appTypeId + '", "system": "' + false + '"}';
		var checkbox = '<input type="checkbox" name="groupTech" class="techCheck" value=\'' + checkValue + '\' title="' + desc + '" />' + value;
		if ($("#multiTechGroup ul").has("li").length === 0) {
			$("#multiTechGroup ul").append('<li>' + checkbox + '</li>');
		} else {
			$("#multiTechGroup ul li:last").after('<li>' + checkbox + '</li>');
		}
		techGroupToAdd.push(checkValue);
// 		$("#multiTechGroup ul li:last").after('<li>' + checkbox + '</li>');
	}
	 
	function selectTech() {
		var checkedTechSize = $('#multiTechGroup :checked').size();
		if (checkedTechSize < 1) {
			$("#reportMsg").html("<s:text name='please.select.one.technology'/>");
			return false;
		}
	}
</script>