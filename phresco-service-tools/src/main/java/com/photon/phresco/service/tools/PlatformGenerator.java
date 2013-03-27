/**
 * Phresco Service Root
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
package com.photon.phresco.service.tools;

import java.util.ArrayList;
import java.util.List;

import com.photon.phresco.commons.model.PlatformType;
import com.photon.phresco.commons.model.Property;
import com.photon.phresco.commons.model.Role;
import com.photon.phresco.commons.model.Status;
import com.photon.phresco.commons.model.TechnologyOptions;
import com.photon.phresco.service.impl.DbService;

public class PlatformGenerator extends DbService {
	
	public PlatformGenerator() {
		super();
	}
	
	private List<PlatformType> createPlatForm() {
		List<PlatformType> types = new ArrayList<PlatformType>();
		PlatformType platformType = new PlatformType("Windows", 86);
		platformType.setId("Windows x86");
		types.add(platformType);
		platformType = new PlatformType("Windows", 64);
		platformType.setId("Windows x64");
		types.add(platformType);
		platformType = new PlatformType("Mac", 86);
		platformType.setId("Mac x86");
		types.add(platformType);
		platformType = new PlatformType("Mac", 64);
		platformType.setId("Mac x64");
		types.add(platformType);
		platformType = new PlatformType("Linux", 86);
		platformType.setId("Linux x86");
		types.add(platformType);
		platformType = new PlatformType("Linux", 64);
		platformType.setId("Linux x64");
		types.add(platformType);
		platformType = new PlatformType("Windows7", 86);
		platformType.setId("Windows7 x86");
		types.add(platformType);
		platformType = new PlatformType("Windows7", 64);
		platformType.setId("Windows7 x64");
		types.add(platformType);
		platformType = new PlatformType("Server", 86);
		platformType.setId("Server x86");
		types.add(platformType);
		platformType = new PlatformType("Server", 64);
		platformType.setId("Server x64");
		types.add(platformType);
		return types;
	}
	
	private List<Property> createProperties() {
		List<Property> properties = new ArrayList<Property>();
		properties.add(createProperty("Phresco Forum URL", "phresco_forum", "phresco.forum.url", "Phresco Forum URL", 
				"http://172.16.18.86:7070/jforum"));
		return properties;
	}
	
	private Property createProperty(String description, String id, String key, String name, String value) {
		Property property = new Property();
		property.setCustomerIds(createCustmerIds());
		property.setSystem(true);
		property.setDescription(description);
		property.setId(id);
		property.setKey(key);
		property.setName(name);
		property.setValue(value);
		return property;
	}
	
	private List<String> createCustmerIds() {
		List<String> customerIds = new ArrayList<String>();
		customerIds.add("photon");
		return customerIds;
	}
	
	private void publishPlatforms() {
		List<PlatformType> platforms = createPlatForm();
		mongoOperation.insertList("platforms", platforms);
	}
	
	private void publishProperties() {
		List<Property> properties = createProperties();
		mongoOperation.insertList("properties", properties);
	}
	
	private List<Role> createRoles() {
		List<Role> roles = new ArrayList<Role>();
		roles.add(createRole("customeradmin", "customeradmin", Status.ACTIVE));
		roles.add(createRole("phrescoadmin", "phrescoadmin", Status.ACTIVE));
		return roles;
	}
	
	private Role createRole(String id, String name, Status status) {
		Role role = new Role();
		role.setId(id);
		role.setSystem(true);
		role.setName(name);
		role.setStatus(status);
		return role;
	}
	
	private void publishRoles() {
		List<Role> roles = createRoles();
		mongoOperation.insert("roles", roles);
	}
	
	private List<TechnologyOptions> createTechnologyOptions() {
		List<TechnologyOptions> options = new ArrayList<TechnologyOptions>();
		options.add(new TechnologyOptions("Code"));
		options.add(new TechnologyOptions("Build"));
		options.add(new TechnologyOptions("Deploy"));
		options.add(new TechnologyOptions("Run Against Source"));
		options.add(new TechnologyOptions("Unit Test"));
		options.add(new TechnologyOptions("Functional Test"));
		options.add(new TechnologyOptions("Perfomance Test"));
		options.add(new TechnologyOptions("Load Test"));
		options.add(new TechnologyOptions("Reports"));
		options.add(new TechnologyOptions("CI"));
		return options;
	}
	
	private void publishTechnologyoptions() {
		List<TechnologyOptions> options = createTechnologyOptions();
		mongoOperation.insertList("options", options);
	}

	private void publish() {
		publishPlatforms();
//		publishProperties();
//		publishRoles();
//		publishTechnologyoptions();
	}
	
	public static void main(String[] args) {
		PlatformGenerator generator = new PlatformGenerator();
		generator.publish();
	}
}
