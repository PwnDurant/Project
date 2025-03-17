package com.zqq.common.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class RegexUtil {
    /**
     * 匹配 邮箱：xxx@xx.xxx(形如：abc@qq.com)
     * ^ 表示匹配字符串的开始。
     * [a-z0-9]+ 表示匹配一个或多个小写字母或数字。
     * ([._\\-]*[a-z0-9])* 表示匹配零次或多次下述模式：一个点、下划线、反斜杠或短横线，后面跟着一个或多个小写字母或数字。这部分是可选的，并且可以重复出现。
     * @ 字符字面量，表示电子邮件地址中必须包含的"@"符号。
     * ([a-z0-9]+[-a-z0-9]*[a-z0-9]+.) 表示匹配一个或多个小写字母或数字，后面可以跟着零个或多个短横线或小写字母和数字，然后是一个小写字母或数字，最后是一个点。这是匹配域名的一部分。
     * {1,63} 表示前面的模式重复1到63次，这是对顶级域名长度的限制。
     * [a-z0-9]+ 表示匹配一个或多个小写字母或数字，这是顶级域名的开始部分。
     * $ 表示匹配字符串的结束。
     */
    private static final String emailRegex = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
    /**
     * 进行简单校验, 为URL即可
     * https://gitee.com/bubble-fish666/
     * ^：匹配字符串的开始。
     * (https?)：匹配http或https。
     * :\/\/：匹配://。
     * ([a-zA-Z0-9.-]+)：匹配域名，可以包含字母、数字、点和破折号。
     * (:\d+)?：可选部分，匹配冒号和端口号。
     * (\/[^\s]*)?：可选部分，匹配斜杠和路径，路径中不包含空白字符。
     * (\?[^\s]*)?：可选部分，匹配查询参数，参数中不包含空白字符。
     * $：匹配字符串的结束。
     */
    private static final String urlRegex = "^(https?):\\/\\/([a-zA-Z0-9.-]+)(:\\d+)?(\\/[^\\s]*)?(\\?[^\\s]*)?$";

    /**
     * 邮箱：xxx@xx.xxx(形如：abc@qq.com)
     *
     * @param content
     * @return
     */

    public static boolean checkMail(String content) {
        if (!StringUtils.hasText(content)) {
            return false;
        }
        return Pattern.matches(emailRegex, content);
    }

    public static boolean checkURL(String content) {
        if (!StringUtils.hasText(content)) {
            return false;
        }
        return Pattern.matches(urlRegex, content);
    }

    public static void main(String[] args) {
        System.out.println(checkMail("bite@126.com"));
    }
}
