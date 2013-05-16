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

<script type="text/javascript">
	$(document).ready(function() {
		clickMenu($("a[name='adminTab']"),$("#subcontainer"));
		clickMenu($("a[name='rolesTab']"),$("#subcontainer"));
		clickMenu($("a[name='permissionsTab']"),$("#subcontainer"));
		loadContent("customerList", '', $("#subcontainer"));
		activateMenu($("#customerList"));
		
		$('#rolesSubmenu').hide();
		$('#permissionsSubmenu').hide();
		
		$(".tabs li a").click(function() {
			$(".tabs li a").removeClass("active").addClass("inactive");
			$(this).addClass("active");
			if ($(this).attr("id") == "rolesMenu") {
				showLoadingIcon();
				$("#permissionsSubmenu").slideUp();
				$("#rolesSubmenu").slideDown();
				$("#rolesSubmenu .active").removeClass("active").addClass("inactive");
				$("#rolesSubmenu li:first-child a").addClass("active");
				var params = "appliesTo=";
				params = params.concat("service");
				loadContent("roleList", "", $("#subcontainer"), params);
			} else if ($(this).attr("id") == "permissionsMenu") {
				showLoadingIcon();
				$("#rolesSubmenu").slideUp();
				$("#permissionsSubmenu").slideDown();
				$("#permissionsSubmenu .active").removeClass("active").addClass("inactive");
				$("#permissionsSubmenu li:first-child a").addClass("active");
				var params = "appliesTo=";
				params = params.concat("service");
				loadContent("permissionList", "", $("#subcontainer"), params);
			} else if ($(this).attr("name") == "adminTab") {
				$("#rolesSubmenu").slideUp();
				$("#permissionsSubmenu").slideUp();
			}
	    });
		
		$("#rolesSubmenu").click(function() {
			$("#rolesMenu").addClass("active");
		});
		
		$("#permissionsSubmenu").click(function() {
			$("#permissionsMenu").addClass("active");
		});
	});
</script>

<div class="control-group customer_name">
	<s:label key="lbl.hdr.bottomrightnavlab" cssClass="control-label admin_label labelbold" theme="simple"/>
</div>
<nav>
	<ul class="tabs">
		<li>
			<a id="customerList" class="active" name="adminTab" href="#">
			<s:label key="lbl.hdr.adm.cust"  theme="simple"/></a>
		</li>
		<li>
			<a id="userList" class="inactive" name="adminTab" href="#">
			<s:label key="lbl.hdr.adm.users"  theme="simple"/></a>
		</li>
		<li>
			<a class="inactive" id="rolesMenu" href="#">
			<s:label key="lbl.hdr.adm.rles"  theme="simple"/></a>
			<ul class="submenu" id=rolesSubmenu>
				<li>
					<a href="#" class="active" id="roleList" name="rolesTab" additionalParam="appliesTo=service">
						<s:text name="lbl.hdr.adm.rles.service"/>
					</a>
				</li>
				<li>
					<a href="#" class="inactive" id="roleList" name="rolesTab" additionalParam="appliesTo=framework">
						<s:text name="lbl.hdr.adm.rles.framework" />
					</a>
				</li>
			</ul>
		</li>
		<li>
			<a class="inactive" id="permissionsMenu" href="#">
			<s:label key="lbl.hdr.adm.perms"  theme="simple"/></a>
			<ul class="submenu" id="permissionsSubmenu">
				<li>
					<a href="#" class="active" id="permissionList" name="permissionsTab" additionalParam="appliesTo=service">
						<s:text name="lbl.hdr.adm.rles.service"/>
					</a>
				</li>
				<li>
					<a href="#" class="inactive" id="permissionList" name="permissionsTab" additionalParam="appliesTo=framework">
						<s:text name="lbl.hdr.adm.rles.framework" />
					</a>
				</li>
			</ul>
		</li>
		<li>
			<a id="ldap" class="inactive" name="adminTab" href="#">
			<s:label key="lbl.hdr.adm.ldapstng"  theme="simple"/></a>
		</li>
		<li>
			<a id="videoList" class="inactive" name="adminTab" href="#">
			<s:label key="lbl.hdr.adm.videos"  theme="simple"/></a>
		</li>
		<li>
			<a id="globalurlList" class="inactive" name="adminTab" href="#">
			<s:label key="lbl.hdr.adm.glblurl"  theme="simple"/></a>
		</li>
	</ul>		
</nav>

<section id="subcontainer" class="navTopBorder">

</section>