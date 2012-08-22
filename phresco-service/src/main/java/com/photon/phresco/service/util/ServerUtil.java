package com.photon.phresco.service.util;

import java.io.File;
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
import com.photon.phresco.util.Constants;
import com.phresco.pom.util.PomProcessor;

public class ServerUtil {
    
    private static JarInputStream jarInputStream = null;
    
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
                if (jarEntry.getName().contains("pom.xml")) {
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
    
}
