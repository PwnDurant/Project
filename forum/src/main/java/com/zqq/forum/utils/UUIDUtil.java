package com.zqq.forum.utils;

import java.util.UUID;

public class UUIDUtil {

    public static String UUID_36(){
        return UUID.randomUUID().toString();
    }

    public static String UUID_32(){
        return UUID.randomUUID().toString().replace("-","");
    }

}

