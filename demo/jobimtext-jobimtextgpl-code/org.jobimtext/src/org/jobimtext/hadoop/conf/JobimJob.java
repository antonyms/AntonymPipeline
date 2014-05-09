package org.jobimtext.hadoop.conf;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement()
@XmlSeeAlso({ PigJob.class, AggregatePerFeature.class,
	SimCounts1WithFeatures.class })
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class JobimJob {
	@XmlJavaTypeAdapter(MapAdapter.class)
	protected Map<String, String> hadoopConfiguration;
	protected String name;

	public JobimJob() {
		hadoopConfiguration = new HashMap<String, String>();
	}

	public abstract void run(Map<String, String> generalSettings)
			throws Exception;

	public String getName() {
		return name;
	}

	

}
