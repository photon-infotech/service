/**
 * Service Web Archive
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
package com.photon.phresco.service.admin.actions.components;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactGroup.Type;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.CoreOption;
import com.photon.phresco.commons.model.RequiredOption;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.client.api.ServiceManager;
import com.photon.phresco.service.client.impl.CacheKey;
import com.photon.phresco.service.model.FileInfo;
import com.photon.phresco.service.util.ServerUtil;
import com.sun.jersey.api.client.ClientResponse;

public class Component extends ServiceBaseAction {

	private static final long serialVersionUID = 6801037145464060759L;
	
	private static final Logger S_LOGGER = Logger.getLogger(Features.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	private static byte[] componentByteArray = null;
	
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
    
    private List<String> dependentModGroupId = null;
    
    private String fromPage = "";

    private String nameError = "";
    private String artifactIdError = "";
    private String groupIdError = "";
    private String fileError = "";
    private String verError = "";
    private boolean errorFound = false;
	
    public String technologies() {
    	if (isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Component.technologies()");
    	}
    	
    	try {
      		setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
    		List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
    		setReqAttribute(REQ_ARCHE_TYPES, technologies);
    		//To remove the selected dependent component moduleIds from the session
            removeSessionAttribute(SESSION_COMP_DEPENDENT_MOD_IDS);
    	} catch (PhrescoException e) {
    	    return showErrorPopup(e, getText(EXCEPTION_COMPONENT_LIST));
    	}
    	
    	return COMP_COMPONENT_LIST;
    }
    
    public String list() {
    	if (isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Component.list()");
    	}
    	
    	try {
    	    List<ArtifactGroup> moduleGroup = getServiceManager().getFeatures(getCustomerId(), getTechnology(), Type.COMPONENT.name());
            setReqAttribute(REQ_FEATURES_MOD_GRP, moduleGroup);
    		setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
    	} catch (PhrescoException e){
    	    return showErrorPopup(e, getText(EXCEPTION_COMPONENT_LIST));
    	}
    	
    	return COMP_COMPONENT_LIST;
    }
	
	public String add() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.add()");
		}
		
		try {
		List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
		setReqAttribute(REQ_ARCHE_TYPES, technologies);
		setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
		}catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_COMPONENT_ADD));
		}
	
		return COMP_COMPONENT_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.edit()");
		}
		
		try {
		    ServiceManager serviceManager = getServiceManager();
		    ArtifactGroup moduleGroup = serviceManager.getFeature(getModuleGroupId(), getCustomerId(), getTechnology(), Type.COMPONENT.name());
	        setReqAttribute(REQ_FEATURES_MOD_GRP, moduleGroup);
	        List<Technology> technologies = serviceManager.getArcheTypes(getCustomerId());
	        setReqAttribute(REQ_ARCHE_TYPES, technologies);
	        setReqAttribute(REQ_FEATURES_SELECTED_MODULEID, getModuleId());
	        setReqAttribute(REQ_FROM_PAGE, EDIT);
	        setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
        } catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_COMPONENT_EDIT));
		}

		return COMP_COMPONENT_ADD;
	}
	
	public String save() throws PhrescoException, IOException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.save()");
		}
		
		try {
		    ArtifactGroup moduleGroup = createModuleGroup();
		    Map<String, InputStream> inputStreamMap = new HashMap<String, InputStream>();
            if (componentByteArray != null) {
            	inputStreamMap.put(moduleGroup.getName(), new ByteArrayInputStream(componentByteArray));
            }
            getServiceManager().createFeatures(moduleGroup, inputStreamMap, getCustomerId());
			addActionMessage(getText(COMPONENT_ADDED, Collections.singletonList(getName())));
		} catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_COMPONENT_SAVE));    		
		}

		return technologies();
	}
	
	public String update() throws PhrescoException, IOException {
        if (isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Component.update()");
        }
        
        try {
            ArtifactGroup moduleGroup = createModuleGroup();
            Map<String, InputStream> inputStreamMap = new HashMap<String, InputStream>();
            if (componentByteArray != null) {
            	inputStreamMap.put(moduleGroup.getName(), new ByteArrayInputStream(componentByteArray));
            }
            getServiceManager().updateFeature(moduleGroup, inputStreamMap, getCustomerId());
        } catch (PhrescoException e) {
            return showErrorPopup(e, getText(EXCEPTION_COMPONENT_UPDATE));
        }
        
        return technologies();
    }
	
	private ArtifactGroup createModuleGroup() throws PhrescoException {
        if (isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.createModuleGroup()");
        }
        
        try {
            ArtifactGroup artifactGroup = new ArtifactGroup();
            artifactGroup.setName(getName());
            if (StringUtils.isNotEmpty(getModuleGroupId())) {
                artifactGroup.setId(getModuleGroupId());
            }
            artifactGroup.setDescription(getDescription());
            artifactGroup.setGroupId(getGroupId());
            artifactGroup.setArtifactId(getArtifactId());
            artifactGroup.setType(Type.COMPONENT);

            // To set appliesto tech and core
            List<CoreOption> appliesTo = new ArrayList<CoreOption>();
            CoreOption moduleCoreOption = new CoreOption(getTechnology(), Boolean.parseBoolean(getModuleType()));
            appliesTo.add(moduleCoreOption);
            artifactGroup.setAppliesTo(appliesTo);
            
            artifactGroup.setCustomerIds(Arrays.asList(getCustomerId()));
            
            //To set the details of the version
            ArtifactInfo artifactInfo = new ArtifactInfo();
            artifactInfo.setDescription(getDescription());
            artifactInfo.setHelpText(getHelpText());
            artifactInfo.setVersion(getVersion());
            
            //To set whether the feature is default to the technology or not
            List<RequiredOption> required = new ArrayList<RequiredOption>();
            RequiredOption requiredOption = new RequiredOption(getTechnology(), Boolean.parseBoolean(getDefaultType()));
            required.add(requiredOption);
            artifactInfo.setAppliesTo(required);
            
            //To set dependencies
            artifactInfo.setDependencyIds((List<String>) getSessionAttribute(SESSION_COMP_DEPENDENT_MOD_IDS));
            
            artifactGroup.setVersions(Arrays.asList(artifactInfo));
            
            return artifactGroup;
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
    }
	
	public String delete() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.delete()");
		}
		
		try {
			String[] techIds = getHttpRequest().getParameterValues(REST_QUERY_TECHID);
			String customerId = getCustomerId();
            String tech = getTechnology();
            String type = getType();
            CacheKey key = new CacheKey(customerId, type, tech);
			if (ArrayUtils.isNotEmpty(techIds)) {
				for (String techId : techIds) {
					ClientResponse clientResponse = getServiceManager().deleteFeature(techId);
					if (clientResponse.getStatus() != RES_CODE_200) {
						addActionError(getText(COMPONENT_NOT_DELETED));
					}
				}
				addActionMessage(getText(COMPONENT_DELETED));
			}
		} catch (PhrescoException e) {
		    return showErrorPopup(e, getText(EXCEPTION_COMPONENT_DELETE));
		}
		
		return technologies();
	}
	
	public String uploadFile() throws PhrescoException, IOException {
        if (isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.uploadFile()");
        }
        
        PrintWriter writer = null;
        try {
            writer = getHttpResponse().getWriter();
            InputStream is = getHttpRequest().getInputStream();
            byte[] tempFeaByteArray = IOUtils.toByteArray(is);
            componentByteArray = tempFeaByteArray;
            
            ArtifactGroup artifactGroupInfo = ServerUtil.getArtifactinfo(new ByteArrayInputStream(tempFeaByteArray), ServerUtil.getFileExtension(getFileName()));
            FileInfo fileInfo = new FileInfo();
            getHttpResponse().setStatus(getHttpResponse().SC_OK);
            if (artifactGroupInfo != null) {
                fileInfo.setMavenJar(true);
                fileInfo.setSuccess(true);
                fileInfo.setGroupId(artifactGroupInfo.getGroupId());
                fileInfo.setArtifactId(artifactGroupInfo.getArtifactId());
                List<ArtifactInfo> versions = artifactGroupInfo.getVersions();
                fileInfo.setVersion(versions.get(0).getVersion());
                Gson gson = new Gson();
                String json = gson.toJson(fileInfo);
                writer.print(json);
            } else {
                writer.print(MAVEN_JAR_FALSE);
            }
            writer.flush();
            writer.close();
        } catch (PhrescoException e) {
            getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
            writer.print(SUCCESS_FALSE);
            return showErrorPopup(e, getText(EXCEPTION_COMPONENT_UPLOAD_FILE));
        }
        
        return SUCCESS;
    }
	
	public void removeComponentFile() {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.removeUploadedFile()");
		}
		
		componentByteArray = null;
	}
	
	public String listConponentsDependency() throws PhrescoException {
        if (isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.featurelist()");
        }
        
        try {
            List<ArtifactGroup> moduleGroup = getServiceManager().getFeatures(
                    getCustomerId(), getTechnology(), Type.COMPONENT.name());
            setReqAttribute(REQ_FEATURES_MOD_GRP, moduleGroup);
            setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
        } catch (PhrescoException e) {
            return showErrorPopup(e, getText(EXCEPTION_COMPONENT_LIST_DEPENDENCY));
        }
        
        return COMP_COMPONENT_LIST;
    }
	
	public void saveDependentComponents() {
        if (isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.saveDependentFeatures()");
        }
        
        List<String> dependentModuleIds = new ArrayList<String>(dependentModGroupId.size());
        if (CollectionUtils.isNotEmpty(dependentModGroupId)) {
            for (String dependentModGroup : dependentModGroupId) {
                dependentModuleIds.add(getHttpRequest().getParameter(dependentModGroup));
            }
        }
        setSessionAttribute(SESSION_COMP_DEPENDENT_MOD_IDS, dependentModuleIds);
    }

	public String validateForm() throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Component.validateForm()");
		}
		
		boolean isError = false;
        //Empty validation for name
        if (StringUtils.isEmpty(getName())) {
            setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
            isError = true;
        }
        //Validate whether file is selected during add
        if (/*!EDIT.equals(getFromPage()) &&*/ componentByteArray == null) {
            setFileError(getText(KEY_I18N_ERR_APPLNJAR_EMPTY));
            isError = true;
        }
        if (componentByteArray != null) {
            //Empty validation for groupId if file is selected
            if (StringUtils.isEmpty(getGroupId())) {
                setGroupIdError(getText(KEY_I18N_ERR_GROUPID_EMPTY));
                isError = true;
            }
            //Empty validation for artifactId if file is selected
            if (StringUtils.isEmpty(getArtifactId())) {
                setArtifactIdError(getText(KEY_I18N_ERR_ARTIFACTID_EMPTY));
                isError = true;
            }
            //Empty validation for version if file is selected
            if (StringUtils.isEmpty(getVersion())) {
                setVerError(getText(KEY_I18N_ERR_VER_EMPTY));
                isError = true;
            }
            //To check whether the version already exist
            // TODO: Lohes(check must be done by querying the DB)
            /*if (StringUtils.isNotEmpty(getVersion()) && (StringUtils.isEmpty(getFromPage()) 
                    || (!getVersion().equals(getOldVersion())))) {
                List<ArtifactGroup> moduleGroups = getServiceManager().getFeatures(getCustomerId(), getTechnology(), getType());
                if (StringUtils.isNotEmpty(getVersion())) {
                    for (ArtifactGroup moduleGroup : moduleGroups) {
                        List<ArtifactInfo> versions = moduleGroup.getVersions();
                        if (CollectionUtils.isNotEmpty(versions)) {
                            for (ArtifactInfo module : versions) {
                                if (module.getName().equalsIgnoreCase(getName())
                                        && module.getVersion().equals(getVersion())) {
                                    setVerError(getText(KEY_I18N_ERR_VER_ALREADY_EXISTS));
                                    isError = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }*/
        }
        if (isError) {
            setErrorFound(true);
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
    
    public List<String> getDependentModGroupId() {
        return dependentModGroupId;
    }

    public void setDependentModGroupId(List<String> dependentModGroupId) {
        this.dependentModGroupId = dependentModGroupId;
    }
}