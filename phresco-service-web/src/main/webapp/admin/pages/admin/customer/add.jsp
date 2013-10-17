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

<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.ArrayList"%>

<%@ page import="org.apache.commons.collections.MapUtils"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.collections.CollectionUtils" %>

<%@ page import="com.photon.phresco.commons.model.Customer" %>
<%@ page import="com.photon.phresco.service.admin.commons.ServiceUIConstants" %>
<%@ page import="com.photon.phresco.service.admin.actions.util.ServiceActionUtil" %>
<%@ page import="com.photon.phresco.commons.model.Customer.LicenseType" %>
<%@ page import="com.photon.phresco.commons.model.ApplicationType" %>
<%@ page import="com.photon.phresco.commons.model.Technology" %>
<%@ page import="com.photon.phresco.commons.model.TechnologyOptions"%>

<%
	Customer customer = (Customer) request.getAttribute(ServiceUIConstants.REQ_CUST_CUSTOMER);
	String fromPage = (String) request.getAttribute(ServiceUIConstants.REQ_FROM_PAGE);
	List<Technology> technologies = (List<Technology>) request.getAttribute(ServiceUIConstants.REQ_ARCHE_TYPES);
	List<TechnologyOptions> options = (List<TechnologyOptions>) request.getAttribute(ServiceUIConstants.REQ_TECHNOLOGY_OPTION);
	
	String title = ServiceActionUtil.getTitle(ServiceUIConstants.CUSTOMERS, fromPage);
	String buttonLbl = ServiceActionUtil.getButtonLabel(fromPage);
	String pageUrl = ServiceActionUtil.getPageUrl(ServiceUIConstants.CUSTOMERS, fromPage);
	String progressTxt = ServiceActionUtil.getProgressTxt(ServiceUIConstants.CUSTOMERS, fromPage);
	
	List<String> permissionIds = (List<String>) session.getAttribute(ServiceUIConstants.SESSION_PERMISSION_IDS);
	String per_disabledStr = "";
	String per_disabledClass = "btn-primary";
	
	//For edit
	String id = "";
	String name = "";
	String description = "";
	String emailId = "";
	String address = "";
	String state = "";
	String zipCode = "";
	String contactNo = "";
	String fax = "";
	String helpText = "";
	String repoUrl = "";
	String repoName = "";
	String repoURL = "";
	String repoPassword = "";
	String repoUserName = "";
	String snapShotRepoUrl = "";
	String groupRepoUrl = "";
	String baseRepoUrl = "";
	String country = "";
	String disabled = "";
	String disabledClass = "";
	LicenseType licenseType = null;
	String validFrom = null;
	String validUpto = null;
	String icon = "";
	String loginLogoMargin = "";
	String pageLogoPadding = "";
	String headerLinkColor = "";
	String headerActiveLinkColor = ""; 
	String editNavigationLink = "";
	String buttonBackGroundColor = "";
	String consoleHeaderColor = "";
	String copyrightLabelColor = "";
	String headerBackGroundcolorTop = "";
	String headerBackGroundcolorBottom = "";
	String footerBackGroundcolorTop = "";
	String footerBackGroundcolorBottom = "";
	String pageTitleBackGroundTop = "";
	String pageTitleBackGroundBottom = "";
	String editNavigationActiveBackGroundTop = "";
	String editNavigationActiveBackGroundBottom = "";
	String bottomButtonPanelTop = "";
	String bottomButtonPanelBottom = "";
	String welcomeUserIcon = "";
	String context = "";
	String customerBaseColor = "";
	String PageTitleColor = "";
	String CopyrightText = "";
	String CustomerTitle = "";
	
	List<String> applicableTechnologies = new ArrayList();
	List<ApplicationType> applicableAppTypes = null;
	Map<String, String> frameworkTheme = null;
	List<String> selectedOptions = null;
	if (customer != null) {
	    if (StringUtils.isNotEmpty(customer.getId())) {
			id = customer.getId();
		}
		if (StringUtils.isNotEmpty(customer.getName())) {
			name = customer.getName();
		}
		if (StringUtils.isNotEmpty(customer.getDescription())) {
			description = customer.getDescription();
		}
		if (StringUtils.isNotEmpty(customer.getEmailId())) {
			emailId = customer.getEmailId();
		}
		if (StringUtils.isNotEmpty(customer.getAddress())) {
			address = customer.getAddress();
		}
		if (StringUtils.isNotEmpty(customer.getState())) {
			state = customer.getState();
		} 
		if (StringUtils.isNotEmpty(customer.getZipcode())) {
			zipCode = customer.getZipcode();
		} 
		if (StringUtils.isNotEmpty(customer.getContactNumber())) {
			contactNo = customer.getContactNumber();
		} 
		if (StringUtils.isNotEmpty(customer.getFax())) {
			fax = customer.getFax();
		} 
		if (StringUtils.isNotEmpty(customer.getHelpText())) {
			helpText = customer.getHelpText();
		}
		if (StringUtils.isNotEmpty(customer.getRepoInfo().getReleaseRepoURL())) {
			repoUrl = customer.getRepoInfo().getReleaseRepoURL();
		}
		if (StringUtils.isNotEmpty(customer.getRepoInfo().getSnapshotRepoURL())) {
			snapShotRepoUrl = customer.getRepoInfo().getSnapshotRepoURL();
		}
		if (StringUtils.isNotEmpty(customer.getRepoInfo().getGroupRepoURL())) {
			groupRepoUrl = customer.getRepoInfo().getGroupRepoURL();
		}
		if (StringUtils.isNotEmpty(customer.getRepoInfo().getBaseRepoURL())) {
			baseRepoUrl = customer.getRepoInfo().getBaseRepoURL();
		}
		if (customer.getValidFrom() != null) {
			Date formattedString = customer.getValidFrom();
			SimpleDateFormat newDateFormat = new SimpleDateFormat("MM/dd/yyyy");      
		 	Date d =newDateFormat.parse(newDateFormat.format(formattedString));  
		 	validFrom = newDateFormat.format(formattedString);  
		}
		if (customer.getValidUpto() != null) {
			Date formattedString = customer.getValidUpto();
			SimpleDateFormat newDateFormat = new SimpleDateFormat("MM/dd/yyyy");      
		 	Date d =newDateFormat.parse(newDateFormat.format(formattedString));  
		 	validUpto = newDateFormat.format(formattedString);  
		}
		if (customer.getType() != null) {
			licenseType = customer.getType();
		}
		if (customer.getContext() != null) {
			context = customer.getContext();
		}
		if (CollectionUtils.isNotEmpty(permissionIds) && !permissionIds.contains(ServiceUIConstants.PER_MANAGE_CUSTOMERS)) {
			per_disabledStr = "disabled";
			per_disabledClass = "btn-disabled";
		} else {
			disabledClass = "btn-primary";
			if (customer.isSystem()) {
				disabledClass = "btn-disabled";
				disabled = "disabled";
			}
		}
		frameworkTheme = customer.getFrameworkTheme();
		selectedOptions = customer.getOptions();
		if (MapUtils.isNotEmpty(frameworkTheme)) {
			loginLogoMargin = frameworkTheme.get("loginLogoMargin");
			pageLogoPadding = frameworkTheme.get("pageLogoPadding");
			headerLinkColor = frameworkTheme.get("headerLinkColor");
			headerActiveLinkColor = frameworkTheme.get("headerActiveLinkColor");
			editNavigationLink = frameworkTheme.get("editNavigationLink");
			buttonBackGroundColor = frameworkTheme.get("buttonBackGroundColor");
			consoleHeaderColor = frameworkTheme.get("consoleHeaderColor");
			copyrightLabelColor = frameworkTheme.get("copyrightLabelColor");
			headerBackGroundcolorTop = frameworkTheme.get("headerBackGroundcolorTop");
			headerBackGroundcolorBottom = frameworkTheme.get("headerBackGroundcolorBottom");
			footerBackGroundcolorTop = frameworkTheme.get("footerBackGroundcolorTop");
			footerBackGroundcolorBottom = frameworkTheme.get("footerBackGroundcolorBottom");
			pageTitleBackGroundTop = frameworkTheme.get("pageTitleBackGroundTop");
			pageTitleBackGroundBottom = frameworkTheme.get("pageTitleBackGroundBottom");
			editNavigationActiveBackGroundTop = frameworkTheme.get("editNavigationActiveBackGroundTop");
			editNavigationActiveBackGroundBottom = frameworkTheme.get("editNavigationActiveBackGroundBottom");
			bottomButtonPanelTop = frameworkTheme.get("bottomButtonPanelTop");
			bottomButtonPanelBottom = frameworkTheme.get("bottomButtonPanelBottom");
			customerBaseColor = frameworkTheme.get("bottomButtonPanelBottom");
			welcomeUserIcon = frameworkTheme.get("welcomeUserIcon");
			PageTitleColor = frameworkTheme.get("pageTitleColor");
			CopyrightText = frameworkTheme.get("copyRightLabel");
			CustomerTitle = frameworkTheme.get("customerTitle");
			context = customer.getContext();
		}
		
		if (StringUtils.isNotEmpty(customer.getRepoInfo().getRepoName())) {
			repoName = customer.getRepoInfo().getRepoName();
		}
		
		if (StringUtils.isNotEmpty(customer.getRepoInfo().getReleaseRepoURL())) {
			repoURL = customer.getRepoInfo().getReleaseRepoURL();
		}
		
		if (StringUtils.isNotEmpty(customer.getRepoInfo().getRepoUserName())) {
			repoUserName = customer.getRepoInfo().getRepoUserName();
		}
		
		if (StringUtils.isNotEmpty(customer.getRepoInfo().getRepoPassword())) {
			repoPassword = customer.getRepoInfo().getRepoPassword();
		}
		
		if(StringUtils.isNotEmpty(customer.getCountry())) {
			country = customer.getCountry();
		}
	}
