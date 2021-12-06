package com.misakanetwork.lib_common.entity;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.entity
 * class name：MultipleFileModel
 * desc：多文件Model
 */
public class MultipleFileModel {
    private String path;
    private boolean isLocal;

    public MultipleFileModel(String path, boolean isLocal) {
        this.path = path;
        this.isLocal = isLocal;
    }

    public String getPath() {
        return path == null ? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }
}
