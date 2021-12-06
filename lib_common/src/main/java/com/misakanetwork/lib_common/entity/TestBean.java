package com.misakanetwork.lib_common.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.entity
 * class name：TestBean
 * desc：for test
 */
public class TestBean implements Parcelable {
    private String testString;

    public TestBean(String testString) {
        this.testString = testString;
    }

    protected TestBean(Parcel in) {
        testString = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(testString);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TestBean> CREATOR = new Creator<TestBean>() {
        @Override
        public TestBean createFromParcel(Parcel in) {
            return new TestBean(in);
        }

        @Override
        public TestBean[] newArray(int size) {
            return new TestBean[size];
        }
    };

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }
}
