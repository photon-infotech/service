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
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.CoreOption;
import com.photon.phresco.commons.model.RequiredOption;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.admin.actions.ServiceBaseAction;
import com.photon.phresco.service.client.api.Content;
import com.photon.phresco.service.model.FileInfo;
import com.photon.phresco.service.util.ServerUtil;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

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
	
	public String modulesList() {
	    if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.modulesList()");
        }
	    
        setReqAttribute(REQ_FEATURES_HEADER, getText(KEY_I18N_FEATURE_MOD_ADD));
        setReqAttribute(REQ_FEATURES_TYPE, REQ_FEATURES_TYPE_FEATURE); //for handle in feature sub menu
	    return setTechnologiesInRequest();
	}
	
	public String jsLibList() {
	    if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.jsLibList()");
        }
	    
        setReqAttribute(REQ_FEATURES_HEADER, getText(KEY_I18N_FEATURE_JS_ADD));
        setReqAttribute(REQ_FEATURES_TYPE, REQ_FEATURES_TYPE_JAVASCRIPT); //for handle in feature sub menu
	    return setTechnologiesInRequest();
    }
	
    private String setTechnologiesInRequest() {
    	if (s_isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Features.list()");
    	}
    	
    	try {
    		List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
    		setReqAttribute(REQ_ARCHE_TYPES, technologies);
    		featureByteArray = null;
    	} catch (PhrescoException e) {
    	    return showErrorPopup(e, EXCEPTION_FEATURE_LIST);
    	}
    	
    	return COMP_FEATURES_LIST;
    }
    
    public String listFeatures() {
    	if (s_isDebugEnabled) {
    		S_LOGGER.debug("Entering Method  Features.featurelist()");
    	}
    	
    	try {
    		List<ArtifactGroup> moduleGroup = getServiceManager().getModules(getCustomerId(), getTechnology(), getType());
    		setReqAttribute(REQ_FEATURES_MOD_GRP, moduleGroup);
    		setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
    		if (StringUtils.isNotEmpty(from)) {
    		    return COMP_FEATURES_DEPENDENCY;
    		}
    	} catch (PhrescoException e) {
    	    return showErrorPopup(e, EXCEPTION_FEATURE_LIST);
    	}
    	
    	return COMP_FEATURES_LIST;
    }
	
	public String add() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.add()");
		}
		
		try {
            List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
            setReqAttribute(REQ_ARCHE_TYPES, technologies);
            setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
            setReqAttribute(REQ_FEATURES_TYPE, getType());
            if (REQ_FEATURES_MODULE.equals(getType())) {
                getHttpRequest().setAttribute(REQ_FEATURES_HEADER,
                        getText(KEY_I18N_FEATURE_MOD_ADD));
            } else {
                getHttpRequest().setAttribute(REQ_FEATURES_HEADER,
                        getText(KEY_I18N_FEATURE_JS_ADD));
            }
        } catch (PhrescoException e) {
            return showErrorPopup(e, EXCEPTION_FEATURE_ADD);            
        }
        
        return COMP_FEATURES_ADD;
	}
	
	public String edit() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.edit()");
		}
		
		try {
		    List<Technology> technologies = getServiceManager().getArcheTypes(getCustomerId());
		    setReqAttribute(REQ_ARCHE_TYPES, technologies);
		    ArtifactGroup moduleGroup = getServiceManager().getFeature(getModuleGroupId(), getCustomerId());
		    setReqAttribute(REQ_FEATURES_MOD_GRP, moduleGroup);
		    setReqAttribute(REQ_FEATURES_SELECTED_MODULEID, getModuleId());
		    setReqAttribute(REQ_FROM_PAGE, EDIT);
		    setReqAttribute(REQ_CUST_CUSTOMER_ID, getCustomerId());
			if (REQ_FEATURES_MODULE.equals(getType())) {
				getHttpRequest().setAttribute(REQ_FEATURES_HEADER, getText(KEY_I18N_FEATURE_MOD_EDIT));
			} else {
				getHttpRequest().setAttribute(REQ_FEATURES_HEADER, getText(KEY_I18N_FEATURE_JS_EDIT));
			}
		} catch (PhrescoException e) {
			showErrorPopup(e, EXCEPTION_FEATURE_EDIT);
		}

		return COMP_FEATURES_ADD;
	}
	
	public String save() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.save()");
		}
		
		try {
			MultiPart multiPart = new MultiPart();
			BodyPart jsonPart = new BodyPart();
			jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			jsonPart.setEntity(createModuleGroup());
			Content content = new Content(Content.Type.JSON, getName(), null, null, null, 0);
			jsonPart.setContentDisposition(content);
			multiPart.bodyPart(jsonPart);
			InputStream featureIs = new ByteArrayInputStream(featureByteArray);
			BodyPart binaryPart = getServiceManager().createBodyPart(getName(), Content.Type.JAR, featureIs);
			multiPart.bodyPart(binaryPart);
			
			ClientResponse clientResponse = getServiceManager().createFeatures(multiPart, getCustomerId());
			if (clientResponse.getStatus() != RES_CODE_200 && clientResponse.getStatus() != RES_CODE_201) {
				addActionError(getText(FEATURE_NOT_UPDATED, Collections.singletonList(getName())));
			} else {
				addActionMessage(getText(FEATURE_UPDATED, Collections.singletonList(getName())));
			}
		} catch (PhrescoException e) {
			showErrorPopup(e, EXCEPTION_FEATURE_SAVE);
		} 

		return setTechnologiesInRequest();
	}
	
	public String update() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.update()");
		}
		
		try {
			MultiPart multiPart = new MultiPart();
            BodyPart jsonPart = new BodyPart();
            jsonPart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
            jsonPart.setEntity(createModuleGroup());
            Content content = new Content(Content.Type.JSON, getName(), null, null, null, 0);
            jsonPart.setContentDisposition(content);
            multiPart.bodyPart(jsonPart);
            if (featureByteArray != null) {
                InputStream featureIs = new ByteArrayInputStream(featureByteArray);
                BodyPart binaryPart = getServiceManager().createBodyPart(getName(), Content.Type.JAR, featureIs);
                multiPart.bodyPart(binaryPart);
            }
			    
			ClientResponse clientResponse = getServiceManager().updateFeature(multiPart, getModuleGroupId(), getCustomerId());
			if (clientResponse.getStatus() != RES_CODE_200 && clientResponse.getStatus() != RES_CODE_201) {
                addActionError(getText(FEATURE_NOT_UPDATED, Collections.singletonList(getName())));
            } else {
                addActionMessage(getText(FEATURE_UPDATED, Collections.singletonList(getName())));
            }
		} catch (PhrescoException e) {
			showErrorPopup(e, EXCEPTION_FEATURE_UPDATE);
		}
		
		return setTechnologiesInRequest();	
	}
	
	private ArtifactGroup createModuleGroup() throws PhrescoException {
	    if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method  Features.createModuleGroup()");
        }
	    
        ArtifactGroup moduleGroup;
        CoreOption moduleCoreOption;
        RequiredOption requiredOption;
        List<CoreOption> appliesTo = new ArrayList<CoreOption>();
        List<RequiredOption> required = new ArrayList<RequiredOption>();
        
        try {
            moduleGroup = new ArtifactGroup();
            List<ArtifactInfo> modules = new ArrayList<ArtifactInfo>();
            ArtifactInfo module = new ArtifactInfo();
            if (StringUtils.isNotEmpty(getFromPage())) {
                moduleGroup.setId(getModuleGroupId());
            }
            moduleGroup.setName(getName());
            if (FEATURES_CORE.equals(getModuleType())) {
            	moduleCoreOption= new CoreOption(getTechnology(), true);
            } else {
            	moduleCoreOption= new CoreOption(getTechnology(), false);;
            }
            
            appliesTo.add(moduleCoreOption);
            moduleGroup.setAppliesTo(appliesTo);
            //TODO: Change the Data type : ARUN PRASANNA
//            moduleGroup.setType(type);
            List<String> customerIds = new ArrayList<String>();
            customerIds.add(getCustomerId());
           
            moduleGroup.setCustomerIds(customerIds);
            moduleGroup.setArtifactId(getArtifactId());
            moduleGroup.setGroupId(getGroupId());
            module.setName(getName());
            module.setDescription(getDescription());
            
            if (StringUtils.isNotEmpty(getDefaultType())) {
            	requiredOption = new RequiredOption(getTechnology(), true);
            } else {
            	requiredOption = new RequiredOption(getTechnology(), false);
            }
            required.add(requiredOption);
            module.setAppliesTo(required);
            
            module.setHelpText(getHelpText());
            module.setVersion(getVersion());
            modules.add(module);
            moduleGroup.setVersions(modules);
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
        
        return moduleGroup;
    }
	
	public String delete() throws PhrescoException {
		if (s_isDebugEnabled) {
			S_LOGGER.debug("Entering Method  Features.delete()");
		}
		
		try {
			String[] moduleGroups = getHttpRequest().getParameterValues(REQ_FEATURES_MOD_GRP);
			if (ArrayUtils.isNotEmpty(moduleGroups)) {
				for (String moduleGroup : moduleGroups) {
					ClientResponse clientResponse = getServiceManager().deleteFeature(moduleGroup, getCustomerId());
					if (clientResponse.getStatus() != RES_CODE_200) {
						addActionError(getText(FEATURE_NOT_DELETED));
					}
				}
				addActionMessage(getText(FEATURE_DELETED));
			}
		} catch (PhrescoException e) {
			showErrorPopup(e, EXCEPTION_FEATURE_DELETE);
		}
		
		return setTechnologiesInRequest();
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
    		
        	ArtifactGroup archetypeInfo = ServerUtil.getArtifactinfo(new ByteArrayInputStream(tempFeaByteArray));
        	FileInfo fileInfo = new FileInfo();
            getHttpResponse().setStatus(getHttpResponse().SC_OK);
            if (archetypeInfo != null) {
            	fileInfo.setMavenJar(true);
            	fileInfo.setSuccess(true);
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
		
		try {
            boolean isError = false;
            //Empty validation for name
            if (StringUtils.isEmpty(getName())) {
                setNameError(getText(KEY_I18N_ERR_NAME_EMPTY));
                isError = true;
            }
            //Validate whether file is selected during add
            if (!EDIT.equals(getFromPage()) && featureByteArray == null) {
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
                if (StringUtils.isNotEmpty(getVersion()) && (StringUtils.isEmpty(getFromPage()) 
                        || (!getVersion().equals(getOldVersion())))) {
                    List<ArtifactGroup> moduleGroups = getServiceManager().getModules(getCustomerId(), getTechnology(), getType());
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