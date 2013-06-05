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
package com.photon.phresco.service.api;

import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.model.ServerConfiguration;
import com.photon.phresco.util.ServiceConstants;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration implements ServiceConstants{
	
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(MongoConfig.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	private ServerConfiguration config;
	
	public MongoConfig() throws PhrescoException {
		PhrescoServerFactory.initialize();
		config = PhrescoServerFactory.getServerConfig();
	}
	
	@Override
	@Bean 
	public Mongo mongo() throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("MongoConfig.mongo : Entry");
			LOGGER.debug("MongoConfig.mongo", "host=" + config.getDbHost(), "port=" + config.getDbPort());
		}
		Mongo mongo = null;
		try {
			mongo = new Mongo(config.getDbHost(), config.getDbPort());
		} catch (UnknownHostException e) {
			LOGGER.error("MongoConfig.mongo " , "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"" );
			throw new PhrescoException(e, EX_PHEX00002);
		} catch (MongoException e) {
			LOGGER.error("MongoConfig.mongo " , "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"" );
			throw new PhrescoException(e, EX_PHEX00003);
		}
		if(isDebugEnabled) {
			LOGGER.debug("MongoConfig.mongo : Exit");
		}
		return mongo;
	}

	@Override
	@Bean
	public MongoTemplate mongoTemplate() throws PhrescoException {
		if(isDebugEnabled) {
			LOGGER.debug("MongoConfig.mongoTemplate : Entry");
			LOGGER.debug("MongoConfig.mongoTemplate", "dbname=" + config.getDbName(), "dbcollection=" + config.getDbCollection(),
					"username=" + config.getDbUserName(), "password=" + config.getDbPassword());
		}
		MongoTemplate mongoTemplate = null;
		try {
			mongoTemplate = new MongoTemplate(mongo(), config.getDbName() , config.getDbCollection());
			if(StringUtils.isNotEmpty(config.getDbUserName())) {
				mongoTemplate.setUsername(config.getDbUserName());
			}
			if(StringUtils.isNotEmpty(config.getDbPassword())) {
				mongoTemplate.setPassword(config.getDbPassword());
			}
		}catch (MongoException e) {
			LOGGER.error("MongoConfig.mongoTemplate " , "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"" );
			throw new PhrescoException(e, EX_PHEX00003);
		}
		if(isDebugEnabled) {
			LOGGER.debug("MongoConfig.mongoTemplate : Exit");
		}
		return mongoTemplate;
	}
}
