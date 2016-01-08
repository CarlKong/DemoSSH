package com.augmentum.ot.util;

import java.util.ArrayList;
import java.util.List;

public class StringHandlerUtils {

    /**
     * change list of string to string with ","
     * 
     * @param stringList
     * @return
     */
    public static String listToString(List<String> stringList, String strflag) {
        if (stringList == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(strflag);
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * Turn String to List
     * 
     * @param str
     * @return
     */
    public static List<String> strToArray(String str, String flag) {
        List<String> strList = new ArrayList<String>();
        if (str == null || "".equals(str)) {
            // do nothing
        } else {
            for (int i = 0; i < str.split(flag).length; i++) {
                if (str.split(flag)[i] != null && !"".equals(str.split(flag)[i])) {
                    strList.add(str.split(flag)[i]);
                }
            }
        }
        return strList;
    }

    public static String bulidPhraseQueryString(String filed, String value) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(filed);
        strBuilder.append(":\"");
        strBuilder.append(value);
        strBuilder.append("\"~0");
        String phraseQueryStr = strBuilder.toString();
        return phraseQueryStr;
    }
}
