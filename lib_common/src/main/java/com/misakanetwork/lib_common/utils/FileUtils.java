package com.misakanetwork.lib_common.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created By：Misaka10085
 * on：2021/4/14
 * package：com.misakanetwork.lib_common.utils
 * class name：FileUtils
 * desc：FileUtils
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 获取到sd卡的根目录，并以String形式返回
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory() + "/";
    }

    /**
     * uri转path
     * https://blog.csdn.net/chy555chy/article/details/104198956
     */
    public static String getPathFromUri(Context context, Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的Uri，通过document id处理，内部会调用Uri.decode(docId)进行解码
            String docId = DocumentsContract.getDocumentId(uri);
            // primary:Azbtrace.txt
            // video:A1283522
            String[] splits = docId.split(":");
            String type = null, id = null;
            if (splits.length == 2) {
                type = splits[0];
                id = splits[1];
            }
            switch (uri.getAuthority()) {
                case "com.android.externalstorage.documents":
                    if ("primary".equals(type)) {
                        path = Environment.getExternalStorageDirectory() + File.separator + id;
                    }
                    break;
                case "com.android.providers.downloads.documents":
                    if ("raw".equals(type)) {
                        path = id;
                    } else {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                        path = getMediaPathFromUri(context, contentUri, null, null);
                    }
                    break;
                case "com.android.providers.media.documents":
                    Uri externalUri = null;
                    switch (type) {
                        case "image":
                            externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            break;
                        case "video":
                            externalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            break;
                        case "audio":
                            externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                            break;
                    }
                    if (externalUri != null) {
                        String selection = "_id=?";
                        String[] selectionArgs = new String[]{id};
                        path = getMediaPathFromUri(context, externalUri, selection, selectionArgs);
                    }
                    break;
            }
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            path = getMediaPathFromUri(context, uri, null, null);
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri(uri.fromFile)，直接获取图片路径即可
            path = uri.getPath();
        }
        // 确保如果返回路径，则路径合法
        return path == null ? null : (new File(path).exists() ? path : null);
    }

    private static String getMediaPathFromUri(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path;
        String authority = uri.getAuthority();
        path = uri.getPath();
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (!path.startsWith(sdPath)) {
            int sepIndex = path.indexOf(File.separator, 1);
            if (sepIndex == -1) path = null;
            else {
                path = sdPath + path.substring(sepIndex);
            }
        }

        if (path == null || !new File(path).exists()) {
            ContentResolver resolver = context.getContentResolver();
            String[] projection = new String[]{MediaStore.MediaColumns.DATA};
            Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    try {
                        int index = cursor.getColumnIndexOrThrow(projection[0]);
                        if (index != -1) path = cursor.getString(index);
                        L.e(TAG, "getMediaPathFromUri query " + path);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        path = null;
                    } finally {
                        cursor.close();
                    }
                }
            }
        }
        return path;
    }

    /**
     * path转file
     */
    public static File getFileFromPath(String path) {
        return new File(path);
    }

    /**
     * file转path
     */
    public static String getPathFromFile(File file) {
        return file.getPath();
    }

    /**
     * path转uri
     */
    public static Uri getUriFromPath(String path) {
        return Uri.parse(path);
    }

    /**
     * uri转file
     */
    public static File getFileFromUri(Uri uri) {
        File file = null;   //图片地址
        try {
            file = new File(new URI(uri.toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * file转uri
     */
    public static Uri getUriFromFile(File file) {
        return Uri.parse(file.toString());
    }

    /**
     * 文件是否存在
     *
     * @param strFile 全路径
     */
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取文件后缀
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * 通过文件路径直接修改文件名
     *
     * @param filePath    需要修改的文件的完整路径
     * @param newFileName 需要修改的文件的名称
     */
    public static String fixFileName(String filePath, @NotNull String newFileName) {
        File f = new File(filePath);
        newFileName = newFileName.trim();
        String newFilePath = null;
        if (f.isDirectory()) { // 判断是否为文件夹
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + newFileName;
        } else {
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + newFileName
                    + filePath.substring(filePath.lastIndexOf("."));
        }
        File nf = new File(newFilePath);
        try {
            boolean isOk = f.renameTo(nf); // 修改文件名
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
        return newFilePath;
    }

    /**
     * 创建文件或文件夹
     *
     * @param fileName 文件名或问文件夹全路径
     */
    public static void createFolderFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            // 创建文件夹
            file.mkdir();
            System.out.println("创建了文件夹");
        }
    }

    /**
     * 删除文件及文件夹
     *
     * @param dir new File(filePath)
     */
    public static void deleteDirWithFile(File dir) {
        if (dir == null || !dir.exists())
            return;
        if (dir.listFiles() == null) {
            dir.delete();
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWithFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 获取文件大小
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     *
     * @param file 文件全路径
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 将图片文件加入到相册
     *
     * @param context context
     * @param dstPath 全路径
     */
    public static void albumUpdate(final Context context, final String dstPath, String fileName) {
        Bitmap bitmap = BitmapUtils.getBitmapByPath(dstPath);
        BitmapUtils.addPictureToAlbum(context, bitmap, fileName);
    }
}
