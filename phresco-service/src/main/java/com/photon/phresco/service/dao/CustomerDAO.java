/*
 * ###
 * Phresco Service
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

package com.photon.phresco.service.dao;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
    private Date validFrom;
    private Date validUpto;
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
                .append("type", type)
                .toString();
    }
}
