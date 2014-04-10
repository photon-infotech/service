/**
 * Phresco Service
 *
 * Copyright (C) 1999-2014 Photon Infotech Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.photon.phresco.service.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.photon.phresco.commons.model.ApplicationType;
import com.photon.phresco.commons.model.Customer.LicenseType;
import com.photon.phresco.commons.model.Customer.UIType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDAO extends BaseDAO {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private String emailId;
    
	/**
     * 
     */
    private String address;
    
    /**
     * 
     */
    private String country;
    
    /**
     * 
     */
    private String state;
    
    /**
     * 
     */
    private String zipcode;
    
    /**
     * 
     */
    private String contactNumber;
    
    /**
     * 
     */
    private String fax;
	
    /**
	 * 
	 */
	private Date validFrom;
	
	/**
	 * 
	 */
	private Date validUpto;
	
	/**
	 * 
	 */
	private LicenseType type;
	
	/**
	 * 
	 */
	private String repoInfoId;
	
	/**
	 * 
	 */
	private String icon;
	
	/**
	 * 
	 */
	private List<String> applicableTechnologies;
	
	/**
	 * 
	 */
	private List<ApplicationType> applicableAppTypes;
	
	/**
	 * 
	 */
	private Map<String, String> frameworkTheme;
	
	/**
	 * 
	 */
	private List<String> options;
	
	/**
	 * 
	 */
	private String context;
	
	/**
	 * 
	 */
	private UIType uiType;

	/**
	 * 
	 */
	private String supportEmail;
	/**
	 * 
	 */
	private String supportPassword;
	/**
	 * 
	 */
	private String supportSmtpHost;
	
	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return this.emailId;
	}

	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return this.zipcode;
	}

	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the contactNumber
	 */
	public String getContactNumber() {
		return this.contactNumber;
	}

	/**
	 * @param contactNumber the contactNumber to set
	 */
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return this.fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the validFrom
	 */
	public Date getValidFrom() {
		return this.validFrom;
	}

	/**
	 * @param validFrom the validFrom to set
	 */
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	/**
	 * @return the validUpto
	 */
	public Date getValidUpto() {
		return this.validUpto;
	}

	/**
	 * @param validUpto the validUpto to set
	 */
	public void setValidUpto(Date validUpto) {
		this.validUpto = validUpto;
	}

	/**
	 * @return the type
	 */
	public LicenseType getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(LicenseType type) {
		this.type = type;
	}

	/**
	 * @return the repoInfoId
	 */
	public String getRepoInfoId() {
		return this.repoInfoId;
	}

	/**
	 * @param repoInfoId the repoInfoId to set
	 */
	public void setRepoInfoId(String repoInfoId) {
		this.repoInfoId = repoInfoId;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return this.icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the applicableTechnologies
	 */
	public List<String> getApplicableTechnologies() {
		return this.applicableTechnologies;
	}

	/**
	 * @param applicableTechnologies the applicableTechnologies to set
	 */
	public void setApplicableTechnologies(List<String> applicableTechnologies) {
		this.applicableTechnologies = applicableTechnologies;
	}

	/**
	 * @return the applicableAppTypes
	 */
	public List<ApplicationType> getApplicableAppTypes() {
		return this.applicableAppTypes;
	}

	/**
	 * @param applicableAppTypes the applicableAppTypes to set
	 */
	public void setApplicableAppTypes(List<ApplicationType> applicableAppTypes) {
		this.applicableAppTypes = applicableAppTypes;
	}

	/**
	 * @return the frameworkTheme
	 */
	public Map<String, String> getFrameworkTheme() {
		return this.frameworkTheme;
	}

	/**
	 * @param frameworkTheme the frameworkTheme to set
	 */
	public void setFrameworkTheme(Map<String, String> frameworkTheme) {
		this.frameworkTheme = frameworkTheme;
	}

	/**
	 * @return the options
	 */
	public List<String> getOptions() {
		return this.options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(List<String> options) {
		this.options = options;
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return this.context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @param uiType
	 */
	public void setUiType(UIType uiType) {
		this.uiType = uiType;
	}

	/**
	 * @return
	 */
	public UIType getUiType() {
		return uiType;
	}

	/**
	 * @return
	 */
	public String getSupportEmail() {
		return supportEmail;
	}

	/**
	 * @param supportEmail
	 */
	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}

	/**
	 * @return
	 */
	public String getSupportPassword() {
		return supportPassword;
	}

	/**
	 * @param supportPassword
	 */
	public void setSupportPassword(String supportPassword) {
		this.supportPassword = supportPassword;
	}

	/**
	 * @return
	 */
	public String getSupportSmtpHost() {
		return supportSmtpHost;
	}

	/**
	 * @param supportSmtpHost
	 */
	public void setSupportSmtpHost(String supportSmtpHost) {
		this.supportSmtpHost = supportSmtpHost;
	}
}