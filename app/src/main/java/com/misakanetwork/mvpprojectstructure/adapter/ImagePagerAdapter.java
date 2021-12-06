package com.misakanetwork.mvpprojectstructure.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.misakanetwork.lib_common.utils.glide.LoadImageUtils;
import com.misakanetwork.mvpprojectstructure.MainApplication;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.bean.ImageSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.adapter
 * class name：ImagePagerAdapter
 * desc：
 */
public class ImagePagerAdapter extends PagerAdapter {
    private List<String> imglist = new ArrayList<String>();
    private LayoutInflater inflater;
    private Context context;
    private ImageSize imageSize;
    private ImageView smallImageView = null;
    private boolean isFromGoods = false;
    private boolean isLocal = false;

    private Activity activity;

    public void setIsFromGoods(boolean is) {
        this.isFromGoods = is;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public void setDatas(List<String> datas) {
        if (datas != null)
            this.imglist = datas;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
    }

    public ImagePagerAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (imglist == null) return 0;
        return imglist.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.item_pager_image, container, false);
        if (view != null) {
            final PhotoView imageView = (PhotoView) view.findViewById(R.id.image);
            final FrameLayout layout = (FrameLayout) view.findViewById(R.id.serviceImglayout);
            final SubsamplingScaleImageView longImage = (SubsamplingScaleImageView) view.findViewById(R.id.longImg_View);
            if (imageSize != null) {
                //预览imageView
                smallImageView = new ImageView(context);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize.getHeight());
                layoutParams.gravity = Gravity.CENTER;
                smallImageView.setLayoutParams(layoutParams);
//                smallImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ((FrameLayout) view).addView(smallImageView);
            }


            if (isLocal) {
                RequestOptions options = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(container)
                        .asBitmap()
                        .load(imglist.get(position))
                        .apply(options)
                        .into(new SimpleTarget<Bitmap>(480, 800) {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                int width = resource.getWidth();
                                int height = resource.getHeight();
                                if (height >= (width * 3)) {
                                    longImage.setVisibility(View.VISIBLE);
                                    imageView.setVisibility(View.GONE);
                                    LoadImageUtils.displayLongPic(resource, longImage);
                                } else {
                                    longImage.setVisibility(View.GONE);
                                    imageView.setVisibility(View.VISIBLE);
                                    imageView.setImageBitmap(resource);
                                }
                            }
                        });
//                Glide.with(context).load(Uri.fromFile(new File(imglist.get(position)))).into(imageView);
//                imageView.setImageBitmap(BitmapFactory.decodeFile(imglist.get(position)));
            } else {
                String url = "";
                if (!TextUtils.isEmpty(imglist.get(position))) {
                    if (imglist.get(position).contains("http")) {
                        url = imglist.get(position);
                    } else {
                        url = MainApplication.getInstance().getImgConfig() + imglist.get(position);
                    }
                }
                String finalUrl = url;
                Glide.with(context)
                        .asBitmap()//强制Glide返回一个Bitmap对象
                        .load(url)
                        .into(new SimpleTarget<Bitmap>(480, 800) {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                int width = resource.getWidth();
                                int height = resource.getHeight();
                                Log.e("onResourceReady", "width " + width); //200px
                                Log.e("onResourceReady", "height " + height); //200px
                                if (height >= (width * 3)) {
                                    longImage.setVisibility(View.VISIBLE);
                                    imageView.setVisibility(View.GONE);
                                    LoadImageUtils.displayLongPic(resource, longImage);
                                } else {
                                    longImage.setVisibility(View.GONE);
                                    imageView.setVisibility(View.VISIBLE);
                                    RequestOptions requestOptions = new RequestOptions()
                                            .placeholder(R.drawable.img_load)
                                            .error(R.drawable.img_load);
                                    Glide.with(context)
                                            .load(finalUrl)
                                            .apply(requestOptions)
                                            .into(imageView);
                                }
                            }
                        });
            }

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!isLocal) {
//                        SaveImageDialog saveImageDialog = new SaveImageDialog(context , activity.getWindowManager());
//                        saveImageDialog.show();
//                        saveImageDialog.setClickListener(new SaveImageDialog.ClickListenerInterface() {
//                            @Override
//                            public void save() {
//                                RxPermissions rxPermissions = new RxPermissions(activity);
//                                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                                        .subscribe(new Observer<Boolean>() {
//                                            @Override
//                                            public void onCompleted() {
//
//                                            }
//
//                                            @Override
//                                            public void onError(Throwable e) {
//
//                                            }
//
//                                            @Override
//                                            public void onNext(Boolean aBoolean) {
//                                                if (aBoolean) {
//                                                    String path = "";
//                                                    if (!TextUtils.isEmpty(imglist.get(position))) {
//                                                        if (imglist.get(position).contains("http")) {
//                                                            path = imglist.get(position);
//                                                        }else {
//                                                            path = MyApplication.getInstance().getImgConfig() + imglist.get(position);
//                                                        }
//                                                    }
//                                                    DownloadImageUtils.downloadLatestFeature(context, path);
//                                                } else {
//                                                    Toast.makeText(context, R.string.permission_request_denied, Toast.LENGTH_LONG)
//                                                            .show();
//                                                }
//                                            }
//                                        });
//                            }
//                        });
                    }
                    return false;
                }
            });

            longImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    SaveImageDialog saveImageDialog = new SaveImageDialog(context , activity.getWindowManager());
//                    saveImageDialog.show();
//                    saveImageDialog.setClickListener(new SaveImageDialog.ClickListenerInterface() {
//                        @Override
//                        public void save() {
//                            RxPermissions rxPermissions = new RxPermissions(activity);
//                            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                                    .subscribe(new Observer<Boolean>() {
//                                        @Override
//                                        public void onCompleted() {
//
//                                        }
//
//                                        @Override
//                                        public void onError(Throwable e) {
//
//                                        }
//
//                                        @Override
//                                        public void onNext(Boolean aBoolean) {
//                                            if (aBoolean) {
//                                                String path = "";
//                                                if (!TextUtils.isEmpty(imglist.get(position))) {
//                                                    if (imglist.get(position).contains("http")) {
//                                                        path = imglist.get(position);
//                                                    }else {
//                                                        path = MyApplication.getInstance().getImgConfig() + imglist.get(position);
//                                                    }
//                                                }
//                                                DownloadImageUtils.downloadLatestFeature(context, path);
//                                            } else {
//                                                Toast.makeText(context, R.string.permission_request_denied, Toast.LENGTH_LONG)
//                                                        .show();
//                                            }
//                                        }
//                                    });
//                        }
//                    });
                    return false;
                }
            });

            longImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) context).finish();
                }
            });

            imageView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    ((Activity) context).finish();
                }
            });

            imageView.setOnOutsidePhotoTapListener(new OnOutsidePhotoTapListener() {
                @Override
                public void onOutsidePhotoTap(ImageView imageView) {
                    ((Activity) context).finish();
                }
            });

            container.addView(view, 0);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
