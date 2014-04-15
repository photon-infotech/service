/**
 * Phresco Service Root
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
 */
package com.photon.phresco.service.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.photon.phresco.commons.model.WebService;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.impl.DbService;
import com.photon.phresco.util.ServiceConstants;
import com.photon.phresco.util.TechnologyTypes;

public class WebServiceInfoGenerator extends DbService implements ServiceConstants {
    
    public WebServiceInfoGenerator() {
    	super();
    }
    
    private List<WebService> createWebServices() {
        List<WebService> webServices = new ArrayList<WebService>();
        webServices.add(createWebService("restjson", "REST/JSON", "1.0", "REST JSON web services"));
        webServices.add(createWebService("restxml", "REST/XML", "1.0", "REST XML web services"));
        webServices.add(createWebService("soap1.0", "SOAP", "1.0", "SOAP web services"));
        webServices.add(createWebService("saop2.0", "SOAP", "2.0", "SOAP web services"));
        return webServices;
    }
    
    private WebService createWebService(String id, String name, String version, String description) {
    	WebService webService = new WebService();
    	webService.setId(id);
    	webService.setName(name);
    	webService.setVersion(version);
    	webService.setAppliesToTechs(createTechs());
    	webService.setDescription(description);
    	webService.setSystem(true);
    	return webService;
    }
    
    private List<String> createTechs() {
        String[] techs = new String[]{TechnologyTypes.ANDROID_NATIVE,TechnologyTypes.ANDROID_HYBRID,
                TechnologyTypes.HTML5_MOBILE_WIDGET,TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET,
                TechnologyTypes.HTML5_WIDGET,TechnologyTypes.IPHONE_HYBRID,TechnologyTypes.IPHONE_NATIVE};
        return Arrays.asList(techs);
    }
    
    private void generate() throws PhrescoException {
       List<WebService> webServices = createWebServices();
       mongoOperation.insertList(WEBSERVICES_COLLECTION_NAME, webServices);
    }
    
    public static void main(String[] args) throws PhrescoException {
        WebServiceInfoGenerator generator = new WebServiceInfoGenerator();
        generator.generate();
    }
}
