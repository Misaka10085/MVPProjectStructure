package com.misakanetwork.lib_common.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created By：Misaka10085
 * on：2021/6/18
 * package：com.misakanetwork.lib_common.utils
 * class name：StrUtils
 * desc：String Utils(包含多类型空判断)
 */
public class StrUtils {

    /**
     * 判断对象是否为空<br>
     * 1,字符串(null或者"")都返回true<br>
     * 2,数字类型(null或者0)都返回true<br>
     * 3,集合类型(null或者不包含元素都返回true)<br>
     * 4,数组类型不包含元素返回true(包含null元素返回false)<br>
     * 5,其他对象仅null返回true
     */
    public static boolean isEmpty(Object obj) {
        return isEmpty(obj, true);
    }

    public static boolean isEmptyNumber(Object obj, boolean includeZeroToEmpty) {
        return isEmpty(obj, includeZeroToEmpty);
    }

    public static boolean isEmpty(Object obj, boolean includeNum) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Number && includeNum) {
            Number num = (Number) obj;
            if (num.intValue() == 0) {
                return true;
            } else {
                return false;
            }
        } else if (obj instanceof String) {
            String str = (String) obj;
            if ((str == null) || str.equals("")) {
                return true;
            } else {
                return false;
            }
        } else if (obj instanceof Collection<?>) {
            Collection<?> c = (Collection<?>) obj;
            return c.isEmpty();
        } else if (obj instanceof Map<?, ?>) {
            Map<?, ?> m = (Map<?, ?>) obj;
            return m.isEmpty();
        } else if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            return length == 0;
        } else {
            return false;
        }
    }

    /**
     * 获取集合长度，为null返回0
     */
    public static int getSize(List list) {
        return list != null ? list.size() : 0;
    }

    /**
     * 移除集合中的null项目，[null]
     */
    public static void removeNull(List list) {
        if (isEmpty(list)) return;
        for (Object object : list) {
            if (object == null)
                list.remove(object);
        }
    }

    /**
     * list<String>转string
     */
    public static String listToString(List<String> list) {
        if (list == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        // 第一个前面不拼接","
        for (String string : list) {
            if (first) {
                first = false;
            } else {
                result.append(",");
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * string分割转list
     */
    public static List<String> stringToList(String str, @NotNull String symbol) {
        if (TextUtils.isEmpty(str)) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>();
        if (str.contains(symbol)) {
            String[] result = str.split(symbol);
            list.addAll(Arrays.asList(result));
        } else {
            list.add(str);
        }
        return list;
    }

    /**
     * 是否为net url
     */
    public static boolean isUrl(String str) {
        if (str == null) return false;
        return str.startsWith("http");
    }

    /**
     * 判断一个字符串是否是一个英文字符
     */
    public static boolean isEnChar(String str) {
        return !TextUtils.isEmpty(str) && str.length() == 1 && str.matches("^[a-zA-Z]*");
    }

    /**
     * 判断一个字符串是否为中文
     */
    public static boolean isChineseChar(String str) {
        if (isEmpty(str)) {
            return false;
        }
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    /**
     * 判断是否是数字
     */
    private static boolean isNum(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断2个字符串是否相同（无视大小写：A==a）
     */
    public static boolean isEqualsNoCase(String str1, String str2) {
        return str1.toLowerCase().equals(str2.toLowerCase());
    }

    /**
     * 去除字符串最后一个字符
     */
    public static String removeLastChar(String str, String c) {
        if (isEmpty(str)) {
            return "";
        }
        if (isEmpty(c)) {
            return str;
        }
        if (str.length() <= c.length()) {
            if (str.equals(c)) {
                return "";
            } else {
                return str;
            }
        }
        String lastC = str.substring(str.length() - c.length());
        if (lastC.equals(c)) {
            return str.substring(0, str.length() - c.length());
        } else {
            return str;
        }
    }

    /**
     * 去除字符串第一个特定字符
     */
    public static String removeFirstChar(String str, String c) {
        if (isEmpty(str)) {
            return "";
        }
        if (isEmpty(c)) {
            return str;
        }
        if (str.startsWith(c)) {
            return str.substring(c.length());
        } else {
            return str;
        }
    }

    /**
     * 在指定位置添加内容
     */
    public static String addContentAt(String oldStr, String addStr, int index) {
        if (oldStr.length() < index) {
            return oldStr;
        }
        StringBuilder sb = new StringBuilder(oldStr);
        sb.insert(index, addStr);
        return sb.toString();
    }

    /**
     * 设置字符串中的数字显示颜色
     */
    public static void setNumberTextColor(String str, TextView textView, Context c, int resourcesID) {
        char[] s = str.toCharArray();
        SpannableString ss = new SpannableString(str);
        for (int i = 0; i < s.length; i++) {
            if (isNum(String.valueOf(s[i]))) {
                ss.setSpan(new ForegroundColorSpan(c.getResources().getColor(resourcesID)), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textView.setText(ss);
    }

    /**
     * 把字符串转为double型，失败返回0
     */
    public static double str2double(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 把字符串转为Double型，失败返回null
     */
    public static Double str2doubleNull(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把字符串转为float型，失败返回0
     */
    public static float str2float(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 把字符串转为int型，失败返回0
     */
    public static int str2int(String str) {
        return str2int(str, 0);
    }

    /**
     * 把字符串转为int型，失败返回def
     */
    public static int str2int(String str, int def) {
        if (isEmpty(str)) {
            return def;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

    /**
     * double转String,至少保留小数点后两位
     */
    public static String doubleToStringMore(double num) {
        String strNum = String.valueOf(num);
        int n = strNum.indexOf(".");
        if (n > 0) {
            // 截取小数点后的数字
            String dotNum = strNum.substring(n + 1);
            if ("0".equals(dotNum)) {
                return strNum + "0";
            } else {
                if (dotNum.length() == 1) {
                    return strNum + "0";
                } else {
                    return strNum;
                }
            }
        } else {
            return strNum + ".00";
        }
    }

    /**
     * 动态去除.00情况，保留小数点后2位
     */
    public static String formatNumber(String number) {
        if (number == null) {
            return "";
        }
        if (number.indexOf(".") > 0) {
            if (!number.contains(".00") && !number.endsWith(".0")) {
                return doubleToString(Double.parseDouble(number));
            }
            // 正则表达
            number = number.replaceAll("0+?$", ""); // 去掉后面无用的零
            number = number.replaceAll("[.]$", ""); // 如小数点后面全是零则去掉小数点
        }
        if (number.indexOf(".") > 0) {
            BigDecimal b = new BigDecimal(number);
            return String.valueOf(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        } else {
            return number;
        }
    }

    public static String doubleToString(double num) {
        // 使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }


    /**
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     *
     * @param str 需要处理的字符串
     * @return 处理完之后的字符串
     */
    public static String addComma(String str) {
        DecimalFormat decimalFormat = new DecimalFormat(",###");
        return decimalFormat.format(Double.parseDouble(str));
    }

    /**
     * 返回中文金额单位结果
     */
    public static String[] chineseCode = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static String unit[][] = {{"", "万", "亿"}, {"", "十", "百", "千"}};
    private static String[] elseUnit = {"分", "角"};

    public static String getPriceUnitString(boolean isPoint, String sNum) {
        StringBuilder result = new StringBuilder();
        String[] numArray = sNum.split("");
        // 判断是整数部分还是小数部分
        if (isPoint) {
            // 小数部分
            // 判断是否是00
            if (sNum.equals("00")) {
                // 00加圆整
                result.append("圆整");
            } else {
                result.append("圆");
                // 非00需要加角分单位
                for (int i = 0; i < numArray.length; i++) {
                    int value = Integer.parseInt(numArray[i]) % 10;
                    result.append(chineseCode[value]).append(elseUnit[numArray.length - 1 - i]);
                }
            }
        } else {
            // 整数部分
            int integerPart = (int) Math.floor(Double.valueOf(sNum));
            for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
                String p = "";
                for (int j = 0; j < unit[1].length; j++) {
                    //每次除以10确定当前大写汉字是什么
                    p = chineseCode[integerPart % 10] + unit[1][j] + p;
                    integerPart = integerPart / 10;
                }
                // 使用正则去判断0
                result.insert(0, p.replaceAll("(零.)*零$", "") + unit[0][i]);
            }
        }
        // 把多余的零替换掉
        result = new StringBuilder(result.toString().replaceAll("(零.)+", "零"));
        if (result.charAt(0) == '零') {
            result = new StringBuilder(result.substring(1, result.length()));
        }
        if (result.length() > 1) {
            if (result.charAt(0) == '一' && result.charAt(1) == '十') {
                result = new StringBuilder(result.substring(1, result.length()));
            }
        }
        return result.toString();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的除法运算方法div
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException 如果精确范围小于0，抛出异常信息
     */
    public static double div(double value1, double value2, int scale) throws IllegalAccessException {
        // 如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        // 默认保留两位会有错误，这里设置保留小数点后4位
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
