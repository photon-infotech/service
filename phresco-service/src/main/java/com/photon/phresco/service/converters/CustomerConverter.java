/*
 * ###
 * Phresco Service
 * 
 * Copyright (C) 1999 - 2012 Photon Infotech Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ###
 */
package com.photon.phresco.service.converters;

import org.springframework.data.document.mongodb.MongoOperations;

import com.photon.phresco.commons.model.Customer;
import com.photon.phresco.commons.model.RepoInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.dao.CustomerDAO;
import com.photon.phresco.util.ServiceConstants;

public class CustomerConverter implements Converter<CustomerDAO, Customer>, ServiceConstants {
    
    /*
     * Used To Convert The DAO Object Into Customer Object To Include RepoInfo
     */
    public Customer convertDAOToObject(CustomerDAO dao,
            MongoOperations mongoOperation) throws PhrescoException {
        Customer customer = new Customer();
        customer.setId(dao.getId());
        customer.setName(dao.getName());
        customer.setDescription(dao.getDescription());
        customer.setAddress(dao.getAddress());
        customer.setContactNumber(dao.getContactNumber());
        customer.setCountry(dao.getCountry());
        customer.setEmailId(dao.getEmailId());
        customer.setFax(dao.getFax());
        customer.setHelpText(dao.getHelpText());
        customer.setState(dao.getState());
        customer.setStatus(dao.getStatus());
//        customer.setType(dao.getType());
        customer.setZipcode(dao.getZipcode());
        customer.setValidFrom(dao.getValidFrom());
        customer.setValidUpto(dao.getValidUpto());
        PhrescoServerFactory.initialize();
        RepoInfo repoInfo = PhrescoServerFactory.getDbManager().getRepoInfo(dao.getId());
        customer.setRepoInfo(repoInfo);
        return customer;
    }
    
    /*
     * Used To Create CustomerDAO Without RepoInfo Details
     */
    public CustomerDAO convertObjectToDAO(Customer customer)
            throws PhrescoException {
        CustomerDAO dao = new CustomerDAO();
        dao.setId(customer.getId());
        dao.setName(customer.getName());
        dao.setDescription(customer.getDescription());
        dao.setAddress(customer.getAddress());
        dao.setContactNumber(customer.getContactNumber());
        dao.setCountry(customer.getCountry());
        dao.setEmailId(customer.getEmailId());
        dao.setFax(customer.getFax());
        dao.setHelpText(customer.getHelpText());
        dao.setState(customer.getState());
        dao.setStatus(customer.getStatus());
//        dao.setType(customer.getType());
        dao.setValidFrom(customer.getValidFrom());
        dao.setValidUpto(customer.getValidUpto());
        dao.setZipcode(customer.getZipcode());
        return dao;
    }
    
}
