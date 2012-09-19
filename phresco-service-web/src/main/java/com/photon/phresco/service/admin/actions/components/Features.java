/*
 * ###
 * Service Web Archive
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
package com.photon.phresco.service.admin.actions.components;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.ArchetypeInfo;
import com.photon.phresco.model.Documentation;
import com.photon.phresco.model.Documentation.DocumentationType;
import com.photon.phresco.model.Module;
import com.photon.phresco.model.ModuleGroup;
import com.photon.phresco.model.Technology;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.admin.commons.LogErrorReport;
import com.photon.phresco.service.client.api.Content;
import com.photon.phresco.service.util.ServerUtil;
import com.photon.phresco.util.ServiceConstants;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

public class Features extends ServiceBaseAction {

	private static final long serialVersionUID = 6801037145464060759L;
	private static final Logger S_LOGGER = Logger.getLogger(Features.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private static byte[] featureByteArray = null;
	
	private String name = "";
	private String customerId = "";
	private String description = "";
    private String helpText = "";
    private String technology = "";
    private String type = "";
    private String groupId = "";
    private String artifactId = "";
    private String version = "";
    private String moduleType = "";
    private String defaultType = "";
    private String oldVersion = "";
    private String moduleGroupId = "";
    private String moduleId = "";
    
    private String fromPage = "";
    private String from = "";

	private String nameError = "";
	private String artifactIdError = "";
    private String groupIdError = "";
	private String fileError = "";
	private String verError = "";
	private boolean errorFound = false;
    
	public String menu() {
		if (isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Features.menu()");
    	}

		getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
		featureByteArray = null;

    	return COMP_FEATURES_LIST;
    }
	
    public String list() {
    	if (isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Features.list()");
    	}
    	
    	try {
      		getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
			if (REQ_FEATURES_MODULE.equals(type)) {
				getHttpRequest().setAttribute(REQ_FEATURES_HEADER, getText(KEY_I18N_FEATURE_MOD_ADD));
			} else {
				getHttpRequest().setAttribute(REQ_FEATURES_HEADER, getText(KEY_I18N_FEATURE_JS_ADD));
			}
    		List<Technology> technologies = getServiceManager().getArcheTypes(customerId);
    		getHttpRequest().setAttribute(REQ_ARCHE_TYPES, technologies);
    	} catch (PhrescoException e){
    		new LogErrorReport(e, FEATURE_LIST_EXCEPTION);
    		
    		return LOG_ERROR;
    	}
    	
    	return COMP_FEATURES_LIST;
    }
    
    public String featuresWithList() {
    	if (isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Features.featurelist()");
    	}
    	
    	try {
    		List<ModuleGroup> moduleGroup = getServiceManager().getFeaturesByTech(customerId, technology, type);
    		getHttpRequest().setAttribute(REQ_FEATURES_MOD_GRP, moduleGroup);
    		getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
    		if (StringUtils.isNotEmpty(from)) {
    		    return COMP_FEATURES_DEPENDENCY;
    		}
    	} catch (PhrescoException e){
    		new LogErrorReport(e, FEATURE_LIST_EXCEPTION);
    		
    		return LOG_ERROR;
    	}
    	
    	return COMP_FEATURES_LIST;
    }
	
	public String add() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.add()");
		}
		
		List<Technology> technologies = getServiceManager().getArcheTypes(customerId);
		getHttpRequest().setAttribute(REQ_ARCHE_TYPES, technologies);
		getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
		getHttpRequest().setAttribute(REQ_FEATURES_TYPE, type);
		if (REQ_FEATURES_MODULE.equals(type)) {
			getHttpRequest().setAttribute(REQ_FEATURES_HEADER, getText(KEY_I18N_FEATURE_MOD_ADD));
		} else {
			getHttpRequest().setAttribute(REQ_FEATURES_HEADER, getText(KEY_I18N_FEATURE_JS_ADD));
		}
		
		return COMP_FEATURES_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.edit()");
		}
		
		try {
		    List<Technology> technologies = getServiceManager().getArcheTypes(customerId);
	        getHttpRequest().setAttribute(REQ_ARCHE_TYPES, technologies);
		    ModuleGroup moduleGroup = getServiceManager().getFeature(moduleGroupId, customerId);
			getHttpRequest().setAttribute(REQ_FEATURES_MOD_GRP, moduleGroup);
			getHttpRequest().setAttribute(REQ_FEATURES_SELECTED_MODULEID, moduleId);
			getHttpRequest().setAttribute(REQ_FROM_PAGE, REQ_EDIT);
			getHttpRequest().setAttribute(REQ_CUST_CUSTOMER_ID, customerId);
			if (REQ_FEATURES_MODULE.equals(type)) {
				getHttpRequest().setAttribute(REQ_FEATURES_HEADER, getText(KEY_I18N_FEATURE_MOD_EDIT));
			} else {
				getHttpRequest().setAttribute(REQ_FEATURES_HEADER, getText(KEY_I18N_FEATURE_JS_EDIT));
			}
		} catch (PhrescoException e) {
			new LogErrorReport(e, FEATURE_EDIT_EXCEPTION);
    		
			return LOG_ERROR;
		}

		return COMP_FEATURES_ADD;
	}
	
	public String save() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.save()");
		}
		
		try {
			MultiPart multiPart = new MultiPart();
			BodyPart jsonPart = new BodyPart();
			jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			jsonPart.setEntity(createModuleGroup());
			Content content = new Content("object", name, null, null, null, 0);
			jsonPart.setContentDisposition(content);
			multiPart.bodyPart(jsonPart);
			InputStream featureIs = new ByteArrayInputStream(featureByteArray);
			BodyPart binaryPart = getServiceManager().createBodyPart(name, FILE_FOR_APPTYPE, featureIs);
			multiPart.bodyPart(binaryPart);
			
			ClientResponse clientResponse = getServiceManager().createFeatures(multiPart, customerId);
			if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200 && clientResponse.getStatus() != ServiceConstants.RES_CODE_201) {
				addActionError(getText(FEATURE_NOT_UPDATED, Collections.singletonList(name)));
			} else {
				addActionMessage(getText(FEATURE_UPDATED, Collections.singletonList(name)));
			}
		} catch (PhrescoException e) {
			new LogErrorReport(e, FEATURE_SAVE_EXCEPTION);
    		
			return LOG_ERROR;
		} 

		return list();
	}
	
	public String update() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.update()");
		}
		
		try {
			MultiPart multiPart = new MultiPart();
            BodyPart jsonPart = new BodyPart();
            jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
            jsonPart.setEntity(createModuleGroup());
            Content content = new Content("object", name, null, null, null, 0);
            jsonPart.setContentDisposition(content);
            multiPart.bodyPart(jsonPart);
            InputStream featureIs = new ByteArrayInputStream(featureByteArray);
            BodyPart binaryPart = getServiceManager().createBodyPart(name, FILE_FOR_APPTYPE, featureIs);
            multiPart.bodyPart(binaryPart);
			    
			ClientResponse clientResponse = getServiceManager().updateFeature(multiPart, moduleGroupId, customerId);
			if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200 && clientResponse.getStatus() != ServiceConstants.RES_CODE_201) {
                addActionError(getText(FEATURE_NOT_ADDED, Collections.singletonList(name)));
            } else {
                addActionMessage(getText(FEATURE_ADDED, Collections.singletonList(name)));
            }
		} catch (PhrescoException e) {
			new LogErrorReport(e, FEATURE_UPDATE_EXCEPTION);
    	
			return LOG_ERROR;
		}
		
		return list();	
	}
	
	private ModuleGroup createModuleGroup() throws PhrescoException {
	    if (isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.createModuleGroup()");
        }
	    
        ModuleGroup moduleGroup;
        try {
            moduleGroup = new ModuleGroup();
            List<Module> modules = new ArrayList<Module>();
            Module module = new Module();
            if (StringUtils.isNotEmpty(fromPage)) {
                moduleGroup.setId(moduleGroupId);
            }
            moduleGroup.setName(name);
            moduleGroup.setTechId(technology);
            if (FEATURES_CORE.equals(moduleType)) {
                moduleGroup.setCore(true);
            } else {
                moduleGroup.setCore(false);
            }
            moduleGroup.setType(type);
            moduleGroup.setCustomerId(customerId);
            module.setName(name);
            List<Documentation> docs = new ArrayList<Documentation>();
            Documentation descDoc = new Documentation();
            descDoc.setType(DocumentationType.DESCRIPTION);
            descDoc.setContent(description);
            docs.add(descDoc);
            Documentation helpTextDoc = new Documentation();
            helpTextDoc.setType(DocumentationType.HELP_TEXT);
            helpTextDoc.setContent(helpText);
            docs.add(helpTextDoc);
            module.setDocs(docs);
            module.setVersion(version);
            module.setArtifactId(artifactId);
            module.setGroupId(groupId);
            if (StringUtils.isNotEmpty(defaultType)) {
                module.setRequired(true);
            } else {
                module.setRequired(false);
            }
            modules.add(module);
            moduleGroup.setVersions(modules);
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
        
        return moduleGroup;
    }
	
	public String delete() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.delete()");
		}
		
		try {
			String[] moduleGroups = getHttpRequest().getParameterValues(REQ_FEATURES_MOD_GRP);
			if (ArrayUtils.isNotEmpty(moduleGroups)) {
				for (String moduleGroup : moduleGroups) {
					ClientResponse clientResponse = getServiceManager().deleteFeature(moduleGroup, customerId);
					if (clientResponse.getStatus() != ServiceConstants.RES_CODE_200) {
						addActionError(getText(FEATURE_NOT_DELETED));
					}
				}
				addActionMessage(getText(FEATURE_DELETED));
			}
		} catch (PhrescoException e) {
			new LogErrorReport(e, FEATURE_DELETE_EXCEPTION);
			
    		return LOG_ERROR;
		}
		
		return list();
	}
	
	public String uploadFile() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.uploadFile()");
		}
		
		PrintWriter writer = null;
		try {
            writer = getHttpResponse().getWriter();
        	InputStream is = getHttpRequest().getInputStream();
        	byte[] tempFeaByteArray = IOUtils.toByteArray(is);
    		featureByteArray = tempFeaByteArray;
        	ArchetypeInfo archetypeInfo = ServerUtil.getArtifactinfo(new ByteArrayInputStream(tempFeaByteArray));
            getHttpResponse().setStatus(getHttpResponse().SC_OK);
            if (archetypeInfo != null) {
            	archetypeInfo.setMavenJar(true);
            	archetypeInfo.setSuccess(true);
            	Gson gson = new Gson();
                String json = gson.toJson(archetypeInfo);
            	writer.print(json);
            } else {
            	writer.print(MAVEN_JAR_FALSE);
        	}
	        writer.flush();
	        writer.close();
		} catch (Exception e) {
			getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
            writer.print(SUCCESS_FALSE);
			throw new PhrescoException(e);
		}
		
		return SUCCESS;
	}
	
	public void removeUploadedFile() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.removeUploadedFile()");
		}
		
		featureByteArray = null;
	}

	public String validateForm() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.validateForm()");
		}
		
		try {
            boolean isError = false;
            if (StringUtils.isEmpty(name)) {
                setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
                isError = true;
            }
            if (featureByteArray == null) {
                setFileError(getText(KEY_I18N_ERR_APPLNJAR_EMPTY));
                isError = true;
            } else {
                if (StringUtils.isEmpty(groupId)) {
                    setGroupIdError(getText(KEY_I18N_ERR_GROUPID_EMPTY));
                    isError = true;
                }
                if (StringUtils.isEmpty(artifactId)) {
                    setArtifactIdError(getText(KEY_I18N_ERR_ARTIFACTID_EMPTY));
                    isError = true;
                }
                if (StringUtils.isEmpty(version)) {
                    setVerError(getText(KEY_I18N_ERR_VER_EMPTY));
                    isError = true;
                }
                if (StringUtils.isNotEmpty(version) && (StringUtils.isEmpty(fromPage) 
                        || (!version.equals(oldVersion)))) {//To check whether the version already exist
                    List<ModuleGroup> moduleGroups = getServiceManager().getFeaturesByTech(customerId, technology, type);
                    if (StringUtils.isNotEmpty(version)) {
                        for (ModuleGroup moduleGroup : moduleGroups) {
                            List<Module> versions = moduleGroup.getVersions();
                            if (CollectionUtils.isNotEmpty(versions)) {
                                for (Module module : versions) {
                                    if (module.getName().equalsIgnoreCase(name)
                                            && module.getVersion().equals(
                                                    version)) {
                                        setVerError(getText(KEY_I18N_ERR_VER_ALREADY_EXISTS));
                                        isError = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (isError) {
                setErrorFound(true);
            }
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
        return SUCCESS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNameError() {
		return nameError;
	}
	
	public void setNameError(String nameError) {
		this.nameError = nameError;
	}
	
	public String getFileError() {
		return fileError;
	}

	public void setFileError(String fileError) {
		this.fileError = fileError;
	}

	public boolean getErrorFound() {
		return errorFound;
	}

	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getVerError() {
		return verError;
	}

	public void setVerError(String verError) {
		this.verError = verError;
	}

	public String getOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(String oldVersion) {
		this.oldVersion = oldVersion;
	}
	
	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	
	public String getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}

	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
	
	public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

	public String getModuleGroupId() {
        return moduleGroupId;
    }

    public void setModuleGroupId(String moduleGroupId) {
        this.moduleGroupId = moduleGroupId;
    }
    
    public String getArtifactIdError() {
        return artifactIdError;
    }

    public void setArtifactIdError(String artifactIdError) {
        this.artifactIdError = artifactIdError;
    }

    public String getGroupIdError() {
        return groupIdError;
    }

    public void setGroupIdError(String groupIdError) {
        this.groupIdError = groupIdError;
    }
    
    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}