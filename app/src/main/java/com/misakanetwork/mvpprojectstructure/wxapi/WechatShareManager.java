package com.misakanetwork.mvpprojectstructure.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.misakanetwork.mvpprojectstructure.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
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
 * class name：WechatShareManager
 * desc：微信分享工具
 */
public class WechatShareManager {
    private static final int THUMB_SIZE = 150;

    public static final int WECHAT_SHARE_WAY_TEXT = 1;   //文字
    public static final int WECHAT_SHARE_WAY_PICTURE = 2; //图片
    public static final int WECHAT_SHARE_WAY_WEBPAGE = 3;  //链接
    public static final int WECHAT_SHARE_WAY_VIDEO = 4; //视频
    public static final int WECHAT_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneSession;  //会话
    public static final int WECHAT_SHARE_TYPE_FRIENDS = SendMessageToWX.Req.WXSceneTimeline; //朋友圈

    private static WechatShareManager mInstance;
    private IWXAPI mWXApi;
    private Context mContext;

    public WechatShareManager(Context context) {
        this.mContext = context;
        //初始化数据
        //初始化微信分享代码
        initWechatShare(context);
    }

    /**
     * 获取WeixinShareManager实例
     * 非线程安全，请在UI线程中操作
     *
     * @return
     */
    public static WechatShareManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WechatShareManager(context);
        }
        return mInstance;
    }

    private void initWechatShare(Context context) {
        if (mWXApi == null) {
            mWXApi = WXAPIFactory.createWXAPI(context, WXConfig.APP_ID_WX, true);
        }
        mWXApi.registerApp(WXConfig.APP_ID_WX);
    }

    /**
     * 微信聊天分享
     */
    public void shareToWx(String title, String content, String image, String link) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                URL imageurl = null;
                try {
                    imageurl = new URL(image);
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
                if (bitmap1 == null) {
                    bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                }
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = link;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = title;
                msg.description = content;
                msg.setThumbImage(bitmap1);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                mWXApi.sendReq(req);
            }
        }).start();
    }

    /**
     * 图片分享
     *
     * @param url       url
     * @param shareType 类型
     */
    public void shareWebPicture(String url, int shareType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
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
                if (bitmap1 == null) {
                    bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                }
                WXImageObject imgObj = new WXImageObject(bitmap1);

                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = imgObj;

                Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap1, THUMB_SIZE, THUMB_SIZE, true);
                bitmap1.recycle();
//                msg.thumbData = createBitmapThumbnail(thumbBitmap, 128);
                msg.thumbData = bmpToByteArray(thumbBitmap, true);


                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("imgshareappdata");
                req.message = msg;
                req.scene = shareType;
                mWXApi.sendReq(req);
            }
        }).start();
    }

    /**
     * 朋友圈图文分享
     */
    public void shareToTimeLine(String webPageUrl, String title, String content, String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
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
                if (bitmap1 == null) {
                    bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                }
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = webPageUrl;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = title;
                msg.description = content;
                msg.setThumbImage(bitmap1);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                mWXApi.sendReq(req);
            }
        }).start();
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
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

    //图片转byteArray
    private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
