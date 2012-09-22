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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.codehaus.plexus.util.StringUtils;

import com.google.gson.Gson;
import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.commons.model.DownloadInfos;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.api.DbService;
import com.photon.phresco.service.api.RepositoryManager;
import com.photon.phresco.service.client.api.ServiceClientConstant;
import com.photon.phresco.service.client.api.ServiceContext;
import com.photon.phresco.service.client.api.ServiceManager;
import com.photon.phresco.service.client.factory.ServiceClientFactory;
import com.photon.phresco.service.client.impl.RestClient;
import com.photon.phresco.service.client.util.RestUtil;
import com.photon.phresco.service.model.ArtifactInfo;
import com.photon.phresco.util.ServiceConstants;
import com.sun.jersey.api.client.ClientResponse;

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
	private ServiceContext context = null;
    private ServiceManager serviceManager = null;

	public SoftwareDownloadGenerator(File inputDir, File outDir ,File softwareDirectory) throws PhrescoException {
	    super();
		this.outputfile = new File(outDir, DOWNLOADS_JSON_FILE);
		this.softDir = new File(softwareDirectory, "/technologies/" );
		this.workbook = getWorkBook(new File(inputDir,DOWNLOADS_EXCEL_FILE));
		context = new ServiceContext();
        context.put(ServiceClientConstant.SERVICE_URL, RestUtil.getServerPath());
        context.put(ServiceClientConstant.SERVICE_USERNAME, "demouser");
        context.put(ServiceClientConstant.SERVICE_PASSWORD, "phresco");
        serviceManager = ServiceClientFactory.getServiceManager(context);
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
			infos.add(info);
//			uploadSoftwareFileToRepository(info);
		}
//		writeTo(infos);
		RestClient<DownloadInfo> newApp = serviceManager.getRestClient(ServiceConstants.REST_API_ADMIN + ServiceConstants.REST_API_DOWNLOADS);
//        ClientResponse clientResponse = newApp.create(infos);
		mongoOperation.insertList(ServiceConstants.DATABASES_COLLECTION_NAME, infos);
//		uploadFileToRepository();
	}

	// To Deploy Files Set Java VM Argument As -Xmx1300m -Xms300m
	private void uploadSoftwareFileToRepository(DownloadInfo info) throws PhrescoException {
	    if(deployArtifacts == false) {
            return;
        }
		String fileName = info.getName();
		String filePath = info.getFileName();
		String fileExt = getFileExt(filePath);
		List<String> version = info.getVersion();
		
		File softwareFile = new File(softDir, info.getFileName());
		if (softwareFile.exists()) {
			ArtifactInfo attifactInfo = new ArtifactInfo("softwares." + ".files", fileName, "", fileExt , version.get(0));
			System.out.println("Software path = " + softwareFile.getPath());
			manager.addArtifact(attifactInfo, softwareFile, "photon");
			System.gc();
			System.gc();
			System.out.println(fileName + " Uploaded Successfully..... ");
		} else {
			System.out.println("File Not Exists");
		}

	}

	private void uploadFileToRepository() throws PhrescoException {
	        if(deployArtifacts == false) {
	            return;
	        }
			ArtifactInfo info = new ArtifactInfo("softwares", "info", "", "json", "1.0");
			manager.addArtifact(info, outputfile, "photon");
	}

	private void writeTo(List<DownloadInfo> infos) throws PhrescoException {
		try {
			DownloadInfos downloadInfos = new DownloadInfos();
			downloadInfos.setInfos(infos);
			Gson gson = new Gson();
			String downloadFiles = gson.toJson(infos);
			FileWriter writer = new FileWriter(outputfile);
			writer.write(downloadFiles);
			writer.close();
		} catch (Exception e) {
			throw new PhrescoException(e);
		}
	}

	private DownloadInfo createDownloadInfo(Row row) throws PhrescoException {
		DownloadInfo info = new DownloadInfo();
		Cell namecell = row.getCell(1);
		String name = getValue(namecell);

		Cell ver = row.getCell(4);
		String version = getValue(ver);
		String[] versions = StringUtils.split(version, ",");

//		Cell url = row.getCell(9);
//		if (url == null || Cell.CELL_TYPE_BLANK == url.getCellType()) {
//			return null;
//		}

		Cell apptocell = row.getCell(2);
		String appliesTo = getValue(apptocell);
		String[] applyTo = StringUtils.split(appliesTo, ",");

//		Cell fileNameCell = row.getCell(10);
//		String fileName = getValue(fileNameCell);

		Cell fileSizeCell = row.getCell(11);
		String fileSize = "";
		if (fileSizeCell != null && Cell.CELL_TYPE_BLANK != fileSizeCell.getCellType()) {
		    fileSize = getValue(fileSizeCell);
		}
		
		Cell platformCell = row.getCell(5);
		if (platformCell .equals(null) || Cell.CELL_TYPE_BLANK == platformCell.getCellType()) {
			return null;
		}

		Cell categoryCell = row.getCell(3);
		String category = getValue(categoryCell);

//		Cell extension = row.getCell(9);
//		String fileExt = getValue(extension);
//		String fileExtension = getFileExt(fileExt);
		
//		String repositoryURL = PhrescoServerFactory.getRepositoryManager().getRepositoryURL();
//		String repoDownloadUrl = repositoryURL+ SEPERATOR + DOWNLOAD + SEPERATOR + FILES + SEPERATOR + name + SEPERATOR + version + SEPERATOR + name + "-" + version + "." + fileExtension;

		String platforms = getValue(platformCell);
		String[] platform = StringUtils.split(platforms, ",");

		info.setName(name);
		info.setCustomerId("photon");
		info.setDownloadURL(" ");
		info.setFileSize(fileSize);
		info.setVersion(Arrays.asList(versions));
		info.setType(category);
		info.setAppliesToTechs(Arrays.asList(applyTo));
		info.setPlatform(Arrays.asList(platform));
		info.setSystem(true);
		return info;

	}
	
	private String getFileExt(String fileName) {
		
		String fileExtension = "zip";
		if (fileName.endsWith(".tar.gz")) {
			fileExtension = "tar.gz";
		} else if (fileName.endsWith(".tar")) {
			fileExtension = "tar";
		} else if (fileName.endsWith(".zip")) {
			fileExtension = "zip";
		} else if (fileName.endsWith(".exe")) {
			fileExtension = "exe";
		} else if (fileName.endsWith(".pkg")) {
			fileExtension = "pkg";
		}
		
		return fileExtension;
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
		File outFile = new File("D:\\work\\phresco\\MasterNew\\servicenew\\phresco-service-runner\\delivery\\tools\\files");
		File softDir = new File("D:\\work\\phresco\\Phresco-binaries\\technologies\\");
		SoftwareDownloadGenerator generate = new SoftwareDownloadGenerator(inputFile, outFile , softDir);
		generate.publish();
	}
}
