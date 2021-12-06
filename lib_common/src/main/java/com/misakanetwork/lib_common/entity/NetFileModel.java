package com.misakanetwork.lib_common.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.entity
 * class name：NetFileModel
 * desc：文件上传后返回的网络文件信息Model
 */
public class NetFileModel implements MultipleFileInterface, Parcelable {
    private String sourcePath;
    private String smallPath;
    private String bigPath;
    private String fileName;
    private String fileExt;
    private String fullUrl;
    private Integer sourceFileLength;
    private boolean tag;

    protected NetFileModel(Parcel in) {
        sourcePath = in.readString();
        smallPath = in.readString();
        bigPath = in.readString();
        fileName = in.readString();
        fileExt = in.readString();
        fullUrl = in.readString();
        if (in.readByte() == 0) {
            sourceFileLength = null;
        } else {
            sourceFileLength = in.readInt();
        }
        tag = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sourcePath);
        dest.writeString(smallPath);
        dest.writeString(bigPath);
        dest.writeString(fileName);
        dest.writeString(fileExt);
        dest.writeString(fullUrl);
        if (sourceFileLength == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sourceFileLength);
        }
        dest.writeByte((byte) (tag ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NetFileModel> CREATOR = new Creator<NetFileModel>() {
        @Override
        public NetFileModel createFromParcel(Parcel in) {
            return new NetFileModel(in);
        }

        @Override
        public NetFileModel[] newArray(int size) {
            return new NetFileModel[size];
        }
    };

    public String getSourcePath() {
        return sourcePath == null ? "" : sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getSmallPath() {
        return smallPath == null ? "" : smallPath;
    }

    public void setSmallPath(String smallPath) {
        this.smallPath = smallPath;
    }

    public String getBigPath() {
        return bigPath == null ? "" : bigPath;
    }

    public void setBigPath(String bigPath) {
        this.bigPath = bigPath;
    }

    public String getFileName() {
        return fileName == null ? "" : fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExt() {
        return fileExt == null ? "" : fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFullUrl() {
        return fullUrl == null ? "" : fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public Integer getSourceFileLength() {
        return sourceFileLength == null ? 0 : sourceFileLength;
    }

    public void setSourceFileLength(Integer sourceFileLength) {
        this.sourceFileLength = sourceFileLength;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    @Override
    public MultipleFileModel getMultipleFileModel() {
        return new MultipleFileModel(getSourcePath(), false);
    }
}