%>

<form id="formCustomerAdd" class="form-horizontal customer_list">
	<h4 class="hdr">
	   <%= title %>
	</h4>
	<div class="content_adder">
		<div class="control-group" id ="nameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.name'/>
			</label>
			<div class="controls">
				<input id="custmname" placeholder="<s:text name='place.hldr.cust.add.name'/>" class="input-xlarge" name="name" type="text" 
				    value="<%= name %>" maxlength="30" title="30 Characters only">
					<span class="help-inline" id="nameError"></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.desc'/>
			</label>
			<div class="controls">
				<textarea id="textarea" placeholder="<s:text name='place.hldr.cust.add.desc'/>" class="input-xlarge" rows="3" 
				    name="description" maxlength="150" title="150 Characters only"><%= description %></textarea>
			</div>
		</div>

		<div class="control-group" id ="mailControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.cust.mail'/>
			</label>
			<div class="controls">
				<div class="input-prepend">
					<span class="add-on"> <i class="icon-envelope"></i></span> 
					<input id="inputIcon" class="span2" type="text" name="email" 
                        value="<%= emailId %>">
					<span class="help-inline" id="mailError"></span>
				</div>
			</div>
		</div>

		<div class="control-group" id="addresControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.cust.adrs'/>
			</label>
			<div class="controls">
				<textarea id="textarea" placeholder="<s:text name='place.hldr.cust.add.address'/>" class="input-xlarge" rows="3" 
				    name="address" maxlength="150" title="150 Characters only"><%= address %></textarea>
				<span class="help-inline applyerror" id="addresError"></span>
			</div>
		</div>

		<div class="control-group" id="conControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.cust.cntry'/>
			</label>
			<div class="controls">
				<select id="countryList" name="country">
					<option value="">- select -</option>
					<option value="AF">Afghanistan</option>
					<option value="AL">Albania</option>
					<option value="DZ">Algeria</option>
					<option value="AS">American Samoa</option>
					<option value="AD">Andorra</option>
					<option value="AG">Angola</option>
					<option value="AI">Anguilla</option>
					<option value="AG">Antigua &amp; Barbuda</option>
					<option value="AR">Argentina</option>
					<option value="AA">Armenia</option>
					<option value="AW">Aruba</option>
					<option value="AU">Australia</option>
					<option value="AT">Austria</option>
					<option value="AZ">Azerbaijan</option>
					<option value="BS">Bahamas</option>
					<option value="BH">Bahrain</option>
					<option value="BD">Bangladesh</option>
					<option value="BB">Barbados</option>
					<option value="BY">Belarus</option>
					<option value="BE">Belgium</option>
					<option value="BZ">Belize</option>
					<option value="BJ">Benin</option>
					<option value="BM">Bermuda</option>
					<option value="BT">Bhutan</option>
					<option value="BO">Bolivia</option>
					<option value="BL">Bonaire</option>
					<option value="BA">Bosnia &amp; Herzegovina</option>
					<option value="BW">Botswana</option>
					<option value="BR">Brazil</option>
					<option value="BC">British Indian Ocean Ter</option>
					<option value="BN">Brunei</option>
					<option value="BG">Bulgaria</option>
					<option value="BF">Burkina Faso</option>
					<option value="BI">Burundi</option>
					<option value="KH">Cambodia</option>
					<option value="CM">Cameroon</option>
					<option value="CA">Canada</option>
					<option value="IC">Canary Islands</option>
					<option value="CV">Cape Verde</option>
					<option value="KY">Cayman Islands</option>
					<option value="CF">Central African Republic</option>
					<option value="TD">Chad</option>
					<option value="CD">Channel Islands</option>
					<option value="CL">Chile</option>
					<option value="CN">China</option>
					<option value="CI">Christmas Island</option>
					<option value="CS">Cocos Island</option>
					<option value="CO">Colombia</option>
					<option value="CC">Comoros</option>
					<option value="CG">Congo</option>
					<option value="CK">Cook Islands</option>
					<option value="CR">Costa Rica</option>
					<option value="CT">Cote D'Ivoire</option>
					<option value="HR">Croatia</option>
					<option value="CU">Cuba</option>
					<option value="CB">Curacao</option>
					<option value="CY">Cyprus</option>
					<option value="CZ">Czech Republic</option>
					<option value="DK">Denmark</option>
					<option value="DJ">Djibouti</option>
					<option value="DM">Dominica</option>
					<option value="DO">Dominican Republic</option>
					<option value="TM">East Timor</option>
					<option value="EC">Ecuador</option>
					<option value="EG">Egypt</option>
					<option value="SV">El Salvador</option>
					<option value="GQ">Equatorial Guinea</option>
					<option value="ER">Eritrea</option>
					<option value="EE">Estonia</option>
					<option value="ET">Ethiopia</option>
					<option value="FA">Falkland Islands</option>
					<option value="FO">Faroe Islands</option>
					<option value="FJ">Fiji</option>
					<option value="FI">Finland</option>
					<option value="FR">France</option>
					<option value="GF">French Guiana</option>
					<option value="PF">French Polynesia</option>
					<option value="FS">French Southern Ter</option>
					<option value="GA">Gabon</option>
					<option value="GM">Gambia</option>
					<option value="GE">Georgia</option>
					<option value="DE">Germany</option>
					<option value="GH">Ghana</option>
					<option value="GI">Gibraltar</option>
					<option value="GB">Great Britain</option>
					<option value="GR">Greece</option>
					<option value="GL">Greenland</option>
					<option value="GD">Grenada</option>
					<option value="GP">Guadeloupe</option>
					<option value="GU">Guam</option>
					<option value="GT">Guatemala</option>
					<option value="GN">Guinea</option>
					<option value="GY">Guyana</option>
					<option value="HT">Haiti</option>
					<option value="HW">Hawaii</option>
					<option value="HN">Honduras</option>
					<option value="HK">Hong Kong</option>
					<option value="HU">Hungary</option>
					<option value="IS">Iceland</option>
					<option value="IN">India</option>
					<option value="ID">Indonesia</option>
					<option value="IA">Iran</option>
					<option value="IQ">Iraq</option>
					<option value="IR">Ireland</option>
					<option value="IM">Isle of Man</option>
					<option value="IL">Israel</option>
					<option value="IT">Italy</option>
					<option value="JM">Jamaica</option>
					<option value="JP">Japan</option>
					<option value="JO">Jordan</option>
					<option value="KZ">Kazakhstan</option>
					<option value="KE">Kenya</option>
					<option value="KI">Kiribati</option>
					<option value="NK">Korea North</option>
					<option value="KS">Korea South</option>
					<option value="KW">Kuwait</option>
					<option value="KG">Kyrgyzstan</option>
					<option value="LA">Laos</option>
					<option value="LV">Latvia</option>
					<option value="LB">Lebanon</option>
					<option value="LS">Lesotho</option>
					<option value="LR">Liberia</option>
					<option value="LY">Libya</option>
					<option value="LI">Liechtenstein</option>
					<option value="LT">Lithuania</option>
					<option value="LU">Luxembourg</option>
					<option value="MO">Macau</option>
					<option value="MK">Macedonia</option>
					<option value="MG">Madagascar</option>
					<option value="MY">Malaysia</option>
					<option value="MW">Malawi</option>
					<option value="MV">Maldives</option>
					<option value="ML">Mali</option>
					<option value="MT">Malta</option>
					<option value="MH">Marshall Islands</option>
					<option value="MQ">Martinique</option>
					<option value="MR">Mauritania</option>
					<option value="MU">Mauritius</option>
					<option value="ME">Mayotte</option>
					<option value="MX">Mexico</option>
					<option value="MI">Midway Islands</option>
					<option value="MD">Moldova</option>
					<option value="MC">Monaco</option>
					<option value="MN">Mongolia</option>
					<option value="MS">Montserrat</option>
					<option value="MA">Morocco</option>
					<option value="MZ">Mozambique</option>
					<option value="MM">Myanmar</option>
					<option value="NA">Nambia</option>
					<option value="NU">Nauru</option>
					<option value="NP">Nepal</option>
					<option value="AN">Netherland Antilles</option>
					<option value="NL">Netherlands (Holland, Europe)</option>
					<option value="NV">Nevis</option>
					<option value="NC">New Caledonia</option>
					<option value="NZ">New Zealand</option>
					<option value="NI">Nicaragua</option>
					<option value="NE">Niger</option>
					<option value="NG">Nigeria</option>
					<option value="NW">Niue</option>
					<option value="NF">Norfolk Island</option>
					<option value="NO">Norway</option>
					<option value="OM">Oman</option>
					<option value="PK">Pakistan</option>
					<option value="PW">Palau Island</option>
					<option value="PS">Palestine</option>
					<option value="PA">Panama</option>
					<option value="PG">Papua New Guinea</option>
					<option value="PY">Paraguay</option>
					<option value="PE">Peru</option>
					<option value="PH">Philippines</option>
					<option value="PO">Pitcairn Island</option>
					<option value="PL">Poland</option>
					<option value="PT">Portugal</option>
					<option value="PR">Puerto Rico</option>
					<option value="QA">Qatar</option>
					<option value="ME">Republic of Montenegro</option>
					<option value="RS">Republic of Serbia</option>
					<option value="RE">Reunion</option>
					<option value="RO">Romania</option>
					<option value="RU">Russia</option>
					<option value="RW">Rwanda</option>
					<option value="NT">St Barthelemy</option>
					<option value="EU">St Eustatius</option>
					<option value="HE">St Helena</option>
					<option value="KN">St Kitts-Nevis</option>
					<option value="LC">St Lucia</option>
					<option value="MB">St Maarten</option>
					<option value="PM">St Pierre &amp; Miquelon</option>
					<option value="VC">St Vincent &amp; Grenadines</option>
					<option value="SP">Saipan</option>
					<option value="SO">Samoa</option>
					<option value="AS">Samoa American</option>
					<option value="SM">San Marino</option>
					<option value="ST">Sao Tome &amp; Principe</option>
					<option value="SA">Saudi Arabia</option>
					<option value="SN">Senegal</option>
					<option value="SC">Seychelles</option>
					<option value="SL">Sierra Leone</option>
					<option value="SG">Singapore</option>
					<option value="SK">Slovakia</option>
					<option value="SI">Slovenia</option>
					<option value="SB">Solomon Islands</option>
					<option value="OI">Somalia</option>
					<option value="ZA">South Africa</option>
					<option value="ES">Spain</option>
					<option value="LK">Sri Lanka</option>
					<option value="SD">Sudan</option>
					<option value="SR">Suriname</option>
					<option value="SZ">Swaziland</option>
					<option value="SE">Sweden</option>
					<option value="CH">Switzerland</option>
					<option value="SY">Syria</option>
					<option value="TA">Tahiti</option>
					<option value="TW">Taiwan</option>
					<option value="TJ">Tajikistan</option>
					<option value="TZ">Tanzania</option>
					<option value="TH">Thailand</option>
					<option value="TG">Togo</option>
					<option value="TK">Tokelau</option>
					<option value="TO">Tonga</option>
					<option value="TT">Trinidad &amp; Tobago</option>
					<option value="TN">Tunisia</option>
					<option value="TR">Turkey</option>
					<option value="TU">Turkmenistan</option>
					<option value="TC">Turks &amp; Caicos Is</option>
					<option value="TV">Tuvalu</option>
					<option value="UG">Uganda</option>
					<option value="UA">Ukraine</option>
					<option value="AE">United Arab Emirates</option>
					<option value="GB">United Kingdom</option>
					<option value="US">United States of America</option>
					<option value="UY">Uruguay</option>
					<option value="UZ">Uzbekistan</option>
					<option value="VU">Vanuatu</option>
					<option value="VS">Vatican City State</option>
					<option value="VE">Venezuela</option>
					<option value="VN">Vietnam</option>
					<option value="VB">Virgin Islands (Brit)</option>
					<option value="VA">Virgin Islands (USA)</option>
					<option value="WK">Wake Island</option>
					<option value="WF">Wallis &amp; Futana Is</option>
					<option value="YE">Yemen</option>
					<option value="ZR">Zaire</option>
					<option value="ZM">Zambia</option>
					<option value="ZW">Zimbabwe</option>
				</select>
				<span class="help-inline" id="conError"></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.adm.cust.state'/>
			</label>
			<div class="controls">
				<input id="statefld" placeholder="<s:text name='place.hldr.cust.add.state'/>" class="input-xlarge" type="text" name="state"
				    value="<%= state %>" maxlength="50" title="50 Characters only">
			</div>
		</div>

		<div class="control-group" id= "zipControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.cust.zipcode'/>
			</label>
			<div class="controls">
				<input id="zipcodefld" placeholder="<s:text name='place.hldr.cust.add.zipcode'/>" class="input-xlarge" type="text" name="zipcode"
				    value="<%= zipCode %>" maxlength="20" title="20 Characters only">
				<span class="help-inline" id="zipError"></span>
			</div>
		</div>

		<div class="control-group" id= "numControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.cust.cont'/>
			</label>
			<div class="controls">
				<input id="contactNo" placeholder="<s:text name='place.hldr.cust.add.contact.no'/>" class="input-xlarge" type="text" name="number"
				    value="<%= contactNo %>" maxlength="16" title="16 Characters only">
				<span class="help-inline" id="numError"></span>
			</div>
		</div>

		<div class="control-group" id= "faxControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.cust.fax'/>
			</label>
			<div class="controls">
				<input id="faxfld" placeholder="<s:text name='place.hldr.cust.add.fax.no'/>" class="input-xlarge" type="text" name="fax"
				    value="<%= fax %>" maxlength="21" title="21 Characters only">
				<span class="help-inline" id="faxError"></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.adm.cust.hlptxt'/>
			</label>
			<div class="controls">
				<textarea id="hlptext" class="input-xlarge" title="150 Characters only" maxlength="150" name="helpText" 
						placeholder="Help Text"></textarea>
			</div>
		</div>

		<div class="control-group" id="licenControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.cust.linctype'/>
			</label>
			<div class="controls">
				<select id="licenseType" name="licence">
					<option value="">- select -</option>
					<option value="<%= LicenseType.TYPE_BRONZE %>"><%= LicenseType.TYPE_BRONZE %></option>
					<option value="<%= LicenseType.TYPE_SILVER %>"><%= LicenseType.TYPE_SILVER %></option>
					<option value="<%= LicenseType.TYPE_GOLD %>"><%= LicenseType.TYPE_GOLD %></option>
				</select>
				<span class="help-inline" id="licenError"></span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.adm.cust.Vlddatefrom'/>
			</label>
			<div class="controls">
				<input id="fromdate" class="datealign" placeholder="<s:text name='place.hldr.cust.add.valid.date'/>" type="text" name="validFrom" 
				   value="<%= validFrom != null ? validFrom : "" %>">
			</div>
		</div>

		<div class="control-group">
			<label class="control-label labelbold">
					<s:text name='lbl.hdr.adm.cust.vlddateto'/>
			</label>
			<div class="controls">
				<input id="todate" class="datealign" placeholder="<s:text name='place.hldr.cust.add.valid.date'/>" type="text" name="validUpTo" 
				    value="<%= validUpto != null ? validUpto : "" %>">
			</div>
		</div>
	
		<div class="control-group" id ="repoNameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='place.hldr.cust.add.reponame'/>
			</label>
			<div class="controls">
				<input id="reponame" placeholder="<s:text name='lbl.hdr.adm.cust.repoName'/>" class="input-xlarge" name="repoName" type="text" 
				    value="<%= repoName %>" maxlength="30" title="30 Characters only">
					<span class="help-inline" id="repoNameError"></span>
			</div>
		</div>
		
		<div class="control-group" id ="repoURLControl">
			<label class="control-label labelbold">
				<s:text name='place.hldr.cust.add.repoURL'/>
			</label>
			<div class="controls">
				<input id="repourl" placeholder="<s:text name='lbl.hdr.adm.cust.repoURL'/>" class="input-xlarge" name="repoURL" type="text" 
				    value="<%= repoURL %>" />
				    <span class="help-inline" id="repoURLError"></span>
			</div>
		</div>
		
		<div class="control-group hideContent repoMndatory" id ="repoUserNameControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='place.hldr.cust.add.repoUserName'/>
			</label>
			<div class="controls">
				<input id="repousername" placeholder="<s:text name='lbl.hdr.adm.cust.repoUserName'/>" class="input-xlarge"
					name="repoUserName" type="text" value="<%= repoUserName %>" maxlength="30" title="30 Characters only">
					<span class="help-inline" id="repoUserNameError"></span>
			</div>
		</div>
		
		<div class="control-group hideContent repoMndatory" id ="repoPasswordControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='place.hldr.cust.add.repoPassword'/>
			</label>
			<div class="controls">
				<input id="repopassword" placeholder="<s:text name='lbl.hdr.adm.cust.repoPassword'/>" class="input-xlarge" 
					name="repoPassword" type="password" value="<%= repoPassword %>" maxlength="30" title="30 Characters only">
					<span class="help-inline" id="repoPasswordError"></span>
			</div>
		</div>
		<div class="control-group" id="contextControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.adm.cust.context'/>
			</label>
			<div class="controls">
				<input id="context" placeholder="<s:text name='place.hldr.cust.add.context'/>" class="input-xlarge" type="text" name="context"
				    value="<%= context %>" maxlength="50" title="50 Characters only">
				    <span class="help-inline" id="contextError"></span>
			</div>
		</div>
		
		<%
			if(CollectionUtils.isNotEmpty(technologies)) {
		%>
		<div class="control-group" id="applyControl">
			<label class="control-label labelbold">
				<span class="mandatory">*</span>&nbsp;<s:text name='lbl.hdr.comp.appliesto'/>
			</label>
			<div class="controls">
					<div class="typeFields" id="typefield">
					<div class="multilist-scroller multiselct" id="appliesToDiv">
					<ul>
						<li>
							<input type="checkbox" value="" id="checkAllAuto"  onclick="checkAllEvent(this,$('.applsChk'), false);" style="margin: 3px 8px 6px 0;">All
						</li>
						<%
							for (Technology technology : technologies) {
								String checkedStr = "";
								if (customer!= null) {
									List<String> appliesTos = customer.getApplicableTechnologies();
									if (appliesTos.contains(technology.getId())) {
										checkedStr = "checked";
									} else {
										checkedStr = "";
									}
								}
						%>		
								<li>
									<input type="checkbox" id="appliestoCheckbox" name="appliesTo" onclick= "checkboxEvent($('#checkAllAuto'),'applsChk');" value="<%= technology.getId() %>"  <%= checkedStr %>
										class="check applsChk"><%= technology.getName() %>
								</li>
						<%  
							}
						%>
					</ul>
				</div>
			</div>
          		 <span class="help-inline applyerror" id="applyError"></span>
			</div>
	 	</div>
		<%  
			}
		%>
		
		<div class="control-group" id="optionsControl">
			<label class="control-label labelbold">
				<s:text name='lbl.hdr.comp.applicable'/>
			</label>
			<div class="controls">
					<div class="typeFields" id="typefield">
					<div class="multilist-scroller multiselct" id="optionsDiv">
						<ul>
							<li>
								<input type="checkbox" value="" id="checkAllOptions" onclick="checkAllEvent(this,$('.optionsChk'), false);"
									style="margin: 3px 8px 6px 0;"><s:text name='lbl.all'/>
							</li>
							<%
								if (CollectionUtils.isNotEmpty(options)) {
									String checkedStr = "";
									for (TechnologyOptions option : options) {
										if (CollectionUtils.isNotEmpty(selectedOptions)) {
										    if (selectedOptions.contains(option.getId())) {
												checkedStr = "checked";
											} else {
											    checkedStr = "";
											}
										}
							%>
										<li>
											<input type="checkbox" id="optionsCheckbox" name="options" value="<%= option.getId() %>" class="check optionsChk"
												onclick="checkboxEvent($('#checkAllOptions'), 'optionsChk')" <%= checkedStr %>><%= option.getOption() %> 
										</li>
							<%		}
								}
							%>
						</ul>
					</div>
				</div>
          		<span class="help-inline applyerror" id="optionsError"></span>
			</div>
		</div>

		<section class="lft_menus_container adminaddtheme">
			 <span	class="siteaccordion openreg"> 
			 	<span>
			 		<s:text	name='place.hldr.cust.add.frameworktheme' />
				</span>
		    </span>
			<div class="mfbox siteinnertooltiptxt hideContent" style="display: none;">
				<div class="scrollpanel">
					<section class="scrollpanel_inner"> 

						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.customerBasedColor' /> 
							</label>
							<div class="controls">
								<input id="cusomerBasecolor" placeholder="<s:text name='place.hldr.cust.customerBaseColor'/>"class="input-xlarge" type="text" name="cusomerBasecolor"
									value="<%= customerBaseColor %>" maxlength="50" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>

						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.loginLogoMargin' /> 
							</label>
							<div class="controls">
								<input id="loginLogoMargin" placeholder="<s:text name='place.hldr.cust.loginLogoMargin'/>"class="input-xlarge" type="text" name="loginLogoMargin"
									value="<%= loginLogoMargin %>" maxlength="50" title="Margin in pixel eg: 10px">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold"> 
								<s:text	name='lbl.hdr.adm.cust.pageLogoPadding'/>
							 </label>
							<div class="controls">
								<input id="pageLogoPadding" placeholder="<s:text name='place.hldr.cust.pageLogoPadding'/>"class="input-xlarge" type="text" name="pageLogoPadding"
									value="<%= pageLogoPadding %>" maxlength="50" title="padding in pixed eg: 10px">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.headerLinkColor' /> 
							</label>
							<div class="controls">
								<input id="headerLinkColor" placeholder="<s:text name='place.hldr.cust.headerLinkColor'/>"class="input-xlarge" type="text" name="headerLinkColor"
									value="<%= headerLinkColor %>" maxlength="50" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.headerActiveLinkColor' /> 
							</label>
							<div class="controls">
								<input id="headerActiveLinkColor" placeholder="<s:text name='place.hldr.cust.headerActiveLinkColor'/>"class="input-xlarge" type="text" name="headerActiveLinkColor"
									value="<%= headerActiveLinkColor %>" maxlength="50" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.editNavigationLink' />
							 </label>
							<div class="controls">
								<input id="editNavigationLink" placeholder="<s:text name='place.hldr.cust.editNavigationLink'/>"class="input-xlarge" type="text" name="editNavigationLink"
									value="<%= editNavigationLink %>" maxlength="50" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.buttonBackGroundColor' />
							 </label>
							<div class="controls">
								<input id="buttonBackGroundColor" placeholder="<s:text name='place.hldr.cust.buttonBackGroundColor'/>"class="input-xlarge" type="text" name="buttonBackGroundColor"
									value="<%= buttonBackGroundColor %>" maxlength="50" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.consoleHeaderColor' />
							 </label>
							<div class="controls">
								<input id="consoleHeaderColor" placeholder="<s:text name='place.hldr.cust.consoleHeaderColor'/>"class="input-xlarge" type="text" name="consoleHeaderColor"
									value="<%= consoleHeaderColor %>" maxlength="50" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.copyrightLabelColor' />
							 </label>
							<div class="controls">
								<input id="copyrightLabelColor" placeholder="<s:text name='place.hldr.cust.copyrightLabelColor'/>"class="input-xlarge" type="text" name="copyrightLabelColor"
									value="<%= copyrightLabelColor %>" maxlength="50" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.welcomeUserIcon' />
							 </label>
							<div class="controls">
								<input id="welcomeUserIcon" placeholder="<s:text name='place.hldr.cust.welcomeusericon'/>"class="input-xlarge" type="text" name="welcomeUserIcon"
									value="<%= welcomeUserIcon %>" maxlength="50" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.PageTitleColor' />
							 </label>
							<div class="controls">
								<input id="welcomeUserIcon" placeholder="<s:text name='place.hldr.cust.PageTitleColor'/>"class="input-xlarge" type="text" name="welcomeUserIcon"
									value="<%= PageTitleColor %>" maxlength="50" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.CopyrightText' />
							 </label>
							<div class="controls">
								<textarea id="CopyrightText" placeholder="<s:text name='place.hldr.cust.CopyrightText'/>" class="input-xlarge" rows="3" 
				 			   		name="copyrightText" maxlength="150" title="web address link with anchor tag "><%= CopyrightText %></textarea>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								 <s:text name='lbl.hdr.adm.cust.CustomerTitle' />
							 </label>
							<div class="controls">
								<input id="CustomerTitle" placeholder="<s:text name='place.hldr.cust.CustomerTitle'/>"class="input-xlarge" type="text" name="CustomerTitle"
									value="<%= CustomerTitle %>" maxlength="50" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								<s:text name='lbl.hdr.adm.cust.headerBackGroundcolor'/>
							</label>
							<div class="controls">
								<input id="headerBackGroundcolorTop" placeholder="<s:text name='place.hldr.cust.headerBackGroundTopColor'/>" class="input-xlarge" type="text" name="headerBackGroundcolorTop"
								   value="<%= headerBackGroundcolorTop%>"  maxlength="25" title="Color Hashcode eg: #FFFFFF">
								 <input id="headerBackGroundcolorBottom" placeholder="<s:text name='place.hldr.cust.headerBackGroundBottomColor'/>" class="input-xlarge" type="text" name="headerBackGroundcolorBottom"
								   value="<%= headerBackGroundcolorBottom%>"  maxlength="25" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								<s:text name='lbl.hdr.adm.cust.footerBackGroundcolor'/>
							</label>
							<div class="controls">
								<input id="footerBackGroundcolorTop" placeholder="<s:text name='place.hldr.cust.footerBackGroundTopColor'/>" class="input-xlarge" type="text" name="footerBackGroundcolorTop"
								   value="<%= footerBackGroundcolorTop%>"  maxlength="25" title="Color Hashcode eg: #FFFFFF">
								 <input id="footerBackGroundcolorBottom" placeholder="<s:text name='place.hldr.cust.footerBackGroundBottomColor'/>" class="input-xlarge" type="text" name="footerBackGroundcolorBottom"
								   value="<%= footerBackGroundcolorBottom%>"  maxlength="25" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								<s:text name='lbl.hdr.adm.cust.pageTitleBackGroundcolor'/>
							</label>
							<div class="controls">
								<input id="pageTitleBackGroundTop" placeholder="<s:text name='place.hldr.cust.pageTitleBackGroundTopColor'/>" class="input-xlarge" type="text" name="pageTitleBackGroundTop"
								   value="<%= pageTitleBackGroundTop%>"  maxlength="25" title="Color Hashcode eg: #FFFFFF">
								 <input id="pageTitleBackGroundBottom" placeholder="<s:text name='place.hldr.cust.pageTitleBackGroundBottomColor'/>" class="input-xlarge" type="text" name="pageTitleBackGroundBottom"
								   value="<%= pageTitleBackGroundBottom%>"  maxlength="25" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								<s:text name='lbl.hdr.adm.cust.editNavigationActiveBackGroundcolor'/>
							</label>
							<div class="controls">
								<input id="editNavigationActiveBackGroundTop" placeholder="<s:text name='place.hldr.cust.editNavigationActiveBackGroundTopColor'/>" class="input-xlarge" type="text" name="editNavigationActiveBackGroundTop"
								   value="<%= editNavigationActiveBackGroundTop%>"  maxlength="25" title="Color Hashcode eg: #FFFFFF">
								 <input id="editNavigationActiveBackGroundBottom" placeholder="<s:text name='place.hldr.cust.editNavigationActiveBackGroundBottomColor'/>" class="input-xlarge" type="text" name="editNavigationActiveBackGroundBottom"
								   value="<%= editNavigationActiveBackGroundBottom%>"  maxlength="25" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label labelbold">
								<s:text name='lbl.hdr.adm.cust.bottomButtonPanelcolor'/>
							</label>
							<div class="controls">
								<input id="bottomButtonPanelTop" placeholder="<s:text name='place.hldr.cust.bottomButtonPanelTopcolor'/>" class="input-xlarge" type="text" name="bottomButtonPanelTop"
								   value="<%= bottomButtonPanelTop %>"  maxlength="25" title="Color Hashcode eg: #FFFFFF">
								<input id="bottomButtonPanelBottom" placeholder="<s:text name='place.hldr.cust.bottomButtonPanelBottomcolor'/>" class="input-xlarge" type="text" name="bottomButtonPanelBottom"
								   value="<%= bottomButtonPanelBottom %>"  maxlength="25" title="Color Hashcode eg: #FFFFFF">
							</div>
						</div>
						
						<div class="control-group" id="iconControl">
							<label class="control-label labelbold"> 
							 	<s:text name='LoginLogoImage' />
							</label>
							<div class="controls" style="float: left; margin-left: 3%;">
								<div id="login-image-file-uploader" class="file-uploader" title ="<s:text name='title.icon.size'/>">
									<noscript>
										<p>Please enable JavaScript to use file uploader.</p>s
										<!-- or put a simple form for upload here -->
									</noscript>
								</div>
							</div>
							<span class="help-inline fileError" id="iconError"></span>
						</div>
					</section>
				</div>
			</div>
		</section>

	</div>
	<div class="bottom_button">
		<input type="button" id="" class="btn <%=disabledClass%> <%= per_disabledClass %>" <%= per_disabledStr %> <%= disabled %> value="<%= buttonLbl %>" 
			 onclick="validate('<%= pageUrl %>', $('#formCustomerAdd'), $('#subcontainer'), '<%= progressTxt %>', $('.content_adder :input'));" />
		<input type="button" id="customerCancel" class="btn btn-primary" value="<s:text name='lbl.btn.cancel'/>" onclick="getCustomersList();" />
	</div>
	<!-- Hidden Fields -->
	<input type="hidden" name="fromPage" value="<%= StringUtils.isNotEmpty(fromPage) ? fromPage : "" %>"/>
	<input type="hidden" name="customerId" value="<%= id %>"/>
	<input type="hidden" name="oldName" value="<%= name %>"/>
	<input type="hidden" name="snapshotRepoUrl" value="<%= snapShotRepoUrl %>">
	<input type="hidden" name="groupRepoUrl" value="<%= groupRepoUrl %>">
	<input type="hidden" name="baseRepoUrl" value="<%= baseRepoUrl %>">
	<input type="hidden" name="oldContext" value="<%= context %>"/>
