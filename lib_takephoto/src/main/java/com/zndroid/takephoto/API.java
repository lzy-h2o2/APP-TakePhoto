package com.zndroid.takephoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @name:API
 * @author:lazy
 * @email:luzhenyuxfcy@sina.com
 * @date : 2020/4/8 16:39
 * @version:
 * @description:TODO
 */
public abstract class API<T> implements LifecycleCallBack {
    public abstract T getDefault(Activity activity, CallBack callBack);
    protected abstract String getTag();

    protected void showLog(String msg) {
        if (BuildConfig.DEBUG)
            Log.i(getTag(), msg);
    }

    protected void showError(String msg) {
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
}
