package main.java.org.example.com.frame.data;

import main.java.org.example.com.frame.data.DataList;
import main.java.org.example.com.util.Global;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Data extends LinkedHashMap<String, Object> implements java.io.Serializable {
    public Data() {
        super();
    }

    public Data(Map<String, Object> map) {
        super(map);
    }

    public Data(String key, Object data) {
        super();
        add(key, data);
    }

    public Object add(String key, Object data) {
        return put(key.toUpperCase(), data);
    }

    public void set(String key, Object data) {
        remove(key.toUpperCase());
        add(key, data);
    }

    public String get(String key) {
        return Global.NullCheck(String.valueOf(getOrDefault(key.toUpperCase(), "")), "");
    }

    public String get(int idx) {
        String rValue = "";
        Set<String> keys = keySet();

        if (keys.size() < 1)
            return null;

        Iterator<String> iterator = keys.iterator();
        int index = 0;

        while (iterator.hasNext()) {
            String currentKey = iterator.next();
            if (idx == index) {
                rValue = get(currentKey);
                break;
            }
            index++;
        }

        return rValue;
    }

    public Object getObject(int idx) {
        Object rValue = null;
        Set<String> keys = keySet();

        if (keys.isEmpty())
            return null;

        Iterator<String> iterator = keys.iterator();
        int index = 0;

        while (iterator.hasNext()) {
            String currentKey = iterator.next();
            if (idx == index) {
                rValue = getObject(currentKey);
                break;
            }
            index++;
        }

        return rValue;
    }

    public Object getObject(String key) {
        Object value = get(key.toUpperCase());
        return (value instanceof String) ? Global.NullCheck((String) value, "") : value;
    }

    public DataList getList(String key) {
        Object value = get(key.toUpperCase());
        return (value instanceof String) ? new DataList() : (DataList) value;
    }
}
