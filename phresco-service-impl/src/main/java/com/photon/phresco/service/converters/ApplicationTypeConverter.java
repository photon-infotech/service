/**
 * Phresco Service Implemenation
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
package com.photon.phresco.service.converters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.document.mongodb.MongoOperations;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.ApplicationType;
import com.photon.phresco.commons.model.TechnologyGroup;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.ApplicationTypeDAO;
import com.photon.phresco.util.ServiceConstants;

public class ApplicationTypeConverter implements Converter<ApplicationTypeDAO, ApplicationType>, ServiceConstants {

	@Override
	public ApplicationType convertDAOToObject(ApplicationTypeDAO applicationTypeDAO,
			MongoOperations mongoOperation) throws PhrescoException {
		ApplicationType applicationType = new ApplicationType();
		applicationType.setId(applicationTypeDAO.getId());
		applicationType.setName(applicationTypeDAO.getName());
		applicationType.setSystem(applicationTypeDAO.isSystem());
		applicationType.setDescription(applicationTypeDAO.getDescription());
		if(CollectionUtils.isNotEmpty(applicationTypeDAO.getTechGroupIds())) {
			List<TechnologyGroup> techGroups = mongoOperation.find(TECH_GROUP_COLLECTION_NAME, 
					new Query(Criteria.whereId().in(applicationTypeDAO.getTechGroupIds().toArray())), TechnologyGroup.class);
			applicationType.setTechGroups(techGroups);
		}
		applicationType.setCustomerIds(applicationTypeDAO.getCustomerIds());
		return applicationType;
	}

	@Override
	public ApplicationTypeDAO convertObjectToDAO(ApplicationType applicationType)
			throws PhrescoException {
		ApplicationTypeDAO applicationTypeDAO = new ApplicationTypeDAO();
		applicationTypeDAO.setId(applicationType.getId());
		applicationTypeDAO.setName(applicationType.getName());
		List<String> techIds = new ArrayList<String>();
		List<TechnologyGroup> techGroups = applicationType.getTechGroups();
		for (TechnologyGroup technologyGroup : techGroups) {
			techIds.add(technologyGroup.getId());
		}
		applicationTypeDAO.setTechGroupIds(techIds);
		applicationTypeDAO.setSystem(applicationType.isSystem());
		applicationTypeDAO.setCustomerIds(applicationType.getCustomerIds());
		applicationTypeDAO.setDescription(applicationType.getDescription());
		return applicationTypeDAO;
	}

}
