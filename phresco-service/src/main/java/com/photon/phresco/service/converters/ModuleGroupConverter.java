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

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.Module;
import com.photon.phresco.model.ModuleGroup;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.ModuleGroupDAO;
import com.photon.phresco.util.ServiceConstants;

public class ModuleGroupConverter implements Converter<ModuleGroupDAO, ModuleGroup>, ServiceConstants {

    @Override
    public ModuleGroup convertDAOToObject(ModuleGroupDAO moduleGroupDAO,
            MongoOperations mongoOperation) throws PhrescoException {
        ModuleGroup moduleGroup = new ModuleGroup();
        moduleGroup.setId(moduleGroupDAO.getId());
        moduleGroup.setCore(moduleGroupDAO.isCore());
        moduleGroup.setCustomerId(moduleGroupDAO.getCustomerId());
        moduleGroup.setImageURL(moduleGroupDAO.getImageURL());
        moduleGroup.setName(moduleGroupDAO.getName());
        moduleGroup.setSystem(moduleGroupDAO.isSystem());
        moduleGroup.setTechId(moduleGroupDAO.getTechId());
        moduleGroup.setType(moduleGroupDAO.getType());
        Query query = new Query(Criteria.where(REST_API_MODULEID).is(moduleGroup.getId()));
        List<Module> modules = mongoOperation.find(MODULES_COLLECTION_NAME, query, Module.class);
        moduleGroup.setVersions(modules);
        return moduleGroup;
    }

    @Override
    public ModuleGroupDAO convertObjectToDAO(ModuleGroup moduleGroup)
            throws PhrescoException {
        ModuleGroupDAO moduleGroupDAO = new ModuleGroupDAO();
        moduleGroupDAO.setId(moduleGroup.getId());
        moduleGroupDAO.setCustomerId(moduleGroup.getCustomerId());
        moduleGroupDAO.setCore(moduleGroup.isCore());
        moduleGroupDAO.setImageURL(moduleGroup.getImageURL());
        moduleGroupDAO.setName(moduleGroup.getName());
        moduleGroupDAO.setSystem(moduleGroup.isSystem());
        moduleGroupDAO.setTechId(moduleGroup.getTechId());
        moduleGroupDAO.setType(moduleGroup.getType());
        return moduleGroupDAO;
    }

}
