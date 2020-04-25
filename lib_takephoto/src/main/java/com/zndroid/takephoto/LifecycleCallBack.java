package com.zndroid.takephoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @name:LifecycleCallBack
 * @author:lazy
 * @email:luzhenyuxfcy@sina.com
 * @date : 2020/4/8 23:14
 * @version:
 * @description:TODO
 */
public interface LifecycleCallBack {
    void onCreate(Bundle savedInstanceState);
    void onSaveInstanceState(Bundle outState);
    void onDestroy();
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
