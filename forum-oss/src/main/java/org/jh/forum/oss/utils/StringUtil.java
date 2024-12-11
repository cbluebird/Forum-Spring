package org.jh.forum.oss.utils;

import java.util.UUID;

public class StringUtil {
    public static String getRandomFileName(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (fileName.isEmpty() || index == -1) {
            throw new IllegalArgumentException();
        }
        String suffix = fileName.substring(index).toLowerCase();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid + suffix;
    }
}