package xiatian.pim.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 具有属性的接口
 * 
 * @author <a href="mailto:xiat@ruc.edu.cn">xiatian</a>
 * @version $version$
 * @date 2007-8-28
 */
public interface Propertable extends Serializable {
	/** 
	 * 增加新属性, 如果该属性已经存在，则覆盖
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, Object value);
	
	/**
	 * 获取指定名称的属性
	 * @param key
	 */
	public String getProperty(String key);
	
	public String getProperty(String key, String defaultValue);
	
	public int getPropertyInt(String key, int defalutValue);
	
	public boolean getPropertyBool(String key, boolean defaultValue);

    public List<Integer> getPropertyIntList(String key);

    public void setPropertyIntList(String key, List<Integer> list);
	
	/**
	 * 删除指定的属性
	 * @param key
	 * @return 返回刚删除的键值
	 */
	public String removeProperty(String key);
	
	/**
	 * 列举所有的属性集合
	 * @return
	 */
	public Map<String, String> listProperties();
	
	public String getProperties();
	
	public void setProperties(String properties);
}
