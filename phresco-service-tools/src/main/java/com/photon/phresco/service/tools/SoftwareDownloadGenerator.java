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
import com.photon.phresco.commons.model.ArtifactInfo;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.commons.model.PlatformType;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbService;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.service.api.RepositoryManager;

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
    private Map<String, PlatformType> platformMap = new HashMap<String, PlatformType>();
    private Map<String, String> filePathMap = new HashMap<String, String>();
    
    private void initMap() {
    	platformMap.put("Windows86", PlatformType.WIN32);
    	platformMap.put("Windows786",PlatformType.WIN32);
    	platformMap.put("Windows64", PlatformType.WIN64);
    	platformMap.put("Windows764", PlatformType.WIN64);
    	platformMap.put("Linux64", PlatformType.LINUX64);
    	platformMap.put("Linux86", PlatformType.LINUX32);
    	platformMap.put("server86", PlatformType.WINSERVER32);
    	platformMap.put("server64", PlatformType.WINSERVER64);
    }
    
	public SoftwareDownloadGenerator(File inputDir, File outDir ,File softwareDirectory) throws PhrescoException {
	    super();
		this.outputfile = new File(outDir, DOWNLOADS_JSON_FILE);
		this.softDir = softwareDirectory;
		this.workbook = getWorkBook(new File(inputDir,DOWNLOADS_EXCEL_FILE));
		initMap();
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

		Iterator<Row> rowIterator = downLoadInfoSheet.iterator();
		List<DownloadInfo> infos = new ArrayList<DownloadInfo>();
		DownloadInfo info = null;
		for (int i = 0; i < noofRows; i++) {
			rowIterator.next();
		}
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			info = createDownloadInfo(row);
			mongoOperation.save(DOWNLOAD_COLLECTION_NAME, info);
			infos.add(info);
			uploadSoftwareFileToRepository(info);
		}
		System.out.println(new Gson().toJson(infos));
	}

//	// To Deploy Files Set Java VM Argument As -Xmx1300m -Xms300m
	private void uploadSoftwareFileToRepository(DownloadInfo info) throws PhrescoException {
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
		
		if(next.getCell(2) != null) {
    		Cell cell = next.getCell(2);
    		String technology = getValue(cell);
    		String[] split = StringUtils.split(technology, ",");
    		info.setAppliesToTechIds(Arrays.asList(split));
    	}
		
		if(next.getCell(3) != null) {
    		Cell cell = next.getCell(3);
    		String category = getValue(cell);
    		info.setCategory(category);
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
    		List<PlatformType> types = createPlatform(platform);
    		info.setPlatform(types);
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
		if(next.getCell(9) != null) {
    		Cell cell = next.getCell(9);
    		packaging = getValue(cell);
    		info.setPackaging(packaging);
    		groupId = "downloads.files";
    		artifactId  = name.toLowerCase().replace(" ", "");
    	}
		
		List<ArtifactInfo> createArticatInfo = createArticatInfo(versions, info.getId(), size);
		
		info.setVersions(createArticatInfo);
		info.setGroupId(groupId);
		info.setArtifactId(artifactId);
		info.setPackaging(packaging);
		info.setDescription(description);
		info.setSystem(true);
		List<String> customerIds = new ArrayList<String>();
		customerIds.add("photon");
		info.setCustomerIds(customerIds);
		
		return info;

	}

	private List<ArtifactInfo> createArticatInfo(String[] split, String id, long size) {
		List<ArtifactInfo> versions = new ArrayList<ArtifactInfo>();
		for (String version : split) {
			ArtifactInfo info = new ArtifactInfo();
			info.setArtifactGroupId(id);
			info.setVersion(version);
			info.setFileSize(size);
			versions.add(info);
		}
		return versions;
	}

	private List<PlatformType> createPlatform(String[] platform) {
		List<PlatformType> types = new ArrayList<PlatformType>();
		for (String type : platform) {
			PlatformType platformType = platformMap.get(type);
			if(platformType != null) {
				types.add(platformType);
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
