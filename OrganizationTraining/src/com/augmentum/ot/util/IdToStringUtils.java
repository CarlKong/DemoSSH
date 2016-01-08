package com.augmentum.ot.util;

public class IdToStringUtils {

    public static String addPrefixForId(String prefix, int number, int id) {
        String str = "";
        String idStr = id + "";
        if (idStr.length() >= number) {
            str = prefix + idStr;
        } else {
            int a = number - idStr.length();
            String b= "";
            for(int i = 0; i < a; i++) {
                b = b + "0";
            }
            str = prefix + b + idStr;
        }
        return str;
    }
}
