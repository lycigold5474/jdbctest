package main.java.org.example.com.frame.database.runner;

import main.java.org.example.com.frame.data.Data;
import main.java.org.example.com.frame.data.DataList;
import main.java.org.example.com.frame.database.querylog.QueryLogger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlRunner {
    private SqlRunner() {
    }

    public static DataList executeQuery(JdbcTemplate tmp, String sql, DataList params) throws Exception {
        try{
            DataList list = new DataList();
            Object[] args = SqlRunnerUtil.getDataListToArray(params);
            RowMapper mapper = SqlRunnerUtil.getSelectDataRowMapper();
            return new DataList(tmp.query(sql, args, mapper));
        } catch (Exception e) {
            try {
                QueryLogger.queryLoggingError(sql, params);
            } catch (Exception ex) {}
            e.printStackTrace();
            throw e;
        }
    }

    public static DataList executeQuery(JdbcTemplate tmp, String sql, Data dataMap) throws Exception {
        try {
            sql = SqlRunnerUtil.parseAutoQuery(sql, dataMap);

            sql = eraseComment(sql);
            DataList params = new DataList();
            StringBuffer newSql = new StringBuffer();
            LinkedList<Integer> tmpArgTypes = new LinkedList<>();
            Pattern p = Pattern.compile(":[a-zA-Z_가-힣][a-zA-Z_0-9가-힣]+(#[a-zA-Z_가-힣][a-zA-Z_0-9가-힣]+)?"); // Parameter RegExp
            Matcher m = p.matcher(sql);
            int preIdx = 0;
            int lenMultiParams = 0;
            String strQuestion = "";
            String[] multiParams = null;
            while(m.find()){
                if (m.group().indexOf("#") != -1) {
                    multiParams = dataMap.get(m.group().split("#")[0].substring(1)).split(",");
                } else {
                    multiParams = dataMap.get(m.group().substring(1)).split(",");
                }
                if (multiParams == null) {
                    multiParams = new String[0];
                }
                lenMultiParams = multiParams.length;
                strQuestion = "?";
                for (int i = 1; i < lenMultiParams; i++){
                    strQuestion += ",?";
                }
                newSql.append(sql.substring(preIdx, m.start()) + strQuestion);
                preIdx = m.start() + m.group().length();
                if (m.group().indexOf("#") != -1){
                    for (int i=0; i< lenMultiParams; i++){
                        params.add(multiParams[i]);
                        tmpArgTypes.add(SqlRunnerUtil.dbTypeConverter(m.group().split("#")[1]));
                    }
                } else {
                    for(int i = 0; i < lenMultiParams; i++) {
                        params.add(multiParams[i]);
                        tmpArgTypes.add(java.sql.Types.VARCHAR);
                    }
                }
            }

            newSql.append(sql.substring(preIdx));
            int[] argTypes = new int[tmpArgTypes.size()];
            for (int i = 0; i < argTypes.length; i++) {
                argTypes[i] = (int) (tmpArgTypes.get(i));
            }
            Object[] args = SqlRunnerUtil.getDataListToArray(params);
            RowMapper mapper = SqlRunnerUtil.getSelectDataRowMapper();
            if (params.size() == 0) {
                return executeQuery(tmp, sql);
            } else {
                return new DataList( tmp.query(newSql.toString(), args, argTypes, mapper));
            }
        }catch (Exception e){
            try {
                QueryLogger.queryLoggingError(sql, dataMap);
            } catch (Exception ex) {}
            e.printStackTrace();
            throw e;
        }
    }

    public static DataList executeQuery(JdbcTemplate tmp, String sql) throws Exception {
        try {
            RowMapper mapper = SqlRunnerUtil.getSelectDataRowMapper();
            return new DataList(tmp.query(sql, mapper));
        } catch (Exception e) {
            try {
                QueryLogger.queryLoggingError(sql);
            } catch (Exception ex) {}
            e.printStackTrace();
            throw e;
        }
    }

    private static String eraseComment(String sql) {
        StringBuffer newSql = new StringBuffer();
        Pattern p = Pattern.compile("--.*");
        Matcher m = p.matcher(sql);
        int preIdx = 0;
        while (m.find()) {
            newSql.append(sql.substring(preIdx, m.start()));
            preIdx = m.end();
        }
        newSql.append(sql.substring(preIdx));
        sql = newSql.toString();
        newSql = new StringBuffer();
        p = Pattern.compile("/\\*[^+].*?\\*/", Pattern.DOTALL); // for oracle
        m = p.matcher(sql);
        preIdx = 0;
        while(m.find()){
            newSql.append(sql.substring(preIdx, m.start()));
            preIdx = m.end();
        }
        newSql.append(sql.substring(preIdx));
        return newSql.toString();
    }
}
