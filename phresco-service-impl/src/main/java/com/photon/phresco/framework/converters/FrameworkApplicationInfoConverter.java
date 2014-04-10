/**
 * Phresco Service Implemenation
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
package com.photon.phresco.framework.converters;

import org.springframework.data.document.mongodb.MongoOperations;
import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.ApplicationInfoDAO;
import com.photon.phresco.util.ServiceConstants;

public class FrameworkApplicationInfoConverter implements Converter<ApplicationInfoDAO, ApplicationInfo>, ServiceConstants{

	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(FrameworkApplicationInfoConverter.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	@Override
	public ApplicationInfo convertDAOToObject(ApplicationInfoDAO dao,
			MongoOperations mongoOperation) throws PhrescoException {
		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setCode(dao.getCode());
		applicationInfo.setVersion(dao.getVersion());
		applicationInfo.setCustomerIds(dao.getCustomerIds());
		applicationInfo.setDescription(dao.getDescription());
		applicationInfo.setId(dao.getId());
		applicationInfo.setName(dao.getName());
		applicationInfo.setSelectedComponents(dao.getSelectedComponents());
		applicationInfo.setSelectedDatabases(dao.getSelectedDatabases());
		applicationInfo.setSelectedFrameworks(dao.getSelectedFrameworks());
		applicationInfo.setSelectedJSLibs(dao.getSelectedJSLibs());
		applicationInfo.setSelectedModules(dao.getSelectedModules());
		applicationInfo.setSelectedServers(dao.getSelectedServers());
		applicationInfo.setSelectedWebservices(dao.getSelectedWebservices());
		applicationInfo.setTechInfo(dao.getTechInfo());
		applicationInfo.setPilot(dao.getPilot());
		applicationInfo.setSystem(dao.isSystem());
		applicationInfo.setFunctionalFramework(dao.getFunctionalFramework());
		applicationInfo.setFunctionalFrameworkInfo(dao.getFunctionalFrameworkInfo());
		applicationInfo.setShowServer(dao.isShowServer());
		applicationInfo.setShowDatabase(dao.isShowDatabase());
		applicationInfo.setShowWebservice(dao.isShowWebservice());
		applicationInfo.setShowTestingFramework(dao.isShowTestingFramework());
		return applicationInfo;
	}

	@Override
	public ApplicationInfoDAO convertObjectToDAO(ApplicationInfo applicationInfo)
			throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("ApplicationInfoConverter.convertObjectToDAO:Entry");
		}
		ApplicationInfoDAO applicationInfoDAO = new ApplicationInfoDAO();
		applicationInfoDAO.setId(applicationInfo.getId());
		applicationInfoDAO.setCode(applicationInfo.getCode());
		applicationInfoDAO.setDescription(applicationInfo.getDescription());
		applicationInfoDAO.setEmailSupported(applicationInfo.isEmailSupported());
		applicationInfoDAO.setName(applicationInfo.getName());
		applicationInfoDAO.setPilotInfo(applicationInfo.getPilotInfo());
		applicationInfoDAO.setSelectedComponents(applicationInfo.getSelectedComponents());
		applicationInfoDAO.setSelectedDatabases(applicationInfo.getSelectedDatabases());
		applicationInfoDAO.setSelectedFrameworks(applicationInfo.getSelectedFrameworks());
		applicationInfoDAO.setSelectedJSLibs(applicationInfo.getSelectedJSLibs());
		applicationInfoDAO.setSelectedModules(applicationInfo.getSelectedModules());
		applicationInfoDAO.setSelectedServers(applicationInfo.getSelectedServers());
		applicationInfoDAO.setSelectedWebservices(applicationInfo.getSelectedWebservices());
		applicationInfoDAO.setSystem(applicationInfo.isSystem());
		applicationInfoDAO.setTechInfo(applicationInfo.getTechInfo());
		applicationInfoDAO.setCustomerIds(applicationInfo.getCustomerIds());
		applicationInfoDAO.setVersion(applicationInfo.getVersion());
		if(applicationInfo.getPilotContent() != null) {
			applicationInfoDAO.setArtifactGroupId(applicationInfo.getPilotContent().getId());
		}
		applicationInfoDAO.setPhoneEnabled(applicationInfo.isPhoneEnabled());
		applicationInfoDAO.setTabletEnabled(applicationInfo.isTabletEnabled());
		applicationInfoDAO.setPilot(applicationInfo.isPilot());
		applicationInfoDAO.setFunctionalFramework(applicationInfo.getFunctionalFramework());
		applicationInfoDAO.setFunctionalFrameworkInfo(applicationInfo.getFunctionalFrameworkInfo());
		applicationInfoDAO.setShowServer(applicationInfo.isShowServer());
		applicationInfoDAO.setShowDatabase(applicationInfo.isShowDatabase());
		applicationInfoDAO.setShowWebservice(applicationInfo.isShowWebservice());
		applicationInfoDAO.setShowTestingFramework(applicationInfo.isShowTestingFramework());
		if (isDebugEnabled) {
			LOGGER.debug("ApplicationInfoConverter.convertObjectToDAO:Exit");
		}
		return applicationInfoDAO;
	}
}
