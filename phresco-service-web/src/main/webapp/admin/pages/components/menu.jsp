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

<%@ page import="org.apache.commons.collections.CollectionUtils"%>

<%@ page import="com.photon.phresco.commons.model.ArtifactGroup.Type" %>
<%@ page import="com.photon.phresco.commons.model.Customer"%>
<%@ page import="com.photon.phresco.commons.model.User"%>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants"%>
<%@ page import="com.photon.phresco.util.ServiceConstants"%>

<% 
    String customerId = (String)session.getAttribute("customerId"); 
%>

<script type="text/javascript">
	var customerId = "";
	$(document).ready(function() {
		var customerId = localStorage["selectedCustomerId"];
		$("#customerSelect").val(customerId);
		
		clickMenu($("a[name='compTab']"), $("#subcontainer"), $('#formCustomerId'));
		activateMenu($("#features"));
		
	  	$(".tabs li a").click(function() {
			if($(this).attr("id")=="featuresMenu") {
				$("#testmenu").slideDown();
				$("#testmenu .active").removeClass("active").addClass("inactive");
				$("#testmenu li:first-child a").addClass("active");
			} else if($(this).attr("name")=="compTab") {
				$("#testmenu").slideUp();
			}
	    });
	  	
		//Customer change event	  	
	  	$('.customer_listbox').ddslick({
        	onSelected: function(data) {
        		var selectedId = data.selectedData.value;
				customerChangeEvent(selectedId);
        	}
        });
	});
	
	function customerChangeEvent(selectedId) {
		showLoadingIcon();
		$('#customerId').val(selectedId);
		localStorage["selectedCustomerId"] = selectedId;
		var selectedMenu = $("a[name='compTab'][class='active']").prop("id");
		loadContent("fetchLogoImgUrl", $('#formCustomerId'), '', '', true, 'changeLogo');
		
		//Handles the click event of the sub tabs
		clickMenu($("a[name='featureTab']"), $("#subcontainer"), $('#formCustomerId'));
		
		//To load the page by default
		loadContent("technologies", $('#formCustomerId'), $("#subcontainer"), "type=<%= Type.FEATURE.name() %>");
		
		//To activate the module menu by default
		
		$(".tabs li a").removeClass("active").addClass("inactive");
		$(".tabs li:first-child .submenu ").show();
		$(".tabs li:first-child .submenu li:first-child a").addClass("active");
		activateMenu($("#featuresMenu"));
		
		loadContent("fetchCustomerId", $('#formCustomerId'), '', '', false, true, '');
	}
	
	function changeLogo(data) {
		showLoadingIcon();
		$('#logoImg').attr("src",  "data:image/png;base64," + data.logoImgUrl);
		var copyright = data.copyRight;
		if (!isBlank(copyright)) {
			$(".copyrit").html(copyright);
		} else {
			$(".copyrit").html("&copy; 2013 Photon Infotech Pvt Ltd. |<a onclick='open_win();'> www.photon.in</a>");
		}
	}
	
	function open_win() {
		window.open("http://www.photon.in");
    }
</script>

<%
    User userInfo = (User) session.getAttribute(ServiceUIConstants.SESSION_USER_INFO);
	List<Customer> customers  = (List<Customer>) session.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMERS);
%>
	<form id="formCustomerId" class="form">
		<div class="control-group customer_name">
			<s:label key="lbl.hdr.bottomleftnavlab" cssClass="control-label admin_label labelbold" theme="simple"/>
			<div class="ddright">
				<s:label key="lbl.hdr.comp.customer" cssClass="control-label custom_label labelbold" theme="simple"/>
				<div class="controls customer_select_div">
					<select id="customerSelect" name="customerSelect" class="customer_listbox">
		                <% 
		                    if (CollectionUtils.isNotEmpty(customers)) { 
					            for (Customer customer : customers) { 
					    %>
		                            <option value="<%= customer.getId() %>"><%= customer.getName() %></option>
						<% 
					            }
					        }
					    %>
					</select>
					<input type="hidden" id="customerId" name="customerId" value=""/>
				</div>
			</div>
		</div>
		<div class="clear"></div>
	</form>
<nav>
	<ul class="tabs">
		<li>
			<a href="#" class="active" name="compTab" id="featuresMenu"><s:label key="lbl.hdr.comp.featrs" theme="simple"/></a>
			<ul class="submenu" id="testmenu">
				<li>
					<a href="#" class="active" id="technologies" name="featureTab" additionalParam="type=<%= Type.FEATURE.name() %>">
						<s:text name="lbl.hdr.comp.featrs.modules" />
					</a>
				</li>
				<li>
					<a href="#" class="inactive" id="technologies" name="featureTab" additionalParam="type=<%= Type.JAVASCRIPT.name() %>">
						<s:text name="lbl.hdr.comp.featrs.jslib"/>
					</a>
				</li>
				<li>
					<a href="#" class="inactive" id="technologies" name="featureTab" additionalParam="type=<%= Type.COMPONENT.name() %>">
						<s:text name="lbl.hdr.comp.component"/>
					</a>
				</li>
			</ul>
		</li>
		<li>
			<a href="#" class="inactive" name="compTab" id="archetypesList"><s:label key="lbl.hdr.comp.arhtyp" theme="simple"/></a>
		</li>
		<li>
			<a href="#" class="inactive" name="compTab" id="applntypesList"><s:label key="lbl.hdr.comp.aplntyp" theme="simple"/></a>
		</li>
		<li>
			<a href="#" class="inactive" name="compTab" id="configtempList"><s:label key="lbl.hdr.comp.cnfigtmplt" theme="simple"/></a>
		</li>
		<li>
			<a href="#" class="inactive" name="compTab" id="pilotprojList"><s:label key="lbl.hdr.comp.pltprjt" theme="simple"/></a>
		</li>
		<li>
			<a href="#" class="inactive" name="compTab" id="downloadList"><s:label key="lbl.hdr.adm.dwnld"  theme="simple"/></a>
		</li>
	</ul>
			
</nav>			

<section id="subcontainer" class="navTopBorder">

</section>
