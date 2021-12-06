package com.misakanetwork.lib_common.utils;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.entity
 * class name：EmojiJudgementUtils
 * desc：Emoji字符判断处理Utils
 */
public class EmojiJudgementUtils {

    /**
     * 判断是否存在特殊字符串
     *
     * @param content emoji content
     */
    public static boolean hasEmoji(String content) {
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    /**
     * 去除字符串中的emoji字符
     *
     * @param str emoji content
     */
    public static String replaceEmoji(String str) {
        if (!hasEmoji(str)) {
            return str;
        } else {
            str = str.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", "");
            str = str.replace(" ", "");
            return toUtf8(str);
        }
    }

    /**
     * string to utf8
     */
    private static String toUtf8(String str) {
        return new String(str.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }
}
