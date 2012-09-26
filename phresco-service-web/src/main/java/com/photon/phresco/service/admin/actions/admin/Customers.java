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
package com.photon.phresco.service.admin.actions.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.commons.model.Customer;
import com.photon.phresco.commons.model.Customer.LicenseType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.util.ServiceConstants;
import com.sun.jersey.api.client.ClientResponse;

public class Customers extends ServiceBaseAction  { 
	
	private static final long serialVersionUID = 6801037145464060759L;
	private static final Logger S_LOGGER = Logger.getLogger(Customers.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private String name = null;
	private String nameError = null;
	private String description = null;
	private String email = null;
	private String mailError = null;
	private String address = null;
	private String addressError = null;
	private String zipcode = null;
	private String zipError = null;
	private String number = null;
	private String numError = null;
	private String fax = null;
	private String faxError = null;
	private String country = null;
	private String conError = null;
	private String state = null;
    private String licence = null;
	private String licenError = null;
	private boolean errorFound = false;
	private Date validFrom = null;
	private Date validUpTo = null;
	private String repoURL = null;
	private String fromPage = null;
	private String customerId = null;
	private String oldName = null;
	private String helpText = null;

    public String list() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Customers.list()");
	    }
		
		try {
            List<Customer> customers = getServiceManager().getCustomers();
			getHttpRequest().setAttribute(REQ_CUST_CUSTOMERS, customers);
		} catch (PhrescoException e) {
//			new LogErrorReport(e, CUSTOMERS_LIST_EXCEPTION);
			
			return LOG_ERROR;	
		}
		
