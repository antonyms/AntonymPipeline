/*******************************************************************************
 * Copyright 2012
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
package org.jobimtext.api.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Configuration class for the database connection
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DatabaseThesaurusConfiguration {

	public DatabaseTableConfiguration tables = new DatabaseTableConfiguration();

	public String dbUser;
	public String dbUrl;
	public String dbPassword;
	public String jdbcString;

	public String similarTermsQuery;
	public String similarTermsTopQuery;
	public String similarTermsGtScoreQuery;
	public String similarTermScoreQuery;
	
	public String similarContextsQuery;
	public String similarContextsTopQuery;
	public String similarContextsGtScoreQuery;

	public String termsCountQuery;
	public String contextsCountQuery;

	public String termContextsCountQuery;
	public String termContextsScoreQuery;
	public String termContextsScoresQuery;
	public String termContextsScoresTopQuery;
	public String termContextsScoresGtScoreQuery;
	public String sensesQuery;
	public String senseCUIsQuery;
	public String isasQuery;

	@XmlTransient
	private HashMap<String, String> tableStringMapping = null;
	@XmlTransient
	private List<String> tableValuesSorted = null;

	

	public String getSimilarTermsTopQuery(int top) {
		return replaceTables(similarTermsTopQuery, "$numberOfEntries",
				Integer.toString(top));
	}

	public String getSimilarTermsGtScoreQuery() {
		return replaceTables(similarTermsGtScoreQuery);
	}

	public String getSimilarContextsQuery() {
		return replaceTables(similarContextsQuery);
	}

	public String getSimilarContextsTopQuery(int top) {
		return replaceTables(similarContextsTopQuery, "$numberOfEntries",
				Integer.toString(top));
	}

	public String getSimilarContextsGtScoreQuery() {
		return replaceTables(similarContextsGtScoreQuery);
	}

	public HashMap<String, String> getTableStringMapping() {
		return tableStringMapping;
	}

	public List<String> getTableValuesSorted() {
		return tableValuesSorted;
	}

	public DatabaseTableConfiguration getTables() {
		return tables;
	}

	public void setTables(DatabaseTableConfiguration tables) {
		this.tables = tables;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getJdbcString() {
		return jdbcString;
	}

	public void setJdbcString(String jdbcString) {
		this.jdbcString = jdbcString;
	}

	public void saveAsXml(PrintStream ps) throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(DatabaseThesaurusConfiguration.class);

		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(this, ps);
	}

	public void saveAsXml(File file) throws JAXBException,
			FileNotFoundException {
		saveAsXml(new PrintStream(file));
	}

	public static DatabaseThesaurusConfiguration getFromXmlFile(File name)
			throws JAXBException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		JAXBContext jaxbContext = JAXBContext
				.newInstance(DatabaseThesaurusConfiguration.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		DatabaseThesaurusConfiguration conf = (DatabaseThesaurusConfiguration) jaxbUnmarshaller
				.unmarshal(name);
		return conf;
	}

	public static DatabaseThesaurusConfiguration getFromXmlFile(String name)
			throws JAXBException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		return getFromXmlFile(new File(name));
	}

	private String replaceTables(String query, String... replacements) {

		if (tableStringMapping == null) {
			tableStringMapping = new HashMap<String, String>();
			tableValuesSorted = new ArrayList<String>();
			for (Field f : tables.getClass().getDeclaredFields()) {
				String name = f.getName();

				try {

					String value = f.get(this.tables).toString();
					tableStringMapping.put(name, value);
					tableValuesSorted.add(name);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}catch(NullPointerException e){
					System.err.println("The table parameter: "+f.toString()+ " does not has a value assigned");
				}

			}
			// sort the table names according to their length (longest is first
			// element) to avoid replacing parts of table names
			Collections.sort(tableValuesSorted, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o2.length() - o1.length();
				}
			});
		}
		String sb = query;
		for (String key : tableValuesSorted) {
			sb = sb.replace("$" + key, tableStringMapping.get(key));
		}
		if (replacements.length % 2 != 0) {
			throw new IllegalArgumentException(
					"the arguemnts should also be modulo two==0: Key1 Value1 Key2 Value2");
		}
		for (int i = 0; i < replacements.length; i += 2) {
			sb = sb.replace(replacements[i], replacements[i + 1]);
		}

		return sb.toString();
	}

	public String getSimilarTermsQuery() {
		return replaceTables(similarTermsQuery);
	}

	public String getTermsCountQuery() {
		return replaceTables(termsCountQuery);
	}

	public String getContextsCountQuery() {
		return replaceTables(contextsCountQuery);
	}

	public String getTermContextsCountQuery() {

		return replaceTables(termContextsCountQuery);
	}

	public String getTermContextsScoreQuery() {

		return replaceTables(termContextsScoreQuery);
	}

	public String getTermContextsScoresQuery() {

		return replaceTables(termContextsScoresQuery);
	}

	public String getTermContextsScoresTopQuery(int numberOfEntries) {
		return replaceTables(termContextsScoresTopQuery, "$numberOfEntries",
				Integer.toString(numberOfEntries));
	}

	public String getTermContextsScoresGtScore() {
		return replaceTables(termContextsScoresGtScoreQuery);
	}

	public String getSensesCUIsQuery() {
		return replaceTables(senseCUIsQuery);
	}

	public String getIsasQuery() {
		return replaceTables(isasQuery);
	}

	public String getSensesQuery() {
		return replaceTables(sensesQuery);
	}

	public String getSimilarTermScoreQuery() {
		return replaceTables(similarTermScoreQuery);
	}

}
