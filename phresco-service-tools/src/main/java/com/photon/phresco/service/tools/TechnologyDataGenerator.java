/*
 * ###
 * Phresco Service Tools
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
/*******************************************************************************
 * Copyright (c) 2011 Photon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Photon Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.photon.in/legal/ppl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Photon - initial API and implementation
 ******************************************************************************/
package com.photon.phresco.service.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.codehaus.plexus.util.StringUtils;
import org.w3c.dom.Element;

import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.CoreOption;
import com.photon.phresco.commons.model.RequiredOption;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.api.DbService;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.client.api.ServiceContext;
import com.photon.phresco.service.client.api.ServiceManager;
import com.photon.phresco.service.converters.ConvertersFactory;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.util.ServiceConstants;
import com.photon.phresco.util.TechnologyTypes;


public class TechnologyDataGenerator extends DbService implements ServiceConstants {

    private File inputRootDir;
    private File outputRootDir;
    private File moduleBinariesDir;
    private static final String SHEET_NAME_MODULE = "Modules";
    private static final int NO_OF_ROWS_TO_SKIP = 1;
    private static final String DELIMITER = ",";
    private static final String ID = "mod_";
    private static final String FORWARD_SLASH = "/";
    static final Map<String, String> INPUT_EXCEL_MAP = new HashMap<String, String>(16);
    static final Map<String, File> CONTENTS_HOME_MAP = new HashMap<String, File>(16);
    static final Map<String, String> SNO_AND_NAME_MAP = new HashMap<String, String>(16);
    static final Map<String, String[]> NAME_AND_DEP_MAP = new HashMap<String, String[]>(16);
    public ServiceContext context = null;
    public ServiceManager serviceManager = null;
    private static Map<String, ArtifactGroup> moduleMap = new HashMap<String, ArtifactGroup>();
    RepositoryManager repManager = null;
    
    //For enable deploying artifacts
    private boolean deploy = false;
    
    File outFile = null;

