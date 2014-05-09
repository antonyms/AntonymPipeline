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
package org.jobimtext.holing.extractor;

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

import org.jobimtext.holing.extractor.TokenExtractors.CoveredText;



@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JobimExtractorConfiguration {
	
	public String keyValuesDelimiter = "\t";
	public String attributeDelimiter = "#";
	public String valueDelimiter = "_";
	public String extractorClassName="";
	public String valueRelationPattern = "$values#$relation";
	public String holeSymbol = "@";

	
	

	public String getHoleSymbol() {
		return holeSymbol;
	}

	public void setHoleSymbol(String holeSymbol) {
		this.holeSymbol = holeSymbol;
	}

	public String getKeyValuesDelimiter() {
		return keyValuesDelimiter;
	}

	public void setKeyValuesDelimiter(String keyValuesDelimiter) {
		this.keyValuesDelimiter = keyValuesDelimiter;
	}

	public String getAttributeDelimiter() {
		return attributeDelimiter;
	}

	public void setAttributeDelimiter(String attributeDelimiter) {
		this.attributeDelimiter = attributeDelimiter;
	}

	public String getValueDelimiter() {
		return valueDelimiter;
	}

	public void setValueDelimiter(String valueDelimiter) {
		this.valueDelimiter = valueDelimiter;
	}

	public String getExtractorClassName() {
		return extractorClassName;
	}

	public void setExtractorClassName(String extractorClassName) {
		this.extractorClassName = extractorClassName;
	}

	public String getValueRelationPattern() {
		return valueRelationPattern;
	}

	public void setValueRelationPattern(String valueRelationPattern) {
		this.valueRelationPattern = valueRelationPattern;
	}

	public void saveAsXml(PrintStream ps) throws JAXBException{
		JAXBContext context = JAXBContext.newInstance(JobimExtractorConfiguration.class);
		
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(this,ps);
	}
	public static void main(String[] args) throws FileNotFoundException, JAXBException {
		JobimExtractorConfiguration conf = new JobimExtractorConfiguration();
		conf.attributeDelimiter="#";
		conf.keyValuesDelimiter="";
		conf.valueDelimiter="_";
		
		conf.extractorClassName=CoveredText.class.getName();
		conf.valueRelationPattern="$relation($values)";
		conf.holeSymbol="@";
		conf.saveAsXml(new File("config/extractor_covered_text.xml"));
	}
	
	public void saveAsXml(File file) throws JAXBException, FileNotFoundException{
		saveAsXml(new PrintStream(file));
	}
	public static JobimAnnotationExtractor getExtractorFromXmlFile(File name) throws JAXBException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		JAXBContext jaxbContext = JAXBContext.newInstance(JobimExtractorConfiguration.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		JobimExtractorConfiguration conf = (JobimExtractorConfiguration) jaxbUnmarshaller.unmarshal(name);
		return conf.getExtractor();
	}
	public static JobimAnnotationExtractor getExtractorFromXmlFile(String name) throws JAXBException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		return getExtractorFromXmlFile(new File(name));
	}


	private JobimAnnotationExtractor getExtractor() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		@SuppressWarnings("unchecked")
		Class<JobimAnnotationExtractor> cls = ((Class<JobimAnnotationExtractor>)Class.forName(extractorClassName));
		return cls.getConstructor(JobimExtractorConfiguration.class).newInstance(this);
	}


}
