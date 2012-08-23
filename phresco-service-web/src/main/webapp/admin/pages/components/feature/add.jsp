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

<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.model.Module" %>
<%@ page import="com.photon.phresco.model.ModuleGroup" %>
<%@ page import="com.photon.phresco.model.Technology" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>

<%
	ModuleGroup moduleGroup = (ModuleGroup)request.getAttribute(ServiceUIConstants.REQ_MODULE_GROUP); 
    List<Technology> technologys = (List<Technology>)request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
    String customerId = (String) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER_ID);
    String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
    
  	//For edit
    String name = "";
    String description = "";
    String helpText = "";
    String version = "";
	if (moduleGroup != null) {
		if (StringUtils.isNotEmpty(moduleGroup.getName())) {
			name = moduleGroup.getName();
		}
		if (StringUtils.isNotEmpty(moduleGroup.getDescription())) {
			description = moduleGroup.getDescription();
		}
		if (CollectionUtils.isNotEmpty(moduleGroup.getVersions())) {
			List<Module> versions = moduleGroup.getVersions();
			if (CollectionUtils.isNotEmpty(versions)) {
				for (Module moduleVersion : versions) {
					version = moduleVersion.getVersion();
				}
			}
		}
		if (StringUtils.isNotEmpty(moduleGroup.getHelpText())) {
			helpText = moduleGroup.getHelpText();
		}
	}
%>

<form id="formFeatureAdd" class="form-horizontal customer_list" method="post" enctype="multipart/form-data">
	<h4 class="hdr"><s:label key="lbl.hdr.comp.featrs.title" theme="simple"/></h4>	
	<div class="content_adder">

		<div class="control-group" id="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.name'/>
			</label>
			<div class="controls">
				<input id="input01" placeholder="<s:text name='place.hldr.feature.add.name'/>" 
				     class="input-xlarge" type="text" name="name" value="<%= name %>">
				<span class="help-inline" id="nameError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.desc'/>
			</label>
			<div class="controls">
				<input id="input01" placeholder="<s:text name='place.hldr.feature.add.desc'/>" 
				     class="input-xlarge" type="text" name="description" value="<%= description %>">
			</div>
		</div>
		
		<div class="control-group" id="verControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.version'/>
			</label>
			<div class="controls">
				<input id="input01" placeholder="<s:text name='place.hldr.feature.add.version'/>" 
				     class="input-xlarge" type="text" name="version" value="<%= version %>">
				<span class="help-inline" id="verError"></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.help'/>
			</label>
			<div class="controls">
				<textarea id="input01" placeholder="<s:text name='place.hldr.feature.add.help.text'/>" 
				               class="input-xlarge" value="<%= helpText %>" rows="2" cols="10" ></textarea>
			</div>
		</div>
		
		<div class="control-group" id="fileControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.file'/>
			</label>
			
			 <div class="controls" style="float: left; margin-left: 3%;">
				<div id="feature-file-uploader" class="file-uploader">
					<noscript>
						<p>Please enable JavaScript to use file uploader.</p>
						<!-- or put a simple form for upload here -->
					</noscript>
				</div>
			</div>
			 <span class="help-inline fileError" id="fileError"></span>
		</div>
		
		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.dependency'/>
			</label>
			<div class="controls">
				<a data-toggle="modal" href="#myModal"><input type="button" 
				        class="btn btn-primary addiconAlign" value="Select Dependency"></a>
			</div>
		</div>
	</div>
	
	<div id="myModal" class="modal hide fade">
		<div class="modal-header">
		  <a class="close" data-dismiss="modal" >&times;</a>
		  <h3><s:label key="lbl.hdr.comp.featr.popup.title" theme="simple"/></h3>
		</div>
		<div class="modal-body">
			<div class="control-group">
				<div class="external_features_wrapper">
					<div class="theme_accordion_container popupaccord" id="coremodule_accordion_container">
						<section class="accordion_panel_wid">
							<div class="accordion_panel_inner">
								<section class="lft_menus_container">
									<span class="siteaccordion">
										<span>
											<input type="checkbox" name="loginForm" id="">&nbsp;&nbsp;Login form 
											<p id="loginForm" class="accord_veralign"></p>
										</span>
									</span>
									<div class="mfbox siteinnertooltiptxt">
										<div class="scrollpanel">
											<section class="scrollpanel_inner">
												<table class="download_tbl">
													<thead>
														<tr>
															<th></th>
															<th class="accordiantable modallbl-color"><s:label key="lbl.hdr.cmp.name" theme="simple"/></th>
															<th class="accordiantable modallbl-color"><s:label key="lbl.hdr.comp.ver" theme="simple"/></th>
														</tr>
													</thead>
																										
													<tbody>
														<tr>
															<td class="editFeatures_td1">
																<input type="radio" class="" name="loginForm" value="2.0">
															</td>
															<td class="editFeatures_td2">
																<div class="accordalign"></div>
																<a href="#" name="ModuleDesc" class="modallbl-color">Login form</a>
															</td>
															<td class="editFeatures_td4 modallbl-color">2.0</td>
														</tr>
													</tbody>
												</table>
											</section>
										</div>
									</div>
								</section>  
							</div>
						</section>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
		  <a href="#" class="btn btn-primary" data-dismiss="modal"><s:label key="lbl.hdr.comp.cancel" theme="simple"/></a>
		  <a href="#" class="btn btn-primary" data-dismiss="modal" ><s:label key="lbl.hdr.comp.ok" theme="simple"/></a>
		</div>
	</div>
	
	<div class="bottom_button">
     	<input type="button" id="featuresSave" class="btn btn-primary" value="<s:text name='lbl.hdr.comp.save'/>" 
			onclick="validate('featuresSave', $('#formFeatureAdd'), $('#subcontainer'), 'Creating Feature');"/> 
		<input type="button" id="featuresCancel" class="btn btn-primary" value="<s:text name='lbl.hdr.comp.cancel'/>"
			onclick="loadContent('featuresList', $('#formFeatureAdd'), $('#subcontainer'));" />
	</div>
	
	<!-- Hidden Fields -->
	<input type="hidden" name="customerId" value="<%= customerId %>"> 
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
    <input type="hidden" name="techId" value="<%= moduleGroup != null ? moduleGroup.getId() : "" %>"/>
    <input type="hidden" name="oldName" value="<%= moduleGroup != null ? moduleGroup.getName() : "" %>"/>
