package com.photon.phresco.service.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.photon.phresco.service.impl.DbService;
import com.photon.phresco.util.TechnologyTypes;
import com.phresco.pom.site.Reports;

public class PhrescoReportGenerator extends DbService {
	
	private static Map<String, List<Reports>> siteReport = new HashMap<String,List<Reports>>();
	
	public PhrescoReportGenerator() {
		super();
	}
	
	private void initMap() {
		siteReport.put(TechnologyTypes.JAVA, createAllReports());
		siteReport.put(TechnologyTypes.JAVA_STANDALONE, createAllReports());
		siteReport.put(TechnologyTypes.JAVA_WEBSERVICE, createAllReports());
		siteReport.put(TechnologyTypes.ANDROID_HYBRID, createAllReports());
		siteReport.put(TechnologyTypes.ANDROID_NATIVE, createAllReports());
		siteReport.put(TechnologyTypes.HTML5, createAllReports());
		siteReport.put(TechnologyTypes.HTML5_MOBILE_WIDGET, createAllReports());
		siteReport.put(TechnologyTypes.HTML5_MULTICHANNEL_JQUERY_WIDGET, createAllReports());
		siteReport.put(TechnologyTypes.HTML5_WIDGET, createAllReports());
		
		siteReport.put(TechnologyTypes.PHP, createInfoandSurefireReports());
		siteReport.put(TechnologyTypes.PHP_DRUPAL7, createInfoandSurefireReports());
		siteReport.put(TechnologyTypes.PHP_DRUPAL6, createInfoandSurefireReports());
		siteReport.put(TechnologyTypes.NODE_JS_WEBSERVICE, createInfoandSurefireReports());
		siteReport.put(TechnologyTypes.SHAREPOINT, createInfoandSurefireReports());
		siteReport.put(TechnologyTypes.WORDPRESS, createInfoandSurefireReports());
		
		siteReport.put(TechnologyTypes.IPHONE_NATIVE, createInfoReports());
		siteReport.put(TechnologyTypes.IPHONE_HYBRID, createInfoReports());
	}

	private List<Reports> createAllReports() {
		List<Reports> allReports = new ArrayList<Reports>();
		allReports.add(Reports.PROJECT_INFO);
		allReports.add(Reports.JAVADOC);
		allReports.add(Reports.COBERTURA);
		allReports.add(Reports.JDEPEND);
		allReports.add(Reports.JXR);
		allReports.add(Reports.PMD);
		allReports.add(Reports.SUREFIRE_REPORT);
		return allReports;
	}
	
	private List<Reports> createInfoandSurefireReports() {
		List<Reports> infoandSurefireReports = new ArrayList<Reports>();
		infoandSurefireReports.add(Reports.PROJECT_INFO);
		infoandSurefireReports.add(Reports.SUREFIRE_REPORT);
		return infoandSurefireReports;
	}
	
	private List<Reports> createInfoReports() {
		List<Reports> infoReports = new ArrayList<Reports>();
		infoReports.add(Reports.PROJECT_INFO);
		return infoReports;
	}
	
	private void publish() {
		initMap();
		Set<String> keySet = siteReport.keySet();
		for (String techId : keySet) {
			List<Reports> reports = siteReport.get(techId);
			for (Reports report : reports) {
				report.setTechId(techId);
				mongoOperation.save("reports", report);
			}
		}
	}
	
	public static void main(String[] args) {
		PhrescoReportGenerator generator = new PhrescoReportGenerator();
		generator.publish();
	}
}
