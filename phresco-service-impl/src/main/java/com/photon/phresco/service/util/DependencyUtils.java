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
package com.photon.phresco.service.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.logger.SplunkLogger;
import com.photon.phresco.service.api.PhrescoServerFactory;
import com.photon.phresco.util.ArchiveUtil;
import com.photon.phresco.util.ArchiveUtil.ArchiveType;
import com.photon.phresco.util.ServiceConstants;
import com.photon.phresco.util.Utility;

/**
 * @author arunachalam_l
 *
 */
public final class DependencyUtils {
	private static final SplunkLogger LOGGER = SplunkLogger.getSplunkLogger(DependencyUtils.class.getName());
	private static Boolean isDebugEnabled = LOGGER.isDebugEnabled();

	private DependencyUtils(){
		//restrict instantiation 
	}


	/**
	 * Finds out the {@link ArchiveType} for the given extension.
	 * @param extension
	 * @return
	 */
	public static ArchiveType getArchiveType(String extension) {
		if (isDebugEnabled) {
			LOGGER.debug("DependencyUtils.getArchiveType:Entry");
			LOGGER.info("DependencyUtils.getArchiveType","extension=\""+ extension +"\"");
		}
		ArchiveType archiveType = ArchiveType.ZIP;
		if(extension.equals(".tar.gz")) {
			archiveType = ArchiveType.TARGZ;
		} else if (extension.endsWith(".tar")){
			archiveType = ArchiveType.TAR;
		}
		if (isDebugEnabled) {
			LOGGER.debug("DependencyUtils.getArchiveType:Exit");
		}
		return archiveType;
	}



	/**
	 * Returns the file extension of the content.
	 * @param contentURL
	 * @return
	 */
	public static String getExtension(String contentURL) {
		if (isDebugEnabled) {
			LOGGER.debug("DependencyUtils.getExtension:Entry");
			LOGGER.info("DependencyUtils.getExtension","contentURL=\""+ contentURL +"\"");
		}
		String extension = ".zip";
		if(contentURL.endsWith("tar.gz")){
			extension = ".tar.gz";
		} else if(contentURL.endsWith("tar")){
			extension = ".tar";
		}
		if (isDebugEnabled) {
			LOGGER.debug("DependencyUtils.getExtension:Exit");
		}
		return extension;
	}
	
	/**
	 * Extracts the given compressed file (of type tar, targz, and zip) into given location.
	 * See also
	 * {@link ArchiveType} and {@link ArchiveUtil}
	 * @param contentURL
	 * @param path
	 * @throws PhrescoException
	 */
	public static void extractFiles(String contentURL, File path, String customerId) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("DependencyUtils.extractFiles:Entry");
			if(StringUtils.isEmpty(customerId)) {
				LOGGER.warn(ServiceConstants.DEPENDENCY_UTIL_EXTRACT_FILES,ServiceConstants.STATUS_BAD_REQUEST, "message=\"customerId is empty\"");
			}
			LOGGER.info(ServiceConstants.DEPENDENCY_UTIL_EXTRACT_FILES,"contentURL=\""+ contentURL +"\"", ServiceConstants.PATH_EQUALS_SLASH+ path.getPath()+"\"", ServiceConstants.CUSTOMER_ID_EQUALS_SLASH+ customerId +"\"");
		}
		extractFiles(contentURL, null, path, customerId);
		if(isDebugEnabled) {
			LOGGER.debug("DependencyUtils.extractFiles:Exit");
		}
	}
	
	/**
	 * Extracts the given compressed file (of type tar, targz, and zip) into given location.
	 * See also
	 * {@link ArchiveType} and {@link ArchiveUtil}
	 * @param contentURL
	 * @param path
	 * @throws PhrescoException
	 */
	public static void extractFiles(String contentURL, String folderName, File path, String customerId) throws PhrescoException {
		if (isDebugEnabled) {
			LOGGER.debug("DependencyUtils.extractFiles:Entry");
			LOGGER.info(ServiceConstants.DEPENDENCY_UTIL_EXTRACT_FILES,"contentURL=\""+ contentURL +"\"","folderName=\""+ folderName +"\""
					,ServiceConstants.PATH_EQUALS_SLASH+ path.getName() +"\"",ServiceConstants.CUSTOMER_ID_EQUALS_SLASH+ customerId +"\"");
		}
		assert !StringUtils.isEmpty(contentURL);
		
		PhrescoServerFactory.initialize();
		String extension = getExtension(contentURL);
		File archive = new File(Utility.getPhrescoTemp(), UUID.randomUUID().toString()+extension);
		FileOutputStream fos = null;
		OutputStream out = null;
		try {
			InputStream inputStream = PhrescoServerFactory.getRepositoryManager().getArtifactAsStream(contentURL, customerId);
			fos = new FileOutputStream(archive);
			out = new BufferedOutputStream(fos);
			
			IOUtils.copy(inputStream, out);
			
			out.flush();
			out.close();
			fos.close();
			ArchiveType archiveType = getArchiveType(extension);	
			if (isDebugEnabled) {
				LOGGER.debug("extractFiles() path="+path.getPath());
			}
			ArchiveUtil.extractArchive(archive.toString(), path.getAbsolutePath(), folderName, archiveType);
			archive.delete();
			if(isDebugEnabled) {
				LOGGER.debug("DependencyUtils.extractFiles:Exit");
			}
		} 
		catch (FileNotFoundException e) {
			LOGGER.error(ServiceConstants.DEPENDENCY_UTIL_EXTRACT_FILES, ServiceConstants.STATUS_FAILURE, "message=\"" + e.getLocalizedMessage() + "\"");
			return;
		}
		
		catch (IOException e) {
			LOGGER.error(ServiceConstants.DEPENDENCY_UTIL_EXTRACT_FILES, ServiceConstants.STATUS_FAILURE, "message=\"" + e.getLocalizedMessage() + "\"");
			throw new PhrescoException(e);
		} catch (PhrescoException pe ){
			LOGGER.error(ServiceConstants.DEPENDENCY_UTIL_EXTRACT_FILES, ServiceConstants.STATUS_FAILURE, "message=\"" + pe.getLocalizedMessage() + "\"");
			if(pe.getCause() instanceof FileNotFoundException) {
				return;
			}
			throw pe;
		}finally {
			Utility.closeStream(fos);
			Utility.closeStream(out);
		}
	}
	
	public static void copyFilesTo(File[] files, File destPath) throws IOException {
		if (isDebugEnabled) {
			LOGGER.debug("DependencyUtils.copyFilesTo:Entry");
			LOGGER.info("DependencyUtils.copyFilesTo","destPath=\""+ destPath +"\"");
		}
		if(files == null || destPath == null) {
			//nothing to copy
			return; 
		}
		for (File file : files) {
			if(file.isDirectory()) {
				FileUtils.copyDirectory(file, destPath);
			} else {
				FileUtils.copyFileToDirectory(file, destPath);
			}
		}
		if(isDebugEnabled) {
			LOGGER.debug("DependencyUtils.copyFilesTo:Exit");
		}

	}
}
