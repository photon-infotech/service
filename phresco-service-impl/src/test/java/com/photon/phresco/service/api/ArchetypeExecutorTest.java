/**
 * Phresco Service Implemenation
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
package com.photon.phresco.service.api;

import java.io.File;

import org.junit.Ignore;

import com.photon.phresco.commons.model.ProjectInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.util.FileUtil;

public class ArchetypeExecutorTest extends AbstractPhrescoTest {

	private String PHP_PROJECT_NAME = "phpproject";
	private String PHTN_PHP_PRJ = "PHTN_PHP_PRJ";
	private File projectFolder = null;

	@Ignore
	public void setUp() throws PhrescoException {
		super.setUp();
	}

	@Ignore
	public void tearDown() throws PhrescoException {
		super.tearDown();
		if (projectFolder != null) {
			FileUtil.delete(projectFolder);
		}
	}

	@Ignore
	public final void testExecute() throws PhrescoException {
		//test for phresco-php-archetype
//		projectFolder = archetypeExecutor.execute(createPHPProjectInfo());
//		String[] fileList = projectFolder.list();
//		for (String fileName : fileList) {
//			assertEquals(PHTN_PHP_PRJ, fileName);
//		}
	}

	@Ignore
	private ProjectInfo createPHPProjectInfo() {
		ProjectInfo info = new ProjectInfo();
		info.setName(PHP_PROJECT_NAME);
		info.setDescription("PHP Project created using Phresco");
//		info.setCode(PHTN_PHP_PRJ);
//		info.setVersion("1.0.0");
//		Technology technology = new Technology("tech-php", "PHP");
//		info.setTechnology(technology);
		return info;
	}
}