</form>

<script type="text/javascript">
	//To check whether the device is ipad or not and then apply jquery scrollbar
	
	if (!isiPad()) {
		$(".content_adder").scrollbars();
		$(".multilist-scroller").scrollbars();
		$(".accordion_panel_inner").scrollbars();		
	}
	
	$(document).ready(function() {
		hideLoadingIcon();
		setLicenseType();
		createLoginLogoUploader();
		checkboxEvent($('#checkAllAuto'),'applsChk');
		checkboxEvent($('#checkAllOptions'), 'optionsChk');
		 // for edit - to show selected country while page loads 
		 $("#countryList option[value='<%= country %>']").attr('selected', 'selected'); 
		 
		// To check for the special character in name
        $('#custmname').bind('input propertychange', function (e) {
            var name = $(this).val();
            name = checkForSplChr(name);
            $(this).val(name);        
		});
		
		// To check allow only underscore and hypen in context
		$('#context').bind('input propertychange', function (e) {
			var context = $(this).val();
			context = allowHypenUnderscore(context);
			$(this).val(context);
		});
		
    	// To check for the special character in state
        $('#statefld').bind('input propertychange', function (e) {
            var state = $(this).val();
            statevalue = allowAlpha(state);
            $(this).val(statevalue);
		});
    	
     	// To check for the special character in zipcode
        $('#zipcodefld').bind('input propertychange', function (e) {
            var zipcode = $(this).val();
            zipcode = checkForSplChr(zipcode);
            $(this).val(zipcode); 
            $(this).focus();
		});
     	
     	// To check for the special character in ContactNumber and fax
        $('#contactNo, #faxfld').bind('input propertychange', function (e) {
            var numbr = $(this).val();
            numbr = allowNumHyphenPlus(numbr);
            $(this).val(numbr);        
		});
     	
        $("#repourl").blur(function(event) {
        	if ($(this).val() != "") {
				$('#repousername').addClass('outLineColor');
				$('#repopassword').addClass('outLineColor');
        	}
 		});
        
        $("#repousername").blur(function(event) {
        	if ($(this).val() != "") {
				$('#repousername').removeClass('outLineColor');
        	}
 		});
        
        $("#repopassword").blur(function(event) {
        	if ($(this).val() != "") {
				$('#repopassword').removeClass('outLineColor');
        	}
 		});
     	
		// Date picker
		<% if (StringUtils.isEmpty(fromPage)) { %>
				document.getElementById('fromdate').value = '';
				document.getElementById('todate').value = '';
		<% } %>
		
		$(function() {
			$("#fromdate").datepicker({
				showOn : "button",
				buttonImage : "images/calendar.gif",
				buttonImageOnly : true
			});
			
			$("#todate").datepicker({
				showOn : "button",
				buttonImage : "images/calendar.gif",
				buttonImageOnly : true
			});
		});
	});
	
	function getCustomersList() {
		showLoadingIcon();
		loadContent('customerList', $('#formCustomerAdd'), $('#subcontainer'));
	}
	
	function createLoginLogoUploader() {
		var loginLogoImgUploader = new qq.FileUploader ({
            element: document.getElementById('login-image-file-uploader'),
            action: 'uploadLoginLogoIcon',
            multiple: false,
            allowedExtensions : ["png"],
            uploadId: 'customerUploadId',
            type: 'customerImageFile',
            buttonLabel: '<s:label key="lbl.hdr.adm.upload.login.logo" />',
            typeError : '<s:text name="err.invalid.img.file" />',
            params: {type: 'customerImageFile'}, 
            debug: true
        });
	}
	
	function removeUploadedJar(obj, btnId) {
		$(obj).parent().remove();
		var type = $(obj).attr("tempattr"); 
		var params = "";
		$.ajax({
			url : "removeImage",
			data : params,
			type : "POST",
			success : function(data) {
			}
		});
		enableDisableUploads(type, $("#" + btnId));
	} 
	
	function jarError(data, type) {
		var controlObj;
		var msgObj;
		if (type == "customerImageFile") {
			controlObj = $("#iconControl");
			msgObj = $("#iconError");
		}
		if (data != undefined && !isBlank(data)) {
			showError(controlObj, msgObj, data);
		} else {
			hideError(controlObj, msgObj);			 
		}
	}
	
	//To show/hide the username and password field
	//It will be enabled only when the user gives the repo URL
	$('#repourl').live('input', function() {
		if (!isBlank($(this).val())) {
			$(".repoMndatory").show();
		} else {
			$(".repoMndatory").hide();
		}
	});
	
	$('#reponame').bind('input propertychange', function (e) {
		var reponame = $(this).val();
		reponame = allowAlphaNum(reponame);
		$(this).val(reponame.trim());
	});
	
	
	function setLicenseType() {
		var license = '<%= licenseType %>';
		  $("select#licenseType option").filter(function() {
		     return $(this).text() == license; 
		 }).attr('selected', true); 
	} 
	 
	function findError(data) {
		if (!isBlank(data.nameError)) {
			showError($("#nameControl"), $("#nameError"), data.nameError);
		} else {
			hideError($("#nameControl"), $("#nameError"));
		}
		
		if (!isBlank(data.mailError)) {
			showError($("#mailControl"), $("#mailError"), data.mailError);
		} else {
			hideError($("#mailControl"), $("#mailError"));
		}
		
		if (!isBlank(data.addressError)) {
			showError($("#addresControl"), $("#addresError"), data.addressError);
		} else {
			hideError($("#addresControl"), $("#addresError"));
		}
		
		if (!isBlank(data.zipError)) {
			showError($("#zipControl"), $("#zipError"), data.zipError);
		} else {
			hideError($("#zipControl"), $("#zipError"));
		}
		
		if (!isBlank(data.numError)) {
			showError($("#numControl"), $("#numError"), data.numError);
		} else {
			hideError($("#numControl"), $("#numError"));
		}
		
		if (!isBlank(data.faxError)) {
			showError($("#faxControl"), $("#faxError"), data.faxError);
		} else {
			hideError($("#faxControl"), $("#faxError"));
		}
		
		if (!isBlank(data.conError)) {
			showError($("#conControl"), $("#conError"), data.conError);
		} else {
			hideError($("#conControl"), $("#conError"));
		}
		
		if (!isBlank(data.licenError)) {
			showError($("#licenControl"), $("#licenError"), data.licenError);
		} else {
			hideError($("#licenControl"), $("#licenError"));
		}
		
		if (!isBlank(data.repoNameError)) {
			showError($("#repoNameControl"), $("#repoNameError"), data.repoNameError);
		} else {
			hideError($("#repoNameControl"), $("#repoNameError"));
		}
		
		if (!isBlank(data.repoUserNameError)) {
			showError($("#repoUserNameControl"), $("#repoUserNameError"), data.repoUserNameError);
		} else {
			hideError($("#repoUserNameControl"), $("#repoUserNameError"));
		}
		
		if (!isBlank(data.repoPasswordError)) {
			showError($("#repoPasswordControl"), $("#repoPasswordError"), data.repoPasswordError);
		} else {
			hideError($("#repoPasswordControl"), $("#repoPasswordError"));
		}
		
		if (!isBlank(data.repoURLError)) {
			showError($("#repoURLControl"), $("#repoURLError"), data.repoURLError);
		} else {
			hideError($("#repoURLControl"), $("#repoURLError"));
		}
		
		if (!isBlank(data.contextError)) {
			showError($("#contextControl"), $("#contextError"), data.contextError);
		} else {
			hideError($("#contextControl"), $("#contextError"));
		}
		
		if (!isBlank(data.applyError)) {
			showError($("#applyControl"), $("#applyError"), data.applyError);
		} else {
			hideError($("#applyControl"), $("#applyError"));
		}
	}
</script>