		return ADMIN_CUSTOMER_LIST;	
	}
	
	public String add() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Customers.add()");
	    }
		
		return ADMIN_CUSTOMER_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method Customers.edit()");
		}

		try {
			Customer customer = getServiceManager().getCustomer(customerId);
			getHttpRequest().setAttribute(REQ_CUST_CUSTOMER, customer);
			getHttpRequest().setAttribute(REQ_FROM_PAGE, EDIT);
		} catch (PhrescoException e) {
//			new LogErrorReport(e, CUSTOMERS_ADD_EXCEPTION);
			
			return LOG_ERROR;	
		}
		
		return ADMIN_CUSTOMER_ADD;
	}
	
	public String save() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Customers.save()");
	    }
	    
		try {
			List<Customer> customers = new ArrayList<Customer>();
			Customer customer = new Customer();
			customer.setName(name);
			customer.setDescription(description);
			customer.setEmailId(email);
			customer.setAddress(address);
			customer.setCountry(country);
			customer.setState(state);
			customer.setZipcode(zipcode);
			customer.setContactNumber(number);
			customer.setFax(fax);
			customer.setHelpText(helpText);
			LicenseType licenceType = LicenseType.valueOf(licence);
			customer.setType(licenceType);
			customer.setValidFrom(validFrom);
			customer.setValidUpto(validUpTo);
			customers.add(customer);
			ClientResponse clientResponse = getServiceManager().createCustomers(customers);
			if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200) {
				addActionError(getText(CUSTOMER_NOT_ADDED, Collections.singletonList(name)));
			} else {
				addActionMessage(getText(CUSTOMER_ADDED, Collections.singletonList(name)));
			}
		} catch (PhrescoException e) {
//			new LogErrorReport(e, CUSTOMERS_SAVE_EXCEPTION);
			
			return LOG_ERROR;	
		}
		
		return list();
	}

	public String update() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Customers.update()");
	    }

		try {
			Customer customer = new Customer();
			customer.setId(customerId);
			customer.setName(name);
			customer.setDescription(description);
			customer.setEmailId(email);
            customer.setAddress(address);
            customer.setCountry(country);
            customer.setState(state);
            customer.setZipcode(zipcode);
            customer.setContactNumber(number);
            customer.setFax(fax);
            customer.setHelpText(helpText);
            LicenseType licenceType = LicenseType.valueOf(licence);
            customer.setType(licenceType);
            customer.setValidFrom(validFrom);
            customer.setValidUpto(validUpTo);
			getServiceManager().updateCustomer(customer, customerId);
		} catch (PhrescoException e) {
//			new LogErrorReport(e, CUSTOMERS_UPDATE_EXCEPTION);
			
			return LOG_ERROR;	
		}
		
		return list();
	}
	
	public String delete() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Customers.delete()");
	    }
		
		try {
			String[] customerIds = getHttpRequest().getParameterValues(REQ_CUST_CUSTOMER_ID);
			if (customerIds != null) {
				for (String customerId : customerIds) {
			    	ClientResponse clientResponse =getServiceManager().deleteCustomer(customerId);
			    	if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200) {
			        	addActionError(getText(CUSTOMER_NOT_DELETED));
			        }
				}
				addActionMessage(getText(CUSTOMER_DELETED));
			}
		} catch (PhrescoException e) {
//			new LogErrorReport(e, CUSTOMERS_DELETE_EXCEPTION);
			
			return LOG_ERROR;	
		}
		
		return list();
	}
	
	public String validateForm() throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entering Method Customers.validateForm()");
        }
	    
		boolean isError = false;
		if (StringUtils.isEmpty(name)) {
			setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
			isError = true;
		} else if (StringUtils.isEmpty(fromPage) || (!name.equals(oldName))) {
			// to check duplication of name
			List<Customer> customers = getServiceManager().getCustomers();
			if (customers != null) {
				for (Customer customer : customers) {
					if (customer.getName().equalsIgnoreCase(name)) {
						setNameError(getText(KEY_I18N_ERR_NAME_ALREADY_EXIST));
			    		isError = true;
						break;
					}
				}
			}
		}
		
		if (StringUtils.isEmpty(email)) {
			setMailError(getText(KEY_I18N_ERR_EMAIL_EMPTY));
			isError = true;
		} 
		
		if (StringUtils.isNotEmpty(email)) {
	   		Pattern p = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	   		Matcher m = p.matcher(email);
	   		boolean b = m.matches();
	   		if (!b) {
	   			setMailError(getText(INVALID_EMAIL));
	   			isError = true;
	   		}
	   	}
		
		if (StringUtils.isEmpty(address)) {
			setAddressError(getText(KEY_I18N_ERR_ADDRS_EMPTY));
			isError = true;
		} 
		
		if (StringUtils.isEmpty(zipcode)) {
			setZipError(getText(KEY_I18N_ERR_ZIPCODE_EMPTY));
			isError = true;
		} 
		
		if (StringUtils.isEmpty(number)) {
			setNumError(getText(KEY_I18N_ERR_CONTNUM_EMPTY));
			isError = true;
		} 
		
		if (StringUtils.isEmpty(fax)) {
			setFaxError(getText(KEY_I18N_ERR_FAXNUM_EMPTY));
			isError = true;
		} 
		
		if (StringUtils.isEmpty(country)) {
			setConError(getText(KEY_I18N_ERR_COUN_EMPTY));
			isError = true;
		} 
		
		if (StringUtils.isEmpty(licence)) {
			setLicenError(getText(KEY_I18N_ERR_LICEN_EMPTY));
			isError = true;
		}
		
		if (isError) {
            setErrorFound(true);
        }
		
		return SUCCESS;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNameError() {
		return nameError;
	}
	
	public void setNameError(String nameError) {
		this.nameError = nameError;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMailError() {
		return mailError;
	}

	public void setMailError(String mailError) {
		this.mailError = mailError;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressError() {
		return addressError;
	}

	public void setAddressError(String addressError) {
		this.addressError = addressError;
	}
	
	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getZipError() {
		return zipError;
	}

	public void setZipError(String zipError) {
		this.zipError = zipError;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumError() {
		return numError;
	}

	public void setNumError(String numError) {
		this.numError = numError;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFaxError() {
		return faxError;
	}

	public void setFaxError(String faxError) {
		this.faxError = faxError;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getConError() {
		return conError;
	}

	public void setConError(String conError) {
		this.conError = conError;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public String getLicenError() {
		return licenError;
	}

	public void setLicenError(String licenError) {
		this.licenError = licenError;
	}
	
	public boolean isErrorFound() {
		return errorFound;
	}
	
	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getRepoURL() {
		return repoURL;
	}

	public void setRepoURL(String repoURL) {
		this.repoURL = repoURL;
	}
	
	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidUpTo() {
		return validUpTo;
	}

	public void setValidUpTo(Date validUpTo) {
		this.validUpTo = validUpTo;
	}
	
	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	
	public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }
}