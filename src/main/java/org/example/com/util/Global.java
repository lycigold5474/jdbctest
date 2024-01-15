package main.java.org.example.com.util;

public class Global {

    /**
     * Null, "" String 값일 경우 대치문자열로 return 해준다
     * @param paramstr check할 문자열
     * @param repstr 대치할 문자열
     * @return
     */
    public static String NullCheck(String paramstr, String repstr){
        if(paramstr == null || paramstr.trim().equals("") || paramstr.equals("null")){
            return repstr;
        } else {
            return paramstr;
        }
    }
}
