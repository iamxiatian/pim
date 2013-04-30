package xiatian.pim.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 属性接口的默认实现
 *
 * @author <a href="mailto:xiat@ruc.edu.cn">xiatian</a>
 * @version $version$
 * @date 2007-8-28
 */
public class PropertyHandler implements Propertable {
    private static final long serialVersionUID = 5736145659599884198L;

    /**
     * 用于存放属性信息
     */
    private Map<String, String> propertiesMap;

    public PropertyHandler() {
        this.propertiesMap = new HashMap<String, String>();
    }

    public final String getProperties() {
        StringBuffer sb = new StringBuffer();
        for (String key : propertiesMap.keySet()) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(key);
            sb.append("=");
            String value = propertiesMap.get(key);
            //替换掉value中的回车和换行符号为特殊符号[@RETURN@]和[@CARRAGE@]
            value = value.replaceAll("\n", "[@RETURN@]");
            value = value.replaceAll("\r", "[@CARRAGE@]");
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 根据属性字符串，设置属性信息，主要是为了数据库OR映射使用
     */
    public final void setProperties(String properties) {
        if (properties == null) {
            properties = "";
            return;
        }

        String[] results = properties.split("\n");
        for (String line : results) {
            int pos = line.indexOf('=');
            if (pos < 0) {
                continue;
            }

            String key = line.substring(0, pos);
            String value = line.substring(pos + 1);
            //还原回车和换行符号, 注意在REGEX中，[]符号需要转义
            value = value.replaceAll("\\[@RETURN@\\]", "\n");
            value = value.replaceAll("\\[@CARRAGE@\\]", "\r");

            propertiesMap.put(key, value);
        }
    }

    public final void setPropertyIntList(String key, List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : list) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(i);
        }
        setProperty(key, sb.toString());
    }

    public final List<Integer> getPropertyIntList(String key) {
        List<Integer> list = new ArrayList<Integer>();
        String str = getProperty(key, "");
        String[] array = str.split(",");
        for (String num : array) {
            try {
                if (num != null) {
                    int n = Integer.parseInt(num);
                    list.add(n);
                }
            } catch (Exception e) {

            }
        }
        return list;
    }

    /**
     * 设置属性, 如果该属性已经存在，则覆盖，不存在，则增加新属性
     *
     * @param key
     * @param value
     */
    public final void setProperty(String key, Object value) {
        propertiesMap.put(key, value.toString());
    }

    /**
     * 获取指定名称的属性
     *
     * @param key
     */
    public final String getProperty(String key) {
        return propertiesMap.get(key);
    }

    public final String getProperty(String key, String defaultValue) {
        String value = propertiesMap.get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    public int getPropertyInt(String key, int defalutValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                System.out.println("getPropertyInt occured error:" + e.getMessage());
            }
        }

        return defalutValue;
    }

    public void setPropertyBool(String key, boolean v) {
        if (v) {
            setProperty(key, "true");
        } else {
            setProperty(key, "false");
        }
    }

    public boolean getPropertyBool(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            if (value.toLowerCase().trim().startsWith("t")) {
                return true;
            } else {
                return false;
            }
        }

        return defaultValue;
    }

    /**
     * 删除指定的属性
     *
     * @param key
     * @return 返回刚删除的键值
     */
    public final String removeProperty(String key) {
        return propertiesMap.remove(key);
    }

    /**
     * 列举所有的属性集合
     *
     * @return
     */
    public final Map<String, String> listProperties() {
        return propertiesMap;
    }
}
