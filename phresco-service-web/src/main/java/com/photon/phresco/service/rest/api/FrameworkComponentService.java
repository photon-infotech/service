package com.photon.phresco.service.rest.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.photon.phresco.commons.model.ArtifactElement;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.Technology;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.exception.PhrescoWebServiceException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.converters.ConvertersFactory;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.dao.CustomerDAO;
import com.photon.phresco.service.impl.DbService;
import com.photon.phresco.util.ServiceConstants;
import com.wordnik.swagger.annotations.ApiError;
import com.wordnik.swagger.annotations.ApiErrors;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = ServiceConstants.REST_API_FRAMEWORK_COMPONENT)
public class FrameworkComponentService extends DbService{
	
	private static final Logger S_LOGGER= Logger.getLogger("SplunkLogger");
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	public FrameworkComponentService() {
		super();
	}
	
	/**
	 * Returns the list of modules
	 * @return
	 */
	@ApiOperation(value = " Retrives features from db")
	@ApiErrors(value = {@ApiError(code=500, reason = "Failed to retrive"), @ApiError(code=204, reason = "Features not found")})
    @RequestMapping(value= REST_API_MODULES, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public @ResponseBody List<ArtifactGroup> findModules(HttpServletResponse response,
			@ApiParam(value = "Customerid to fetch", name = REST_QUERY_CUSTOMERID) @QueryParam(REST_QUERY_CUSTOMERID) String customerId, 
			@ApiParam(value = "Feature type to fetch", name = REST_QUERY_TYPE) @QueryParam(REST_QUERY_TYPE) String type, 
			@ApiParam(value = "Techid to fetch", name = REST_QUERY_TECHID) @QueryParam(REST_QUERY_TECHID) String techId) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into FrameworkComponentService.findModules()" + type);
	    }
	    List<ArtifactGroup> modules = null;
		try {
			Query query = new Query();
			if(StringUtils.isNotEmpty(customerId)) {
        		List<String> customers = new ArrayList<String>();
        		customers.add(customerId);
        		if(StringUtils.isNotEmpty(techId)) {
        			CustomerDAO customer = DbService.getMongoOperation().findOne(CUSTOMERS_COLLECTION_NAME, 
        					new Query(Criteria.whereId().is(customerId)), CustomerDAO.class);
        			if(CollectionUtils.isNotEmpty(customer.getApplicableTechnologies())) {
        				if(customer.getApplicableTechnologies().contains(techId)) {
        					customers.add(DEFAULT_CUSTOMER_NAME);
        				}
        			}
        		}
        		Criteria customerCriteria = Criteria.where(DB_COLUMN_CUSTOMERIDS).in(customers.toArray());
        		query.addCriteria(customerCriteria);
        	}
			Criteria typeQuery = Criteria.where(DB_COLUMN_ARTIFACT_GROUP_TYPE).is(type);
			List<String> technologies = new ArrayList<String>();
			technologies.add(techId);
			if(StringUtils.isNotEmpty(techId)) {
				Technology techInDB = getTechnologyById(techId);
				if(CollectionUtils.isNotEmpty(techInDB.getArchetypeFeatures())) {
					technologies.addAll(techInDB.getArchetypeFeatures());
				}
			}
			Criteria techIdQuery = Criteria.where(DB_COLUMN_APPLIESTOTECHID).in(technologies.toArray());
			query = query.addCriteria(typeQuery);
			query = query.addCriteria(techIdQuery);
			
			List<ArtifactGroupDAO> artifactGroupDAOs = DbService.getMongoOperation().find(ARTIFACT_GROUP_COLLECTION_NAME,
					query, ArtifactGroupDAO.class);
			if(CollectionUtils.isEmpty(artifactGroupDAOs)) {
				response.setStatus(204);
				return modules;
		    }
			modules = convertDAOToModule(artifactGroupDAOs);
			response.setStatus(200);
			return modules;
		} catch(Exception e) {
			response.setStatus(500);
			throw new PhrescoWebServiceException(e, EX_PHEX00005, ARTIFACT_GROUP_COLLECTION_NAME);
		}
	}
	
	private List<ArtifactGroup> convertDAOToModule(List<ArtifactGroupDAO> moduleDAOs) throws PhrescoException {
		Converter<ArtifactGroupDAO, ArtifactGroup> artifactConverter = 
            (Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getFrameworkConverter(ArtifactGroupDAO.class);
	    List<ArtifactGroup> modules = new ArrayList<ArtifactGroup>();
	    for (ArtifactGroupDAO artifactGroupDAO : moduleDAOs) {
			ArtifactGroup artifactGroup = artifactConverter.convertDAOToObject(artifactGroupDAO, DbService.getMongoOperation());
			modules.add(artifactGroup);
		}
        return modules;
    }
	
	@ApiOperation(value = " Retrives features from db")
	@ApiErrors(value = {@ApiError(code=500, reason = "Failed to retrive"), @ApiError(code=204, reason = "Description not found")})
    @RequestMapping(value= REST_API_MODULES_DESC, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public @ResponseBody ArtifactElement findModules(HttpServletResponse response,
			@ApiParam(value = "Artifactgroupid to fetch desc", name = DB_COLUMN_ARTIFACT_GROUP_ID) 
			@QueryParam(DB_COLUMN_ARTIFACT_GROUP_ID) String artifactGroupId) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into FrameworkComponentService.artifactGroupId()" + artifactGroupId);
	    }
		try {
			ArtifactElement artifactElement = DbService.getMongoOperation().findOne(ARTIFACT_ELEMENT_COLLECTION_NAME, 
					new Query(Criteria.where(DB_COLUMN_ARTIFACT_GROUP_ID).is(artifactGroupId)), ArtifactElement.class);
			if(artifactElement == null) {
				response.setStatus(204);
				return artifactElement;
			}
			response.setStatus(200);
			return artifactElement;
		} catch(Exception e) {
			response.setStatus(500);
			throw new PhrescoWebServiceException(e, EX_PHEX00005, ARTIFACT_GROUP_COLLECTION_NAME);
		}
	}
}
