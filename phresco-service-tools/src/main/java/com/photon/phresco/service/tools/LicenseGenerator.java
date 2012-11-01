package com.photon.phresco.service.tools;

import java.util.ArrayList;
import java.util.List;

import com.photon.phresco.commons.model.License;
import com.photon.phresco.service.impl.DbService;

public class LicenseGenerator extends DbService {

	public LicenseGenerator() {
		super();
	}

	private void publish() {
		List<License> licenses = createLicenses();
		mongoOperation.insertList("licenses", licenses);
	}

	private List<License> createLicenses() {
		List<License> licenses = new ArrayList<License>();
		licenses.add(createLicense("Academic Free License 3.0 (AFL-3.0)"));
		licenses.add(createLicense("GNU Affero General Public License 3.0 (AGPL-3.0)"));
		licenses.add(createLicense("Adaptive Public License (APL-1.0)"));
		licenses.add(createLicense("Apache License 2.0 (Apache-2.0)"));
		licenses.add(createLicense("Apple Public Source License (APSL-2.0)"));
		licenses.add(createLicense("Artistic license 2.0 (Artistic-2.0)"));
		licenses.add(createLicense("Attribution Assurance Licenses (AAL)"));
		licenses.add(createLicense("BSD 3-Clause License (BSD-3-Clause)"));
		licenses.add(createLicense("BSD 2-Clause License (BSD-2-Clause)"));
		licenses.add(createLicense("Boost Software License (BSL-1.0)"));
		licenses.add(createLicense("Computer Associates Trusted Open Source License 1.1 (CATOSL-1.1)"));
		licenses.add(createLicense("Common Development and Distribution License 1.0 (CDDL-1.0)"));
		licenses.add(createLicense("Common Public Attribution License 1.0 (CPAL-1.0)"));
		licenses.add(createLicense("CUA Office Public License Version 1.0 (CUA-OPL-1.0)"));
		licenses.add(createLicense("EU DataGrid Software License (EUDatagrid)"));
		licenses.add(createLicense("Eclipse Public License 1.0 (EPL-1.0)"));
		licenses.add(createLicense("Educational Community License, Version 2.0 (ECL-2.0)"));
		licenses.add(createLicense("Eiffel Forum License V2.0 (EFL-2.0)"));
		licenses.add(createLicense("Entessa Public License (Entessa)"));
		licenses.add(createLicense("European Union Public License, Version 1.1 (EUPL-1.1)"));
		licenses.add(createLicense("Fair License (FAIR)"));
		licenses.add(createLicense("Frameworx License (Frameworx-1.0)"));
		licenses.add(createLicense("GNU Affero General Public License v3 (AGPL-3.0)"));
		licenses.add(createLicense("GNU General Public License version 2.0 (GPL-2.0)"));
		licenses.add(createLicense("GNU General Public License version 3.0 (GPL-3.0)"));
		licenses.add(createLicense("GNU Library or  General Public License version 2.1 (LGPL-2.1)"));
		licenses.add(createLicense("GNU Library or General Public License version 3.0 (LGPL-3.0)"));
		licenses.add(createLicense("Historical Permission Notice and Disclaimer (HPND)"));
		licenses.add(createLicense("IBM Public License 1.0 (IPL-1.0)"));
		licenses.add(createLicense("IPA Font License (IPA)"));
		licenses.add(createLicense("ISC License (ISC)"));
		licenses.add(createLicense("LaTeX Project Public License 1.3c (LPPL-1.3c)"));
		licenses.add(createLicense("Lucent Public License Version 1.02 (LPL-1.02)"));
		licenses.add(createLicense("MirOS Licence (MirOS)"));
		licenses.add(createLicense("Microsoft Public License (Ms-PL)"));
		licenses.add(createLicense("Microsoft Reciprocal License (Ms-RL)"));
		licenses.add(createLicense("MIT license (MIT)"));
		licenses.add(createLicense("Motosoto License (Motosoto)"));
		licenses.add(createLicense("Mozilla Public License 2.0 (MPL-2.0)"));
		licenses.add(createLicense("Multics License (Multics)"));
		licenses.add(createLicense("NASA Open Source Agreement 1.3 (NASA 1.3)"));
		licenses.add(createLicense("NTP License (NTP)"));
		licenses.add(createLicense("Naumen Public License (Naumen)"));
		licenses.add(createLicense("Nethack General Public License (NGPL)"));
		licenses.add(createLicense("Nokia Open Source License (Nokia)"));
		licenses.add(createLicense("Non-Profit Open Software License 3.0 (NPOSL-3.0)"));
		licenses.add(createLicense("OCLC Research Public License 2.0 (OCLC-2.0)"));
		licenses.add(createLicense("Open Font License 1.1 (OFL 1.1)"));
		licenses.add(createLicense("Open Group Test Suite License (OGTSL)"));
		licenses.add(createLicense("Open Software License 3.0 (OSL-3.0)"));
		licenses.add(createLicense("PHP License 3.0 (PHP-3.0)"));
		licenses.add(createLicense("The PostgreSQL License (PostgreSQL)"));
		licenses.add(createLicense("Python License (Python-2.0) "));
		licenses.add(createLicense("CNRI Python license (CNRI-Python)"));
		licenses.add(createLicense("Q Public License (QPL-1.0)"));
		licenses.add(createLicense("RealNetworks Public Source License V1.0 (RPSL-1.0)"));
		licenses.add(createLicense("Reciprocal Public License 1.5 (RPL-1.5)"));
		licenses.add(createLicense("Ricoh Source Code Public License (RSCPL)"));
		licenses.add(createLicense("Simple Public License 2.0 (SimPL-2.0)"));
		licenses.add(createLicense("Sleepycat License (Sleepycat)"));
		licenses.add(createLicense("Sun Public License 1.0 (SPL-1.0)"));
		licenses.add(createLicense("Sybase Open Watcom Public License 1.0 (Watcom-1.0)"));
		licenses.add(createLicense("University of Illinois/NCSA Open Source License (NCSA)"));
		licenses.add(createLicense("Vovida Software License v. 1.0 (VSL-1.0)"));
		licenses.add(createLicense("W3C License (W3C)"));
		licenses.add(createLicense("wxWindows Library License (WXwindows)"));
		licenses.add(createLicense("X.Net License (Xnet)"));
		licenses.add(createLicense("Zope Public License 2.0 (ZPL-2.0)"));
		licenses.add(createLicense("zlib/libpng license (Zlib)"));
			
		return licenses;
	}

	private License createLicense(String name) {
		License license = new License();
		license.setName(name);
		license.setDescription(name);
		license.setSystem(true);
		return license;
	}

	public static void main(String[] args) {
		LicenseGenerator licenseGenerator = new LicenseGenerator();
		licenseGenerator.publish();
	}
}
