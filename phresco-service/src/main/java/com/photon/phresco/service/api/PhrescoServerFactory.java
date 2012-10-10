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
/*******************************************************************************
 * Copyright (c) 2011 Photon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Photon Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.photon.in/legal/ppl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Photon - initial API and implementation
 ******************************************************************************/
package com.photon.phresco.service.api;

import java.lang.reflect.Constructor;

import com.photon.phresco.commons.model.ApplicationInfo;
import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.model.ServerConfiguration;

public class PhrescoServerFactory {

    private static final String DOCUMENT_GENERATOR_IMPL_CLASS = "com.photon.phresco.service.impl.DocumentGeneratorImpl";
    private static final String ARCHETYPE_EXECUTOR_IMPL_CLASS = "com.photon.phresco.service.impl.ArchetypeExecutorImpl";
    private static final String REPOSITORY_MANAGER_IMPL_CLASS = "com.photon.phresco.service.impl.RepositoryManagerImpl";
	private static final String DB_MANAGER_IMPL_CLASS = "com.photon.phresco.service.impl.DbManagerImpl";
	private static final String DEPENDENCY_MANAGER_IMPL_CLASS = "com.photon.phresco.service.impl.DependencyManagerImpl";
    private static final String PROJECT_SERVICE_MANAGER_IMPL_CLASS = "com.photon.phresco.service.impl.ProjectServiceManagerImpl";
	
    private static final String SERVER_CONFIG_FILE = "server.config";

    private static RepositoryManager repositoryManager 	= null;
    private static ServerConfiguration serverConfig     = null;
	private static TweetCacheManager tweetCacheManager 	= null;
	private static DbManager dbManager = null;
	private static DependencyManager dependencyManager = null;

    public static synchronized void initialize() throws PhrescoException {
        if (serverConfig == null) {
            serverConfig = new ServerConfiguration(SERVER_CONFIG_FILE);

            repositoryManager = (RepositoryManager) constructClass(REPOSITORY_MANAGER_IMPL_CLASS, serverConfig);
            dbManager = (DbManager) constructClass(DB_MANAGER_IMPL_CLASS);
            dependencyManager = (DependencyManager) constructClass(DEPENDENCY_MANAGER_IMPL_CLASS);
        }
    }

    private static Object constructClass(String className, ServerConfiguration serverConfig) throws PhrescoException {
        try {
            @SuppressWarnings("rawtypes")
			Class clazz = Class.forName(className);
            @SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor constructor = clazz.getDeclaredConstructor(ServerConfiguration.class);
            return constructor.newInstance(serverConfig);
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
    }

    private static Object constructClass(String className) throws PhrescoException {
        try {
            @SuppressWarnings("rawtypes")
            Class clazz = Class.forName(className);
            return clazz.newInstance();
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
    }

    public static RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }
    
    public static DbManager getDbManager() {
        return dbManager;
    }
    
    public static ArchetypeExecutor getArchetypeExecutor() throws PhrescoException {
        return (ArchetypeExecutor) constructClass(ARCHETYPE_EXECUTOR_IMPL_CLASS, serverConfig);
    }

    public static DocumentGenerator getDocumentGenerator() throws PhrescoException {
        return (DocumentGenerator) constructClass(DOCUMENT_GENERATOR_IMPL_CLASS);
    }
    
    public static synchronized ProjectServiceManager getProjectService() throws PhrescoException {
		return (ProjectServiceManager) constructClass(PROJECT_SERVICE_MANAGER_IMPL_CLASS);
	}
	
	public static TweetCacheManager getTweetCacheManager() {
        return tweetCacheManager;
    }
    
    public static ServerConfiguration getServerConfig() {
    	return serverConfig;
    }
    
    public static DependencyManager getDependencyManager() {
    	return dependencyManager;
    }
    
    public static void main(String[] args) throws PhrescoException {
        initialize();
    }
}
