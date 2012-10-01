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

package com.photon.phresco.service.converters;

import java.util.HashMap;
import java.util.Map;

import com.photon.phresco.commons.model.Element;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.dao.BaseDAO;
import com.photon.phresco.service.dao.DownloadsDAO;
import com.photon.phresco.service.dao.ProjectInfoDAO;
import com.photon.phresco.service.dao.TechnologyDAO;

/**
 * @author kumar_s
 *
 */
public class ConvertersFactory {
	
	public static final Map<Class<? extends BaseDAO>, Converter<? extends BaseDAO, ? extends Element>> CONVERTERS_MAP = 
		new HashMap<Class<? extends BaseDAO>, Converter<? extends BaseDAO, ? extends Element>>(32);  
	
	static {
		initConverters();
	}

	private static void initConverters() {
		CONVERTERS_MAP.put(ArtifactGroupDAO.class, new ArtifactGroupConverter());
		CONVERTERS_MAP.put(TechnologyDAO.class, new TechnologyConverter());
		CONVERTERS_MAP.put(ProjectInfoDAO.class, new ProjectInfoConverter());
		CONVERTERS_MAP.put(DownloadsDAO.class, new DownloadsConverter());
	}

	public static final Converter<? extends BaseDAO, ? extends Element> getConverter(Class<? extends BaseDAO> clazz) {
		return CONVERTERS_MAP.get(clazz);
	}

}
