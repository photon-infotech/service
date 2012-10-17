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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.google.gson.Gson;
import com.photon.phresco.commons.model.ArtifactGroup;
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.commons.model.DownloadInfo.Category;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.Converter;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.converters.ConvertersFactory;
import com.photon.phresco.service.dao.ArtifactGroupDAO;
import com.photon.phresco.service.dao.DownloadsDAO;
import com.photon.phresco.service.impl.DbService;

public class SoftwareDownloadGenerator extends DbService {

	private static final String DOWNLOADS_JSON_FILE = "downloads.json";
	private static final String DOWNLOADS_EXCEL_FILE = "PHTN_PHRESCO_OpenSourceUsage.xls";

	private boolean deployArtifacts = false;
	private static final int noofRows = 1;
	private File outputfile = null;
	private File softDir = null;
	private HSSFWorkbook workbook = null;
	private String DOWNLOADS = "Downloads";

	private static final String DOWNLOAD = "downloads";
	private static final String SEPERATOR = "/";
	private static final String FILES = "files";
	RepositoryManager manager = null;
    private Map<String, String> platformMap = new HashMap<String, String>();
    private Map<String, String> filePathMap = new HashMap<String, String>();
    private Map<String, Category> categoryMap = new HashMap<String, Category>();
    
    private void initMap() {
    	platformMap.put("Windows86", "Windows x86");
    	platformMap.put("Windows786","Windows7 x86");
    	platformMap.put("Windows64", "Windows x64");
    	platformMap.put("Windows764", "Windows7 x64");
    	platformMap.put("Linux64", "Linux x64");
    	platformMap.put("Linux86", "Linux x86");
    	platformMap.put("server86", "Server x86");
    	platformMap.put("server64", "Server x64");
    	platformMap.put("Mac86", "Mac x86");
    	platformMap.put("Mac64", "Mac x64");
    }
    
    private void initCategoryMap() {
    	categoryMap.put("Server", Category.SERVER);
    	categoryMap.put("Database", Category.DATABASE);
    	categoryMap.put("Editor", Category.EDITOR);
    	categoryMap.put("Tools", Category.TOOLS);
    	categoryMap.put("Others", Category.OTHERS);
    }
    
	public SoftwareDownloadGenerator(File inputDir, File outDir ,File softwareDirectory) throws PhrescoException {
	    super();
		this.outputfile = new File(outDir, DOWNLOADS_JSON_FILE);
		this.softDir = softwareDirectory;
		this.workbook = getWorkBook(new File(inputDir,DOWNLOADS_EXCEL_FILE));
		initMap();
		initCategoryMap();
		PhrescoServerFactory.initialize();
		manager = PhrescoServerFactory.getRepositoryManager();
	}

	private HSSFWorkbook getWorkBook(File inputFile) throws PhrescoException {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(inputFile);
			return new HSSFWorkbook(fs);
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

	public void publish() throws PhrescoException {
		HSSFSheet downLoadInfoSheet = workbook.getSheet(DOWNLOADS);
		List<DownloadInfo> infos = new ArrayList<DownloadInfo>();
		Iterator<Row> rowIterator = downLoadInfoSheet.iterator();
		DownloadInfo info = null;
		for (int i = 0; i < noofRows; i++) {
			rowIterator.next();
		}
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			info = createDownloadInfo(row);
//			saveDownloads(info);
//			uploadSoftwareFileToRepository(info.getArtifactGroup());
			infos.add(info);
		}
		System.out.println(new Gson().toJson(infos));
	}

	private void saveDownloads(DownloadInfo info) throws PhrescoException {
		Converter<DownloadsDAO, DownloadInfo> downlodConverter = 
			(Converter<DownloadsDAO, DownloadInfo>) ConvertersFactory.getConverter(DownloadsDAO.class);
		DownloadsDAO downloadDAO = downlodConverter.convertObjectToDAO(info);
		ArtifactGroup artifactGroup = info.getArtifactGroup();
//		saveArtifactGroup(artifactGroup);
//		mongoOperation.save(DOWNLOAD_COLLECTION_NAME, downloadDAO);
	}
	
	private void saveArtifactGroup(ArtifactGroup artifactGroup) throws PhrescoException {
        List<ArtifactInfo> versions = artifactGroup.getVersions();
        List<String> versionIds = new ArrayList<String>();
        Converter<ArtifactGroupDAO, ArtifactGroup> converter = 
            (Converter<ArtifactGroupDAO, ArtifactGroup>) ConvertersFactory.getConverter(ArtifactGroupDAO.class);
        ArtifactGroupDAO moduleGroupDAO = converter.convertObjectToDAO(artifactGroup);
        String id2 = moduleGroupDAO.getId();
        for (ArtifactInfo module : versions) {
        	versionIds.add(module.getId());
            module.setArtifactGroupId(id2);
            mongoOperation.save(ARTIFACT_INFO_COLLECTION_NAME, module);
        }
        moduleGroupDAO.setVersionIds(versionIds);
      mongoOperation.save(ARTIFACT_GROUP_COLLECTION_NAME, moduleGroupDAO);
    }
	
