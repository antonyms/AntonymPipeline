package com.ibm.bluej.util.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;



/**
 * When reading from or writing to a file, a filename ending with .gz indicates the file
 * is/should-be gzipped for any write method, if any directories need to be created to write the
 * file, they should be created automatically the methods should not throw checked exceptions, if
 * there is an I/O problem, they throw an Error
 * 
 * @author psuryan
 * @author mchowdh (added a method to enable appending text to an existing file)
 * 
 */
public class FileUtil {

  public static String GZIP_EXTN = ".gz";

  public static String EXTN_CHR = ".";

  private static final int BUFFER_SIZE = 2 << 16;
  
  private static OutputStream getOutputStream(File file) throws FileNotFoundException, IOException {
    OutputStream os = new FileOutputStream(file);
    if (file.getName().endsWith(GZIP_EXTN)) {
      return new GZIPOutputStream(os, BUFFER_SIZE);
    }
    return new BufferedOutputStream(os, BUFFER_SIZE);
  }

  private static InputStream getInputStream(File file) throws FileNotFoundException, IOException {
    InputStream is = new FileInputStream(file);
    if (file.getName().endsWith(GZIP_EXTN)) {
      return new GZIPInputStream(is, BUFFER_SIZE);
    }
    return new BufferedInputStream(is, BUFFER_SIZE);
  }

  private static boolean isDirOrHiddenOrHasNoDot(File f) {
    String name = f.getName();
    if (f.isDirectory()) { // dir
      return true;
    }
    if (!name.contains(EXTN_CHR)) { // no extn file
      return true;
    }
    if (name.startsWith(EXTN_CHR)) { // hidden
      if (name.lastIndexOf(EXTN_CHR) == name.indexOf(EXTN_CHR)) { // no
        // extn
        return true;
      }
    }
    return false;
  }

  /**
   * writes the text to the file wither compressed or uncompressed
   * 
   * @param file
   * @param text
   */
  public static void writeFileAsString(File file, String text) {
    try {
      ensureWriteable(file);
      OutputStream os = getOutputStream(file);
      // since we are not sure about the stream type anymore
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
      out.write(text);
      out.close();
    } catch (IOException e) {
      throw new IOError(e);
    }
  }

  /**
   * writes the text to the file, given a fileName and text
   * 
   * @param filename
   * @param text
   */
  public static void writeFileAsString(String filename, String text) {
    File f = new File(filename);
    ensureWriteable(f);
    writeFileAsString(f, text);
  }

  /**
   * creates any directories that need to exist to create and write the file should not create the
   * file
   */
  public static void ensureWriteable(File f) {
    File parent = f.getParentFile();
    if (!parent.exists() && !parent.mkdirs()) {
      throw new IOError(new IllegalStateException("Couldn't create parent dir: " + parent));
    }
  }

  /**
   * read the contents of the file into a string and return it
   * 
   * @param filename
   * @return file contents as String
   */
  public static String readFileAsString(String filename) {
    return readFileAsString(new File(filename));
  }

  /**
   * read the contents of the file into a string and return it if the file doesn't exist, they
   * return null = * Based on
   * https://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html (APACHE LICENSE)
   * 
   * @param file
   * @return file contents as String
   */
  public static String readFileAsString(File file) {
    try {
      return readStreamAsString(getInputStream(file));
    } catch (FileNotFoundException e) {
      return null;
    } catch (IOException e) {
      throw new Error(e);
    }
  }

  /**
   * reads the input stream into a String and closes it
   * 
   * @param is
   * @return stream contents as String
   */
  public static String readStreamAsString(InputStream is) {
    String content;
    Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
    content = scanner.hasNext() ? scanner.next() : "";
    scanner.close();
    return content;
  }

