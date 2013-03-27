/**
 * phresco-ldap
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
package com.photon.phresco.ldap;

public interface LDAPConstants {

    /*
     * Constants for LDAP properties
     */
    String LDAP_CONTEXT_FACTORY = "phresco.ldap.contextfactory";
    String LDAP_URL = "phresco.ldap.url";
    String LDAP_BASEDN = "phresco.ldap.basedn";
    String LDAP_LOGIN_ATTRIBUTE = "phresco.ldap.login.attribute";
    String LDAP_DISPLAY_NAME_ATTRIBUTE = "phresco.ldap.attribute.displayName";
    String LDAP_MAIL_ATTRIBUTE = "phresco.ldap.attribute.mail";
    String LDAP_CUSTOMER_NAME="phresco.ldap.attribute.customerName";
    String LDAP_PHRESCO_ENABLED="phresco.ldap.attribute.phrescoEnabled";
    
}
