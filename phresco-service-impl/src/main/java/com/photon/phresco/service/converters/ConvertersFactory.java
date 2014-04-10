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
package com.photon.phresco.service.converters;

import java.util.HashMap;
import java.util.Map;

import com.photon.phresco.commons.model.Element;
import com.photon.phresco.framework.converters.FrameworkApplicationInfoConverter;
import com.photon.phresco.framework.converters.FrameworkArtifactGroupConverter;
import com.photon.phresco.framework.converters.FrameworkCustomerConverter;
import com.photon.phresco.framework.converters.FrameworkProjectInfoConverter;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.ApplicationInfoDAO;
import com.photon.phresco.service.dao.ApplicationTypeDAO;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.dao.BaseDAO;
import com.photon.phresco.service.dao.CustomerDAO;
import com.photon.phresco.service.dao.DownloadsDAO;
import com.photon.phresco.service.dao.ProjectInfoDAO;
import com.photon.phresco.service.dao.TechnologyDAO;
import com.photon.phresco.service.dao.VideoInfoDAO;
import com.photon.phresco.service.dao.VideoTypeDAO;

/**
 * @author kumar_s
 *
 */
public final class ConvertersFactory {
	
	private ConvertersFactory() {
		
	}
	
	/**
	 * 
	 */
	private static final Map<Class<? extends BaseDAO>, Converter<? extends BaseDAO, ? extends Element>> CONVERTERS_MAP = 
		new HashMap<Class<? extends BaseDAO>, Converter<? extends BaseDAO, ? extends Element>>(32);  
	
	/**
	 * 
	 */
	private static final Map<Class<? extends BaseDAO>, Converter<? extends BaseDAO, ? extends Element>> FRAMEWORK_CONVERTERS_MAP = 
		new HashMap<Class<? extends BaseDAO>, Converter<? extends BaseDAO, ? extends Element>>(32);
	
	static {
		initConverters();
		initFrameworkConverters();
	}

	/**
	 * 
	 */
	private static void initConverters() {
		CONVERTERS_MAP.put(ArtifactGroupDAO.class, new ArtifactGroupConverter());
		CONVERTERS_MAP.put(TechnologyDAO.class, new TechnologyConverter());
		CONVERTERS_MAP.put(ProjectInfoDAO.class, new ProjectInfoConverter());
		CONVERTERS_MAP.put(DownloadsDAO.class, new DownloadsConverter());
		CONVERTERS_MAP.put(ApplicationInfoDAO.class, new ApplicationInfoConverter());
		CONVERTERS_MAP.put(VideoTypeDAO.class, new VideoTypeConverter());
		CONVERTERS_MAP.put(VideoInfoDAO.class, new VideoConverter());
		CONVERTERS_MAP.put(ApplicationTypeDAO.class, new ApplicationTypeConverter());
		CONVERTERS_MAP.put(CustomerDAO.class, new CustomerConverter());
	}
	
	/**
	 * 
	 */
	private static void initFrameworkConverters() {
		FRAMEWORK_CONVERTERS_MAP.put(CustomerDAO.class, new FrameworkCustomerConverter());
		FRAMEWORK_CONVERTERS_MAP.put(ArtifactGroupDAO.class, new FrameworkArtifactGroupConverter());
		FRAMEWORK_CONVERTERS_MAP.put(ProjectInfoDAO.class, new FrameworkProjectInfoConverter());
		FRAMEWORK_CONVERTERS_MAP.put(ApplicationInfoDAO.class, new FrameworkApplicationInfoConverter());
	}

	/**
	 * @param clazz
	 * @return
	 */
	public static Converter<? extends BaseDAO, ? extends Element> getConverter(Class<? extends BaseDAO> clazz) {
		return CONVERTERS_MAP.get(clazz);
	}
	
	/**
	 * @param clazz
	 * @return
	 */
	public static Converter<? extends BaseDAO, ? extends Element> getFrameworkConverter(Class<? extends BaseDAO> clazz) {
		return FRAMEWORK_CONVERTERS_MAP.get(clazz);
	}
	
}