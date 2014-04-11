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
package com.photon.phresco.service.admin.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.photon.phresco.commons.model.LogInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.admin.actions.admin.Videos;



public class ErrorReport extends ServiceBaseAction {
	private static final long serialVersionUID = 1L;
	
//	private static final Logger S_LOGGER = Logger.getLogger(ErrorReport.class);
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(Videos.class.getName());
	private static Boolean debugEnabled  = LOGGER.isDebugEnabled();
	
	private String message = null;
	private String action = null;
	private String userid = null;
	private String trace = null;
	private String reportStatus = null;
	
	public String sendReport() throws PhrescoException {
		if (debugEnabled) {
			LOGGER.debug("ErrorReport.sendReport : Entry");
		}
        try {
        	if (debugEnabled) {
				if (StringUtils.isEmpty(userid)) {
				LOGGER.warn("ErrorReport.sendReport", "status=\"Bad Request\"", "message=\"userId is empty\"");
				}
				LOGGER.info("ErrorReport.sendReport", "userId=" + "\"" + userid);
			}
        	if (debugEnabled) {
				if (StringUtils.isEmpty(message)) {
				LOGGER.warn("ErrorReport.sendReport", "status=\"Bad Request\"", "message=\"message is empty\"");
				}
				LOGGER.info("ErrorReport.sendReport", "message=" + "\"" + message);
			}
        	if (debugEnabled) {
				if (StringUtils.isEmpty(action)) {
				LOGGER.warn("ErrorReport.sendReport", "status=\"Bad Request\"", "message=\"action is empty\"");
				}
				LOGGER.info("ErrorReport.sendReport", "action=" + "\"" + action);
			}
        	LogInfo loginfo = new LogInfo(message, trace, action, userid);
        	List<LogInfo> infos = new ArrayList<LogInfo>();
        	infos.add(loginfo);
 //        	ClientResponse response = getServiceManager().sendErrorReport(infos);
        } catch (Exception e) {
        	if(debugEnabled) {
				LOGGER.error("ErrorReport.sendReport", "status=\"Failure\"", "message=\"" + e.getLocalizedMessage() + "\"");
			}
        	throw new PhrescoException(e);
        }
        
        if (debugEnabled) {
			LOGGER.debug("ErrorReport.sendReport() : Exit");
		}
        return "success";
	}
	
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * @return the trace
	 */
	public String getTrace() {
		return trace;
	}
	/**
	 * @param trace the trace to set
	 */
	public void setTrace(String trace) {
		this.trace = trace;
	}

	/**
	 * @return the reportStatus
	 */
	public String getReportStatus() {
		return reportStatus;
	}

	/**
	 * @param reportStatus the reportStatus to set
	 */
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	

}
