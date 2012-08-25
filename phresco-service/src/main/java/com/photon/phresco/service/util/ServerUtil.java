package com.photon.phresco.service.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Base64;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.ArchetypeInfo;
import com.photon.phresco.model.I18NString;
import com.photon.phresco.model.L10NString;
import com.photon.phresco.util.Constants;
import com.photon.phresco.util.FileUtil;
import com.photon.phresco.util.Utility;
import com.phresco.pom.util.PomProcessor;

public class ServerUtil {
    
    private static JarInputStream jarInputStream = null;
    
    /**
     * Get the artifact information from jar inputstream
     * @param inputJarStream
     * @param fileName
     * @return
     * @throws PhrescoException
     */
    public static ArchetypeInfo getArtifactinfo(InputStream inputJarStream) throws PhrescoException {
        File jarFile = writeFileFromStream(inputJarStream, null);
        ArchetypeInfo artifactInfo = getArtifactInfoFromJar(jarFile);
        FileUtil.delete(jarFile);
        return artifactInfo;
    }
    
    /**
     * Find archetype info from the given jar file
     * @return
     * @throws IOException 
     * @throws JAXBException 
     */
    public static ArchetypeInfo getArtifactInfoFromJar(File jarFile) throws PhrescoException {
        ArchetypeInfo info = null;
        String pomFile = null;
        try {
            JarFile jarfile = new JarFile(jarFile);
            for(Enumeration<JarEntry> em = jarfile.entries(); em.hasMoreElements();) {
                JarEntry jarEntry = em.nextElement();
                if (jarEntry.getName().endsWith("pom.xml")) {
                    pomFile = jarEntry.getName();
                }
            }
            if(pomFile != null) {
                ZipEntry entry = jarfile.getEntry(pomFile);
                InputStream inputStream = jarfile.getInputStream(entry);
                PomProcessor processor = new PomProcessor(inputStream);
                info = new ArchetypeInfo();
                info.setGroupId(processor.getGroupId());
                info.setArtifactId(processor.getArtifactId());
                info.setVersion(processor.getVersion());
            }
        }catch (Exception e) {
            throw new PhrescoException(e);
        }
        return info;
    }
    
    /**
     * Validate the given jar is valid maven  jar
     * @return
     * @throws PhrescoException 
     */
    public static boolean validateMavenJar(InputStream inputJar) throws PhrescoException {
    	boolean returnValue = false;
    	try {
            jarInputStream = new JarInputStream(inputJar);
            JarEntry nextJarEntry = jarInputStream.getNextJarEntry();
            while ((nextJarEntry = jarInputStream.getNextJarEntry()) != null) {
                if (nextJarEntry.getName().contains("pom.xml")) {
                	returnValue = true;
                	break;
                }
            }
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
        
        return returnValue;
    }
    
    /**
     * Validate the given jar is valid maven archetype jar
     * @return
     * @throws PhrescoException 
     */
    public static boolean validateArchetypeJar(InputStream inputJar) throws PhrescoException {
        try {
            jarInputStream = new JarInputStream(inputJar);
            JarEntry nextJarEntry = jarInputStream.getNextJarEntry();
            while ((nextJarEntry = jarInputStream.getNextJarEntry()) != null) {
                if(nextJarEntry.getName().equals(ServerConstants.ARCHETYPE_METADATA_FILE) || 
                        nextJarEntry.getName().equals(ServerConstants.ARCHETYPE_FILE)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
        return false;
    }
    
    /**
     * Validate the given jar is valid maven plugin jar
     * @return
     * @throws PhrescoException 
     */
    public static boolean validatePluginJar(InputStream inputJar) throws PhrescoException {
        try {
            jarInputStream = new JarInputStream(inputJar);
            JarEntry nextJarEntry = jarInputStream.getNextJarEntry();
            while ((nextJarEntry = jarInputStream.getNextJarEntry()) != null) {
                if(nextJarEntry.getName().equals(ServerConstants.PLUGIN_COMPONENTS_XML_FILE) || 
                        nextJarEntry.getName().equals(ServerConstants.PLUGIN_XML_FILE)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
        return false;
    }
    
    /**
     * To Get Temp FolderPath
     * @return
     */
    public static String getTempFolderPath() {
        
        String tempFolderPath = "";
        String systemTempFolder = System.getProperty(Constants.JAVA_TMP_DIR);
        String uuid = UUID.randomUUID().toString();

        // handled the file separator since java.io.tmpdir does not return
        // the last file separator in linux and Mac OS
        if ((systemTempFolder.endsWith(File.separator))) {
            tempFolderPath = systemTempFolder + Constants.PHRESCO_FOLDER_NAME;
        } else {
            tempFolderPath = systemTempFolder + File.separator + Constants.PHRESCO_FOLDER_NAME;
        }

        tempFolderPath = tempFolderPath + File.separator + uuid;
        File tempFolder = new File(tempFolderPath);
        tempFolder.mkdirs();
        
        return tempFolderPath;
    }
    
    /**
     * To Encrypt The Given String
     * @param inputString
     * @return
     */
    public static String encryptString(String inputString) {
        byte[] encodeBase64 = Base64.encodeBase64(inputString.getBytes());
        String encodedString = new String(encodeBase64);
        
        return encodedString;
    }
    
    /**
     * To Decrypt The Given String
     * @param inputString
     * @return
     */
    public static String decryptString(String inputString) {
        byte[] decodedBytes = Base64.decodeBase64(inputString);
        String decodedString = new String(decodedBytes);
        
        return decodedString;
    }
    
    public static File writeFileFromStream(InputStream inputStream, String path) throws PhrescoException {
        File artifactFile = null;
        FileOutputStream fileOutStream = null;
        if (path == null) {
            artifactFile = new File(ServerUtil.getTempFolderPath() + "/" + "temp" + ".jar");
        } else {
            artifactFile = new File(path);
        }
        try {
            fileOutStream = new FileOutputStream(artifactFile);
            byte buf[] = new byte[1024];
            int len;
            while((len = inputStream.read(buf))>0) {
                fileOutStream.write(buf,0,len);
            }
      } catch (IOException e) {
          throw new PhrescoException();
      } finally {
          Utility.closeStream(inputStream);
          Utility.closeStream(fileOutStream);
      }
        return artifactFile;
        
    }
    
	/**
     * To create the i18n String
     * @param inputString
     * @return I18NString
     */
    public static I18NString createI18NString(String str) {
    	I18NString displayName;
    	L10NString value;
    	displayName = new I18NString();
    	value = new L10NString("en-US", str);
    	displayName.add(value);
    	
    	return displayName;
	}
}