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

<%@ taglib uri="/struts-tags" prefix="s"%>

<%@ page import="java.util.List"%>

<%@ page import="com.google.gson.Gson"%>

<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.TechnologyOptions"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>

<%
	Gson gson = new Gson();
	List<TechnologyOptions> options = (List<TechnologyOptions>)request.getAttribute(ServiceUIConstants.REQ_TECHNOLOGY_OPTION);
   
	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "btn-primary";
	if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_ARCHETYPES)) {
		per_disabledStr = "disabled";
		per_disabledClass = "btn-disabled";
	}
%>

<form id="formAppFeatures" class="form-horizontal">
	<div class="control-group">
		<label class="control-label labelbold modallbl-color">
			<span class="mandatory">*</span>&nbsp;<s:text name='lbl.name' />
		</label>
		<div class="controls">
			<input type="text" name="appFeatureName" id="appFeatureName" class="span3"  placeholder="<s:text name='place.hldr.appfeature.name'/>" 
			maxlength="30" title="<s:text name='title.30.chars'/>" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label labelbold modallbl-color">
			<span class="mandatory">*</span>&nbsp;<s:text name='lbl.disp.name' />
		</label>
		<div class="controls">
			<input type="text" name="appFeatureDispName" id="appFeatureDispName" class="span3"  placeholder="<s:text name='place.hldr.appfeature.disp.name'/>" 
			maxlength="30" title="<s:text name='title.30.chars'/>" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label labelbold modallbl-color">
			<s:text name='lbl.desc'/>
		</label>
		<div class="controls">
			<textarea name="appFeatureDesc" id="appFeatureDesc" class="input-xlarge" 
				 maxlength="150" title="<s:text name='title.150.chars'/>" placeholder="<s:text name='place.hldr.appfeature.desc'/>"></textarea>
			<input type="button" tabindex=3 id="addAppFeaturesButton" class="btn <%= per_disabledClass %> addButton" <%= per_disabledStr %>
				value="<s:text name='lbl.hdr.component.add'/>">
		</div>
	</div>

	<fieldset class="popup-fieldset">
		<legend class="fieldSetLegend" ><s:text name="lbl.comp.app.features"/></legend>
		<div class="popupTypeFields" id="typefield">
            <div class="multilist-scroller multiselect" id='multiAppFeatures'>
                <ul>
                	<%
						if (CollectionUtils.isNotEmpty(options)) {
							for (TechnologyOptions option : options) {
								String disabledStr = "";
								if (option.isSystem()) {
									disabledStr = "disabled";
								}
					%>
			                	<li>
			                		<input type="checkbox" name="appFeatures" value='<%= gson.toJson(option) %>' class="check appFeatures" <%= disabledStr %>><%= option.getOption() %>
								</li>
					<%
							}
						}
					%>
				</ul>
            </div>
		</div>
		<div class="deleteButton">
			<input type="button" tabindex=5 id="removeAppFeatures" class="btn btn-disabled" value="<s:text name='lbl.btn.del'/>" disabled>
		</div>
	</fieldset>
</form>


<script type="text/javascript">
	var techGroupToAdd = [];
	$(document).ready(function() {
		$('#addAppFeaturesButton').click(function() {
			$('#reportMsg').html("");
			var returnValue = true;
			var name = $('#appFeatureName').val();
			var dispName = $('#appFeatureDispName').val();
			var desc = $("#appFeatureDesc").val();
			if (name == "") {
				$("#reportMsg").html("<s:text name='err.msg.feature.name.empty'/>");
				$("#appFeatureName").focus();
				$("#appFeatureName").val("");
				returnValue = false;
			} else if (dispName == "") {
				$("#reportMsg").html("<s:text name='err.msg.feature.dispname.empty'/>");
				$("#appFeatureDispName").focus();
				$("#appFeatureDispName").val("");
				returnValue = false;
			} else {
				$('#multiAppFeatures ul li input[type=checkbox]').each(function() {
					var techOption = $.parseJSON($(this).val());
					var optionName = techOption.id;
					var optionDispName = techOption.option;
					if (name.trim().toLowerCase() == optionName.trim().toLowerCase()) {
						$("#reportMsg").html("<s:text name='err.msg.feature.name.duplicate'/>");
						returnValue = false;
						return false;
					} else if (name.trim().toLowerCase() == optionDispName.trim().toLowerCase()) {
						$("#reportMsg").html("<s:text name='err.msg.feature.dispname.duplicate'/>");
						returnValue = false;
						return false;
					}
				});
			}
			if (returnValue) {
				$('#appFeaturesOk').attr("disabled", false);
				$("#appFeaturesOk").addClass("btn-primary");
				addRow();
				$("#appFeatureName").val("");
				$("#appFeatureDispName").val("");
				$("#appFeatureDesc").val("");
			}
		});
		
		$("input[name=appFeatures]").live("click", function() {
			if ($("input[name=appFeatures]:checked").length > 0) {
				$("#removeAppFeatures").attr("disabled", false).addClass("btn-primary").removeClass("btn-disabled");
			} else {
				$("#removeAppFeatures").attr("disabled", true).addClass("btn-disabled").removeClass("btn-primary");
			}
		});
		
		//To remove the added Technology value
	    $('#removeAppFeatures').click(function() {
	    	var appFeatureIds = [];
	        $('#multiAppFeatures ul li input[type=checkbox]:checked').each( function() {
	        	if ($(this).attr("new") !== "true") {
	        		appFeatureIds.push($.parseJSON($(this).val()).id);
	        	}
				$(this).parent().remove();
	        });
	        if (appFeatureIds.length > 0) {
	        	var params = "appFeatures=" + appFeatureIds;
	            loadContent('deleteAppFeatures', '', '', params, false);
	            $('#appFeaturesOk').attr("disabled", false);
	    		$("#appFeaturesOk").addClass("btn-primary");        	
	        }
	    });
		
		$('#appFeatureName').focus();
	});
	
	function addRow() {
		var name = $('#appFeatureName').val();
		var dispName = $('#appFeatureDispName').val();
		var desc = $("#appFeatureDesc").val();
		var checkValue = '{"id": "' + name + '", "description": "' + desc + '", "option": "' + dispName + '", "system": "' + false + '"}';
		var checkbox = '<input type="checkbox" new="true" name="appFeatures" class="techCheck" value=\'' + checkValue + '\' title="' + desc + '" />' + dispName;
		if ($("#multiAppFeatures ul").has("li").length === 0) {
			$("#multiAppFeatures ul").append('<li>' + checkbox + '</li>');
		} else {
			$("#multiAppFeatures ul li:first").before('<li>' + checkbox + '</li>');
		}
	}
</script>