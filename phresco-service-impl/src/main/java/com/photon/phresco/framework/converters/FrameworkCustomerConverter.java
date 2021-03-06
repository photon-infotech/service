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
package com.photon.phresco.framework.converters;

import org.springframework.data.document.mongodb.MongoOperations;

import com.photon.phresco.commons.model.Customer;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.dao.CustomerDAO;

public class FrameworkCustomerConverter implements Converter<CustomerDAO, Customer> {

	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(FrameworkCustomerConverter.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	@Override
	public Customer convertDAOToObject(CustomerDAO dao,
			MongoOperations mongoOperation) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("FrameworkCustomerConverter.convertDAOToObject:Entry");
		}
		Customer customer = new Customer();
		customer.setDescription(dao.getDescription());
		customer.setId(dao.getId());
		customer.setName(dao.getName());
		customer.setType(dao.getType());
		customer.setFrameworkTheme(dao.getFrameworkTheme());
		customer.setOptions(dao.getOptions());
		customer.setContext(dao.getContext());
		if (isDebugEnabled) {
			LOGGER.debug("FrameworkCustomerConverter.convertDAOToObject:Exit");
		}
		return customer;
	}

	@Override
	public CustomerDAO convertObjectToDAO(Customer customer)
			throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("FrameworkCustomerConverter.convertObjectToDAO:Entry");
		}
		CustomerDAO customerDAO = new CustomerDAO();
		customerDAO.setDescription(customer.getDescription());
		customerDAO.setId(customer.getId());
		customerDAO.setName(customer.getName());
		customerDAO.setType(customer.getType());
		customerDAO.setFrameworkTheme(customer.getFrameworkTheme());
		customerDAO.setOptions(customer.getOptions());
		if (isDebugEnabled) {
			LOGGER.debug("FrameworkCustomerConverter.convertObjectToDAO:Exit");
		}
		return customerDAO;
	}

}
