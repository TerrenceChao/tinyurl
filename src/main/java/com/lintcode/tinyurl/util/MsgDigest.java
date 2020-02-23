package com.lintcode.tinyurl.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MsgDigest {

    private MessageDigest messageDigestMD5;
    private MessageDigest messageDigestSHA1;

    private static MsgDigest instance;
    private MsgDigest() throws NoSuchAlgorithmException {
        // 拿到一個 MD5 轉換器
        messageDigestMD5 = MessageDigest.getInstance("MD5");
        // 拿到一個 SHA1 轉換器
        messageDigestSHA1 = MessageDigest.getInstance("SHA1");
    }

    public static MsgDigest getInstance() throws NoSuchAlgorithmException {
        if (instance == null) {
            instance = new MsgDigest();
        }

        return instance;
    }

    public String stringMD5(String input) {
        // 輸入的字串轉換成位元組陣列
        byte[] inputByteArray = input.getBytes();

        // inputByteArray是輸入字串轉換得到的位元組陣列
        messageDigestMD5.update(inputByteArray);

        // 轉換並返回結果，也是位元組陣列，包含16個元素
        byte[] resultByteArray = messageDigestMD5.digest();

        // 字元陣列轉換成字串返回
        return byteArrayToHex(resultByteArray);
    }

    public String stringSHA1(String input) {
        // 輸入的字串轉換成位元組陣列
        byte[] inputByteArray = input.getBytes();

        // inputByteArray是輸入字串轉換得到的位元組陣列
        messageDigestSHA1.update(inputByteArray);

        // 轉換並返回結果，也是位元組陣列，包含16個元素
        byte[] resultByteArray = messageDigestSHA1.digest();

        // 字元陣列轉換成字串返回
        return byteArrayToHex(resultByteArray);
    }

    public String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一個字元陣列，用來存放每個16進位制字元
        char[] hexDigits = { '0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };

        // new一個字元陣列，這個就是用來組成結果字串的（解釋一下：一個byte是八位二進位制，也就是2位十六進位制字元（2的8次方等於16的2次方））
        char[] resultCharArray =new char[byteArray.length * 2];

        // 遍歷位元組陣列，通過位運算（位運算效率高），轉換成字元放到字元陣列中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b& 0xf];
        }

        // 字元陣列組合成字串返回
        return new String(resultCharArray);
    }
}
