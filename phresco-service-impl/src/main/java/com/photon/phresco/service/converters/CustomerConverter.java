/**
 * Phresco Service Implemenation
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
package com.photon.phresco.service.converters;

import org.springframework.data.document.mongodb.MongoOperations;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.photon.phresco.commons.model.Customer;
import com.photon.phresco.commons.model.RepoInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.CustomerDAO;
import com.photon.phresco.util.ServiceConstants;

public class CustomerConverter implements Converter<CustomerDAO, Customer> {

	@Override
	public Customer convertDAOToObject(CustomerDAO dao,
			MongoOperations mongoOperation) throws PhrescoException {
		Customer customer = new Customer();
		customer.setAddress(dao.getAddress());
		customer.setApplicableTechnologies(dao.getApplicableTechnologies());
		customer.setContactNumber(dao.getContactNumber());
		customer.setCountry(dao.getCountry());
		customer.setCreationDate(dao.getCreationDate());
		customer.setDescription(dao.getDescription());
		customer.setEmailId(dao.getEmailId());
		customer.setFax(dao.getFax());
		customer.setHelpText(dao.getHelpText());
		customer.setId(dao.getId());
		customer.setName(dao.getName());
		RepoInfo repoInfo = mongoOperation.findOne(ServiceConstants.REPOINFO_COLLECTION_NAME, 
				new Query(Criteria.whereId().is(dao.getRepoInfoId())), RepoInfo.class);
		customer.setRepoInfo(repoInfo);
		customer.setState(dao.getState());
		customer.setStatus(dao.getStatus());
		customer.setType(dao.getType());
		customer.setValidFrom(dao.getValidFrom());
		customer.setValidUpto(dao.getValidUpto());
		customer.setZipcode(dao.getZipcode());
		customer.setFrameworkTheme(dao.getFrameworkTheme());
		customer.setSystem(dao.isSystem());
		customer.setOptions(dao.getOptions());
		return customer;
	}

	@Override
	public CustomerDAO convertObjectToDAO(Customer customer)
			throws PhrescoException {
		CustomerDAO customerDAO = new CustomerDAO();
		customerDAO.setAddress(customer.getAddress());
		customerDAO.setApplicableTechnologies(customer.getApplicableTechnologies());
		customerDAO.setContactNumber(customer.getContactNumber());
		customerDAO.setCountry(customer.getCountry());
		customerDAO.setCreationDate(customer.getCreationDate());
		customerDAO.setDescription(customer.getDescription());
		customerDAO.setEmailId(customer.getEmailId());
		customerDAO.setFax(customer.getFax());
		customerDAO.setHelpText(customer.getHelpText());
		customerDAO.setId(customer.getId());
		customerDAO.setName(customer.getName());
		customerDAO.setRepoInfoId(customer.getRepoInfo().getId());
		customerDAO.setState(customer.getState());
		customerDAO.setStatus(customer.getStatus());
		customerDAO.setType(customer.getType());
		customerDAO.setValidFrom(customer.getValidFrom());
		customerDAO.setValidUpto(customer.getValidUpto());
		customerDAO.setZipcode(customer.getZipcode());
		customerDAO.setFrameworkTheme(customer.getFrameworkTheme());
		customerDAO.setSystem(customer.isSystem());
		customerDAO.setOptions(customer.getOptions());
		return customerDAO;
	}

}
