/*******************************************************************************
* Copyright 2012
* Copyright (c) 2012 IBM Corp.
* 
* and
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

package org.jobimtext.api.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DcaLightThesaurusConfiguration {
	public DatabaseTableConfiguration tables = new DatabaseTableConfiguration();
	public int max;
	
	public void saveAsXml(PrintStream ps) throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(DcaLightThesaurusConfiguration.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(this, ps);
	}

	public void saveAsXml(File file) throws JAXBException,
			FileNotFoundException {
		saveAsXml(new PrintStream(file));
	}

	public static DcaLightThesaurusConfiguration getFromXmlFile(File name)
			throws JAXBException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		JAXBContext jaxbContext = JAXBContext
				.newInstance(DcaLightThesaurusConfiguration.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		DcaLightThesaurusConfiguration conf = (DcaLightThesaurusConfiguration) jaxbUnmarshaller
				.unmarshal(name);
		return conf;
	}

	public static DcaLightThesaurusConfiguration getFromXmlFile(String name)
			throws JAXBException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		return getFromXmlFile(new File(name));
	}
}
