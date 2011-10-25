package xiatian.pim.conf;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PimConf {
    private Properties properties = new Properties();
    private File confFile = null;
    private static PimConf instance = new PimConf();

    private void load() throws FileNotFoundException, IOException {
        properties.load(new FileInputStream(confFile));
    }

    private PimConf() {
        String home = System.getProperties().getProperty("user.home");
        confFile = new File(new File(home), "pim.properties");
        try {
            load();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        String v = properties.getProperty(key);
        return v == null ? defaultValue : v;
    }

    public List<String> getList(String key) {
        String v = properties.getProperty(key);
        List<String> list = new ArrayList<String>();
        if (v != null) {
            String[] items = v.split(";");
            for (String item : items) {
                list.add(item);
            }
        }
        return list;
    }

    public void addList(String key, String value) {
        List<String> list = getList(key);
        int matchedPos = -1;
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            if (value.equals(item)) {
                matchedPos = i;
                break;
            }
        }
        if (matchedPos >= 0) {
            list.remove(matchedPos);
        }
        list.add(0, value);

        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            if (sb.length() > 0) {
                sb.append(";");
            }
            sb.append(item);
        }
        properties.put(key, sb.toString());
    }

    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    public int getInt(String key, int defalutValue) {
        try {
            return Integer.parseInt(get(key));
        } catch (Exception e) {
            return defalutValue;
        }
    }

    public void setInt(String key, int value) {
        properties.setProperty(key, Integer.toString(value));
    }

    public void setBoolean(String key, boolean value) {
        properties.setProperty(key, Boolean.toString(value));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        boolean value = defaultValue;
        try {
            value = Boolean.parseBoolean(properties.getProperty(key));
        } catch (Exception e) {
        }
        setBoolean(key, value);
        return value;
    }

    public Color getColor(String key, Color defalutColor) {
        try {
            Color color = new Color(Integer.parseInt(get(key)));
            return color;
        } catch (Exception e) {
            return defalutColor;
        }
    }

    public void setColor(String key, Color color) {
        String rgb = Integer.toString(color.getRGB());
        properties.setProperty(key, rgb);
    }

    public void setFont(String key, Font font) {
        properties.setProperty(key + ".name", font.getName());
        setInt(key + ".style", font.getStyle());
        setInt(key + ".size", font.getSize());
    }

    public Font getFont(String key, Font defaultFont) {
        Font f = getFont(key);
        return f == null ? defaultFont : f;
    }

    public Font getFont(String key) {
        String name = get(key + ".name");
        int style = getInt(key + ".style", 0);
        int size = getInt(key + ".size", 0);
        if (name == null || size == 0)
            return null;
        return new Font(name, style, size);
    }

    public void save() {
        try {
            properties.store(new FileOutputStream(confFile), "");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static PimConf getInstance() {
        return instance;
    }

}
