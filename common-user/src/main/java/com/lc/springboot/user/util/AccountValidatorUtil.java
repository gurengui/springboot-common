package com.lc.springboot.user.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * 账户相关属性验证工具
 * @author gurengui
 */
public class AccountValidatorUtil {
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,20}$";

    /**
     * 正则表达式：验证密码，不含有符号
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";

    /**
     * 正则表达式：验证密码，四种至少含三种
     */
    public static final String REGEX_PASSWORD2 = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,20}$";

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^[1]([3-9])[0-9]{9}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)|(^\\d{17}(\\d|X|x)$)";

    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * 正则表达式：验证ip地址
     */
    public static final String REGEX_IP_ADDR2 = "((1[0-9][0-9]\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)|([1-9][0-9]\\.)|([0-9]\\.)){3}((1[0-9][0-9])|(2[0-4][0-9])|(25[0-5])|([1-9][0-9])|([0-9]))$";

    /**
     * 正则表达式：验证I是否是1-5的整数
     */
    public static final String REGEX_INT_DAY = "[1-5]";


    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr2(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR2, ipAddr);
    }

    public static boolean isIntDay(String day) {
        return Pattern.matches(REGEX_INT_DAY, day);
    }

    public static String transformDate(String DateStr) throws Exception {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] input = DateStr.split("-");
        String result;
        if (now.get(Calendar.MONTH) <= Integer.valueOf(input[0])) {
            result = now.get(Calendar.YEAR) + "-" + DateStr;
        } else {
            result = (now.get(Calendar.YEAR) + 1) + "-" + DateStr;
        }
        Calendar applyCalendar = Calendar.getInstance();
        try {
            applyCalendar.setTime(simpleDateFormat.parse(result));
        }catch (ParseException e) {
            throw new Exception("填写的预约日期格式不合法，格式应为MM-dd");
        }
        if ((applyCalendar.getTimeInMillis() - now.getTimeInMillis())>4*24*3600*1000||(applyCalendar.getTimeInMillis() - now.getTimeInMillis())<-24*3600*1000) {
            throw new Exception("填写的预约日期应是从现在起5天内的日期");
        }
        return simpleDateFormat.format(applyCalendar.getTime());

    }




}