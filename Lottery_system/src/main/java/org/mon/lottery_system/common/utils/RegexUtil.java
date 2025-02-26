package org.mon.lottery_system.common.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @author: zhaoqianqian
 */
public class RegexUtil {

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
        /**
         * ^ 表示匹配字符串的开始。
         * [a-z0-9]+ 表示匹配一个或多个小写字母或数字。
         * ([._\\-]*[a-z0-9])* 表示匹配零次或多次下述模式：一个点、下划线、反斜杠或短横线，后面跟着一个或多个小写字母或数字。这部分是可选的，并且可以重复出现。
         * @ 字符字面量，表示电子邮件地址中必须包含的"@"符号。
         * ([a-z0-9]+[-a-z0-9]*[a-z0-9]+.) 表示匹配一个或多个小写字母或数字，后面可以跟着零个或多个短横线或小写字母和数字，然后是一个小写字母或数字，最后是一个点。这是匹配域名的一部分。
         * {1,63} 表示前面的模式重复1到63次，这是对顶级域名长度的限制。
         * [a-z0-9]+ 表示匹配一个或多个小写字母或数字，这是顶级域名的开始部分。
         * $ 表示匹配字符串的结束。
         */
        String regex = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        return Pattern.matches(regex, content);
    }

    /**
     * 手机号码以1开头的11位数字
     *
     * @param content
     * @return
     */
    public static boolean checkMobile(String content) {
        if (!StringUtils.hasText(content)) {
            return false;
        }
        /**
         * ^ 表示匹配字符串的开始。
         * 1 表示手机号码以数字1开头。
         * [3|4|5|6|7|8|9] 表示接下来的数字是3到9之间的任意一个数字。这是中国大陆手机号码的第二位数字，通常用来区分不同的运营商。
         * [0-9]{9} 表示后面跟着9个0到9之间的任意数字，这代表手机号码的剩余部分。
         * $ 表示匹配字符串的结束。
         */
        String regex = "^1[3|4|5|6|7|8|9][0-9]{9}$";
        return Pattern.matches(regex, content);
    }

    /**
     * 密码强度正则，6到12位
     *
     * @param content
     * @return
     */
    public static boolean checkPassword(String content){
        if (!StringUtils.hasText(content)) {
            return false;
        }
        /**
         * ^ 表示匹配字符串的开始。
         * [0-9A-Za-z] 表示匹配的字符可以是：
         * 0-9：任意一个数字（0到9）。
         * A-Z：任意一个大写字母（从A到Z）。
         * a-z：任意一个小写字母（从a到z）。
         * {6,12} 表示前面的字符集合（数字、大写字母和小写字母）可以重复出现6到12次。
         * $ 表示匹配字符串的结束。
         */
        String regex= "^[0-9A-Za-z]{6,12}$";
        return Pattern.matches(regex, content);
    }
}
