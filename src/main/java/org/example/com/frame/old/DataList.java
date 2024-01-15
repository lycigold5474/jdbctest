package main.java.org.example.com.frame.old;

import main.java.org.example.com.util.Global;

import java.util.*;

public class DataList extends LinkedList implements java.io.Serializable{
    public DataList() { super(); }
    public DataList(List list) {
        super(list);
    }
    public String getJsonDataList() {
        StringBuffer rData = new StringBuffer();
        rData.append("[");
        for(int i = 0; i < size(); i++){
            Data data = (Data) get(i);
            Set keys = data.keySet();
            if(keys.size() < 1)
                return null;
            Iterator iterator = keys.iterator();
            rData.append("{");

            int index = 0;
            while( iterator.hasNext()){
                String key= (String)iterator.next();
                rData.append("'" + key + "':'" + Global.NullCheck(data.get(key), "").replaceAll("'", "\\\\")+"'");
                if(index < data.size() -1)
                    rData.append(",");

                index ++;
            }

            rData.append("}");
            if(i < size() -1)
                rData.append(",");
        }
        rData.append("]");
        return rData.toString();
    }

    public String get(int i, String key) {
        String returnStr = null;
        if(this.size() > 0) {
            if(this.get(i) instanceof Data) {
                Data tempData = (Data)this.get(i);
                returnStr = tempData.get(key);
            }
        }
        return returnStr;
    }

    public void sort(String key, boolean direction) {
        Collections.sort(this, new KeyComparator(key, direction));
    }

    public void numbericSort(String key, boolean direction) {
        Collections.sort(this, new NumbericKeyComparator(key, direction));
    }
}

class KeyComparator implements Comparator {
    private String key = "";
    private boolean direction = false;

    public KeyComparator(String key, boolean direction){
        this.key = key;
        this.direction = direction;
    }

    public int compare(Object o1, Object o2){
        String key1 = (String)((Data)o1).get(this.key);
        String key2 = (String)((Data)o2).get(this.key);

        if(direction){
            return key1.compareTo(key2);
        } else {
            return key1.compareTo(key2) * -1;
        }
    }
}

class NumbericKeyComparator implements Comparator {
    private String key = "";
    private boolean direction = false;

    public NumbericKeyComparator(String key, boolean direction){
        this.key = key;
        this.direction = direction;
    }

    public int compare(Object o1, Object o2){
        double key1 = Double.parseDouble(((Data)o1).get(this.key));
        double key2 = Double.parseDouble(((Data)o2).get(this.key));

        if(direction) {
            return (int) ((key1 - key2) / Math.abs(key1 - key2));
        } else {
            return (int) ((key2 - key1) / Math.abs(key2 - key1));
        }
    }

    public boolean equals(Object obj) {
        return this.equals(obj);
    }
}
