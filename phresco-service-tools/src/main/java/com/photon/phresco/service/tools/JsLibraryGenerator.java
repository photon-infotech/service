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
package com.photon.phresco.service.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactGroup.Type;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.CoreOption;
import com.photon.phresco.commons.model.RequiredOption;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.converters.ConvertersFactory;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.impl.DbService;
import com.photon.phresco.util.TechnologyTypes;


public class JsLibraryGenerator extends DbService {

	private static final String id1 = "jslib_";
	private static final int noOfRowsToSkip = 1;
	private static final String JSLIBRARY = "JSLibraries";
	private static final String DELIMITER = ",";
	static final Map<String, String> INPUT_EXCEL_MAP = new HashMap<String, String>(16);
	private Map<String, ArtifactGroup> artifactMap = new HashMap<String, ArtifactGroup>();
	private RepositoryManager manager;
	private HSSFWorkbook workBook;
	private File outFile;
	private File inputDir;
	private File binariesDir;
	
	private boolean deployArtifact = true;

	static final Map<String, File> contentsHomeMap = new HashMap<String, File>(
			16);


	public JsLibraryGenerator(File inputFileDir, File outDir, File binariesDir2)
			throws PhrescoException {
		super();
		this.outFile = outDir;
		this.inputDir = inputFileDir;
		this.binariesDir = binariesDir2;
		PhrescoServerFactory.initialize();
		this.manager = PhrescoServerFactory.getRepositoryManager();
		initInputExcelMap();
	}
	
	private static void initInputExcelMap() {
        INPUT_EXCEL_MAP.put(TechnologyTypes.PHP, "PHTN_PHRESCO_JS-Libraries.xls");
//        INPUT_EXCEL_MAP.put(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, "PHTN_PHRESCO_Html5_Jquery_Widget_JS-Libraries.xls");
    }

	public void publish() throws PhrescoException {
	    for (String tech : INPUT_EXCEL_MAP.keySet()) {
            generate(tech);
        }
	}


