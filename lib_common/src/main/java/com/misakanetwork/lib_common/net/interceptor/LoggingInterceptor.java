package com.misakanetwork.lib_common.net.interceptor;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.net.interceptor
 * class name：LoggingInterceptor
 * desc：日志拦截器
 */
public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "LoggingInterceptor";
    private final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();

        // 获取body
        String bodyStr = null;
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            bodyStr = buffer.readString(charset);
        }

        // 获取Get参数
        Map<String, String> paramMap = getParamMap(request.url().toString());
        String paramMapLog = "";
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            paramMapLog += entry.getKey() + ":" + entry.getValue() + "\n";
        }

        Log.e(TAG, String.format("发送请求 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n" +
                        "【method】：%s\n" +
                        "【url】：%s\n" +
                        "【get参数】：\n%s" +
                        "【headers】:\n%s" +
                        "【body】：%s",
                request.method(), request.url(), paramMapLog, request.headers(), bodyStr));
        // 获取响应时间
        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        // 获取响应body
        String rBody = null;
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    e.printStackTrace();
                }
            }
            rBody = buffer.clone().readString(charset);
        }

        // rBody可能过长，log一行打印不下，分行处理，一行打印3000个字符
        List<String> rBodys = clip(rBody);

        Log.e(TAG, String.format("收到响应 %s %ss <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n" +
                        "【msg】：%s\n" +
                        "【请求url】：%s\n" +
                        "【请求body】：%s\n" +
                        "【响应body】：\n%s",
                response.code(), tookMs, response.message(), response.request().url(), bodyStr, rBodys.get(0)));

        // 打印剩下部分未打印完的log
        for (int i = 1; i < rBodys.size(); i++) {
            Log.e(TAG, rBodys.get(i));
        }

        return response;
    }


    // 将超过最大长度的字符串分行处理
    private static List<String> clip(String s) {
        List<String> strs = new ArrayList<>();
        int maxlength = 3000;
        int iLen = s.length();
        while (iLen > maxlength) {
            String tmp = s.substring(0, maxlength);
            strs.add(tmp);
            s = s.substring(maxlength);
            iLen = s.length();
        }
        strs.add(s);
        return strs;
    }

    /**
     * 解析出url参数中的键值对，无则返回size为0 的map
     * index.jsp?id=123&name=ins  -->  { id: 123 , name: ins }
     */
    public static Map<String, String> getParamMap(String url) {
        Map<String, String> map = new LinkedHashMap();
        String paramStr = getParamStr(url);
        if (TextUtils.isEmpty(paramStr)) return map;
        String[] keyValues = paramStr.split("[&]");
        for (String keyValue : keyValues) {
            String[] keyValueArr = keyValue.split("[=]");
            map.put(keyValueArr[0], keyValueArr.length > 1 ? keyValueArr[1] : "");
        }
        return map;
    }

    /**
     * 删除url中的链接，只留下参数部分
     * http://www.baidu.com/api/map/getloc?id=1 --> id=1
     */
    public static String getParamStr(String url) {
        int index = url.indexOf("?");
        if (index >= 0) {
            return url.substring(index + 1);
        } else {
            return "";
        }
    }
}
