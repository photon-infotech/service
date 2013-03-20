package com.photon.phresco.service.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.photon.phresco.commons.model.ApplicationType;
import com.photon.phresco.commons.model.Customer.LicenseType;
import com.photon.phresco.commons.model.RepoInfo;

public class CustomerDAO extends BaseDAO {

	private static final long serialVersionUID = 1L;
	
	private String emailId;
    private String address;
    private String country;
    private String state;
    private String zipcode;
    private String contactNumber;
    private String fax;
	private Date validFrom;
	private Date validUpto;
	private LicenseType type;
	private RepoInfo repoInfo;
	private String icon;
	private List<String> applicableTechnologies;
	private List<ApplicationType> applicableAppTypes;
	private Map<String, String> frameworkTheme;
	private List<String> options;
	
	public String getEmailId() {
		return emailId;
	}
	
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getZipcode() {
		return zipcode;
	}
	
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String getContactNumber() {
		return contactNumber;
	}
	
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	public String getFax() {
		return fax;
	}
	
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public Date getValidFrom() {
		return validFrom;
	}
	
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	
	public Date getValidUpto() {
		return validUpto;
	}
	
	public void setValidUpto(Date validUpto) {
		this.validUpto = validUpto;
	}
	
	public LicenseType getType() {
		return type;
	}
	
	public void setType(LicenseType type) {
		this.type = type;
	}
	
	public RepoInfo getRepoInfo() {
		return repoInfo;
	}
	
	public void setRepoInfo(RepoInfo repoInfo) {
		this.repoInfo = repoInfo;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public List<String> getApplicableTechnologies() {
		return applicableTechnologies;
	}
	
	public void setApplicableTechnologies(List<String> applicableTechnologies) {
		this.applicableTechnologies = applicableTechnologies;
	}
			
	public Map<String, String> getFrameworkTheme() {
		return frameworkTheme;
	}
	
	public void setFrameworkTheme(Map<String, String> frameworkTheme) {
		this.frameworkTheme = frameworkTheme;
	}
	
	public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }
	
}
