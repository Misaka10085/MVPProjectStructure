package com.misakanetwork.lib_common.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.entity
 * class name：VersionBean
 * desc：版本更新bean
 */
public class VersionBean implements Parcelable {
    private String id;
    private String updateTime;
    private String createTime;
    private String name;
    private String code;
    private String url;
    private String description;
    private String file;
    private Integer deviceType;
    private Integer isUpdate = 0; // 是否强制更新 0 否， 1 是
    private boolean forceUpdate;

    public VersionBean() {
    }

    protected VersionBean(Parcel in) {
        id = in.readString();
        updateTime = in.readString();
        createTime = in.readString();
        name = in.readString();
        code = in.readString();
        url = in.readString();
        description = in.readString();
        file = in.readString();
        if (in.readByte() == 0) {
            deviceType = null;
        } else {
            deviceType = in.readInt();
        }
        if (in.readByte() == 0) {
            isUpdate = null;
        } else {
            isUpdate = in.readInt();
        }
        forceUpdate = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(updateTime);
        dest.writeString(createTime);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(url);
        dest.writeString(description);
        dest.writeString(file);
        if (deviceType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(deviceType);
        }
        if (isUpdate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isUpdate);
        }
        dest.writeByte((byte) (forceUpdate ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VersionBean> CREATOR = new Creator<VersionBean>() {
        @Override
        public VersionBean createFromParcel(Parcel in) {
            return new VersionBean(in);
        }

        @Override
        public VersionBean[] newArray(int size) {
            return new VersionBean[size];
        }
    };

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdateTime() {
        return updateTime == null ? "" : updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code == null ? "0" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file == null ? "" : file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Integer getDeviceType() {
        return deviceType == null ? -1 : deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getIsUpdate() {
        return isUpdate == null ? 0 : isUpdate;
    }

    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
}
