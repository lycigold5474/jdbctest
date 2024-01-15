package main.java.org.example.com.frame.database.querylog;


import main.java.org.example.com.frame.data.Data;
import main.java.org.example.com.frame.data.DataList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.support.SqlLobValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryLogger {

    public static void printQuery(Class cls, String query){
        Logger logger = null;
        if (cls == null){
            logger = LogManager.getRootLogger();
        } else {
            logger = LogManager.getLogger(cls);
        }
        logger.debug("\n -----------------------------SQL------------------------------------");
        if(cls != null){
            logger.debug("---- class : " + cls.getName());
        }
        logger.debug("\n" + query);
        logger.debug("\n\n-------------------------------------------------------------------");
    }

    public static void printQueryError(Class cls, String query) {
        Logger logger = null;
        if (cls == null) {
            logger = LogManager.getRootLogger();
        } else {
            logger = LogManager.getLogger(cls);
        }
        logger.error("\n--------------------------------- SQL --------------------------------");
        if (cls != null) {
            logger.error("---- class : " + cls.getName());
        }
        logger.error("\n" + query);
        logger.error("\n\n--------------------------------------------------------------------");
    }

    public static void queryLoggingError(String query){
        printQueryError(null, query);
    }

    public static void queryLoggingError(String query, Data map) {
        queryLoggingError(null, query, map);
    }

    public static void queryLoggingError(Class cls, String query, Data map) {
        StringBuffer newSql = new StringBuffer();
        Pattern p = Pattern.compile(":[a-zA-Z_가-힣][a-zA-Z_0-9가-힣]+(#[a-zA-Z_가-힣][a-zA-Z_0-9가-힣]+)?"); // Parameter RegExp
        Matcher m = p.matcher(query);
        int preIdx = 0;
        String strParams = "";
        String[] multiParams = null;
        while (m.find()) {
            if (m.group().indexOf("#") != -1) {
                multiParams = map.get(m.group().split("#")[0].substring(1)).split(",");
            } else {
                multiParams = map.get(m.group().substring(1)).split(",");
            }
            if (multiParams == null) {
                multiParams = new String[0];
            }
            strParams = "'" + multiParams[0] + "'";
            for (int i = 1; i < multiParams.length; i++) {
                strParams += ",'" + multiParams[i] + "'";
            }
            newSql.append(query.substring(preIdx, m.start()) + strParams);
            preIdx = m.start() + m.group().length();
        }

        newSql.append(query.substring(preIdx));
    }


    public static void queryLoggingError(String query, DataList params){
        queryLoggingError(null, query, params);
    }

    public static void queryLoggingError(Class cls, String query, DataList params){
        int length = query.length();
        int index = 0, start = 0, count = 0;
        StringBuffer buffer = new StringBuffer();

        while ((index = query.indexOf('?', start)) != -1){
            while (params.get(count) == null && count < params.size())
                count ++;
            buffer.append(query.substring(start, index));
            if (params.get(count) instanceof SqlLobValue) {
                buffer.append("'" + (((SqlLobValue) params.get(count++)).toString()) + "'");
            } else {
                buffer.append("'" + (String) params.get(count++) + "'");
            }
        }
    }
}
