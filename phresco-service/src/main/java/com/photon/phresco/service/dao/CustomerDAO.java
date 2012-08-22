package com.photon.phresco.service.dao;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.photon.phresco.commons.model.Customer.CustomerStatus;

public class CustomerDAO extends BaseDAO {
    
    private String name;
    private String description;
    private String emailId;
    private String address;
    private String country;
    private String state;
    private String zipcode;
    private String contactNumber;
    private String fax;
    private String helpText;
    Date validFrom;
    Date validUpto;
    private CustomerStatus status;
    private int type;
    
    public CustomerDAO() {
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * @param emailId
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * @param zipcode
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * @return
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * @param contactNumber
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * @return
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return
     */
    public String getHelpText() {
        return helpText;
    }

    /**
     * @param helpText
     */
    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    /**
     * @return
     */
    public Date getValidFrom() {
        return validFrom;
    }

    /**
     * @param validFrom
     */
    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    /**
     * @return
     */
    public Date getValidUpto() {
        return validUpto;
    }

    /**
     * @param validUpto
     */
    public void setValidUpto(Date validUpto) {
        this.validUpto = validUpto;
    }

    /**
     * @return
     */
    public CustomerStatus getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    /**
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }
    
    public String toString() {
        return new ToStringBuilder(this,
                ToStringStyle.DEFAULT_STYLE)
                .append(super.toString())
                .append("emailId", emailId)
                .append("address", address)
                .append("country", country)
                .append("state", state)
                .append("zipcode", zipcode)
                .append("contactNumber", contactNumber)
                .append("fax", fax)
                .append("helpText", helpText)
                .append("validFrom", validFrom)
                .append("validUpto", validUpto)
                .append("status", status)
                .append("type", type)
                .toString();
    }
}
