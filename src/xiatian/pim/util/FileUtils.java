package xiatian.pim.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPInputStream;

public class FileUtils {

	/**
	 * 把字节大小转换成合适的单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getSizeMessage(int size) {
		if (size <= 0) {
			return "未知";
		}

		if (size < 1024) {
			return String.format("%dBytes", size);
		} else if (size < 1048576) {
			return String.format("%.2fK", size / 1024.0);
		} else {
			return String.format("%.2fM", size / 1048576.0);
		}
	}

	public static InputStream getInputStream(String filename,
			boolean gzCompressed) throws IOException {
		InputStream input = null;

		File file = new File(filename);
		if (!file.canRead()) {
			// logger.warn("无法读取文件:" + xmlFile + ", 尝试从classpath中加载.");
			input = FileUtils.class.getClassLoader().getResourceAsStream(
					filename);
			if (input == null) {
				throw new IOException("无法读取文件:" + filename);
			}
		} else {
			input = new FileInputStream(file);
		}
		if (gzCompressed) {
			input = new GZIPInputStream(input);
		}
		return input;
	}

	/**
	 * 从本地硬盘加载文件，读到字符串中
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String loadFromFile(File file) throws IOException {
	  BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));    
		String line = null;
		StringBuilder sb = new StringBuilder();
		while((line=reader.readLine())!=null){		  
		  if(sb.length()>0){
		    sb.append("\n");
		  }
      sb.append(line);
		}
		reader.close();
		return sb.toString();
	}

	 public static void saveToFile(File file, String content) throws IOException {
	   BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	   writer.write(content);
	   writer.close();
	  }
	
	/**
	 * 返回文件的扩展名, 如test.doc，则返回.doc，如果没有扩展名，返回空串
	 * 
	 * @param file
	 * @param withDotChar
	 *            是否在返回的扩展名中包括点
	 * @return
	 */
	public static String getExtension(String filename, boolean withDotChar) {
    int pos = filename.lastIndexOf(File.separator);
    String name = filename;
    if(pos>=0) {
      name = filename.substring(pos+1);
    }
    
    pos = name.lastIndexOf('.');
    if (pos >= 0) {
      return withDotChar?name.substring(pos):name.substring(pos+1);
    } else {
      return "";
    }	  
	}

	/**
	 * 获取不带后缀名的文件名称, 如./test/hello.txt, 返回./test/hello
	 * 
	 * @param filename
	 * @return
	 */
	public static String getNameWithoutExtension(String filename) {
		int pos = filename.lastIndexOf('.');
		if (pos > 0) {
			return filename.substring(0, pos);
		} else {
			return filename;
		}
	}

	/**
	 * 获取不带后缀名的文件名称, 如./test/hello.txt, 返回hello
	 * 
	 * @param filename
	 * @return
	 */
	public static String getSimpleNameWithoutExtension(String filename) {
		int pos1 = filename.lastIndexOf('.');
		int pos2 = filename.lastIndexOf(File.separator);
		pos2 = pos2 >= 0 ? pos2 + 1 : 0;
		if (pos1 > 0) {
			return filename.substring(pos2, pos1);
		} else {
			return filename.substring(pos2);
		}
	}

	/**
	 * This class copies an input file to output file
	 * 
	 * @param String
	 *            input file to copy from
	 * @param String
	 *            output file
	 */
	public static boolean copy(String input, String output) throws Exception {
		int BUFSIZE = 65536;
		try {
			FileInputStream fis = new FileInputStream(input);
			FileOutputStream fos = new FileOutputStream(output);

			int s;
			byte[] buf = new byte[BUFSIZE];
			while ((s = fis.read(buf)) > -1) {
				fos.write(buf, 0, s);
			}
		} catch (Exception ex) {
			throw new Exception("makehome" + ex.getMessage());
		}
		return true;
	}

	/**
	 * This class copies an input files of a directory to another directory and
	 * include subdir by deepCopy flag
	 * 
	 * @param String
	 *            sourcedir the directory to copy from such as:/home/bqlr/images
	 * @param String
	 *            destdir the target directory
	 * @param deepCopy
	 *            copy subdir or not.
	 */
	public static void copyDir(String sourcedir, String destdir,
			boolean deepCopy) throws IOException {
		File dest = new File(destdir);
		File source = new File(sourcedir);

		String[] files = source.list();
		try {
			dest.mkdirs();
		} catch (Exception ex) {
			throw new IOException("CopyDir:" + ex.getMessage());
		}

		for (int i = 0; i < files.length; i++) {
			String sourcefile = source + File.separator + files[i];
			String destfile = dest + File.separator + files[i];
			File temp = new File(sourcefile);
			// 过滤掉.svn，不拷贝该文件
			if (sourcefile.endsWith(".svn")) {
				continue;
			}
			if (temp.isFile()) {
				try {
					copy(sourcefile, destfile);
				} catch (Exception ex) {
					throw new IOException("CopyDir:" + ex.getMessage());
				}
			} else if (temp.isDirectory() && deepCopy) {
				copyDir(sourcefile, destfile, true);
			}
		}
	}

	/**
	 * This class del a directory recursively,that means delete all files and
	 * directorys.
	 * 
	 * @param File
	 *            directory the directory that will be deleted.
	 */
	public static void recursiveRemoveDir(File directory) throws Exception {
		if (!directory.exists())
			throw new IOException(directory.toString() + " do not exist!");
		String[] filelist = directory.list();
		File tmpFile = null;
		for (int i = 0; i < filelist.length; i++) {
			tmpFile = new File(directory.getAbsolutePath(), filelist[i]);
			if (tmpFile.isDirectory()) {
				recursiveRemoveDir(tmpFile);
			} else if (tmpFile.isFile()) {
				tmpFile.delete();
			}
		}
		directory.delete();
	}

	private static String[] IMAGE_EXT_NAMES = new String[] { ".gif", ".jpg",
			".jpeg", ".bmp", ".png" };

	/**
	 * 判断文件名称是否是图片文件
	 */
	public static boolean isImageFile(String filename) {
		String f = filename.toLowerCase();
		for (String ext : IMAGE_EXT_NAMES) {
			if (f.endsWith(ext)) {
				return true;
			}
		}

		return false;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(FileUtils.getExtension("hello.java", true));
		System.out.println(FileUtils.getExtension("hello.java", false));
		System.out.println(FileUtils.getNameWithoutExtension("./hello.java"));
		System.out.println(FileUtils
				.getSimpleNameWithoutExtension("./hello.java"));

		System.out.println(FileUtils.getSizeMessage(100));
		System.out.println(FileUtils.getSizeMessage(1000));
		System.out.println(FileUtils.getSizeMessage(1220));
		System.out.println(FileUtils.getSizeMessage(1220000));
	}

}