  /**
   * save the presumably Serializable object to the file
   * 
   * @param file
   * @param object
   */
  public static void saveObjectAsFile(File file, Object object) {
    try {
      ensureWriteable(file);
      ObjectOutputStream oos = new ObjectOutputStream(getOutputStream(file));
      oos.writeObject(object);
      oos.flush();
      oos.close();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }

  /**
   * save the presumably Serializable object to the file, given a fileName
   * 
   * @param file
   * @param object
   */
  public static void saveObjectAsFile(String fileName, Object object) {
    saveObjectAsFile(new File(fileName), object);
  }

  /**
   * assume the file contains exactly one serialized object, read it and return it if the file
   * doesn't exist, they return null
   * 
   * @param file
   * @return object of type T
   */
  @SuppressWarnings("unchecked")
  public static <T> T loadObjectFromFile(File file) {
    try {
      ObjectInputStream ois = new RefactoringObjectInputStream(getInputStream(file));
      T object = (T) ois.readObject();
      ois.close();
      return object;
    } catch (FileNotFoundException fe) {
      return null;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    } catch (ClassNotFoundException cnfe) {
      throw new Error(cnfe);
    }
  }

  /**
   * Given a fileName, assume the file contains exactly one serialized object, read it and return it
   * if the file doesn't exist, they return null
   * 
   * @param file
   * @return object of type T
   */
  public static <T> T loadObjectFromFile(String fileName) {
    return loadObjectFromFile(new File(fileName));
  }

  /**
   * remove the last extension from a file If it is a directory, then return the name If it is a
   * hidden file without extn, return the name If it is a hidden file with extn, return name sans
   * extn If is a regular file with extn, return the name sans extn
   * 
   * @param fileName
   * @return fileName without extension
   */
  public static String removeExtension(String fileName) {
    File f = new File(fileName);
    int lastDotLoc = fileName.lastIndexOf(EXTN_CHR);
    if (isDirOrHiddenOrHasNoDot(f)) {
      return fileName;
    } else {
      if (lastDotLoc > fileName.indexOf(File.separatorChar)) {
        return fileName.substring(0, lastDotLoc);
      } else {
        return fileName;
      }
    }
  }

	/**
	 * removes the '.whatever.txt.gz' at the end of the file. If the file has multiple extensions it removes them all.
	 * @param file
	 * @return
	 */
	public static String removeExtensions(String file) {
		int extPos = file.indexOf(EXTN_CHR);
		if (extPos != -1 && extPos > file.lastIndexOf(File.separatorChar)) {
			file = file.substring(0, extPos);
		}
		return file;
	}  
  
  /**
   * return the last extension from a filename, including the '.'
   * 
   * @param fileName
   */
  public static String getExtension(String fileName) {
    String EMTY_CHR = "";
    File f = new File(fileName);
    int lastDotLoc = fileName.lastIndexOf(EXTN_CHR);
    if (isDirOrHiddenOrHasNoDot(f)) {
      return EMTY_CHR;
    } else {
      if (lastDotLoc > fileName.indexOf(File.separatorChar)) {
        return fileName.substring(lastDotLoc);
      } else {
        return EMTY_CHR;
      }
    }
  }

  /**
   * if the dirPath ends in a '/' it does nothing otherwise it adds a '/'
   * 
   * @param dirPath
   * @return
   */
  public static String ensureSlash(String dirPath) {
    if (!dirPath.endsWith(File.separator)) {
      return dirPath + File.separatorChar;
    } else {
      return dirPath;
    }
  }

  /**
   * Return true if the file exists
   * 
   * @param path
   * @return true of false
   */
  public static boolean exists(String path) {
    File f = new File(path);
    if (f.exists()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * gets the canonical path for the file, or null if it cannot
   * 
   * @param file
   * @return the canonical path
   */
  public static String getCanonicalPath(File file) {
    try {
      return file.getCanonicalPath();
    } catch (IOException e) {
      throw new IOError(e);
    }
  }

  /**
   * get an input stream for the file that is fast, trading memory for speed
   * 
   * @param file
   * @return InputStream
   */
  public static InputStream fastStream(File file) {
    FileInputStream fis = null;
    FileChannel ch = null;
    byte[] byteArray = null;
    try {
      fis = new FileInputStream(file);
      if (fis != null) {
        ch = fis.getChannel();
        if (ch.size() > 1000000000) {
        	return new BufferedInputStream(fis, BUFFER_SIZE);
        }
        MappedByteBuffer mb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
        byteArray = new byte[mb.capacity()];
        int got;
        while (mb.hasRemaining()) {
          got = Math.min(mb.remaining(), byteArray.length);
          mb.get(byteArray, 0, got);
        }
      }
    } catch (FileNotFoundException fnfe) {
      throw new IOError(fnfe);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    } finally {
      if (ch != null) {
        try {
          ch.close();
          fis.close();
        } catch (IOException ioe2) {
        }
      }
    }
    return new ByteArrayInputStream(byteArray);
  }

  /**
   * read the stdout of the process into a string and return when the function returns the process
   * has completed the stderr of the process is ignored
   * 
   * @param proc
   * @return String
   * @throws InterruptedException
   */
  public static String readProcessAsString(Process proc) throws InterruptedException {
    BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
    StringBuilder sb = new StringBuilder();
    String line = null;
    try {
      while ((line = br.readLine()) != null) {
        sb.append(line);
        sb.append("\n");
      }
    } catch (IOException e) {
      throw new IOError(e);
    }
    return sb.toString();
  }

	public static class FileIterable implements Iterable<File> {
		File root = null;
		public boolean shuffle;
		public FileFilter filter;
		public FileIterable(File root, FileFilter filter) {
			this.root = root;
			this.filter = filter;
		}
		public FileIterable(File root, FileFilter filter, boolean shuffle) {
			this.root = root;
			this.filter = filter;
			this.shuffle = shuffle;
		}
		public FileIterable(File root) {
			this.root = root;
		}
		@Override
		public Iterator<File> iterator() {
			return new FileIterator(root,filter,shuffle);
		}
	}
  
  public static class FileIterator implements Iterator<File> {

    private Queue<File> queue = new LinkedList<File>();

    private FileFilter filter;

    private boolean shuffle;

    private File ptr = null;

    public FileIterator(File root, FileFilter filter, boolean shuffle) {
      queue.add(root);
      this.filter = filter;
      this.shuffle = shuffle;
    }

    public FileIterator(File root, FileFilter filter) {
      queue.add(root);
      this.filter = filter;
    }

    public FileIterator(File root) {
      this(root, null);
    }

    public File peek() {
      if (ptr == null) {
        ptr = next();
      }
      return ptr;
    }

    @Override
    public boolean hasNext() {
      if (ptr != null) {
        return true;
      }
      ptr = next();
      return ptr != null ? true : false;
    }

    @Override
    public File next() {
      if (ptr != null) {
        File file = ptr;
        ptr = null; // reset queue pointer
        return file;
      }
      if (queue.isEmpty()) {
        return null;
      } else {
        File f = queue.remove();
        if (f.isFile()) {
          if ((filter == null) || (filter != null && filter.accept(f))) {
            return f;
          } else {
            return next();
          }
        } else {
          File[] filesInDir = f.listFiles();
          Arrays.sort(filesInDir, new Comparator<File>() {
            @Override
            public int compare(File thisFile, File thatFile) {
              if (thisFile.isDirectory()) {
                if (!thatFile.isDirectory()) {
                  return 1;
                } else {
                  if (shuffle) {
                    return (int) (Math.random() * 2);
                  } else {
                    return thisFile.compareTo(thatFile);
                  }
                }
              } else if (thisFile.isDirectory()) {
                return -1;
              } else {
                if (shuffle) {
                  return (int) (Math.random() * 2);
                } else {
                  return thisFile.compareTo(thatFile);
                }
              }
            }
          });
          List<File> files = Arrays.asList(filesInDir);
          for (File file : files) {
            queue.add(file);
          }
          return next();
        }
      }
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Not Implemented");
    }
  }
  
	/**
	 * constructs PrintStream to write to the file called filename, creating any parent directories, and gzipping the output if the filename ends with ".gz"
	 * @param filename
	 * @return
	 */
	public static PrintStream getFilePrintStream(String filename) {
		try {
			File file = new File(filename);
			ensureWriteable(file);
			OutputStream os = new FileOutputStream(file);
			if (filename.endsWith(".gz")) {
				os = new GZIPOutputStream(os);
			}			
			PrintStream out = new PrintStream(os);
			return out;
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	private static class FileLineIterable implements Iterable<String> {
		String filename = null;
		boolean skipComments = false;
		FileLineIterable(String filename, boolean skipComments) {this.filename = filename; this.skipComments = skipComments;}
		public Iterator<String> iterator() {
			return new FileLineIterator(filename, skipComments);
		}
	}
	private static class FileLineIterator implements Iterator<String> {
		BufferedReader reader;
		String line = null;
		boolean skipComments;
		FileLineIterator(String filename, boolean skipComments) {
			this.skipComments = skipComments;
			try {
				InputStream is = new FileInputStream(filename);
				if (filename.endsWith(".gz")) {
					is = new GZIPInputStream(is);
				}
				reader = new BufferedReader(new InputStreamReader(is));
			} catch (Exception e) {throw new Error(e);}
			nextLine();
		}
		private void nextLine() {
			try {
				if (reader == null) return;
				line = reader.readLine();
				if (line == null) {
					reader.close();
					reader = null;
				}
			} catch (Exception e) {throw new Error(e);}
			if (skipComments && line != null && (line.length() == 0 || line.startsWith("//"))) {
				nextLine();
			}
		}
		
		public boolean hasNext() {
			return line != null;
		}

		public String next() {
			String curLine = line;
			nextLine();
			return curLine;
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public static Iterable<String> getRawLines(String filename) {
		return new FileLineIterable(filename, false);
	}
	
	public static Iterable<String> getLines(String filename) {
		return new FileLineIterable(filename, true);
	}	
	
	public static String readResourceAsString(String resource) {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			if (loader == null) {
				loader = ClassLoader.getSystemClassLoader();
			}
			return FileUtil.readStreamAsString(loader.getResourceAsStream(resource));
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	public static DataOutputStream getDataOutput(String filename) {
		try {
			File file = new File(filename);
			ensureWriteable(file);			
			DataOutputStream out = new DataOutputStream(getOutputStream(file));
			return out;
		} catch (Exception e) {
			throw new Error(e);
		}		
	}
	public static DataInputStream getDataInput(String filename) {
		try {
			File file = new File(filename);	
			DataInputStream in = new DataInputStream(getInputStream(file));
			return in;
		} catch (Exception e) {
			throw new Error(e);
		}			
	}
	public static void ensureWorldReadable(File f) {
		File parent = f.getParentFile();
		if (parent != null && !parent.exists()) {
			ensureWorldReadable(parent);
			parent.mkdir();
			parent.setReadable(true, false);
			parent.setExecutable(true,false);
		}
	}
	public static void closeOutputStream(OutputStream os) {
		if (os != null) {
			try { os.close();
			} catch(Exception e) {}
		}
	}
	
	public static byte[] stringToBytes(String str) {
		try {
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(o);
			gz.write(str.getBytes());
			gz.close();
		return o.toByteArray();
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	public static String bytesToString(byte[] bs) {
		try {
			ByteArrayInputStream i = new ByteArrayInputStream(bs);
			GZIPInputStream gz = new GZIPInputStream(i);
			return readStreamAsString(gz);
		} catch (Exception e) {
			throw new Error(e);
		}
	}
//	/**
//	 * 
//	 * @param process
//	 * @return <stdout, stderr>
//	 */
//	public static Pair<String,String> readProcessAsStringPair(Process process) {
//		final StringBuilder bufout = new StringBuilder();
//		final StringBuilder buferr = new StringBuilder();
//		StreamEater.eatStream(new BufferedReader(new InputStreamReader(process.getInputStream())), new FunSV<String>() {
//			@Override
//			public void f(String o) {
//				bufout.append(o).append('\n');
//			}
//		});
//		StreamEater.eatStream(new BufferedReader(new InputStreamReader(process.getErrorStream())), new FunSV<String>() {
//			@Override
//			public void f(String o) {
//				buferr.append(o).append('\n');
//			}			
//		});
//		try {
//			process.waitFor();
//		} catch (Exception e) {
//			throw new Error(e);
//		}
//		return Pair.of(bufout.toString(), buferr.toString());
//	}
	
	  /**
	   * Ensure that a path exists and is readable, otherwise throw a RuntimeException using a
	   * description of the path.
	   */
	  public static void checkReadableOrThrowError(String description, String path) {
	    if (path == null) {
	      throw new RuntimeException(description + " path is null.");
	    }
	    File file = new File(path);
	    if (!file.exists()) {
	      throw new RuntimeException(description + " path (" + path + ") does not exist.");
	    } else if (!file.canRead()) {
	      throw new RuntimeException(description + " path (" + path + ") exists but does not have readable permissions.");
	    }
	  }
		
	  public static String getCanonicalPath(String path) {
	    return getCanonicalPath(new File(path));
	  }
	  
	  /**
	   * Appends text in an existing file. 
	   * If the file does not exist, a new file is created.
	   * 
	   * @param content
	   * @param outFile
	   * @throws IOException
	   */
	  public static void appendText ( String content, String outFileName ) throws IOException {
		  
		  if ( content == null || content.isEmpty() )
			  return;
		  
		  File file = new File(outFileName);
		  appendText(content, file);
	  }
	  
	  /**
	   * Appends text in an existing file. 
	   * If the file does not exist, a new file is created.
	   * 
	   * @param content
	   * @param outFile
	   * @throws IOException
	   */
	  public static void appendText ( String content, File outFile) throws IOException {
		 
		  if ( content == null || content.isEmpty() )
			  return;
		  
		  if (!outFile.exists()) {
			outFile.createNewFile();
		  }

		  FileWriter fw = new FileWriter(outFile.getAbsoluteFile(), true);
		  BufferedWriter bw = new BufferedWriter(fw);
		  bw.write(content);
		  bw.close();
	  }
}
