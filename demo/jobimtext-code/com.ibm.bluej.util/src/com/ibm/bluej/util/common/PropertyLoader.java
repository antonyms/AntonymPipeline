package com.ibm.bluej.util.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PropertyLoader {

  public static final String DFLT_ETN = ".properties";

	/**
   * Checks classpath for a properties file 'name'
   * Works with either '.' separator or '/' separator and with or without '.properties' extension
   * if the .properties file contains things like keyX=bar, keyY=${keyX}foo 
   * then the ${keyX} will be substituted with the keyX value
   * 
	 * @param name qualified name of properties file
	 * @return
	 */
	public static HashMap<String,String> getPropertiesWithSubstitutions(String name) {
		if (name.endsWith(".properties"))
			name = name.substring(0, name.length()-".properties".length());
		
		Properties props = loadProperties(name);
		HashMap<String, String> currentMap = new HashMap<String, String>();
		for (Map.Entry<Object, Object> e : props.entrySet()) {
			if (e.getKey() instanceof String && e.getValue() instanceof String) {
				currentMap.put((String)e.getKey(), (String)e.getValue());
			} else {
				System.err.println("Bad property: "+e);
			}
		}
		//do replacement on the properties
		HashMap<String,String> nextMap = null;
		boolean modified = true;
		while (modified) {
			modified = false;
			nextMap = new HashMap<String, String>();
			for (Map.Entry<String, String> e : currentMap.entrySet()) {
				String path = e.getValue();
				for (Map.Entry<String, String> oe : currentMap.entrySet()) {
					if (e.getKey() != oe.getKey()) {
						String nextpath = path.replace("${"+oe.getKey()+"}", oe.getValue());
						if (!path.equals(nextpath)) {
							path = nextpath;
							modified = true;
						}
					}
				}
				nextMap.put(e.getKey(), path);
			}
			currentMap = nextMap;
		}
		return currentMap;
	}
  
  /**
   * loads the properties file 'name' from the classpath - default class loader it is very
   * permissive in how the name is formated - it can have the '.properties' extension or not it can
   * use either '.' or '/' as a separator there can be a leading '/' or not Example: Properties mine
   * = PropertyLoader.loadProperties("com.ibm.bluej.mypackage.myprops") loads 'myprops.properties'
   * from a directory like com/ibm/bluej/mypackage in the classpath
   * 
   * @param name
   * @return
   */
  public static Properties loadProperties(String name) {
    //System.out.println(getName(name));
    return getProperties(getClassLoader(), getName(name));
  }

  private static ClassLoader getClassLoader() {
    return  ClassLoader.getSystemClassLoader();
  }

  private static Properties getProperties(ClassLoader loader, String resource) {
    Properties props = new Properties();
    InputStream stream = loader.getResourceAsStream(resource);
    if (stream == null) {
      throw new IllegalArgumentException("Specified resource not found " + resource);
    } else {
      try {
        props.load(stream);
      } catch (IOException e) {
        throw new IllegalArgumentException("Unable to load resource" + resource);
      } finally {
        if (stream != null) {
          try {
            stream.close();
          } catch (IOException e) {
          }
        }
      }
    }
    return props;
  }

  private static String getName(String name) {
    // there can be a leading '/' or not. Remove it
    if (name.startsWith(File.separator)) {
      name = name.substring(1);
    }
    // use either '.' or '/' as a separator
    if (name.endsWith(DFLT_ETN)){
      String[] tokens = name.split(DFLT_ETN);
      if (tokens.length > 1){
        name = name.replace('.', File.separatorChar) +DFLT_ETN;
      }
    } else {
      name = name.replace('.', File.separatorChar);
    }
    
    if (!name.endsWith(DFLT_ETN)) {
      name = name + DFLT_ETN;
    }
    return name;
  }
  
  public static void main(String[] args) {
    System.out.println(getName("1.properties"));
  }
}
