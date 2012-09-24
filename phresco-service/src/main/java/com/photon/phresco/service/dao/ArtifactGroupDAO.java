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

import com.photon.phresco.commons.model.CoreOption;

public class ArtifactGroupDAO extends BaseDAO {
    
    private static final long serialVersionUID = 1L;
    private String groupId;
    private String artifactId;
    private String packaging;
    private String classifier;
    private String type;
    private String imageURL;
    private List<String> versionIds;
    private List<CoreOption> appliesTo;
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getArtifactId() {
        return artifactId;
    }
    
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
    
    public String getPackaging() {
        return packaging;
    }
    
    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }
    
    public String getClassifier() {
        return classifier;
    }
    
    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getImageURL() {
        return imageURL;
    }
    
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    
    public List<CoreOption> getAppliesTo() {
        return appliesTo;
    }
    
    public void setAppliesTo(List<CoreOption> appliesTo) {
        this.appliesTo = appliesTo;
    }

    public List<String> getVersionIds() {
        return versionIds;
    }

    public void setVersionIds(List<String> versionIds) {
        this.versionIds = versionIds;
    }
}
