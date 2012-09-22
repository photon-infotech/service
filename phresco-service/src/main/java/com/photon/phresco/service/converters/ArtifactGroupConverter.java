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

import java.util.List;

import org.springframework.data.document.mongodb.MongoOperations;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.util.ServiceConstants;

public class ArtifactGroupConverter implements Converter<ArtifactGroupDAO, ArtifactGroup>, ServiceConstants {

    @Override
    public ArtifactGroup convertDAOToObject(ArtifactGroupDAO artifactGroupDAO,
            MongoOperations mongoOperation) throws PhrescoException {
        ArtifactGroup artifactGroup = new ArtifactGroup();
        artifactGroup.setArtifactId(artifactGroupDAO.getArtifactId());
        artifactGroup.setClassifier(artifactGroupDAO.getClassifier());
        artifactGroup.setCustomerIds(artifactGroupDAO.getCustomerIds());
        artifactGroup.setDescription(artifactGroupDAO.getDescription());
        artifactGroup.setGroupId(artifactGroupDAO.getGroupId());
        artifactGroup.setHelpText(artifactGroupDAO.getHelpText());
        artifactGroup.setId(artifactGroupDAO.getId());
        artifactGroup.setImageURL(artifactGroupDAO.getImageURL());
        artifactGroup.setName(artifactGroupDAO.getName());
        artifactGroup.setPackaging(artifactGroupDAO.getPackaging());
        artifactGroup.setSystem(artifactGroupDAO.isSystem());
        artifactGroup.setType(artifactGroupDAO.getType());
        artifactGroup.setUsed(artifactGroupDAO.isUsed());
        artifactGroupDAO.setAppliesTo(artifactGroupDAO.getAppliesTo());
        List<ArtifactInfo> versions = mongoOperation.find("ArtifactInfo", 
                new Query(Criteria.whereId().in(artifactGroupDAO.getVersionIds())), ArtifactInfo.class);
        artifactGroup.setVersions(versions);
        return artifactGroup;
    }

    @Override
    public ArtifactGroupDAO convertObjectToDAO(ArtifactGroup artifactGroup)
            throws PhrescoException {
        ArtifactGroupDAO artifactGroupDAO = new ArtifactGroupDAO();
        artifactGroupDAO.setId(artifactGroup.getId());
        artifactGroupDAO.setArtifactId(artifactGroup.getArtifactId());
        artifactGroupDAO.setClassifier(artifactGroup.getClassifier());
        artifactGroupDAO.setCustomerIds(artifactGroup.getCustomerIds());
        artifactGroupDAO.setDescription(artifactGroup.getDescription());
        artifactGroupDAO.setGroupId(artifactGroup.getGroupId());
        artifactGroupDAO.setArtifactId(artifactGroup.getArtifactId());
        artifactGroupDAO.setImageURL(artifactGroup.getImageURL());
        artifactGroupDAO.setName(artifactGroup.getName());
        artifactGroupDAO.setPackaging(artifactGroup.getPackaging());
        artifactGroupDAO.setSystem(artifactGroup.isSystem());
        artifactGroupDAO.setType(artifactGroup.getType());
        artifactGroupDAO.setUsed(artifactGroup.isUsed());
        return artifactGroupDAO;
    }

}