	private void generate(String tech) throws PhrescoException {
        workBook = getWorkBook(tech);
        HSSFSheet sheet = workBook.getSheet(JSLIBRARY);
        Iterator<Row> rowIterator = sheet.rowIterator();
        for (int i = 0; i < noOfRowsToSkip; i++) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            createJSLibrary(row, tech);
            
        }
        storeModuleInfo();
    }
	
	/**
     * To save the module details in Db
     * @throws PhrescoException
     */
    private void storeModuleInfo() throws PhrescoException {
      Set<String> keySet = artifactMap.keySet();
      List<ArtifactGroup> createdModules = new ArrayList<ArtifactGroup> ();
      for (String string : keySet) {
          ArtifactGroup moduleGroup = artifactMap.get(string);
          createdModules.add(moduleGroup);
          convertAndStrore(moduleGroup);
          
      }
      return;
    }


    private void convertAndStrore(ArtifactGroup artifactGroup) throws PhrescoException {
        List<ArtifactInfo> versions = artifactGroup.getVersions();
        List<String> versionIds = new ArrayList<String>();
        Converter<ArtifactGroupDAO, ArtifactGroup> converter = 
            (Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getConverter(ArtifactGroupDAO.class);
        ArtifactGroupDAO moduleGroupDAO = converter.convertObjectToDAO(artifactGroup);
        String id2 = moduleGroupDAO.getId();
        for (ArtifactInfo module : versions) {
        	versionIds.add(module.getId());
            module.setArtifactGroupId(id2);
            System.out.println(module);
            mongoOperation.save(ARTIFACT_INFO_COLLECTION_NAME, module);
        }
        moduleGroupDAO.setVersionIds(versionIds);
      mongoOperation.save(ARTIFACT_GROUP_COLLECTION_NAME, moduleGroupDAO);
    }
    
    private HSSFWorkbook getWorkBook(String tech) throws PhrescoException  {
        FileInputStream fs = null;
        try {
           fs = new FileInputStream(new File(inputDir, INPUT_EXCEL_MAP.get(tech)));
           workBook = new HSSFWorkbook(fs);
        } catch (Exception e) {
            throw new PhrescoException();
        }
        return workBook;
    }

	private String getValue(Cell cell) {
		if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
			return cell.getStringCellValue();
		}

		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			return String.valueOf(cell.getNumericCellValue());
		}

		return null;
	}

	private void createJSLibrary(Row row, String techId) throws PhrescoException {
		ArtifactGroup moduleGroup = new ArtifactGroup();
		
		Cell serialNo = row.getCell(0);
		if (serialNo == null || Cell.CELL_TYPE_BLANK == serialNo.getCellType()) {
			return;
		}

		String name = row.getCell(1).getStringCellValue();
		
		String[] versions = new String[]{"1.0"};
		Cell versionCell = row.getCell(2);
		if (versionCell != null && Cell.CELL_TYPE_BLANK != versionCell.getCellType()) {
			String version = getValue(versionCell);
			versions = StringUtils.split(version,DELIMITER);
		}
		
		Boolean req = false;
		Cell required = row.getCell(9);
        if (required != null && Cell.CELL_TYPE_BLANK != required.getCellType()) {
            req = convertBoolean(getValue(required));
        }
        
		String identifier = id1
				+ row.getCell(1).getStringCellValue().toLowerCase();
		String no = String.valueOf(identifier);
		
		String helpText = "";
		Cell helptext = row.getCell(7);
		if (helptext != null && Cell.CELL_TYPE_BLANK != helptext.getCellType()) {
			helpText = getValue(helptext);
		}
		
		Cell description = row.getCell(4);
        if (description != null && Cell.CELL_TYPE_BLANK != description.getCellType()) {
        	
        }
        
        String fileExt = "js";
        String filePath = "";
		Cell filenameCell = row.getCell(8);
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
			} else if (filePath.endsWith(".zip")) {
				fileExt = "zip";
			}

		}
		List<ArtifactInfo> moduleVersions = createModules(identifier, versions, req, techId);
		
		List<CoreOption> appliesto = null;
		if(techId.equals(TechnologyTypes.PHP)) {
			appliesto = createCoreForPhp();
		} else {
			appliesto = createCoreForJquery();
		}
		
		String groupId = "jslibraries.files";
		String artifactId = "jslib_" + name.toLowerCase().replaceAll(" ", "_");
		moduleGroup.setName(name);
		moduleGroup.setId(identifier);
		moduleGroup.setSystem(true);
		List<String> customerIds = new ArrayList<String>();
		customerIds.add("photon");
		moduleGroup.setCustomerIds(customerIds);
		moduleGroup.setGroupId(groupId);
		moduleGroup.setArtifactId(artifactId);
		moduleGroup.setHelpText(helpText);
		moduleGroup.setPackaging(fileExt);
		moduleGroup.setType(Type.JAVASCRIPT);
		moduleGroup.setAppliesTo(appliesto);
		moduleGroup.setVersions(moduleVersions);
		makeMap(name, moduleGroup);
