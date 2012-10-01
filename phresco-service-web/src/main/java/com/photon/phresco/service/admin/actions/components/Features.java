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
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import com.photon.phresco.service.model.FileInfo;
import com.photon.phresco.service.util.ServerUtil;

public class Features extends ServiceBaseAction {

    private static final long serialVersionUID = 6801037145464060759L;
    
	private static final Logger S_LOGGER = Logger.getLogger(Features.class);
	private static Boolean s_isDebugEnabled = S_LOGGER.isDebugEnabled();
	
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
    
    private List<String> dependentModGroupId = null;
    
    private String fromPage = "";
    private String from = "";

	private String nameError = "";
	private String artifactIdError = "";
    private String groupIdError = "";
	private String fileError = "";
	private String verError = "";
	private boolean errorFound = false;
    
	public String menu() {
		if (s_isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Features.menu()");
    	}

		setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
		featureByteArray = null;
		
    	return COMP_FEATURES_LIST;
    }
	
	public String modulesTechnologies() {
	    if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.modulesList()");
        }
	    
	    try {
            setReqAttribute(REQ_FEATURES_TYPE, REQ_FEATURES_TYPE_MODULE); //for handle in feature sub menu
    	    setTechnologiesInRequest();
    	    //To remove the selected dependent moduleIds from the session
    	    removeSessionAttribute(FEATURES_DEPENDENT_MOD_IDS);
	    } catch (PhrescoException e) {
	        return showErrorPopup(e, EXCEPTION_FEATURE_LIST);
	    }
	    
