package com.misakanetwork.lib_common.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.misakanetwork.lib_common.R;
import com.misakanetwork.lib_common.utils.FileUtils;
import com.misakanetwork.lib_common.utils.L;
import com.misakanetwork.lib_common.utils.SingleToastUtils;
import com.misakanetwork.lib_common.utils.ViewMeasureUtils;

import java.io.File;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.utils.glide
 * class name：LoadImageUtils
 * desc：Glide图片加载Utils
 */
public class LoadImageUtils {
    private static final String TAG = LoadImageUtils.class.getSimpleName();
    private static final Integer defaultPlaceHolder = null;
    private static final Integer defaultErrorHolder = null;
    private static final Integer defaultHeadIconHolder = null;
    private static final DecodeFormat defaultDecodeFormat = DecodeFormat.PREFER_ARGB_8888;

    public static void loadImage(String url, ImageView imageView, boolean small) {
        if (imageView == null) {
            L.e(TAG, ">>> loadImage imageView==null");
            return;
        }
        Glide.with(imageView.getContext().getApplicationContext())
                .load(url)
                .apply(getDefaultRequestOptions(defaultPlaceHolder, defaultErrorHolder,
                        small ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888))
                .into(imageView);
    }

    public static void loadImage(int resId, ImageView imageView, boolean small) {
        if (imageView == null) {
            L.e(TAG, ">>> loadImage imageView==null");
            return;
        }
        Glide.with(imageView)
                .load(resId)
                .apply(getDefaultRequestOptions(defaultPlaceHolder, defaultErrorHolder,
                        small ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888))
                .into(imageView);
    }

