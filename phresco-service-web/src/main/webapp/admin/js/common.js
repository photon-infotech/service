/*
 * ###
 * Service Web Archive
 * 
 * Copyright (C) 1999 - 2012 Photon Infotech Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ###
 */
var jars = [];

function clickMenu(menu, tag, form) {
	
	menu.click(function() {
		showLoadingIcon();
		inActivateAllMenu(menu);
		activateMenu($(this));
		var selectedMenu = $(this).attr("id");
		var additionalParam = $(this).attr('additionalParam');
		loadContent(selectedMenu, form, tag, additionalParam);
	});
}


function copyToClipboard(data) {
    var params = "copyToClipboard=";
    params = params.concat(data);
    loadContent('copyToClipboard','','', params,'');
}

function clickButton(button, tag) {
	button.click(function() {
		var selectedMenu = $(this).attr("id");
		loadContent(selectedMenu, '', tag);
	});
}

function loadJsonContent(url, jsonParam, containerTag) {
	$.ajax({
		url : url,
		data : jsonParam,
		type : "POST",
		contentType: "application/json; charset=utf-8",
		success : function(data) {
			if (containerTag != undefined) {
				loadData(data, containerTag);
			}
		}
	});	
}

function loadContent(pageUrl, form, tag, additionalParams, callSuccessEvent) {
	if (tag != undefined && tag != "" && !isBlank(tag)) {
		showLoadingIcon();
	}
	var params = "";
	if (form != undefined && form != "" && !isBlank(form.serialize())) {
		params = form.serialize();
		if (!isBlank(additionalParams)) {
			params = params.concat("&");
			params = params.concat(additionalParams);	
		} 
	} else if (additionalParams != undefined && additionalParams != "")  {
		params = additionalParams;
	}
	$.ajax({
		url : pageUrl,
		data : params,
		type : "POST",
		success : function(data) {
			loadData(data, tag, pageUrl, callSuccessEvent);
		}
	});
}

function clickSave(pageUrl, params, tag, progressText) {
	if (progressText !== undefined) {
		showProgressBar(progressText);
	} 
	$.ajax({
		url : pageUrl,
		data : params,
		type : "POST",
		success : function(data) {
			hideProgressBar();
			loadData(data, tag);
		}
	});
}

function validate(pageUrl, form, tag, progressText, disabledDiv) {
	if (disabledDiv != undefined && disabledDiv != "") {
		enableDivCtrls(disabledDiv);
	}
	var params = "";
	if (form != undefined && !isBlank(form)) {
		params = form.serialize();
	}
	$.ajax({
		url : pageUrl + "Validate",
		data : params,
		type : "POST",
		success : function(data) {
			if (data.errorFound != undefined && data.errorFound) {
				findError(data);
			} else {
				clickSave(pageUrl, params, tag, progressText);
			}
		}
	});
}

function loadData(data, tag, pageUrl, callSuccessEvent) {
	//To load the login page if the user session is not available
	if (data != undefined && data != "[object Object]" && data != "[object XMLDocument]" 
		&& !isBlank(data) && data.indexOf("Remember me") >= 0) {
		window.location.href = "logout.action";
	} else {
		if (callSuccessEvent != undefined && !isBlank(callSuccessEvent) && callSuccessEvent) {
			successEvent(pageUrl, data);
		} else {
			if (tag !== undefined && !isBlank(tag)) {
				tag.empty();
				tag.html(data);
			}
			accordion();
			setTimeOut();
		}
	}
}

function inActivateAllMenu(allLink) {
	allLink.attr("class", "inactive");
}

function activateMenu(selectedMenu) {
	selectedMenu.attr('class', "active");
}

function checkAllEvent(currentCheckbox, childCheckBox, disable) {
	var checkAll = $(currentCheckbox).prop('checked');
	childCheckBox.prop('checked', checkAll);
	buttonStatus(checkAll);
	if (!checkAll) {
		disable = false;
	}
	toDisableAllCheckbox(currentCheckbox,childCheckBox, disable);
}

function checkboxEvent() {
	var chkboxStatus = $('.check').is(':checked');
	buttonStatus(chkboxStatus);
	if ($('.check').length == $(".check:checked").length) {
		$('#checkAllAuto').prop('checked', true);
	} else {
		$('#checkAllAuto').prop('checked', false);
	}
}

function buttonStatus(checkAll) {
	$('#del').attr('disabled', !checkAll);
	if (checkAll) {
		$('#del').addClass('btn-primary');
	} else {
		$('#del').removeClass('btn-primary');
	}
}

function toDisableCheckAll() {
	if ($(".check:checkbox").length > 0 ) {
		$('#checkAllAuto').prop('disabled', false);
	} else {
		$('#checkAllAuto').prop('disabled', true);
	}
}

function toDisableAllCheckbox(currentCheckbox,childCheckBox, disable) {
	if($(currentCheckbox).is(':checked')){
		childCheckBox.prop('disabled', disable);
	} else {
		childCheckBox.prop('disabled', disable);
		childCheckBox.prop('checked', false);
	}
}