    public TechnologyDataGenerator(File inputRootDir, File outputRootDir, File binariesDir) throws PhrescoException {
        super();
//        PhrescoServerFactory.initialize();
//        repManager = PhrescoServerFactory.getRepositoryManager();
        this.inputRootDir = inputRootDir;
        this.outputRootDir = outputRootDir;
        this.moduleBinariesDir = new File(binariesDir, "/technologies/");
        initContentsHomeMap();
        initInputExcelMap();
        
    }

    
    /**
     * Map To Hold The Technology Wise Features xls Files
     */
    private static void initInputExcelMap() {
        INPUT_EXCEL_MAP.put(TechnologyTypes.PHP,"PHTN_PHRESCO_PHP.xls");
        INPUT_EXCEL_MAP.put(TechnologyTypes.JAVA_WEBSERVICE,"PHTN_PHRESCO_Java-WebService.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.HTML5_WIDGET,"PHTN_PHRESCO_Java-WebService.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.HTML5_MOBILE_WIDGET,"PHTN_PHRESCO_Java-WebService.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET,"PHTN_PHRESCO_Java-WebService.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.PHP_DRUPAL7,"PHTN_PHRESCO_Drupal7.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.SHAREPOINT,"PHTN_PHRESCO_Sharepoint.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.ANDROID_NATIVE,"PHTN_PHRESCO_Andriod-Native.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.IPHONE_NATIVE,"PHTN_PHRESCO_iPhone-Native.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.IPHONE_HYBRID,"PHTN_PHRESCO_iPhone-Native.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.NODE_JS_WEBSERVICE,"PHTN_PHRESCO_NodeJS-WebService.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.ANDROID_HYBRID,"PHTN_PHRESCO_Andriod-Hybrid.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.WORDPRESS, "PHTN_PHRESCO_Wordpress.xls");
	    INPUT_EXCEL_MAP.put(TechnologyTypes.PHP_DRUPAL6, "PHTN_PHRESCO_Drupal6.xls");
        INPUT_EXCEL_MAP.put(TechnologyTypes.JAVA_STANDALONE,"PHTN_PHRESCO_Java-WebService.xls");
        INPUT_EXCEL_MAP.put(TechnologyTypes.PHP_DRUPAL6, "PHTN_PHRESCO_Drupal6.3.xls");
    }

    /**
     * Map To Hold the folder name of each technology 
     */
    private  void initContentsHomeMap() {
        CONTENTS_HOME_MAP.put(TechnologyTypes.PHP,new File(moduleBinariesDir, "php"));
        CONTENTS_HOME_MAP.put(TechnologyTypes.PHP_DRUPAL7, new File(moduleBinariesDir, "drupal"));
        CONTENTS_HOME_MAP.put(TechnologyTypes.SHAREPOINT, new File(moduleBinariesDir, "sharepoint"));
        CONTENTS_HOME_MAP.put(TechnologyTypes.ANDROID_NATIVE, new File(moduleBinariesDir, "android-native"));
        CONTENTS_HOME_MAP.put(TechnologyTypes.IPHONE_NATIVE, new File(moduleBinariesDir, "iphone-native"));
        CONTENTS_HOME_MAP.put(TechnologyTypes.NODE_JS_WEBSERVICE, new File(moduleBinariesDir, "nodejs-webservice"));
        CONTENTS_HOME_MAP.put(TechnologyTypes.ANDROID_HYBRID, new File(moduleBinariesDir, "android-hybrid"));
        CONTENTS_HOME_MAP.put(TechnologyTypes.WORDPRESS, new File(moduleBinariesDir, "wordpress"));
        CONTENTS_HOME_MAP.put(TechnologyTypes.PHP_DRUPAL6, new File(moduleBinariesDir, "drupal6"));
    }

    /**
     * Get xls file from map for the given technology
     * @param techType
     * @return
     * @throws PhrescoException
     */
    private  File getExcelFile(String techType) throws PhrescoException {
        String fileName = INPUT_EXCEL_MAP.get(techType);
        if (fileName == null) {
            throw new PhrescoException("No file defined for " + techType);
        }

        return new File(this.inputRootDir, fileName);
    }
    
    
    /**
     * @param tech
     * @param workBook
     * @throws PhrescoException
     */
    private void createModules(String tech, HSSFWorkbook  workBook)
            throws PhrescoException {
        HSSFSheet sheet = workBook.getSheet(SHEET_NAME_MODULE);
        Iterator<Row> rowsIter = sheet.rowIterator();
        // Skipping first row
        for (int i = 0; i < NO_OF_ROWS_TO_SKIP; i++) {
            rowsIter.next();
        }
        while (rowsIter.hasNext()) {
            Row row = rowsIter.next();
            createModuleGroup(tech, row);
        }
        
        storeModuleInfo();
    }

    /**
     * Handle dependencies.
     *
     * @param modules
     *            the modules
     * @return the modules
     * @throws PhrescoException 
     */
//  private Modules handleDependencies(Modules modules) {
//      Modules modifiedModules = new Modules();
//      List<Module> moduleList = modules.getModule();
//      for (Module module : moduleList) {
//          List<String> depModNameList = new ArrayList<String>();
//          String modName = module.getId();
//          String[] depList = NAME_AND_DEP_MAP.get(modName);
//          if (depList == null) {
//              modifiedModules.getModule().add(module);
//              continue;
//          }
//          for (String dep : depList) {
//              int depNo = (int) (Double.valueOf(dep).doubleValue());
//              String depModName = SNO_AND_NAME_MAP.get(String.valueOf(depNo));
//              depModNameList.add(depModName);
//          }
//
//          if (!depModNameList.isEmpty()) {
//              module.getDependentModules().addAll(depModNameList);
//          }
//
//          modifiedModules.getModule().add(module);
//      }
//      return modifiedModules;
//  }

    /**
     * To save the module details in Db
     * @throws PhrescoException
     */
    private void storeModuleInfo() throws PhrescoException {
//      RestClient<ModuleGroup> applicationTypeClient = serviceManager.getRestClient(REST_API_COMPONENT + REST_API_MODULES);
      Set<String> keySet = moduleMap.keySet();
      List<ArtifactGroup> createdModules = new ArrayList<ArtifactGroup> ();
      for (String string : keySet) {
          ArtifactGroup moduleGroup = moduleMap.get(string);
          createdModules.add(moduleGroup);
          convertAndStrore(moduleGroup);
      }
//      ClientResponse response = applicationTypeClient.create(createdModules);
//      System.out.println(response.getStatus());
      return;
    }


    private void convertAndStrore(ArtifactGroup moduleGroup) throws PhrescoException {
        List<ArtifactInfo> versions = moduleGroup.getVersions();
        List<String> versionIds = new ArrayList<String>();
        
        Converter<ArtifactGroupDAO, ArtifactGroup> converter = 
            (Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getConverter(ArtifactGroupDAO.class);
        ArtifactGroupDAO moduleGroupDAO = converter.convertObjectToDAO(moduleGroup);
        String id2 = moduleGroupDAO.getId();
        for (ArtifactInfo module : versions) {
        	versionIds.add(module.getId());
            module.setArtifactGroupId(id2);
            System.out.println(module);
            mongoOperation.save("ArtifactInfo", module);
        }
        moduleGroupDAO.setVersionIds(versionIds);
      mongoOperation.save("ArtifactDAO", moduleGroupDAO);
    }


    /**
     * Create modulegroup by each row
     * @param techId
     * @param row
     * @throws PhrescoException
     */
    private void createModuleGroup(String techId, Row row) throws PhrescoException {

        ArtifactGroup moduleGroup = new ArtifactGroup();
        
        Cell serialNo = row.getCell(0);
        if (serialNo == null || Cell.CELL_TYPE_BLANK == serialNo.getCellType()) {
            return;
        }

        String name = row.getCell(1).getStringCellValue();
        
        String identifier = ID + row.getCell(1).getStringCellValue().toLowerCase().
            replace(" ", "_");                
        String version = "1.0";
        Cell versionCell = row.getCell(2);
        if (versionCell != null && Cell.CELL_TYPE_BLANK != versionCell.getCellType()) {
            version = getValue(versionCell);
        }
        
        Cell requireCell = row.getCell(3);
        String req = null;
        if (requireCell != null && Cell.CELL_TYPE_BLANK != requireCell.getCellType()) {
            req = getValue(requireCell);
        }
        
        Cell coreCell = row.getCell(4);
        String core = null;
        if (coreCell != null && Cell.CELL_TYPE_BLANK != coreCell.getCellType()) {
            core = getValue(coreCell);
        }
        
        int sNoInt = (int) serialNo.getNumericCellValue();
        SNO_AND_NAME_MAP.put(String.valueOf(sNoInt), identifier);

        Cell depCell = row.getCell(5);
        if (depCell != null && Cell.CELL_TYPE_BLANK != depCell.getCellType()) {
            String depModules = getValue(depCell);
            String[] depModuleIds = StringUtils.split(depModules,DELIMITER);
            NAME_AND_DEP_MAP.put(identifier, depModuleIds);
        }
        
        Cell helptextCell = row.getCell(10);
        String helpText = "";
        if (helptextCell != null && Cell.CELL_TYPE_BLANK != helptextCell.getCellType()) {
            helpText = helptextCell.getStringCellValue();
        }
        
        Cell description = row.getCell(9);
        String docContent = "";
        if (description != null && Cell.CELL_TYPE_BLANK != description.getCellType()) {
            docContent = description.getStringCellValue();
            
        }
        
        String filePath = "";
        String fileExt = "zip";
        Cell filenameCell = row.getCell(13);
        if (filenameCell != null
                && Cell.CELL_TYPE_BLANK != filenameCell.getCellType()) {

            filePath = filenameCell.getStringCellValue().trim();

            if (filePath.endsWith(".tar.gz")) {
                fileExt = "tar.gz";
            } else if (filePath.endsWith(".tar")) {
                fileExt = "tar";
            } else if (filePath.endsWith(".zip")) {
                fileExt = "zip";
            } else if (filePath.endsWith(".jar")) {
                fileExt = "jar";
            }
        }
        
        Cell imagenameCell = row.getCell(16);
        if (imagenameCell != null
                && Cell.CELL_TYPE_BLANK != imagenameCell.getCellType()) {
            
            String imageName = imagenameCell.getStringCellValue().trim();
            moduleGroup.setImageURL(createImageUrl(techId, imageName));
            }
        
        Cell groupNameCell = row.getCell(14);
        String groupId = null;
        if (groupNameCell != null
                && Cell.CELL_TYPE_BLANK != groupNameCell.getCellType()) {
            groupId = groupNameCell.getStringCellValue();
        }

        Cell artifactCell = row.getCell(15);
        String artifactId = null;
        if (artifactCell != null
                && Cell.CELL_TYPE_BLANK != artifactCell.getCellType()) {
            artifactId = artifactCell.getStringCellValue();
        }
        
        List<ArtifactInfo> versions = createModuleList(name, convertBoolean(core), 
                convertBoolean(req), version, identifier, fileExt, techId, filePath, "", groupId, artifactId);
        moduleGroup.setName(name);
        
        if(org.apache.commons.lang.StringUtils.isNotEmpty(groupId)) {
            moduleGroup.setGroupId(groupId);
        } else {
            moduleGroup.setGroupId("modules." + techId + ".files");
        }
        if(org.apache.commons.lang.StringUtils.isNotEmpty(artifactId)) {
            moduleGroup.setArtifactId(artifactId);
        } else {
            moduleGroup.setArtifactId(identifier + "_" + version);
        }
        
        List<CoreOption> options = new ArrayList<CoreOption>();
        if(techId.equals(TechnologyTypes.JAVA_WEBSERVICE)) {
        	options = createCoreOptionsForJava(convertBoolean(core));
        } else {
            CoreOption coreoption = new CoreOption(techId, convertBoolean(core));
            options.add(coreoption);
        }
        
        moduleGroup.setDescription(docContent);
        moduleGroup.setHelpText(helpText);
        moduleGroup.setSystem(true);
        moduleGroup.setType("module");
        List<String> customers = new ArrayList<String>();
        customers.add("photon");
        moduleGroup.setCustomerIds(customers);
        moduleGroup.setVersions(versions);
        moduleGroup.setSystem(true);
        moduleGroup.setAppliesTo(options);
        makeMap(identifier, moduleGroup);
    }

    private List<CoreOption> createCoreOptionsForJava(Boolean core) {
    	System.out.println("Entred To createcore objects");
    	String javaList[] = new String[]{TechnologyTypes.JAVA_STANDALONE, TechnologyTypes.JAVA_WEBSERVICE, TechnologyTypes.HTML5, 
    			TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET, TechnologyTypes.HTML5_MOBILE_WIDGET,
    			TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, TechnologyTypes.HTML5_WIDGET};
    	List<CoreOption> options = new ArrayList<CoreOption>();
    	CoreOption option  = null;
    	for (int i = 0; i < javaList.length; i++) {
			option = new CoreOption(javaList[i], core);
			options.add(option);
		}
    	return options;
	}


	/**
     * To create imageurl
     * @param techId
     * @param imageName
     * @return
     */
    private String createImageUrl(String techId, String imageName) {
        return "images" + FORWARD_SLASH + techId + FORWARD_SLASH + imageName;
    }

    /**
     * create list of modules for each modulegroup
     * @param name
     * @param core
     * @param req
     * @param version
     * @param id
     * @param ext
     * @param techId
     * @param filePath
     * @param imagePath
     * @param artifactId 
     * @param groupId 
     * @return
     * @throws PhresrcoException
     */
    private List<ArtifactInfo> createModuleList(String name, boolean core, boolean req, 
            String version, String id, String ext, String techId, String filePath, String imagePath,
            String groupId, String artifactId) throws PhrescoException {
        List<ArtifactInfo> modules = new ArrayList<ArtifactInfo>();
        ArtifactInfo module = new ArtifactInfo();
        module.setVersion(version);
        List<RequiredOption> requires = new ArrayList<RequiredOption>();
        RequiredOption option = new RequiredOption(techId, req);
        requires.add(option);
        String moduleId = id + "_" + techId.replace("-", "_" + "_") + version;
        module.setId(moduleId);
        modules.add(module);
//        publishModule(techId, module, filePath, ext);
//        publishImageFile(techId, module, imagePath);
        return modules;
    }

    /**
     * Make the module map based on id
     * @param id
     * @param moduleGroup
     */
    private void makeMap(String id, ArtifactGroup moduleGroup) {
        if(moduleMap.containsKey(id)) {
            ArtifactGroup prevVersions = moduleMap.get(id);
            List<ArtifactInfo> versions = prevVersions.getVersions();
            versions.addAll(moduleGroup.getVersions());
            prevVersions.setVersions(versions);
            moduleMap.put(id, prevVersions);
        } else {
            moduleMap.put(id, moduleGroup);
        }
    }
     
    /**
     * To create module content url
     * @param techId
     * @param version
     * @param ext
     * @param id
     * @return
     */
    private String createContentURL(String techId, String version, String ext, String id) {
        return "/modules/" + techId + "/files/" + id + "/"
        + version + "/" + id + "-"
        + version + "." + ext;
    }
    
    /**
     * To Convert the given string to boolean
     * @param strRequired
     * @return
     */
    private Boolean convertBoolean(String strRequired) {
        if ("Yes".equalsIgnoreCase(strRequired)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * To get the string value of given cell
     * @param cell
     * @return
     */
    private String getValue(Cell cell) {
        if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
            return cell.getStringCellValue();
        }

        if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
            return String.valueOf(cell.getNumericCellValue());
        }

        return null;
    }

    /**
     * Initiate the creation process
     * @throws PhrescoException
     */
    public final void generateAll() throws PhrescoException {
        for (String tech : INPUT_EXCEL_MAP.keySet()) {
            generate(tech);
        }
    }

    /**
     * Load the xls file based on technology
     * @param tech
     * @throws PhrescoException
     */
    public void generate(String tech) throws PhrescoException {
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(getExcelFile(tech));
            System.out.println("Processing excel file for "
                    + getExcelFile(tech));

            HSSFWorkbook workBook = new HSSFWorkbook(fs);
            createModules(tech, workBook);

        } catch (IOException e) {
            throw new PhrescoException(e);
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * To publish the image file to repository
     * @param tech
     * @param module
     * @param imagePath
     * @throws PhrescoException
     */
    private void publishImageFile(String tech, ArtifactGroup module, String imagePath)
            throws PhrescoException {
        if(deploy == false) {
            return;
        }
        
        File root = CONTENTS_HOME_MAP.get(tech);
        String groupId = "modules." + tech + ".files";
        File imageFile = new File(root, imagePath);
        System.out.println("Image path is" + imageFile.getPath());
        if (!imageFile.exists()) {
            System.out.println("Module not exist : " + module.getName()
                    + " filePath: " + imagePath);
            System.out.println("artifact.toString() " + imageFile.toString());
            return;
        }
//        ArtifactInfo info = new ArtifactInfo(groupId, module.getId(), "",
//                "png", module.getVersion());
//        repManager.addArtifact(info, imageFile, "photon");
        System.out.println("Module : " + module.getName() + " filePath: "
                + imagePath + " added succesfully");
    }
    
    
    /**
     * tp publish the module content to repository
     * @param tech
     * @param module
     * @param fileURL
     * @param ext
     * @throws PhrescoException
     */
    private void publishModule(String tech, ArtifactGroup module, String fileURL,
            String ext) throws PhrescoException {
        if(deploy == false) {
            return;
        }
        
        File root = CONTENTS_HOME_MAP.get(tech);
        String groupId = "modules." + tech + ".files";
        File artifact = new File(root, fileURL);
//        File pomFile = createPomFile(module, ext, groupId);
        if (!artifact.exists()) {
            System.out.println("Module not exist : " + module.getName()
                    + " filePath: " + fileURL);
            System.out.println("artifact.toString() " + artifact.toString());
            return;
        }

//        ArtifactInfo info = new ArtifactInfo(groupId, module.getId(), "", ext,
//                module.getVersion());
//        info.setPomFile(pomFile);
//        repManager.addArtifact(info, artifact, "photon");
//        pomFile.delete();
        System.out.println("Module : " + module.getName() + " filePath: "
                + fileURL + " added succesfully");
    }

    /**
     * Create pom.xml file for each module
     * @param module
     * @param ext
     * @param groupId2
     * @return
     * @throws PhrescoException
     */
    private File createPomFile(ArtifactInfo module, String ext, String groupId2)
            throws PhrescoException {
        System.out.println("Creating Pom File For " + module.getName());
        File file = new File(outputRootDir, "pom.xml");
        DocumentBuilderFactory domFactory = DocumentBuilderFactory
                .newInstance();
        try {
            DocumentBuilder domBuilder = domFactory.newDocumentBuilder();

            org.w3c.dom.Document newDoc = domBuilder.newDocument();
            Element rootElement = newDoc.createElement("project");
            newDoc.appendChild(rootElement);

            Element modelVersion = newDoc.createElement("modelVersion");
            modelVersion.setTextContent("4.0.0");
            rootElement.appendChild(modelVersion);

            Element groupId = newDoc.createElement("groupId");
            groupId.setTextContent(groupId2);
            rootElement.appendChild(groupId);

            Element artifactId = newDoc.createElement("artifactId");
            artifactId.setTextContent(module.getId());
            rootElement.appendChild(artifactId);

            Element version = newDoc.createElement("version");
            version.setTextContent(module.getVersion());
            rootElement.appendChild(version);

            Element packaging = newDoc.createElement("packaging");
            packaging.setTextContent(ext);
            rootElement.appendChild(packaging);

            Element description = newDoc.createElement("description");
            description.setTextContent("created by phresco");
            rootElement.appendChild(description);

            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(newDoc);
            trans.transform(source, result);
            String xmlString = sw.toString();
            FileWriter writer = new FileWriter(file);
            writer.write(xmlString);
            writer.close();

        } catch (Exception e) {
            throw new PhrescoException();
        }
        return file;
    }
    
    public static void main(String[] args) throws PhrescoException {
    	System.out.println("Hi");
    	File toolsHome = new File("/Users/jeb/work/projects/phresco/source/master/service/phresco-service-runner/delivery/tools");
        File binariesDir = new File("D:\\work\\phresco\\Phresco-binaries\\");
        File inputRootDir = new File(toolsHome, "files");
        File outputRootDir = new File(toolsHome, "repo");
        TechnologyDataGenerator dataGen = new TechnologyDataGenerator(inputRootDir, outputRootDir, binariesDir);
        dataGen.generateAll();
        System.out.println(moduleMap.size());
        Set<String> keySet = moduleMap.keySet();
        List<ArtifactGroup> group = new ArrayList<ArtifactGroup> ();
        for (String string : keySet) {
            ArtifactGroup moduleGroup = moduleMap.get(string);
            group.add(moduleGroup);
        }
        System.out.println(group.size());
    }
}