	    return COMP_FEATURES_LIST;
	}
	
	public String jsLibTechnologies() {
	    if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.jsLibList()");
        }
	    
	    try {
            setReqAttribute(REQ_FEATURES_TYPE, REQ_FEATURES_TYPE_JS);
    	    setTechnologiesInRequest();
            //To remove the selected dependent moduleIds from the session
    	    removeSessionAttribute(FEATURES_DEPENDENT_MOD_IDS);
    	} catch (PhrescoException e) {
            return showErrorPopup(e, EXCEPTION_FEATURE_LIST);
        }
	
	    return COMP_FEATURES_LIST;
    }
	
    private void setTechnologiesInRequest() throws PhrescoException {
    	if (s_isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Features.list()");
    	}
    	
    	try {
    		List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
    		setReqAttribute(REQ_ARCHE_TYPES, technologies);
    		featureByteArray = null;
    	} catch (PhrescoException e) {
    	    throw new PhrescoException(e);
    	}
    }
    
    public String listModules() {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.featurelist()");
        }
        
        try {
            listFeatures(Type.FEATURE);
            setReqAttribute(REQ_FEATURES_TYPE, REQ_FEATURES_TYPE_MODULE);
        } catch (PhrescoException e) {
            return showErrorPopup(e, EXCEPTION_FEATURE_LIST);
        }
        
        return COMP_FEATURES_LIST;
    }
    
    public String listJSLibs() {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.featurelist()");
        }
        
        try {
            listFeatures(Type.JAVASCRIPT);
            setReqAttribute(REQ_FEATURES_TYPE, REQ_FEATURES_TYPE_JS);
        } catch (PhrescoException e) {
            return showErrorPopup(e, EXCEPTION_FEATURE_LIST);
        }
        
        return COMP_FEATURES_LIST;
    }
    
    public String listModulesDependency() {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.featurelist()");
        }
        
        try {
            listFeatures(Type.FEATURE);
        } catch (PhrescoException e) {
            return showErrorPopup(e, EXCEPTION_FEATURE_LIST);
        }
        
        return COMP_FEATURES_LIST;
    }
    
    public String listJSLibsDependency() {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.featurelist()");
        }
        
        try {
            listFeatures(Type.JAVASCRIPT);
        } catch (PhrescoException e) {
            return showErrorPopup(e, EXCEPTION_FEATURE_LIST);
        }
        
        return COMP_FEATURES_LIST;
    }
    
    private void listFeatures(Type type) throws PhrescoException {
    	if (s_isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Features.featurelist()");
    	}
    	
		List<ArtifactGroup> moduleGroup = getServiceManager().getFeatures(getCustomerId(), getTechnology(), type.name());
		setReqAttribute(REQ_FEATURES_MOD_GRP, moduleGroup);
		setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
    }
	
    public String addModules() {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.addModules()");
        }
        
        try {
            setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
            setReqAttribute(REQ_FEATURES_TYPE, getType());
            setTechnologiesInRequest();
        } catch (PhrescoException e) {
            return showErrorPopup(e, EXCEPTION_FEATURE_ADD);            
        }
        
        return COMP_FEATURES_ADD;
    }
    
    public String addJSLibs() {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.addModules()");
        }
        
        try {
            setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
            setReqAttribute(REQ_FEATURES_TYPE, getType());
            setTechnologiesInRequest();
        } catch (PhrescoException e) {
            return showErrorPopup(e, EXCEPTION_FEATURE_ADD);            
        }
        
        return COMP_FEATURES_ADD;
    }
	
	public String editModule() {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.edit()");
		}
		
		try {
		    setTechnologiesInRequest();
		    setReqAttribute(REQ_FEATURES_TYPE, getType());
		    setReqAttribute(FEATURES_SELECTED_TECHNOLOGY, getTechnology());
		    setEditModuleGroupInReq(getTechnology(), Type.FEATURE);
		} catch (PhrescoException e) {
			showErrorPopup(e, EXCEPTION_FEATURE_EDIT);
		}

		return COMP_FEATURES_ADD;
	}
	
	public String editJSLib() {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.edit()");
        }
        
        try {
            setTechnologiesInRequest();
            setReqAttribute(REQ_FEATURES_TYPE, getType());
            setEditModuleGroupInReq(getTechnology(), Type.JAVASCRIPT);
            setReqAttribute(FEATURES_SELECTED_TECHNOLOGY, getTechnology());
        } catch (PhrescoException e) {
            showErrorPopup(e, EXCEPTION_FEATURE_EDIT);
        }

        return COMP_FEATURES_ADD;
    }

    private void setEditModuleGroupInReq(String technology, Type type) throws PhrescoException {
        ArtifactGroup moduleGroup = getServiceManager().getFeature(getModuleGroupId(), getCustomerId(), technology, type.name());
        setReqAttribute(REQ_FEATURES_MOD_GRP, moduleGroup);
        setReqAttribute(REQ_FEATURES_SELECTED_MODULEID, getModuleId());
        setReqAttribute(REQ_FROM_PAGE, EDIT);
        setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
    }
    
    public void saveDependentFeatures() {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.saveDependentFeatures()");
        }
        
        List<String> dependentModuleIds = new ArrayList<String>(dependentModGroupId.size());
        if (CollectionUtils.isNotEmpty(dependentModGroupId)) {
            for (String dependentModGroup : dependentModGroupId) {
                dependentModuleIds.add(getHttpRequest().getParameter(dependentModGroup));
            }
        }
        setSessionAttribute(FEATURES_DEPENDENT_MOD_IDS, dependentModuleIds);
    }
	
	public String saveModules() throws IOException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.save()");
		}
		
		try {
			save(Type.FEATURE);
			setTechnologiesInRequest();
		} catch (PhrescoException e) {
			showErrorPopup(e, EXCEPTION_FEATURE_SAVE);
		}
		
		return modulesTechnologies();
	}
	
	public String saveJSLibs() throws IOException {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.save()");
        }
        
        try {
            save(Type.JAVASCRIPT);
            setTechnologiesInRequest();
        } catch (PhrescoException e) {
            showErrorPopup(e, EXCEPTION_FEATURE_SAVE);
        }
        
        return jsLibTechnologies();
    }

    private void save(Type type) throws PhrescoException, IOException {
        try {
            ArtifactGroup moduleGroup = createModuleGroup(type);
            InputStream inputStream = null;
            if (featureByteArray != null) {
                inputStream = new ByteArrayInputStream(featureByteArray);
            }
            getServiceManager().createFeatures(moduleGroup, inputStream, getCustomerId());
            addActionMessage(getText(FEATURE_ADDED, Collections.singletonList(getName())));
        } catch (PhrescoException e) {
            throw new PhrescoException(e); 
        }
    }
    
    public String updateModule() throws IOException {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.updateModule()");
        }
        
        try {
            update(Type.FEATURE);
            setTechnologiesInRequest();
        } catch (PhrescoException e) {
            showErrorPopup(e, EXCEPTION_FEATURE_SAVE);
        }
        
        return  modulesTechnologies();
    }
    
    public String updateJSLib() throws IOException {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.updateJSLib()");
        }
        
        try {
            update(Type.JAVASCRIPT);
            setTechnologiesInRequest();
        } catch (PhrescoException e) {
            showErrorPopup(e, EXCEPTION_FEATURE_SAVE);
        }
        
        return jsLibTechnologies();
    }

    private void update(Type type) throws PhrescoException, IOException {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method Features.update()");
        }
        
        try {
            ArtifactGroup moduleGroup = createModuleGroup(type);
            InputStream inputStream = null;
            if (featureByteArray != null) {
                inputStream = new ByteArrayInputStream(featureByteArray);
            }
            getServiceManager().updateFeature(moduleGroup, inputStream, getCustomerId());
            addActionMessage(getText(FEATURE_ADDED, Collections.singletonList(getName())));
        } catch (PhrescoException e) {
            throw new PhrescoException(e); 
        }
    }
	
	private ArtifactGroup createModuleGroup(Type type) throws PhrescoException {
	    if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.createModuleGroup()");
        }
        
        try {
            ArtifactGroup artifactGroup = new ArtifactGroup();
            artifactGroup.setName(getName());
            if (StringUtils.isNotEmpty(getFromPage())) {
                artifactGroup.setId(getModuleGroupId());
            }
            artifactGroup.setDescription(getDescription());
            artifactGroup.setGroupId(getGroupId());
            artifactGroup.setArtifactId(getArtifactId());
            artifactGroup.setType(type);

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
            artifactInfo.setDependencyIds((List<String>)getSessionAttribute(FEATURES_DEPENDENT_MOD_IDS));
            
            artifactGroup.setVersions(Arrays.asList(artifactInfo));
            
            return artifactGroup;
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
    }
	
	public String deleteModules() {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.deleteModules()");
        }
        
        try {
            delete();
            setTechnologiesInRequest();
        } catch (PhrescoException e) {
            showErrorPopup(e, EXCEPTION_FEATURE_DELETE);
        }
        
        return modulesTechnologies();
    }
    
    public String deleteJSLibs() {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.deleteJSLibs()");
        }
        
        try {
            delete();
            setTechnologiesInRequest();
        } catch (PhrescoException e) {
            showErrorPopup(e, EXCEPTION_FEATURE_DELETE);
        }
        
        return jsLibTechnologies();
    }
    
	private void delete() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.delete()");
		}
		
		try {
			String[] moduleGroupIds = getHttpRequest().getParameterValues(REQ_FEATURES_MOD_GRP);
			if (ArrayUtils.isNotEmpty(moduleGroupIds)) {
				for (String moduleGroupId : moduleGroupIds) {
					getServiceManager().deleteFeature(moduleGroupId, getCustomerId());
				}
				addActionMessage(getText(FEATURE_DELETED));
			}
		} catch (PhrescoException e) {
			throw new PhrescoException(e);
		}
	}
	
	public String uploadFile() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.uploadFile()");
		}
		
		PrintWriter writer = null;
		try {
            writer = getHttpResponse().getWriter();
        	InputStream is = getHttpRequest().getInputStream();
        	byte[] tempFeaByteArray = IOUtils.toByteArray(is);
    		featureByteArray = tempFeaByteArray;
    		
        	ArtifactGroup artifactGroupInfo = ServerUtil.getArtifactinfo(new ByteArrayInputStream(tempFeaByteArray));
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
		} catch (Exception e) {
			getHttpResponse().setStatus(getHttpResponse().SC_INTERNAL_SERVER_ERROR);
            writer.print(SUCCESS_FALSE);
			throw new PhrescoException(e);
		}
		
		return SUCCESS;
	}
	
	public void removeUploadedFile() {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.removeUploadedFile()");
		}
		
		featureByteArray = null;
	}

	public String validateForm() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.validateForm()");
		}
		
		boolean isError = false;
        //Empty validation for name
        if (StringUtils.isEmpty(getName())) {
            setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
            isError = true;
        }
        //Validate whether file is selected during add
        if (/*!EDIT.equals(getFromPage()) &&*/ featureByteArray == null) {
            setFileError(getText(KEY_I18N_ERR_APPLNJAR_EMPTY));
            isError = true;
        }
        if (featureByteArray != null) {
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
    
    public List<String> getDependentModGroupId() {
        return dependentModGroupId;
    }

    public void setDependentModGroupId(List<String> dependentModGroupId) {
        this.dependentModGroupId = dependentModGroupId;
    }
}