</form>

<script type="text/javascript">
	$(document).ready(function() {
	    createUploader();
		enableScreen();
       
		$("input[type=radio]").change(function() {
	        var name = $(this).attr('name');
	        $("input:checkbox[name='" + name + "']").prop("checked", true);
	        var version = $("input:radio[name='" + name + "']").val();
	        $("p[id='" + name + "']").html(version);
	    });

	    $("input[type=checkbox]").change(function() {
	        var checkboxChecked = $(this).is(":checked");
	        var name = $(this).attr('name');
	        if (!checkboxChecked) {
	            $("input:radio[name='" + name + "']").prop("checked", false);
	            $("p[id='" + name + "']").empty();
	        } else {
	            $("input:radio[name='" + name + "']:first").prop("checked", true);
	            var version = $("input:radio[name='" + name + "']").val();
	            $("p[id='" + name + "']").html(version);
	        }
	    });
	});

    function findError(data) {
        if (data.nameError != undefined) {
            showError($("#nameControl"), $("#nameError"), data.nameError);
        } else {
            hideError($("#nameControl"), $("#nameError"));
        }
        
        if (data.versError != undefined) {
            showError($("#verControl"), $("#verError"), data.versError);
        } else {
            hideError($("#verControl"), $("#verError"));
        }
        
        if (data.fileError != undefined) {
            showError($("#fileControl"), $("#fileError"), data.fileError);
        } else {
            hideError($("#fileControl"), $("#fileError"));
        }
    }
    
	 function applnJarError(data) {
		if (data != undefined) {
			showError($("#fileControl"), $("#fileError"), data);
		} else {
			hideError($("#fileControl"), $("#fileError"));
		}
	} 

	$("input[type=radio]").change(function() {
		var name = $(this).attr('name');
		$("input:checkbox[name='" + name + "']").prop("checked", true);
		var version = $("input:radio[name='" + name + "']").val();
		$("p[id='" + name + "']").html(version);
	});

	$("input[type=checkbox]").change(function() {
		var checkboxChecked = $(this).is(":checked");
		var name = $(this).attr('name');
		if (!checkboxChecked) {
			$("input:radio[name='" + name + "']").prop("checked", false);
			$("p[id='" + name + "']").empty();
		} else {
			$("input:radio[name='" + name + "']:first").prop("checked", true);
			var version = $("input:radio[name='" + name + "']").val();
			$("p[id='" + name + "']").html(version);
		}
	});

	 function createUploader() {
		var featureUploader = new qq.FileUploader({
			element : document.getElementById('feature-file-uploader'),
			action : 'uploadJar',
			multiple : false,
			type : 'applnJar',
			buttonLabel : '<s:label key="lbl.comp.featr.upload" />',
			typeError : '<s:text name="err.invalid.file.selection" />',
			params : {
				type : 'applnJar'
			},
			debug : true
		});
	}
 
	 function removeUploadedJar(obj) {
		$(obj).parent().remove();
		var params = "uploadedJar=";
		params = params.concat($(obj).attr("id"));
		params = params.concat("&type=");
		params = params.concat($(obj).attr("tempattr"));
		$.ajax({
			url : "removeFeatureJar",
			data : params,
			type : "POST",
			success : function(data) {
			}
		});
		enableDisableUpload();
		//pluginJarError();
		applnJarError();
	}

	function enableDisableUpload() {
		if ($('ul[temp="applnJar"] > li').length === 1) {
			$('#feature-file-uploader').find("input[type='file']").attr(
					'disabled', 'disabled');
			$('#feature-file-uploader').find($(".qq-upload-button")).removeClass(
					"btn-primary qq-upload-button").addClass("disabled");
		} else {
			$('#feature-file-uploader').find("input[type='file']").attr(
					'disabled', false);
			$('#feature-file-uploader').find($(".btn")).removeClass("disabled")
					.addClass("btn-primary qq-upload-button");
		}
	} 
</script>