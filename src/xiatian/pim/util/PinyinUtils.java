package xiatian.pim.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 拼音处理的工具，负责从拼音词典加载内容，根据汉字词语或汉字查找拼音
 * 
 * <p>
 * Organization: Knowledge Engeering Laboratory, IRM, Renmin University of China
 * </p>
 * 
 * @author <a href="xiat@ruc.edu.cn">xiatian</a>
 * @version 1.0
 */
public class PinyinUtils {
	/** 拼音的Map词典, 一个汉字可能对应多个拼音, 它所有的拼音放到一个集合中 */
	private Map<Character, String> pinyinDict = null;
	
	/** 单例 */
	private static PinyinUtils instance = null;
	
	private PinyinUtils() throws IOException{
		//从classpath中加载拼音词典文件
		String pinyinDictFile = getClass().getPackage().getName().replaceAll("\\.", "/") + "/F02-GB2312-to-PuTongHua-PinYin.txt";
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(pinyinDictFile);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(input,	"UTF-8"));
		String line = null;

		MyTraverseEvent event = new MyTraverseEvent();
		while ((line = in.readLine()) != null) {
			event.visit(line);
		}

		input.close();
		in.close();
		
		this.pinyinDict = event.getPinyins();
	}
	
	public static PinyinUtils getInstance(){
		if(instance == null){
			try {
				instance = new PinyinUtils();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
		
		return instance;
	}
	
	/**
	 * 获取词语的拼音, 一个词语可能对应多个拼音，把所有可能的组合放到集合中返回
	 * @param word
	 * @return
	 */
	public String getPinyin(String word, boolean abbrevation){	
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<word.length(); i++){
			char hanzi = word.charAt(i);
			String hanzi_pinyin = pinyinDict.get(hanzi);
			if(hanzi_pinyin == null){
				if(isValidChar(hanzi)){
					sb.append(hanzi);
				}
			}else if(abbrevation){
				//如果缩写，只要头信息
				sb.append(hanzi_pinyin.charAt(0));
			}else{
				sb.append(hanzi_pinyin);
			}
		}
		return sb.toString();
	}
	
	public boolean isValidChar(char ch){
		boolean valid = ch>='a' && ch<='z';
		valid = valid || ch>='A' && ch<='Z';
		valid = valid || ch=='-' || ch =='_';
		return valid;
	}
	
	private static class MyTraverseEvent {
		/** 一个汉字对应多个拼音, 多个拼音放到集合中 */
		private Map<Character, String> pinyins = null;
		
		public MyTraverseEvent(){
			this.pinyins = new HashMap<Character, String>();
		}
		
		public Map<Character, String> getPinyins(){
			return pinyins;
		}
		
		public boolean visit(String item) {
			if(item.startsWith("//")){
				return true;
			}
			
			char hanzi = item.charAt(0);
			String pinyin = item.substring(2, item.length()-1);
			
			pinyins.put(hanzi, pinyin);
			return true;
		}		
	}
		
}
