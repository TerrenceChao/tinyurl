package com.lintcode.tinyurl.util;

public class Converter {
    public static final String BASE_62_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int BASE_62 = BASE_62_CHAR.length();

    public static Integer tinyUrlToID(String shortUrl) {
        int id = 0;
        for (int i = 0; i < shortUrl.length(); i++) {
            id = id * 62 + BASE_62_CHAR.indexOf(shortUrl.charAt(i));
        }

        return id;
    }

    public static String idToTinyUrl(int id) {
        String shortUrl = "";
        while (id > 0) {
            shortUrl = BASE_62_CHAR.charAt(id % BASE_62) + shortUrl;
            id = id / BASE_62;
        }

        while (shortUrl.length() < 6) {
            shortUrl = "0" + shortUrl;
        }

        return shortUrl;
    }

//    public static final String BASE_10_CHAR = "0123456789";
//    public static final int BASE_10 = BASE_10_CHAR.length();


    public static String mod(String str, Integer num) {
        Integer code = 0;
        for(int i = str.length() - 1; i >=0; i--) {
            code += BASE_62_CHAR.indexOf(str.charAt(i));
        }

        Integer mod = code % num;
//        System.out.println(" code >>> " + mod);

        return mod.toString();
    }
}