//		publishJSLibrary(groupId, artifactId, versions[0], fileExt, filePath);
	}


	private void makeMap(String name, ArtifactGroup moduleGroup) {
		if(artifactMap.containsKey(name)) {
            ArtifactGroup prevVersions = artifactMap.get(name);
            List<ArtifactInfo> versions = prevVersions.getVersions();
            versions.addAll(moduleGroup.getVersions());
            prevVersions.setVersions(versions);
            artifactMap.put(name, prevVersions);
        } else {
        	artifactMap.put(name, moduleGroup);
        }
	}

	private List<CoreOption> createCoreForJquery() {
		List<CoreOption> options = new ArrayList<CoreOption>();
		CoreOption opt = new CoreOption(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET, true);
		options.add(opt);
		opt = new CoreOption(TechnologyTypes.HTML5_MOBILE_WIDGET, true);
		options.add(opt);
		opt = new CoreOption(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, true);
		options.add(opt);
		opt = new CoreOption(TechnologyTypes.HTML5_WIDGET, true);
		options.add(opt);
		return options;
	}

	private List<CoreOption> createCoreForPhp() {
		List<CoreOption> options = new ArrayList<CoreOption>();
		CoreOption opt = new CoreOption(TechnologyTypes.PHP, true);
		options.add(opt);
		opt = new CoreOption(TechnologyTypes.ANDROID_HYBRID, true);
		options.add(opt);
		opt = new CoreOption(TechnologyTypes.IPHONE_NATIVE, true);
		options.add(opt);
		opt = new CoreOption(TechnologyTypes.IPHONE_HYBRID, true);
		options.add(opt);
		return options;
	}

	private List<ArtifactInfo> createModules(String id, String[] versions, boolean required, String techId) {
		List<RequiredOption> options = null;
		if(techId.equals(TechnologyTypes.PHP)) {
			options = createRequiredForPhp(required);
		} else {
			options = createRequiredForJquery(required);
		}
		
		List<ArtifactInfo> infos = new ArrayList<ArtifactInfo>();
		for (String version : versions) {
			ArtifactInfo module = new ArtifactInfo();
			module.setVersion(version);
			module.setId(id);
			module.setAppliesTo(options);
			infos.add(module);
		}
		return infos;
	}

	private List<RequiredOption> createRequiredForJquery(boolean required) {
		List<RequiredOption> options = new ArrayList<RequiredOption>();
		RequiredOption opt = new RequiredOption(TechnologyTypes.HTML5_JQUERY_MOBILE_WIDGET, required);
		options.add(opt);
		opt = new RequiredOption(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, required);
		options.add(opt);
		opt = new RequiredOption(TechnologyTypes.HTML5_MOBILE_WIDGET, required);
		options.add(opt);
		opt = new RequiredOption(TechnologyTypes.HTML5_WIDGET, required);
		options.add(opt);
		return options;
	}

	private List<RequiredOption> createRequiredForPhp(boolean required) {
		List<RequiredOption> options = new ArrayList<RequiredOption>();
		RequiredOption opt = new RequiredOption(TechnologyTypes.PHP, required);
		options.add(opt);
		opt = new RequiredOption(TechnologyTypes.ANDROID_HYBRID, required);
		options.add(opt);
		opt = new RequiredOption(TechnologyTypes.IPHONE_NATIVE, required);
		options.add(opt);
		opt = new RequiredOption(TechnologyTypes.IPHONE_HYBRID, required);
		options.add(opt);
		return options;
	}

	private Boolean convertBoolean(String value) {
	    if ("Yes".equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    private void publishJSLibrary(String groupId, String artifactId, String version, String ext, String filePath)
			throws PhrescoException {
    	if(deployArtifact == false) {
    		return;
    	}
    	
		File artifact = new File(binariesDir, filePath);
		System.out.println("URL = " + artifact.getPath());
		if (!artifact.exists()) {
			System.out.println("artifact.toString() " + artifact.toString());
			return;
		}

		com.photon.phresco.service.model.ArtifactInfo info = new com.photon.phresco.service.model.ArtifactInfo(groupId, artifactId, "", ext, version);
		manager.addArtifact(info, artifact, "photon");
		System.out.println(artifact.getName() + "Uploaded Successfully..................");
	}

	public static void main(String[] args) throws PhrescoException, IOException {
		File inputFile = new File("D:\\work\\phresco\\MasterNew\\servicenew\\phresco-service-runner\\delivery\\tools\\files");
		File outFile = new File("D:\\");
		File binariesFileDir = new File("D:\\work\\phresco\\Phresco-binaries\\technologies\\js-libraries");
		JsLibraryGenerator gen = new JsLibraryGenerator(inputFile, outFile, binariesFileDir);
		gen.publish();
	}

}