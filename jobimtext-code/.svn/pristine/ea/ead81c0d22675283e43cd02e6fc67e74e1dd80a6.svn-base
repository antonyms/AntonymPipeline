/*******************************************************************************
* Copyright 2012

* 
* FG Language Technologie
* Technische Universitaet Darmstadt
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package org.jobimtext.api.dca;

import java.io.File;
import java.util.Map;

import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.Resource_ImplBase;
import org.jobimtext.api.configuration.DatabaseTableConfiguration;
import org.jobimtext.api.configuration.DcaThesaurusConfiguration;

import com.ibm.sai.dca.client.DCA_Client;

public class DCAResource extends Resource_ImplBase {

	public static final String PARAM_XML_CONFIGURATION_FILE = "DbConfigurationFile";
	public static final String PARAM_SORT_RESULTS = "SortResults";

	//@ConfigurationParameter(name = PARAM_XML_CONFIGURATION_FILE, mandatory = false)
	private String xmlConfigurationFile;

	//@ConfigurationParameter(name = PARAM_SORT_RESULTS, mandatory = false)
	private Boolean sort = false;

	public boolean isSort() {
		return sort;
	}

	public void setSort(boolean sort) {
		this.sort = sort;
	}

	private DCA_Client client;
	private DcaThesaurusConfiguration dcaConf;

	public DatabaseTableConfiguration getDbTables() {
		return dcaConf.tables;
	}

	public void setDbTables(DatabaseTableConfiguration dbTables) {
		this.dcaConf.tables = dbTables;
	}

	public DCAResource() {

	}
	public DCAResource(String xmlConf) {
		this(xmlConf,  true);
	}

	public DCAResource(String xmlConf, boolean sort) {
		this.xmlConfigurationFile = xmlConf;

		this.sort = sort;
	}
	
	


	public DCA_Client getClient() {
		return client;
	}

	@Override
	public boolean initialize(ResourceSpecifier aSpecifier, Map<String, Object> aAdditionalParams) throws ResourceInitializationException {
		boolean returnCode = super.initialize(aSpecifier, aAdditionalParams);
		returnCode &= connect();
		return returnCode;

	}

	public boolean connect() {

		
		try {
			dcaConf = DcaThesaurusConfiguration.getFromXmlFile(new File(xmlConfigurationFile ));
			client = new DCA_Client(dcaConf.dcaConfigFile);
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		return true;
	}

	

	@Override
	public void destroy() {
		System.out.println("[DESTROY Database Resource]");
		client.shudtown();
		super.destroy();
	}

	@Override
	protected void finalize() throws Throwable {
		destroy();
		super.finalize();
	}

}
