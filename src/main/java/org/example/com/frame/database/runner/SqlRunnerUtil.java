package main.java.org.example.com.frame.database.runner;

import main.java.org.example.com.frame.data.Data;
import main.java.org.example.com.frame.data.DataException;
import main.java.org.example.com.frame.data.DataList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.io.Reader;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlRunnerUtil {

    public static ArrayList<String> kknd_cols = null;

    static {
        kknd_cols = new ArrayList<String>();
        kknd_cols.add("KKND_CD");
    }

    private SqlRunnerUtil() {

    }

    public static Object[] getDataListToArray(DataList dataList) throws Exception{
        return (dataList != null && !dataList.isEmpty()) ? dataList.toArray() : null;
    }

    public static RowMapper<Data> getSelectDataRowMapper() {
        return (rs, rownum) -> {
            try {
                ResultSetMetaData rsMetaData = rs.getMetaData();
                int columnCnt = rsMetaData.getColumnCount();
                Data data = new Data();

                for (int i = 1; i <= columnCnt; i++) {
                    String columnName = rsMetaData.getColumnName(i);
                    int columnType = rsMetaData.getColumnType(i);

                    if (Types.CLOB == columnType) {
                        data.add(columnName, clobToString(rs, i));
                    } else if (Types.BLOB == columnType) {
                        data.add(columnName, blobToByteArray(rs, i));
                    } else {
                        data.add(columnName, rs.getString(i) != null ? rs.getString(i) : "");
                    }
                }

                return data;
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataException();
            }
        };
    }

    public static RowMapper getSelectHashMapRowMapper() throws Exception{
        RowMapper rowMapper = (rs, rownum) -> {
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCnt = rsMetaData.getColumnCount();
            Map map = new HashMap<>();
            try {
                for(int i=1; i<= columnCnt; i++){
                    String columnName = rsMetaData.getColumnName(i);
                    int columnType = rsMetaData.getColumnType(i);

                    if(Types.CLOB == columnType){
                        map.put(columnName, clobToString(rs,i));
                    } else if(Types.BLOB == columnType) {
                        map.put(columnName, blobToByteArray(rs, i));
                    } else {
                        if (rs.getString(i) == null) {
                            map.put(columnName, "");
                        } else {
                            map.put(columnName, rs.getString(i));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataException();
            }
            return map;
        };
        return rowMapper;
    }

    public static String parseAutoQuery(String query, Data map){
        if(map == null){
            return query;
        }

        for(int i = 0; i < kknd_cols.size(); i++){
            String key = kknd_cols.get(i);

            map.add(key + "_R", map.get(key));

            Pattern p = Pattern.compile("(?!ECH_CODE)[ \t]+(?i)(IN|=|<>|!=)+[ \t]*(\\()?[ \t]*(:"+ key + ")(\\))?");
            Matcher m = p.matcher(query);

            while(m.find()){
                if(m.group(1).matches("(?i)(IN|=)")){
                    query = StringUtils.replaceOnce(query, m.group(), " IN ( SELECT ECH_CODE FROM XXXX WHERE MAP_CODE IN (SELECT MAP_CODE FROM XXXX WHERE ECH_CODE IN (:" + key + "_R)) )");
                } else {
                    query = StringUtils.replaceOnce(query, m.group(), " NOT IN ( SELECT ECH_CODE FROM XXXX WHERE MAP_CODE IN (SELECT MAP_CODE FROM XXXX WHERE ECH_CODE IN (:" + key + "_R)) )");
                }
            }
        }
        return query;
    }

    public static int dbTypeConverter(String typeStr) {
        int result = -1;
        if (typeStr.toUpperCase().equals("ROWID")){
            result = java.sql.Types.ROWID;
        } else if (typeStr.toUpperCase().equals("BLOB")){
            result = java.sql.Types.BLOB;
        } else if (typeStr.toUpperCase().equals("CHAR")){
            result = java.sql.Types.CHAR;
        } else if (typeStr.toUpperCase().equals("CLOB")){
            result = java.sql.Types.CLOB;
        } else if (typeStr.toUpperCase().equals("DATE")){
            result = java.sql.Types.DATE;
        } else if (typeStr.toUpperCase().equals("NCHAR")){
            result = java.sql.Types.NCHAR;
        } else if (typeStr.toUpperCase().equals("NCLOB")){
            result = java.sql.Types.NCLOB;
        } else if (typeStr.toUpperCase().equals("NUMBER")){
            result = java.sql.Types.NUMERIC;
        } else if (typeStr.toUpperCase().equals("NVARCHAR2")){
            result = java.sql.Types.NVARCHAR;
        } else {
            result = java.sql.Types.VARCHAR;
        }
        return result;
    }




    public static String clobToString(ResultSet rs, int idx) throws Exception {
        String clobString = "";

        if(rs != null) {
            StringBuffer output = new StringBuffer();
            Reader input = rs.getCharacterStream(idx);

            if(input == null) return clobString;

            char[] buffer = new char[1024];
            int byteRead;
            while((byteRead = input.read(buffer, 0, 1024)) != -1){
                output.append(buffer, 0, byteRead);
            }

            input.close();

            clobString = output.toString();
        }

        return clobString;
    }

    public static byte[] blobToByteArray(ResultSet rs, int idx) throws Exception {
        byte[] rValue = null;
        if(rs != null){
            Blob blob = rs.getBlob(idx);
            rValue = blob.getBytes(1, (int) blob.length());
        }

        return rValue;
    }

}
