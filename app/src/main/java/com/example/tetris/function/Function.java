package com.example.tetris.function;

import android.util.Log;

import java.util.ArrayList;
import java.util.Base64;

public class Function {
    public static String listToString(ArrayList<byte[]> list) {
        // 将ArrayList<byte[]>转换为Base64编码的String

        // 创建一个StringBuilder对象，用于存储转换后的字符串
        StringBuilder stringBuilder = new StringBuilder();

        // 遍历ArrayList中的每个byte数组
        for (byte[] byteArray : list) {
            // 将byte数组使用Base64编码转换为字符串
            String base64EncodedString = Base64.getEncoder().encodeToString(byteArray);
            // 将转换后的字符串添加到StringBuilder中，并在末尾添加逗号
            stringBuilder.append(base64EncodedString).append(",");
        }

        // 移除StringBuilder末尾的逗号
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }

        // 将StringBuilder转换为String
        String encodedArrayListString = stringBuilder.toString();

        // 打印转换后的字符串
        Log.d("decode", "listToString: " + encodedArrayListString);

        // 返回转换后的字符串
        return encodedArrayListString;
    }

    public static ArrayList<byte[]> StringToList(String str) {
        // 将Base64编码的String转换为ArrayList<byte[]>

        // 创建一个ArrayList对象，用于存储转换后的byte数组
        ArrayList<byte[]> decodedArrayList = new ArrayList<>();

        // 将输入的字符串按逗号分割为字符串数组
        String[] encodedStringArray = str.split(",");

        // 遍历字符串数组中的每个字符串
        for (String encodedString : encodedStringArray) {
            // 将字符串使用Base64解码转换为byte数组
            byte[] decodedByteArray = Base64.getDecoder().decode(encodedString);
            // 将转换后的byte数组添加到ArrayList中
            decodedArrayList.add(decodedByteArray);
        }

        // 打印转换后的ArrayList
        Log.d("decode", "listToString: " + decodedArrayList);

        // 返回转换后的ArrayList
        return decodedArrayList;
    }
}