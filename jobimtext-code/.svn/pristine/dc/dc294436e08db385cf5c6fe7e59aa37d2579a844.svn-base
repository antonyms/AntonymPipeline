package org.jobimtext.hadoop.conf;

import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

class MapElements {
	@XmlAttribute
	public String key;
	@XmlAttribute
	public String value;

	private MapElements() {
	} // Required by JAXB

	public MapElements(String key, String value) {
		this.key = key;
		this.value = value;
	}
}

public class MapAdapter extends XmlAdapter<MapElements[], Map<String, String>> {
	public MapAdapter() {
	}

	public MapElements[] marshal(Map<String, String> arg0) throws Exception {
		MapElements[] mapElements = new MapElements[arg0.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : arg0.entrySet())
			mapElements[i++] = new MapElements(entry.getKey(), entry.getValue());

		return mapElements;
	}

	public Map<String, String> unmarshal(MapElements[] arg0) throws Exception {
		Map<String, String> r = new TreeMap<String, String>();
		for (MapElements mapelement : arg0)
			r.put(mapelement.key, mapelement.value);
		return r;
	}
}
