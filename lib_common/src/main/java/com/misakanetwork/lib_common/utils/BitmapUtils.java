package com.misakanetwork.lib_common.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created By：Misaka10085
 * on：2021/4/14
 * package：com.misakanetwork.lib_common.utils
 * class name：BitmapUtils
 * desc：BitmapUtils
 */
public class BitmapUtils {

    /**
     * 根据path获取bitmap
     */
    public static Bitmap getBitmapByPath(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    /**
     * 根据uri获取bitmap
     */
    public static Bitmap getBitmapByUri(Context context, Uri uri, Bitmap.CompressFormat compressFormat) {
        Bitmap bitmap = null;
        OutputStream outputStream;
        try {
            outputStream = context.getContentResolver().openOutputStream(uri);
            bitmap.compress(compressFormat, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 根据资源id获取bitmap
     */
    public static Bitmap getBitmapByResourceId(Context context, int resourceId) {
        return BitmapFactory.decodeResource(context.getResources(), resourceId);
    }

    /**
     * 保存图片到picture 目录，Android Q适配，最简单的做法就是保存到公共目录，不用SAF存储
     */
    public static boolean addPictureToAlbum(Context context, Bitmap bitmap, String fileName) {
        bitmap = changeColor(bitmap);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, fileName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        OutputStream outputStream = null;
        try {
            outputStream = context.getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * bitmap中的透明色用白色替换
     */
    public static Bitmap changeColor(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] colorArray = new int[w * h];
        int n = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int color = getMixtureWhite(bitmap.getPixel(j, i));
                colorArray[n++] = color;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Bitmap.createBitmap(colorArray, w, h, Bitmap.Config.RGBA_F16);
        } else {
            return Bitmap.createBitmap(colorArray, w, h, Bitmap.Config.ARGB_8888);
        }
    }

    /**
     * 获取和白色混合颜色
     */
    private static int getMixtureWhite(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb(getSingleMixtureWhite(red, alpha), getSingleMixtureWhite(green, alpha),
                getSingleMixtureWhite(blue, alpha));
    }

    /**
     * 获取单色的混合值
     */
    private static int getSingleMixtureWhite(int color, int alpha) {
        int newColor = color * alpha / 255 + 255 - alpha;
        return Math.min(newColor, 255);
    }
}
