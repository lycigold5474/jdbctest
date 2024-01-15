package main.java.org.example.com.frame.old;

import main.java.org.example.com.util.Global;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Data extends LinkedHashMap implements java.io.Serializable {
    public Data() {
        super();
    }

    public Data(Map map) {super(map);}

    public Data(String key, Object data) {
        super();
        add(key, data);
    }

    public Object add(String key, Object data) {
        return super.put(key.toUpperCase(), data);
    }

    public void set(String key, Object data){
        if(containsKey(key.toUpperCase()))
            remove(key.toUpperCase());
        add(key, data);
    }

    public String get(String key) {
        String rValue = "";
        if(super.get(key.toUpperCase()) != null){
            rValue = Global.NullCheck((super.get(key.toUpperCase()) + ""), "");
        }

        return rValue;
    }

    public String get(int idx) {
        String rValue = "";
        Set keys = keySet();
        if (keys.size() < 1)
            return null;
        Iterator iterator = keys.iterator();

        int index = 0;
        while( iterator.hasNext()){
            String key = (String)iterator.next();

            if(idx == index)
                rValue = get(key);

            index ++;
        }
        return rValue;
    }

    public Object getObject(int idx) {
        Object rValue = "";
        Set keys = keySet();

        if(keys.isEmpty())
            return null;
        Iterator iterator = keys.iterator();

        int index = 0;
        while( iterator.hasNext()){
            String key = (String)iterator.next();
            if(idx == index)
                rValue = getObject(key);
            index ++;
        }

        return rValue;
    }

    public Object getObject(String key) {
        Object rValue = null;
        if(super.get(key.toUpperCase()) != null){
            if(super.get(key.toUpperCase()) instanceof String)
                rValue = Global.NullCheck((super.get(key.toUpperCase()) + ""), "");
            else
                rValue = super.get(key.toUpperCase());
        }
        return rValue;
    }

    public DataList getList(String key){
        Object rValue = null;
        if(super.get(key.toUpperCase()) !=null) {
            if(super.get(key.toUpperCase()) instanceof String)
                rValue = Global.NullCheck((super.get(key.toUpperCase()) + ""), "");
            else
                rValue= super.get(key.toUpperCase());
        }
        return (DataList)rValue;
    }
}
