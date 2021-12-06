package com.misakanetwork.lib_common.utils.xutils;

import android.database.Cursor;

import org.xutils.db.converter.ColumnConverter;
import org.xutils.db.sqlite.ColumnDbType;

/**
 * Created By：Misaka10085
 * on：2021/7/7
 * package：com.misakanetwork.lib_common.utils.xutils
 * class name：DownloadStateConverter
 * desc：DownloadStateConverter
 */
public class DownloadStateConverter implements ColumnConverter<DownloadState> {

    @Override
    public DownloadState getFieldValue(Cursor cursor, int index) {
        int dbValue = cursor.getInt(index);
        return DownloadState.valueOf(dbValue);
    }

    @Override
    public Object fieldValue2DbValue(DownloadState fieldValue) {
        return fieldValue.value();
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}

