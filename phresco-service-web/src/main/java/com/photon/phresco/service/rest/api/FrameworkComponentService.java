package com.photon.phresco.service.rest.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

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

@Path(ServiceConstants.REST_API_FRAMEWORK_COMPONENT)
public class FrameworkComponentService extends DbService{
	
	private static final Logger S_LOGGER= Logger.getLogger(FrameworkComponentService.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	
	public FrameworkComponentService() {
		super();
	}
	
	/**
	 * Returns the list of modules
	 * @return
	 */
	@GET
	@Path (REST_API_MODULES)
	@Produces (MediaType.APPLICATION_JSON)
	public Response findModules(@QueryParam(REST_QUERY_CUSTOMERID) String customerId, @QueryParam(REST_QUERY_TYPE) String type, 
			@QueryParam(REST_QUERY_TECHID) String techId) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into FrameworkComponentService.findModules()" + type);
	    }
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
		    	return Response.status(Response.Status.NO_CONTENT).build();
		    }
		    List<ArtifactGroup> modules = convertDAOToModule(artifactGroupDAOs);
		    ResponseBuilder response = Response.status(Response.Status.OK);
			return response.entity(modules).build();
		} catch(Exception e) {
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
	
	@GET
	@Path (REST_API_MODULES_DESC)
	@Produces (MediaType.APPLICATION_JSON)
	public Response findModules(@QueryParam(DB_COLUMN_ARTIFACT_GROUP_ID) String artifactGroupId) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into FrameworkComponentService.artifactGroupId()" + artifactGroupId);
	    }
		try {
			ArtifactElement artifactElement = DbService.getMongoOperation().findOne(ARTIFACT_ELEMENT_COLLECTION_NAME, 
					new Query(Criteria.where(DB_COLUMN_ARTIFACT_GROUP_ID).is(artifactGroupId)), ArtifactElement.class);
		    ResponseBuilder response = Response.status(Response.Status.OK);
			return response.entity(artifactElement).build();
		} catch(Exception e) {
			throw new PhrescoWebServiceException(e, EX_PHEX00005, ARTIFACT_GROUP_COLLECTION_NAME);
		}
	}
}
