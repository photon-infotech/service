package com.photon.phresco.service.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "data", propOrder = {

})
public class RepoData {
    
    private String contentResourceURI;
    private String id;
    private String name;
    private String provider;
    private String providerRole;
    private String format;
    private String repoType;
    private boolean exposed;
    private String writePolicy;
    private boolean browseable;
    private boolean indexable;
    private int notFoundCacheTTL;
    private String repoPolicy;
    private boolean downloadRemoteIndexes;
    private String defaultLocalStorageUrl;
    
    public RepoData() {
        
    }

    public RepoData(String contentResourceURI, String id, String name, String provider, String providerRole, 
            String format, String repoType, boolean exposed, String writePolicy, boolean browseable, 
            boolean indexable, int notFoundCacheTTL, String repoPolicy, boolean downloadRemoteIndexes) {
        this.contentResourceURI = contentResourceURI;
        this.id = id;
        this.name = name;
        this.provider = provider;
        this.providerRole = providerRole;
        this.format = format;
        this.repoType = repoType;
        this.exposed = exposed;
        this.browseable = browseable;
        this.downloadRemoteIndexes = downloadRemoteIndexes;
        this.indexable = indexable;
        this.notFoundCacheTTL = notFoundCacheTTL;
        this.repoPolicy = repoPolicy;
        this.writePolicy = writePolicy;
    }

    public String getProviderRole() {
        return providerRole;
    }

    public void setProviderRole(String providerRole) {
        this.providerRole = providerRole;
    }

    public String getWritePolicy() {
        return writePolicy;
    }

    public void setWritePolicy(String writePolicy) {
        this.writePolicy = writePolicy;
    }

    public boolean isBrowseable() {
        return browseable;
    }

    public void setBrowseable(boolean browseable) {
        this.browseable = browseable;
    }

    public boolean isIndexable() {
        return indexable;
    }

    public void setIndexable(boolean indexable) {
        this.indexable = indexable;
    }

    public int getNotFoundCacheTTL() {
        return notFoundCacheTTL;
    }

    public void setNotFoundCacheTTL(int notFoundCacheTTL) {
        this.notFoundCacheTTL = notFoundCacheTTL;
    }

    public String getRepoPolicy() {
        return repoPolicy;
    }

    public void setRepoPolicy(String repoPolicy) {
        this.repoPolicy = repoPolicy;
    }

    public boolean isDownloadRemoteIndexes() {
        return downloadRemoteIndexes;
    }

    public void setDownloadRemoteIndexes(boolean downloadRemoteIndexes) {
        this.downloadRemoteIndexes = downloadRemoteIndexes;
    }

    public String getDefaultLocalStorageUrl() {
        return defaultLocalStorageUrl;
    }

    public void setDefaultLocalStorageUrl(String defaultLocalStorageUrl) {
        this.defaultLocalStorageUrl = defaultLocalStorageUrl;
    }

    public String getContentResourceURI() {
        return contentResourceURI;
    }

    public void setContentResourceURI(String contentResourceURI) {
        this.contentResourceURI = contentResourceURI;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRepoType() {
        return repoType;
    }

    public void setRepoType(String repoType) {
        this.repoType = repoType;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    @Override
    public String toString() {
        return "RepoData [providerRole=" + providerRole + ", writePolicy="
                + writePolicy + ", browseable=" + browseable + ", indexable="
                + indexable + ", notFoundCacheTTL=" + notFoundCacheTTL
                + ", repoPolicy=" + repoPolicy + ", downloadRemoteIndexes="
                + downloadRemoteIndexes + ", defaultLocalStorageUrl="
                + defaultLocalStorageUrl + "]";
    }
}