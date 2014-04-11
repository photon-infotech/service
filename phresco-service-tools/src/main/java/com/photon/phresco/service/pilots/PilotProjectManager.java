/**
 * Phresco Service Root
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
package com.photon.phresco.service.pilots;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;

/*the class PilotProjectManager*/
public class PilotProjectManager {

    private RepositoryManager repositoryManager;

    public static final String PILOT_BASE = "D:\\work\\projects\\phresco\\source\\phresco-projects\\trunk";

    public static final String PILOT_GROUP = "pilots";

    /* new PilotProjectManager instantiated */
    public PilotProjectManager() {
        super();
        try {
            PhrescoServerFactory.initialize();
        } catch (PhrescoException e) {
        }
        this.repositoryManager = PhrescoServerFactory.getRepositoryManager();
    }

    /* gets and returns repository manager */
    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    /* Authenticates to deploy artifacts to repository */
    public static void main(String[] args) {
        PilotProjectManager manager = new PilotProjectManager();
        IPilotProject[] pilots = new IPilotProject[] {
                new PhpPilotProject(manager.getRepositoryManager()),
                new DrupalPilotProject(manager.getRepositoryManager()),
                new SharepointPilotProject(manager.getRepositoryManager()),
                new AndroidPilotProject(manager.getRepositoryManager()),
                new IphonePilotProject(manager.getRepositoryManager()),
                new NodejsPilotProject(manager.getRepositoryManager()), };

        try {
            for (IPilotProject project : pilots) {
//                project.buildPilotProject();
//                project.publishPilotProject();
                project.publishMetaData();
            }
        } catch (PhrescoException e) {
            e.printStackTrace();
        }
    }

}
