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

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.photon.phresco.commons.model.Status;

public class UserDAO extends BaseDAO {

	private String userId;
	private String email;
	private String firstName;
	private String lastName;
	private Status status;
	private List<String> roleIds;
	private List<String> customerIds;

	public UserDAO() {
		super();
	}
	
	/**
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return
	 */
	public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return
     */
    public List<String> getRoleIds() {
		return roleIds;
	}
	
	/**
	 * @param roleIds
	 */
	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}
	
	/**
	 * @return
	 */
	public List<String> getCustomerIds() {
		return customerIds;
	}
	
	/**
	 * @param customerIds
	 */
	public void setCustomerIds(List<String> customerIds) {
		this.customerIds = customerIds;
	}
	
	public String toString() {
        return new ToStringBuilder(this,
                ToStringStyle.DEFAULT_STYLE)
                .append(super.toString())
                .append("userId", userId)
                .append("email", email)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("status", status)
                .append("roleIds", roleIds)
                .append("customerIds", customerIds)
                .toString();
    }
}