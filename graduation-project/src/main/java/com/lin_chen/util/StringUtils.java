package com.lin_chen.util;

import java.util.stream.IntStream;

/**
 * StringUtils
 */
public class StringUtils {
    /**
     * @param o o
     * @return
     */
    public static boolean isNull(Object o) {
        return o == null;
    }

    /**
     * @param o o
     * @return
     */
    public static boolean isNotNull(Object o) {
        return !isNull(o);
    }

    /**
     * 是否为空
     *
     * @param s s
     * @return
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * 是否不为空
     *
     * @param s s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 判断是否为只包含空格的字符串或为空的字符串
     *
     * @param str str
     * @return
     */
    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        int strLen = str.length();
        if (strLen != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 判断是否为除空格外还是其他字符的字符串
     *
     * @param str str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断是否为空
     *
     * @param s s
     * @return
     */
    @Deprecated
    public static boolean notEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     *
     * @param c c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * @param s s
     * @return
     */
    public static boolean hasChinese(String s) {
        return IntStream.range(0, s.length()).filter(i -> isChinese(s.charAt(i))).findFirst().isPresent();
    }

    /**
     * @param cs  cs
     * @param num num
     * @return
     */
    public static String multi(CharSequence cs, int num) {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, num).forEach(i -> builder.append(cs));
        return builder.toString();
    }

}
