package com.zqq.forum.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    public static String md5(String str){
        return DigestUtils.md5Hex(str);
    }

    public static String md5Salt(String str,String salt){
        return md5(md5(str)+salt);
    }
}
