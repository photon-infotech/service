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
package com.photon.phresco.service.admin.actions.admin;

import org.apache.log4j.Logger;

import com.photon.phresco.service.admin.actions.ServiceBaseAction;

public class Users extends ServiceBaseAction { 

    private static final long serialVersionUID = 1L;
    
    private static final Logger S_LOGGER = Logger.getLogger(Customers.class);
    private static Boolean s_isDebugEnabled = S_LOGGER.isDebugEnabled();

    public String list() {
        if (s_isDebugEnabled) {
            S_LOGGER.debug("Entering Method Users.list()");
        }
        
		return ADMIN_USER_LIST;	
	}
}