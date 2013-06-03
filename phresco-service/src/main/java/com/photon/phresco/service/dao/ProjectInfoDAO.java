/**
 * Phresco Service
 *
 * Copyright (C) 1999-2013 Photon Infotech Inc.
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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectInfoDAO extends CustomerBaseDAO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1573457137995213967L;
	
	/**
	 * 
	 */
	private String projectCode;
	
	/**
	 * 
	 */
	private List<String> applicationInfoIds;
	
	/**
	 * 
	 */
	private Date startDate;
    
	/**
     * 
     */
    private Date endDate;
	
	/**
	 * 
	 */
	public ProjectInfoDAO() {
		super();
	}

	/**
	 * @return the projectCode
	 */
	public String getProjectCode() {
		return this.projectCode;
	}

	/**
	 * @param projectCode the projectCode to set
	 */
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	/**
	 * @return the applicationInfoIds
	 */
	public List<String> getApplicationInfoIds() {
		return this.applicationInfoIds;
	}

	/**
	 * @param applicationInfoIds the applicationInfoIds to set
	 */
	public void setApplicationInfoIds(List<String> applicationInfoIds) {
		this.applicationInfoIds = applicationInfoIds;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return this.endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