	//	// To Deploy Files Set Java VM Argument As -Xmx1300m -Xms300m
	private void uploadSoftwareFileToRepository(ArtifactGroup info) throws PhrescoException {
	    if(deployArtifacts == false) {
            return;
        }
	    
		String string = filePathMap.get(info.getName());
		if(StringUtils.isNotEmpty(string)) {
			File softwareFile = new File(softDir, string);
			com.photon.phresco.service.model.ArtifactInfo attifactInfo = new com.photon.phresco.service.model.ArtifactInfo(info.getGroupId(), 
					info.getArtifactId(), "", info.getPackaging() , info.getVersions().get(0).getVersion());
			manager.addArtifact(attifactInfo, softwareFile, "photon");
			System.gc();
			System.gc();
			System.out.println(softwareFile.getPath() + " Uploaded Successfully..... ");
		}
		
	}

	private DownloadInfo createDownloadInfo(Row next) throws PhrescoException {
		DownloadInfo info = new DownloadInfo();
		
		String name = "";
		if(next.getCell(1) != null) {
    		Cell cell = next.getCell(1);
    		name = getValue(cell);
    		info.setName(name);
    	}
		
		String id = "downloads_" + name.toLowerCase().replace(" ", "-");
		
		if(next.getCell(2) != null) {
    		Cell cell = next.getCell(2);
    		String technology = getValue(cell);
    		String[] split = StringUtils.split(technology, ",");
    		info.setAppliesToTechIds(Arrays.asList(split));
    	}
		
		if(next.getCell(3) != null) {
    		Cell cell = next.getCell(3);
    		String category = getValue(cell);
    		info.setCategory(categoryMap.get(category));
    	}
		
		String[] versions = null;
		if(next.getCell(4) != null) {
    		Cell cell = next.getCell(4);
    		String version = getValue(cell);
    		versions = StringUtils.split(version, ",");
    	}

		if(next.getCell(5) != null) {
    		Cell platformCell = next.getCell(5);
    		String platforms = getValue(platformCell);
    		String[] platform = StringUtils.split(platforms, ",");
    		List<String> types = createPlatform(platform);
    		info.setPlatformTypeIds(types);
    	}
		
		String description = "";
		if(next.getCell(6) != null) {
    		Cell cell = next.getCell(6);
    		description = getValue(cell);
    	}
		
		String filePath = "";
		if(next.getCell(10) != null) {
    		Cell cell = next.getCell(10);
    		filePath = getValue(cell);
    		filePathMap.put(name, filePath);
    	}
		
		long size = 0;
//		if(next.getCell(11) != null) {
//    		Cell cell = next.getCell(11);
//    		String filesize = getValue(cell);
//    		size = Long.parseLong(filesize);
//    	}
		
		String groupId = "";
		String artifactId  = "";
		String packaging = "zip";
		
		ArtifactGroup artifactGroup = new ArtifactGroup();
		if(next.getCell(9) != null) {
    		Cell cell = next.getCell(9);
    		packaging = getValue(cell);
    		artifactGroup.setPackaging(packaging);
    		groupId = "downloads.files";
    		artifactId  = name.toLowerCase().replace(" ", "");
    	}
		
		List<ArtifactInfo> createArticatInfo = createArticatInfo(versions, info.getId(), size);
		List<String> customerIds = new ArrayList<String>();
		customerIds.add("photon");
		artifactGroup.setId(id);
		artifactGroup.setVersions(createArticatInfo);
		artifactGroup.setGroupId(groupId);
		artifactGroup.setArtifactId(artifactId);
		artifactGroup.setPackaging(packaging);
		artifactGroup.setDescription(description);
		artifactGroup.setCustomerIds(customerIds);
		info.setId(id);
		info.setSystem(true);
		info.setCustomerIds(customerIds);
		info.setArtifactGroup(artifactGroup);
		
		return info;

	}

	private List<ArtifactInfo> createArticatInfo(String[] split, String id, long size) {
		List<ArtifactInfo> versions = new ArrayList<ArtifactInfo>();
		System.out.println(split.length);
		for (String version : split) {
			System.out.println("Entereidn with ID" + id);
			ArtifactInfo info = new ArtifactInfo();
			info.setArtifactGroupId(id);
			info.setVersion(version);
			info.setFileSize(size);
			versions.add(info);
		}
		return versions;
	}

	private List<String> createPlatform(String[] platform) {
		List<String> types = new ArrayList<String>();
		for (String type : platform) {
			String platformId = platformMap.get(type);
			if(platformId != null) {
				types.add(platformId);
			}
		}
		return types;
	}

	private static String getValue(Cell cell) {
		if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
			return cell.getStringCellValue();
		}

		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			return String.valueOf(cell.getNumericCellValue());
		}

		return null;
	}

	public static void main(String[] args) throws PhrescoException {
		File inputFile = new File("D:\\work\\phresco\\MasterNew\\servicenew\\phresco-service-runner\\delivery\\tools\\files");
		File outFile = new File("D:\\work\\service\\phresco-service-runner\\delivery\\tools\\files");
		File softDir = new File("D:\\work\\phresco\\Phresco-binaries\\technologies\\");
		SoftwareDownloadGenerator generate = new SoftwareDownloadGenerator(inputFile, outFile , softDir);
		generate.publish();
	}
}
