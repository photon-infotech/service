/**
 * Phresco Service
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
package com.photon.phresco.service.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement
@XmlType(name = "data", propOrder = {

})
public class GroupRepository {
    
    private String contentResourceURI;
    private String id;
    private String name;
    private String provider;
    private String format;
    private String repoType;
    private boolean exposed;
    private List<Repository> repositories;
    
    public GroupRepository() {
        
    }
    
    public GroupRepository(String contentResourceURI, String id, String name, String provider, String format, String repoType, 
            boolean exposed, List<Repository> repositories) {
        this.contentResourceURI = contentResourceURI;
        this.id = id;
        this.name = name;
        this.provider = provider;
        this.format = format;
        this.repoType = repoType;
        this.exposed = exposed;
        this.repositories = repositories;
    }
    

    /**
     * @return
     */
    public String getContentResourceURI() {
        return contentResourceURI;
    }
    
    /**
     * @param contentResourceURI
     */
    public void setContentResourceURI(String contentResourceURI) {
        this.contentResourceURI = contentResourceURI;
    }
    
    /**
     * @return
     */
    public String getId() {
        return id;
    }
    
    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return
     */
    public String getProvider() {
        return provider;
    }
    
    /**
     * @param provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    /**
     * @return
     */
    public String getFormat() {
        return format;
    }
    
    /**
     * @param format
     */
    public void setFormat(String format) {
        this.format = format;
    }
    
    /**
     * @return
     */
    public String getRepoType() {
        return repoType;
    }
    
    /**
     * @param repoType
     */
    public void setRepoType(String repoType) {
        this.repoType = repoType;
    }
    
    /**
     * @return
     */
    public boolean getExposed() {
        return exposed;
    }
    
    /**
     * @param exposed
     */
    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    /**
     * @return
     */
    public List<Repository> getRepositories() {
        return repositories;
    }

    /**
     * @param repositories
     */
    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }

    public String toString() {
        return new ToStringBuilder(this,
                ToStringStyle.DEFAULT_STYLE)
                .append("contentResourceURI", contentResourceURI)
                .append("id", id)
                .append("name", name)
                .append("provider", provider)
                .append("format", format)
                .append("repoType", repoType)
                .append("exposed", exposed)
                .toString();
    }
}
