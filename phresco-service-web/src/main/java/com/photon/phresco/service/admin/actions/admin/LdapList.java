/**
 * Service Web Archive
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
package com.photon.phresco.service.admin.actions.admin;

import com.photon.phresco.service.admin.actions.ServiceBaseAction;

public class LdapList extends ServiceBaseAction { 

	private static final long serialVersionUID = -5914161099723527437L;

	public String list() {
		return ADMIN_LDAP_LIST;	
	}
}