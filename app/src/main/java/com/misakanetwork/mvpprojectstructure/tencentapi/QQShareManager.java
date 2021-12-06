package com.misakanetwork.mvpprojectstructure.tencentapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.tencentapi
 * class name：QQShareManager
 * desc：QQShareManager
 */
public class QQShareManager {
    /**
     * 链接
     */
    public static final int QQ_SHARE_WAY_WEBPAGE = 3;
    /**
     * QQ
     */
    public static final int QQ_SHARE_TYPE_TALK = 1;
    /**
     * QQ空间
     */
    public static final int QQ_SHARE_TYPE_ZONE = 2;
    /**
     * 分享成功
     */
    public static final int CALLBACK_CODE_SUCCESS = 0;
    /**
     * 取消分享
     */
    public static final int CALLBACK_CODE_CANCEL = 1;
    /**
     * 拒绝访问
     */
    public static final int CALLBACK_CODE_DENY = 2;
    /**
     * 未知
     */
    public static final int CALLBACK_CODE_UNKNOWN = 3;

    private static String appId;
    private Tencent mTencent;
    private QQShare qqShare;
    private QzoneShare qzoneShare;
    private QQShareResponse qqShareResponse;

    public void registShare(Context context){
        //初始化数据
        if(appId == null){
            appId = TencentConfig.APP_ID;
        }
        //初始化分享代码
        if(appId != null && (qqShare == null || qzoneShare == null)){
            mTencent = Tencent.createInstance(appId, context);
            qqShare = new QQShare(context, mTencent.getQQToken());
            qzoneShare = new QzoneShare(context, mTencent.getQQToken());
        }
    }

    /**
     * 分享qq和空间
     * @param shareContent 分享内容
     * @param shareType  选择类型（qq、空间）
     */
    public void shareByQQ(Activity activity, ShareContent shareContent, int shareType){
        shareWebPage(activity, shareType, shareContent);
    }

    private void shareWebPage(Activity activity, int shareType, ShareContent shareContent){
        Bundle params = new Bundle();
        if(shareType == QQ_SHARE_TYPE_ZONE){
            shareWebPageQzone(activity, shareContent, params);
        }else{
            shareWebPageQQ(activity, shareContent, params);
        }
    }

    private void shareWebPageQQ(Activity activity, ShareContent shareContent, Bundle params) {
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.getContent());
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getURL());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareContent.getPicUrl());

        doShareToQQ(activity, params, iUiListener);
    }

    private void shareWebPageQzone(Activity activity, ShareContent shareContent, Bundle params) {
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareContent.getTitle());
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareContent.getContent());
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareContent.getURL());
        ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add(shareContent.getPicUrl());
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        //params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, shareContent.getPicUrl());

        doShareToQzone(activity, params, iUiListener);
    }

    private void doShareToQQ(final Activity activity, final Bundle params, final IUiListener iUiListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(qqShare != null){
                    qqShare.shareToQQ(activity, params, iUiListener);
                }
            }
        }).start();
    }

    private void doShareToQzone(final Activity activity, final Bundle params, final IUiListener iUiListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(qzoneShare != null){
                    qzoneShare.shareToQzone(activity, params, iUiListener);
                }
            }
        }).start();
    }

    private final IUiListener iUiListener = new IUiListener() {
        @Override
        public void onCancel() {
            sendRespCode(CALLBACK_CODE_CANCEL);
        }

        @Override
        public void onComplete(Object response) {
            sendRespCode(CALLBACK_CODE_SUCCESS);
        }

        @Override
        public void onError(UiError e) {
            sendRespCode(CALLBACK_CODE_DENY);
        }

        private void sendRespCode(int code) {
            if(qqShareResponse != null){
                qqShareResponse.respCode(code);
            }
        }
    };

    public interface QQShareResponse{
        /**
         * 分享结果
         * @param code 结果码
         */
        public void respCode(int code);
    }

    /**
     * 注册结果回馈
     * @param qqShareResponse
     */
    public void setOnQQShareResponse(QQShareResponse qqShareResponse){
        this.qqShareResponse = qqShareResponse;
    }

    /**
     * 欢迎关注-阳光小强-http://blog.csdn.net/dawanganban
     * @author lixiaoqiang
     *
     */
    private abstract class ShareContent{
        protected abstract int getShareWay();
        protected abstract String getContent();
        protected abstract String getTitle();
        protected abstract String getURL();
        protected abstract String getPicUrl();
    }

    /**
     * 设置分享链接的内容
     * @author Administrator
     *
     */
    public class ShareContentWebpage extends ShareContent{
        private String title;
        private String content;
        private String url;
        private String picUrl;
        public ShareContentWebpage(String title, String content,
                                   String url, String picUrl){
            this.title = title;
            this.content = content;
            this.url = url;
            this.picUrl = picUrl;
        }

        @Override
        protected String getContent() {
            return content;
        }

        @Override
        protected String getTitle() {
            return title;
        }

        @Override
        protected String getURL() {
            return url;
        }

        @Override
        protected int getShareWay() {
            return QQ_SHARE_WAY_WEBPAGE;
        }

        @Override
        protected String getPicUrl() {
            return picUrl;
        }
    }
}
