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
package com.photon.phresco.service.model;

import java.io.File;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ArtifactInfo {

    private String groupId;
    private String artifact;
    private String classifier;
    private String pack;
    private String version;
    private File pomFile;
    
    public ArtifactInfo() {
        
    }
    
    public ArtifactInfo(String groupId, String artifact, String classifier,
            String pack, String version) {
        super();
        this.groupId = groupId;
        this.artifact = artifact;
        this.classifier = classifier;
        this.pack = pack;
        this.version = version;
    }

    /**
     * @return
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @return
     */
    public String getArtifact() {
        return artifact;
    }

    /**
     * @return
     */
    public String getClassifier() {
        return classifier;
    }

    /**
     * @return
     */
    public String getPackage() {
        return pack;
    }

    /**
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return
     */
    public File getPomFile() {
        return pomFile;
    }

    /**
     * @param pomFile
     */
    public void setPomFile(File pomFile) {
        this.pomFile = pomFile;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString() {
        return new ToStringBuilder(this,
                ToStringStyle.DEFAULT_STYLE)
                .append(super.toString())
                .append("groupId", groupId)
                .append("artifact", artifact)
                .append("classifier", classifier)
                .append("pack", pack)
                .append("version", version)
                .append("pomFile", pomFile)
                .toString();
    }

}
