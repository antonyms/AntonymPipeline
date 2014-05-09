package org.jobimtext.hadoop.conf;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.pig.ExecType;
import org.apache.pig.PigServer;

@XmlRootElement(name="PigJob")
@XmlAccessorType(XmlAccessType.FIELD)
public class PigJob extends JobimJob {
	
	private String scriptPath;
	

	private PigJob() {}

	public PigJob(String name, String scriptPath, Map<String, String> params) {
		this.scriptPath = scriptPath;
		this.hadoopConfiguration = params;
		this.name = name;
	}

	

	public void run(Map<String,String> generalSettings) throws Exception {
		Map<String, String> m = PipelineConfiguration.combineSettings( generalSettings, hadoopConfiguration);
		System.out.println(m);
		Properties prop = new Properties();
		Map<String, String> param = new HashMap<String, String>();
		for(Entry<String, String> e: m.entrySet()){
			if(e.getKey().contains(".")){
				prop.put(e.getKey(), e.getValue());
				
			}else{
				param.put(e.getKey(), e.getValue());
			}
		}
		PigServer pig = new PigServer( ExecType.MAPREDUCE,prop );
		
		System.out.println(pig.getPigContext().getExecType().toString());
		System.out.println(pig.getPigContext().getDfs().getConfiguration().toString());
		//pig.debugOn();
		System.out.println(scriptPath);
		pig.registerScript(scriptPath, param);
		pig.shutdown();
	}

}