function showError(tag, span, errmsg) {
	tag.addClass("error");
	span.text(errmsg);
}

function hideError(tag, span) {
	if (!isBlank(tag)) {
		tag.removeClass("error");
		span.empty();
	} 
}

function setTimeOut() {
	setTimeout(function() {
		$('#successmsg').fadeOut("slow", function () {
			$('#successmsg').remove();
		});
	}, 2000);
	
	setTimeout(function() {
		$('#errormsg').fadeOut("slow", function () {
			$('#errormsg').remove();
		});
	}, 2000);
}

function accordion() {
	/** Accordian starts **/
	var showContent = 0;	
    $('.siteaccordion').removeClass('openreg').addClass('closereg');
    $('.mfbox').css('display','none');
    
    $('.siteaccordion').bind('click',function(e) {
        var _tempIndex = $('.siteaccordion').index(this);
        $('.siteaccordion').removeClass('openreg').addClass('closereg');
        $('.mfbox').each(function(e) {
            if ($(this).css('display')=='block'){
                $(this).slideUp('300');
            }
        });
        if ($('.mfbox').eq(_tempIndex).css('display')=='none') {
            $(this).removeClass('closereg').addClass('openreg');
            $('.mfbox').eq(_tempIndex).slideDown(300,function() {
                
            });
        }
    });
}

function showLoadingIcon() {
	var src = "theme/photon/images/loading_blue.gif";
	var theme =localStorage["color"];
    if (theme == undefined || theme == "theme/photon/css/red.css") {
    	src = "theme/photon/images/loading_red.gif";
    }
    $("#loadingIconDiv").show();
	$("#loadingIconImg").attr("src", src);
    disableScreen();
}

function hideLoadingIcon() {
    $("#loadingIconDiv").hide();
    enableScreen();
}

function showProgressBar(progressText) {
	$(".bar").html(progressText);
	$(".modal-backdrop").show();
	$(".progress").show();
	setInterval(prog, 100);
}

function hideProgressBar() {
	$(".modal-backdrop").hide();
	$(".progress").hide();
}

// It allows A-Z, a-z, 0-9, - and _ 
function checkForSplChr(inputStr) {
	return inputStr.replace(/[^a-zA-Z 0-9\-\_]+/g, '');
}

//It allows A-Z, a-z,
function allowAlpha(inputStr) {
	return inputStr.replace(/[^a-zA-Z]+/g, '');
}

//It allows A-Z, a-z, 0-9, - , _ and .
function checkForSplChrExceptDot(inputStr) {
	return inputStr.replace(/[^a-zA-Z 0-9\.\-\_]+/g, '');
}

//It allows A-Z, a-z, 0-9
function allowAlphaNum(inputStr) {
	return inputStr.replace(/[^a-zA-Z 0-9]+/g, '');
}

//It allows 0-9,- and +
function allowNumHyphenPlus(numbr) {
	return numbr.replace(/[^0-9\-\+]+/g, '');
}

function changeTheme() {
  	if (localStorage["color"] != null) {
        $("link[title='phresco']").attr("href", localStorage["color"]);
    } else {
        $("link[title='phresco']").attr("href", "theme/photon/css/red.css");
    } 
}

function showWelcomeImage() {
	var theme = localStorage['color'];
	if (theme == "theme/photon/css/blue.css") {
		$("link[id='theme']").attr("href", localStorage["color"]);
		$('.headerlogoimg').attr("src","theme/photon/images/phresco_header_blue.png");
		$('.phtaccinno').attr("src","theme/photon/images/acc_inov_blue.png");
		$('.welcomeimg').attr("src","theme/photon/images/welcome_photon_blue.png");
	} else if (theme == null || theme == undefined || theme == "theme/photon/css/red.css") {
		$("link[id='theme']").attr("href", "theme/photon/css/red.css");
		$('.headerlogoimg').attr("src","theme/photon/images/phresco_header_red.png");
		$('.phtaccinno').attr("src","theme/photon/images/acc_inov_red.png");
		$('.welcomeimg').attr("src","theme/photon/images/welcome_photon_red.png");
	}
}

function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}

//To disable the screen by showing an overlay
function disableScreen() {
	$(".modal-backdrop").show();
}

//To enable the screen by hiding an overlay
function enableScreen() {
	$(".modal-backdrop").hide();
}

//To fill the pom details in the textbox if available while uploading the files
function fillTextBoxes(responseJSON, type, fileName) {
	if (type === "pluginJar"){
		$('#jarDetailsDivPopup').show();
	} else if (type === "videoFile") {
		$('#videoDetailsDiv').show();
	} else {
		$('#jarDetailsDiv').show();
	}
	if (responseJSON.mavenJar) {
		disableEnableTextBox(responseJSON.groupId, responseJSON.artifactId, responseJSON.version, true, type, fileName);
	} else {
		disableEnableTextBox('', '', '', false, type, fileName);
	}
}

function checkExistence(fileName){
	for (var i = 0; i < jars.length; i++) {
        if (jars[i] === fileName) {
        	return true;
        }
    }
	return false;
}

