package com.misakanetwork.lib_common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.misakanetwork.lib_common.R;
import com.misakanetwork.lib_common.entity.MessageEvent;
import com.misakanetwork.lib_common.utils.rx.RxBus;

import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils
 * class name：HtmlFromUtils
 * desc：textview加载富文本
 */
public class HtmlFromUtils {

    /**
     * 网络请求获取图片
     */
    private static Bitmap getImageFromNetwork(Context context, String imageUrl) {
        URL myFileUrl = null;
        Drawable drawable = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
//            drawable = Drawable.createFromStream(is, null);
            bitmap = BitmapFactory.decodeStream(is);
//            drawable = Drawable.createFromResourceStream(context.getResources(), null, is, "src", null);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private static Drawable drawable;

    /**
     * 将html字符串中的图片加载出来 设置点击事件 然后TextView进行显示
     *
     * @param context
     * @param v
     * @param sources
     */
    public static void setTextFromHtml(final Activity context, final TextView v, final String sources, final int totalMarginLeftRight, RxBus instance) {
        if (TextUtils.isEmpty(sources) || context == null || v == null)
            return;
        synchronized (HtmlFromUtils.class) { // 同步锁
            v.setMovementMethod(LinkMovementMethod.getInstance()); // 如果想对img标签添加点击事件必须调用这句 使图片可以获取焦点
//            v.setText(Html.fromHtml(sources)); // 默认不处理图片先这样简单设置
            v.setText(context.getString(R.string.string_html_text_view_loading)); // 默认不处理图片先这样简单设置
            new Thread(new Runnable() { // 开启线程加载其中的图片
                @Override
                public void run() {
                    Html.ImageGetter imageGetter = new Html.ImageGetter() { // Html.fromhtml方法中有一个参数 就是ImageGetter 此类负责加载source中的图片

                        @Override
                        public Drawable getDrawable(String source) {
//                            source = "https://admin.tlhappy.com/fileServer/images/2020/9/27/444a2559-d9b4-42f3-a2f1-4eec75cc6084.png";//source就是img标签中src属性值，相对路径的此处可以对其进行处理添加头部
                            Bitmap bitmap = getImageFromNetwork(context, source);
                            drawable = new BitmapDrawable(context.getResources(), bitmap);
                            if (drawable != null) {
                                int left = 0;
                                int w = drawable.getIntrinsicWidth();
                                int h = drawable.getIntrinsicHeight();
                                // 对图片大小进行等比例放大 此处宽高可自行调整
//                                if (w < h && h > 0) {
//                                    float scale = (400.0f / h);
//                                    if (scale >= 1) {
//                                        w = (int) (scale * w);
//                                        h = (int) (scale * h);
//                                    } else { // 防止图片宽小于高时因原图比例缩放过小
//                                        w = DensityUtil.getScreenWidth(context) - DensityUtil.dp2px(totalMarginLeftRight);
//                                        h = w - drawable.getIntrinsicWidth() + h;
//                                    }
//                                } else if (w > h && w > 0) {
//                                    float scale = (1000.0f / w);
//                                    w = (int) (scale * w);
//                                    h = (int) (scale * h);
//                                }
                                w = DensityUtils.getScreenWidth(context) - DensityUtils.dp2px(totalMarginLeftRight);
                                h = w - drawable.getIntrinsicWidth() + h;

                                drawable.setBounds(left, 0, w, h);
                            } else if (drawable == null) {
                                //bindData();
                                return null;
                            }
                            return drawable;
                        }
                    };
                    v.setTextColor(context.getResources().getColor(R.color.color_black));
                    //第三个参数 new URLTagHandler(context)负责添加img标签的点击事件
                    final CharSequence charSequence = Html.fromHtml(sources, imageGetter, new URLTagHandler(context));
                    //在activiy的runOnUiThread方法中更新ui
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            v.setText(charSequence);
                            if (instance != null) { // 加载完毕
                                instance.post(new MessageEvent(MessageEvent.EVENT_HTML_FROM_FINISHED));
                            }
                        }
                    });
                }
            }).start();
        }

    }


    /**
     * 此类负责处理source字符串中的img标签 对其添加点击事件
     */
    private static class URLTagHandler implements Html.TagHandler {

        private Context mContext;

        public URLTagHandler(Context context) {
            mContext = context.getApplicationContext();
        }

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            // 处理标签<img>
            if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
                // 获取长度
                int len = output.length();
                // 获取图片地址
                ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
                String imgURL = images[0].getSource();
                // 使图片可点击并监听点击事件
                output.setSpan(new URLTagHandler.ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        private class ClickableImage extends ClickableSpan {
            private String url;
            private Context context;

            public ClickableImage(Context context, String url) {
                this.context = context;
                this.url = url;
            }

            @Override
            public void onClick(View widget) {
                // 进行图片点击之后的处理
//                Toast.makeText(context, "点击图片的地址" + url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
