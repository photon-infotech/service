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
package com.photon.phresco.service.rest.api;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.photon.phresco.configuration.Environment;
import com.photon.phresco.service.util.ServerConstants;
import com.wordnik.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/settings")
public class EnvironmentService implements ServerConstants {
	
	@ApiOperation(value = " Get default environment")
	@RequestMapping(value = "/env", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public @ResponseBody Environment getEnvInfos() {
        return new Environment("Production", "Production Environment is used for Development purpose only", true, null);
    }
}

