package com.misakanetwork.mvpprojectstructure.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.misakanetwork.mvpprojectstructure.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.wxapi
 * class name：MiniProgramShareUtils
 * desc：小程序分享
 */
public class MiniProgramShareUtils {
    public static void share(Context mContext, String webPageUrl, String path, String title, String desc, final Bitmap bitmap, String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = bitmap;
                IWXAPI api = WXAPIFactory.createWXAPI(mContext, WXConfig.APP_ID_WX);
                WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
                miniProgramObj.webpageUrl = webPageUrl; // 兼容低版本的网页链接
//                miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
                miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW;// 正式版:0，测试版:1，体验版:2
                miniProgramObj.userName = WXConfig.WX_PROGRAM;     // 小程序原始id
                miniProgramObj.path = path;            //小程序页面路径
                WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
                msg.title = title;                    // 小程序消息title
                msg.description = desc;               // 小程序消息desc
                if (bitmap1 == null && url.isEmpty()) {
                    bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                } else { // 根据url获取bitmap
                    URL imageurl = null;
                    try {
                        imageurl = new URL(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap1 = BitmapFactory.decodeStream(is);
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bitmap1 == null) {
                    bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                }
                msg.thumbData = createBitmapThumbnail(bitmap1, 128); // 小程序消息封面图片，小于128k
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = "";
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前支持会话
                api.sendReq(req);
            }
        }).start();
    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     *
     * @param bitmap
     * @param maxKb
     * @return
     */
    public static byte[] createBitmapThumbnail(Bitmap bitmap, int maxKb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxKb && options != 10) {
            output.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);
            options -= 10;
        }
        return output.toByteArray();
    }
}
