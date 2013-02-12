package com.photon.phresco.service.util;

public interface ServerConstants {

    /*
     * Constants for Archetype parameters
     */
    String ARCHETYPE_ARCHETYPEGROUPID = "-DarchetypeGroupId";
    String ARCHETYPE_ARCHETYPEARTIFACTID = "-DarchetypeArtifactId";
    String ARCHETYPE_ARCHETYPEVERSION = "-DarchetypeVersion";
    String ARCHETYPE_GROUPID = "-DgroupId";
    String ARCHETYPE_ARTIFACTID = "-DartifactId";
    String ARCHETYPE_VERSION = "-Dversion";
    String ARCHETYPE_NAME = "-Dname";
    String ARCHETYPE_ARCHETYPEREPOSITORYURL = "-DarchetypeRepository";
    String ARCHETYPE_INTERACTIVEMODE = "-DinteractiveMode";

    /*
     * Constants for Phresco Archetypes
     */
    String PHRESCO_PHP_ARCHETYPE = "phresco-php-archetype";
    String PHRESCO_DRUPAL7_ARCHETYPE = "phresco-drupal7-archetype";
    String PHRESCO_NODEJS_ARCHETYPE = "phresco-nodejs-archetype";
    String PHRESCO_SHAREPOINT_ARCHETYPE = "phresco-sharepoint-archetype";
    String PHRESCO_ANDROID_ARCHETYPE = "phresco-android-archetype";
    String PHRESCO_IPHONE_ARCHETYPE = "phresco-iphone-archetype";

    /*
     * Constants for Technology these values must be synchronous with the client
     * values if required move these constants to phresco commons constants
     */
    String TECHNOLOGY_PHP = "PHP";
    String TECHNOLOGY_PHP_DRUPAL = "PHP_DRUPAL";
    String TECHNOLOGY_NODE_JS = "NODE_JS";
    String TECHNOLOGY_SHAREPOINT = "SHAREPOINT";
    String TECHNOLOGY_ANDROID = "ANDROID";
    String TECHNOLOGY_IPHONE = "IPHONE";

    // TODO:Move this to dependency properties
    String SETTINGS_CONFIG_FILE = "/config/settings/0.1/settings-0.1.json";
    String HOMEPAGE_JSON_FILE = "/videos/homepage/videoinfo/1.0/videoinfo-1.0.json";
    String ADMIN_CONFIG_FILE = "/config/admin/0.1/admin-0.1.json";

    /*
     * Constants for Version
     */
    String FRAMEWORK_VERSION_GROUPID = "com.photon.phresco.framework";
    String FRAMEWORK_VERSION_ARTIFACTID = "phresco-framework-web";
    String SERVICE_VERSION_GROUPID = "com.photon.phresco.service";
    String SERVICE_VERSION_ARTIFACTID = "phresco-service-web";

    // /Keys for configuration to load from config file
    String CONFIG_KEY_REPOSITORY_USERNAME = "repository.username";
    String CONFIG_KEY_REPOSITORY_PASSWORD = "repository.password";
    String CONFIG_KEY_REPOSITORY_PROTOCOL = "repository.protocol";
    String CONFIG_KEY_REPOSITORY_HOST = "repository.host";
    String CONFIG_KEY_REPOSITORY_PORT = "repository.port";
    String CONFIG_KEY_REPOSITORY_PATH = "repository.path";

    // Authentication Constants
    String AUTHENTICATE = "/ldap/authenticate";

    /*
     * Constants for LDAP properties
     */
    String LDAP_CONTEXT_FACTORY = "phresco.ldap.contextfactory";
    String LDAP_URL = "phresco.ldap.url";
    String LDAP_BASEDN = "phresco.ldap.basedn";
    String LDAP_LOGIN_ATTRIBUTE = "phresco.ldap.login.attribute";
    String LDAP_DISPLAY_NAME_ATTRIBUTE = "phresco.ldap.attribute.displayName";
    String LDAP_MAIL_ATTRIBUTE = "phresco.ldap.attribute.mail";
    String LDAP_CUSTOMER_NAME = "phresco.ldap.attribute.customerName";
    String LDAP_PHRESCO_ENABLED = "phresco.ldap.attribute.phrescoEnabled";

    /*
     * Constants for jar processor
     */
    String JAR_FILE = "jar";
    String ARCHETYPE_FILE = "META-INF/maven/archetype.xml";
    String ARCHETYPE_METADATA_FILE = "META-INF/maven/archetype-metadata.xml";
    String PLUGIN_XML_FILE = "META-INF/maven/plugin.xml";
    String PLUGIN_COMPONENTS_XML_FILE = "META-INF/plexus/components.xml";

    // Constants for create pom.xml file for upload
    String POM_FILE_NAME = "pom.xml";
    String POM_PROJECT = "project";
    String POM_MODELVERSION = "modelVersion";
    String POM_MODELVERSION_VAL = "4.0.0";
    String POM_GROUPID = "groupId";
    String POM_ARTIFACTID = "artifactId";
    String POM_VERSION = "version";
    String POM_PACKAGING = "packaging";
    String POM_DESC = "description";
    String POM_DESC_VAL = "created by phresco";
    String POM_OMMIT = "yes";

    /*
     * Constants for sql folder
     */
    String SOURCE_SQL = "/source/sql/";
    String SRC_SQL = "/src/sql/";
}
