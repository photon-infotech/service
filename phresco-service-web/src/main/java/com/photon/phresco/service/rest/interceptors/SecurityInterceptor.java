package com.photon.phresco.service.rest.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.rest.util.AuthenticationUtil;
import com.photon.phresco.util.Constants;
import com.photon.phresco.util.ServiceConstants;

public class SecurityInterceptor implements HandlerInterceptor {
	
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger("SplunkLogger");
	
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object object) throws Exception {
		String authToken = request.getHeader(Constants.PHR_AUTH_TOKEN);
		String swaggerToken = request.getHeader("X-phr-requestfrom");
		if(StringUtils.isNotEmpty(swaggerToken) && swaggerToken.equals("swagger")) {
			return true;
		}
		String path = request.getRequestURI();
		if((path).contains(ServiceConstants.LOGIN) || (path).contains(ServiceConstants.ADMIN_CUSTOMER) || 
				(path).contains(ServiceConstants.CUSTOMER_PROPERTIES) || (path).contains("/api-docs")) {
			LOGGER.debug("SecurityInterceptor.preHandle", "remoteAddress=" + request.getRemoteAddr() , "endpoint=" + path,
					"method=" + request.getMethod());
			return true;
		}
		AuthenticationUtil authTokenUtil = null;
		try {
			authTokenUtil = AuthenticationUtil.getInstance();
		} catch (PhrescoException e) {
			try {
				throw new PhrescoException(e, ServiceConstants.EX_PHEX00007);
			} catch (PhrescoException e1) {
				LOGGER.error("SecurityInterceptor.filter " , "status=\"Failure\"", "message=\"" + e1.getLocalizedMessage() + "\"" );
			}
		}
		LOGGER.debug("SecurityInterceptor.filter", "remoteAddress=" + request.getRemoteAddr() , "token=" + authToken , 
				"user=" + authTokenUtil.getUserName(authToken), "endpoint=" + path);
		boolean isValid = authTokenUtil.isValidToken(authToken);
		if(!isValid) {
			response.setStatus(401);
			return false;
		}
		return true;
	}

}
