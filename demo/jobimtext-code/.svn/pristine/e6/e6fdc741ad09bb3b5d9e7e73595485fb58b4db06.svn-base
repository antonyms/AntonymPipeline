package org.jobimtext.hadoop.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ PigJob.class, AggregatePerFeature.class,
		SimCounts1WithFeatures.class })
public class PipelineConfiguration {
	@XmlAnyElement(lax = true)
	private List<JobimJob> pipeline = new ArrayList<JobimJob>();
	private String localInputDirectory;
	private String localOutputDirectory;
	private String hdfsDirectory;
	@XmlJavaTypeAdapter(MapAdapter.class)
	private Map<String, String> generalHadoopConfiguration;

	public PipelineConfiguration(String localInputDirectory,
			String localOutputDirectory, String hdfsDirectory) {
		this();
		this.localInputDirectory = localInputDirectory;
		this.localOutputDirectory = localOutputDirectory;
		this.hdfsDirectory = hdfsDirectory;
	}

	public PipelineConfiguration() {
		// super();
		generalHadoopConfiguration = new HashMap<String, String>();
		// pipeline = new ArrayList<JobimJob>();
		// generalHadoopConfiguration = new HashMap<String, String>();
	}

	public Map<String, String> getGeneralConfiguration() {
		return generalHadoopConfiguration;
	}

	public String getLocalInputDirectory() {
		return localInputDirectory;
	}

	public void setLocalInputDirectory(String localInputDirectory) {
		this.localInputDirectory = localInputDirectory;
	}

	public String getLocalOutputDirectory() {
		return localOutputDirectory;
	}

	public List<JobimJob> getJobs() {
		return pipeline;
	}

	public void setLocalOutputDirectory(String localOutputDirectory) {
		this.localOutputDirectory = localOutputDirectory;
	}

	public String getHdfsDirectory() {
		return hdfsDirectory;
	}

	public void setHdfsDirectory(String hdfsDirectory) {
		this.hdfsDirectory = hdfsDirectory;
	}

	public void addJob(JobimJob job) {
		pipeline.add(job);
	}

	public void putConf(String string, String string2) {
		generalHadoopConfiguration.put(string, string2);
	}

	public void saveAsXml(File file) throws JAXBException,
			FileNotFoundException {
		saveAsXml(new PrintStream(file));
	}

	public void saveAsXml(PrintStream ps) throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(PipelineConfiguration.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(this, ps);
	}

	public static PipelineConfiguration loadFromXml(File name)
			throws JAXBException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		JAXBContext jaxbContext = JAXBContext
				.newInstance(PipelineConfiguration.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		PipelineConfiguration conf = (PipelineConfiguration) jaxbUnmarshaller
				.unmarshal(name);
		return conf;
	}

	public static PipelineConfiguration loadFromXml(String name)
			throws JAXBException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		return loadFromXml(new File(name));
	}

	public int jobSize() {
		return pipeline.size();
	}

	public void run() throws Exception {
		for (JobimJob j : pipeline) {
			System.out.println("Execute: " + j.getName());
			j.run(generalHadoopConfiguration);
		}
	}

	public static Map<String, String> combineSettings(
			Map<String, String> generalHadoopConfiguration,
			Map<String, String> hadoopConfiguration) {
		Map<String, String> map = new HashMap<String, String>();
		if (generalHadoopConfiguration != null)
			map.putAll(generalHadoopConfiguration);
		if (hadoopConfiguration != null)
			map.putAll(hadoopConfiguration);
		int crepeat = 0;
		final int MAX_REPEAT = 3;
		boolean repeat = true;

		while (repeat && crepeat < MAX_REPEAT) {
			repeat = false;
			for (Entry<String, String> e : map.entrySet()) {
				String val = e.getValue();
				while (val.matches(".*\\{.*\\}.*")) {
					int si = val.indexOf('{');
					int ei = val.indexOf('}');
					String var = val.substring(si + 1, ei);
					if (map.containsKey(var)) {
						val = val.replace("{" + var + "}", map.get(var));
						map.put(e.getKey(), val);
					} else {
						System.err.println("Warning: Key \"" + var
								+ "\" not found ");
						repeat = true;
					}

				}
			}
			crepeat++;
		}

		return map;

	}

	public void simplifyConfiguration() {
		generalHadoopConfiguration = combineSettings(
				generalHadoopConfiguration, null);
	}
}