function arrayPushPop(fileName, isEnable){
	if(isEnable){
		jars.push(fileName);
	}else{
		 for(var i in jars){
	            if(jars[i]==fileName){
	                jars.splice(i,1);
	                break;
	                }
	        }
	}
}

function disableEnableTextBox(groupId, artifactId, jarVersion, isEnable, type, fileName) {
	if (type === "pluginJar") {
		var duplicate = checkExistence(fileName);
		arrayPushPop(fileName, true);
		var groupid = "grouId" ;
		var artifId = "artifId" ;
		var versnId = "versnId" ;
		var fileDetParentDiv = $(document.createElement('div')).attr("id", fileName).attr("class","fileClass");
		fileDetParentDiv.html("<div style='float: left; margin: 0px 10px 0px 0px;'><div class='controls' style='margin-left: 0%;'><input style='text-align:center;'id='" +groupid +"' class='groupId' class='input-xlarge' name='"+fileName+"_groupId" +"' maxlength='40' title='40 Characters only' type='text'  value='" + groupId +"' >" +
				           "</div></div></td>"); 
		fileDetParentDiv.append("<div style='float: left; margin: 0px 10px 0px 0px;'><div class='controls' style='margin-left: 0%;'><input style='text-align:center;'id='" +artifId +"'class='artifactId' class='input-xlarge' name='"+fileName+"_artifactId" +"' maxlength='40' title='40 Characters only' type='text' value='" + artifactId +"' >" +
				              "</div></div>");
		fileDetParentDiv.append("<div style='float: left; margin: 0px 10px 0px 0px;'><div class='controls'  style='margin-left: 0%;'><input style='text-align:center;'id='" +versnId +"'class='jarVersion' name='"+fileName+"_version" +"'  maxlength='30' title='30 Characters only' class='input-xlarge' type='text' value='" +jarVersion +"' > " +
		                   "</div></div>");
		fileDetParentDiv.append("</div>");
		fileDetParentDiv.appendTo("#tableAdd");
		if(duplicate){
			arrayPushPop(fileName, false);
			var lis = $('#tableAdd').children(); // get all the children
			var len=lis.length-1;
			$(lis.get(len)).remove();
			$('li').last().remove()
		}
	} else {
		$('.groupId').val(groupId).attr('disabled', isEnable);
		$('.artifactId').val(artifactId).attr('disabled', isEnable);
		$('.jarVersion').val(jarVersion).attr('disabled', isEnable);
		$('input[name=groupId]').val(groupId);
		$('input[name=artifactId]').val(artifactId);
		$('input[name=jarVersion]').val(jarVersion);
	}
}

function isiPad() {
    return (
        (navigator.platform.indexOf("iPhone") != -1) ||
        (navigator.platform.indexOf("iPad") != -1)
    );
}

//This method is calling from triggering list.jsp
function showDeleteConfirmation(confirmMsg) {
	$('#popupPage').addClass('confirm');
	$('#popupTitle').html("Confirmation Dialog"); 
	$('.modal-body').html(confirmMsg);
	$('#clipboard').hide();
	$('.modal-body').css("height","50px");
	$('#popupPage').css({"width":"649px","position":"relative","left":"48%"});
	$('.popupOk').removeAttr("onclick");
	$('.popupOk').attr("onclick","continueDeletion()");
	$('#popupClose').hide();
	$('#popupPage').modal({
		show: true
	});
}

$('#popupCancel').click(function() {
	confirmDialog('none');
});

function confirmDialog(enableProp, msg) {
    $(".modal-backdrop").css("display", enableProp);
    $(".confirm").show().css("display", enableProp);
}


function copyToClipboard(data) {
    var params = "copyToClipboard=";
    params = params.concat(data);
    $.ajax({
		url : "copyToClipboard",
		data : params,
		type : "POST",
		success : function() {
		}
	});
}

//trim the long content
function textTrim(obj) {
    var val = $(obj).text();
    $(obj).attr("title", val);
    var len = val.length;
    if(len > 50) {
        val = val.substr(0, 50) + "...";
        return val;
    }
    return val;
}

$(document).keydown(function(e) {
    // ESCAPE key pressed
	if (e.keyCode == 27) {
	   showParentPage();
    }
});

//Shows the parent page
function showParentPage() {
	enableScreen();
	$('#popup_div').hide();
}

//To disable the given control
function disableCtrl(control) {
	control.attr("disabled", true);
}

//To enable the given control
function enableCtrl(control) {
	control.removeAttr("disabled");
}

function enableDivCtrls(disabledDiv) {
	disabledDiv.removeAttr("disabled");
}

function enableDisableUploads(type, controlObj){
	if ($('ul[temp='+type+'] > li').length === 1) {
		controlObj.find("input[type='file']").attr('disabled', 'disabled');
		controlObj.find($(".qq-upload-button")).removeClass("btn-primary qq-upload-button").addClass("disabled");
	} else {
		controlObj.find("input[type='file']").attr('disabled', false);
		controlObj.find($(".btn")).removeClass("disabled").addClass("btn-primary qq-upload-button");
	}
}