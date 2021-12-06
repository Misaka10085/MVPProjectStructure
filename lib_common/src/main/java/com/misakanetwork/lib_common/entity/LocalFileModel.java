package com.misakanetwork.lib_common.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.entity
 * class name：LocalFileModel
 * desc：本地文件信息Model
 */
public class LocalFileModel implements MultipleFileInterface, Parcelable {
    private String path;

    public LocalFileModel(String path) {
        this.path = path;
    }

    public LocalFileModel(Parcel in) {
        path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocalFileModel> CREATOR = new Creator<LocalFileModel>() {
        @Override
        public LocalFileModel createFromParcel(Parcel in) {
            return new LocalFileModel(in);
        }

        @Override
        public LocalFileModel[] newArray(int size) {
            return new LocalFileModel[size];
        }
    };

    public String getPath() {
        return path == null ? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public MultipleFileModel getMultipleFileModel() {
        return new MultipleFileModel(path, true);
    }
}