    public static void loadImage(int resId, ImageView imageView, ImageView.ScaleType scaleBefore,
                                 ImageView.ScaleType scaleAfter, boolean small) {
        if (imageView == null) {
            L.e(TAG, ">>> loadImage imageView==null");
            return;
        }
        Glide.with(imageView)
                .load(resId)
                .apply(getDefaultRequestOptions(defaultPlaceHolder, defaultErrorHolder,
                        small ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888))
                .into(new DrawableImageViewTarget(imageView) {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        imageView.setScaleType(scaleBefore);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        imageView.setScaleType(scaleBefore);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        imageView.setScaleType(scaleAfter);
                    }
                });
    }

    public static void loadImage(String url, ImageView imageView, ImageView.ScaleType scaleBefore,
                                 ImageView.ScaleType scaleAfter, boolean small) {
        if (imageView == null) {
            L.e(TAG, ">>> loadImage imageView==null");
            return;
        }
        Glide.with(imageView)
                .load(url)
                .apply(getDefaultRequestOptions(defaultPlaceHolder, defaultErrorHolder,
                        small ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888))
                .into(new DrawableImageViewTarget(imageView) {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        imageView.setScaleType(scaleBefore);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        imageView.setScaleType(scaleBefore);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        imageView.setScaleType(scaleAfter);
                    }
                });
    }

    public static void loadImage(String url, ImageView imageView, RequestOptions requestOptions) {
        if (imageView == null) {
            L.e(TAG, ">>> loadImage imageView==null");
            return;
        }
        Glide.with(imageView)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 头像加载url
     */
    public static void loadHeadImage(String url, ImageView imageView) {
        if (imageView == null) {
            L.e(TAG, ">>> loadHeadImage imageView==null");
            return;
        }
        Glide.with(imageView.getContext().getApplicationContext())
                .load(url)
                .apply(getDefaultRequestOptions(defaultHeadIconHolder, defaultHeadIconHolder, DecodeFormat.PREFER_RGB_565))
                .into(imageView);
    }

    /**
     * 头像加载resourceId
     */
    public static void loadHeadImage(int resId, ImageView imageView) {
        if (imageView == null) {
            L.e(TAG, ">>> loadHeadImage imageView==null");
            return;
        }
        Glide.with(imageView.getContext().getApplicationContext())
                .load(resId)
                .apply(getDefaultRequestOptions(defaultHeadIconHolder, defaultHeadIconHolder, DecodeFormat.PREFER_RGB_565))
                .into(imageView);
    }

    /**
     * 加载自适应大小网络图片、Gif
     * Gif不适用scaleAfter
     *
     * @param context    context
     * @param url        url
     * @param imageView  ImageView
     * @param scaleAfter ImageView.ScaleType
     */
    public static void loadAutoScaleImage(final Context context, String url, final ImageView imageView,
                                          @Nullable ImageView.ScaleType scaleAfter) {
        if (imageView == null) {
            L.e(TAG, ">>> loadAutoScaleImage imageView==null");
            return;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context)
                .load(url)
                .apply(getDefaultRequestOptions(defaultPlaceHolder, defaultErrorHolder, defaultDecodeFormat))
                .into(new DrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        // Gif自适应将自动设为FIT_XY
                        if (url.endsWith(".GIF") || url.endsWith(".gif")) {
                            // 首先设置imageView的ScaleType属性为ScaleType.FIT_XY，让图片不按比例缩放，把图片塞满整个View
                            if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                            // 得到当前imageView的宽度（我设置的是屏幕宽度），获取到imageView与图片宽的比例，然后通过这个比例去设置imageView的高
                            ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                            float scale = (float) vw / (float) resource.getIntrinsicWidth();
                            int vh = Math.round(resource.getIntrinsicHeight() * scale);
                            params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                            imageView.setLayoutParams(params);
                        } else { // 一般图片按照scaleAfter缩放
                            BitmapDrawable bd = (BitmapDrawable) resource;
                            Bitmap bitmap = bd.getBitmap();
                            final int height = bitmap.getHeight();
                            imageView.post(new Runnable() {
                                @Override
                                public void run() {
                                    ViewMeasureUtils.setVHeightPx(height, imageView);
                                    imageView.setScaleType(scaleAfter == null ? ImageView.ScaleType.CENTER_CROP : scaleAfter);
                                }
                            });
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setScaleType(scaleAfter == null ? ImageView.ScaleType.CENTER_CROP : scaleAfter);
                            }
                        });
                    }
                });
    }

    /**
     * 加载长图
     */
    public static void displayLongPic(Bitmap bmp, SubsamplingScaleImageView longImg) {
        longImg.setQuickScaleEnabled(true);
        longImg.setZoomEnabled(true);
        longImg.setPanEnabled(true);
        longImg.setDoubleTapZoomDuration(100);
        longImg.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        longImg.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        longImg.setImage(ImageSource.cachedBitmap(bmp), new ImageViewState(0, new PointF(0, 0), 0));
    }

    /**
     * Glide下载图片
     */
    public static void downloadImage(Context context, String url) {
        Glide.with(context)
                .downloadOnly()
                .load(url)
                .listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        SingleToastUtils.init(context).showNormal(context.getString(R.string.string_add_download_failed));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        SingleToastUtils.init(context).showNormal(context.getString(R.string.string_picture_glide_save_success));
                        FileUtils.albumUpdate(context, resource.getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
                        return false;
                    }
                })
                .preload();
    }

    public static RequestOptions getDefaultRequestOptions(Integer customPlaceHolder, Integer customErrorHolder,
                                                          DecodeFormat customDecodeFormat) {
        if (customPlaceHolder != null && customErrorHolder != null) {
            return new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(customPlaceHolder)
                    .error(customErrorHolder)
                    .format(customDecodeFormat);
        } else if (customPlaceHolder != null) {
            return new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(customPlaceHolder)
                    .format(customDecodeFormat);
        } else if (customErrorHolder != null) {
            return new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(customErrorHolder)
                    .format(customDecodeFormat);
        } else {
            return new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .format(customDecodeFormat);
        }
    }
}
