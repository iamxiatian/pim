package xiatian.pim.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import xiatian.pim.util.FileUtils;

public class PimReader {
  private static final Set<String> supportedFileSuffixes = new HashSet<String>();
  static{
    supportedFileSuffixes.add(".txt");
    supportedFileSuffixes.add(".xml");
  }
  public String load(File file){
    if(file.isDirectory()) {
      return "Category:" + file.getName();
    }
    boolean isTxtFile = false;
    String ext = FileUtils.getExtension(file.getName(), true);
    for(String suffix: supportedFileSuffixes){
      if(ext.equalsIgnoreCase(suffix)){
        isTxtFile = true;
        break;
      }
    }
    
    if(!isTxtFile && ext.length()>0) {
      return "File:" + file.getName();
    }
    
    try{
      //try loading file from file system. 
      if(!file.exists()) {
        return "";
      }else{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line=reader.readLine())!=null){
          if(sb.length()>0){
            sb.append("\n");
          }
          sb.append(line);
        }
        return sb.toString();
      }
    }catch(Exception e){
      e.printStackTrace();
      return e.getLocalizedMessage();
    }     
  }
}
