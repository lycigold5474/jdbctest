package main.java.org.example.com.frame.data;

import main.java.org.example.com.util.Global;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 자바 1.6 -> 1.8로 업그레이드
 * @param <T>
 */
public class DataList<T extends Data> extends LinkedList<T> implements java.io.Serializable {
    public DataList() {
        super();
    }

    public DataList(List<T> list) {
        super(list);
    }

    public String toJson() {
        StringBuilder jsonData = new StringBuilder("[");
        for (int i = 0; i < size(); i++) {
            T data = get(i);
            if (data.keySet().isEmpty()) {
                return null;
            }

            jsonData.append("{");
            int index = 0;
            for (Object keyObject : data.keySet()) {
                if (!(keyObject instanceof String)) {
                    // key가 String 타입이 아닌 경우 처리
                    // 예를 들어, 다른 타입의 key가 있을 경우 어떻게 처리할지에 대한 로직 추가
                    continue;
                }
                String key = (String) keyObject;
                jsonData.append("'" + key + "':'" + Global.NullCheck(data.get(key), "").replaceAll("'", "\\\\") + "'");
                if (index < data.size() - 1) {
                    jsonData.append(",");
                }
                index++;
            }
            jsonData.append("}");

            if (i < size() - 1) {
                jsonData.append(",");
            }
        }
        jsonData.append("]");
        return jsonData.toString();
    }


    public String get(int i, String key) {
        return (size() > 0 && get(i) instanceof Data) ? ((Data) get(i)).get(key) : null;
    }

    public void sort(String key, boolean direction) {
        sortHelper((o1, o2) -> {
            String key1 = o1.get(key);
            String key2 = o2.get(key);
            return direction ? key1.compareTo(key2) : key2.compareTo(key1);
        });
    }

    public void numericSort(String key, boolean direction) {
        sortHelper((o1, o2) -> {
            double numKey1 = Double.parseDouble(o1.get(key));
            double numKey2 = Double.parseDouble(o2.get(key));
            return direction ? Double.compare(numKey1, numKey2) : Double.compare(numKey2, numKey1);
        });
    }

    private void sortHelper(java.util.Comparator<T> comparator) {
        Collections.sort(this, comparator);
    }
}
