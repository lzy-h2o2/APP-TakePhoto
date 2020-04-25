package com.zndroid.takephoto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;

/**
 * @name:TakePhotoAPI
 * @author:lazy
 * @email:luzhenyuxfcy@sina.com
 * @date : 2020/4/8 22:52
 * @version:
 * @description:TODO
 */
public class TakePhotoAPI extends API<TakePhotoAPI> {
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private CallBack callBack;

    @Override
    public TakePhotoAPI getDefault(Activity activity, CallBack callBack) {
        this.callBack = callBack;

        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(callBack).bind(new TakePhotoImpl(activity, callBack));
        }
        return this;
    }

    @Override
    protected String getTag() {
        return "TakePhotoAPI";
    }

    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam, Activity activity) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(activity), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /**
     * 获取TakePhoto主入口
     * */
    public TakePhoto getTakePhoto() {
        return takePhoto;
    }

    /**
     * 打开相册
     * */
    public void openGallery() {
        try {
            showLog("openGallery");
            openSysGallery();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * 打开相册支持多选
     * @param limit 最多支持选择张数
     * */
    public void openGallery(int limit) {
        try {
            showLog("openGallery limit=" + limit);
            openSysGallery(limit);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * 打开相机并拍照
     * */
    public void openCamera() {
        try {
            showLog("openCamera");
            openSysCamera();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * 打开相机并拍照，支持裁剪
     * */
    public void openCameraWithCrop(CropOptions cropOptions) {
        try {
            showLog("openCameraWithCrop");
            openSysCameraWithCrop(cropOptions);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void configCompress(TakePhoto takePhoto) {
        int maxSize = Integer.parseInt("10240");
        int width = Integer.parseInt("400");
        int height = Integer.parseInt("400");
        boolean showProgressBar = true;
        boolean enableRawFile = false;
        CompressConfig config = new CompressConfig.Builder().setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    private void openSysGallery() {
        if (takePhoto == null) showError("'takePhoto is null'");

        configCompress(takePhoto);
        takePhoto.onPickFromGallery();
    }

    private void openSysGallery(int limit) {
        if (takePhoto == null) showError("'takePhoto is null'");

        configCompress(takePhoto);
        if (limit <= 0) limit = 1;
        takePhoto.onPickMultiple(limit);
    }

    /**
     * 打开系统相机
     */
    private void openSysCamera() {
        if (takePhoto == null) showError("'takePhoto is null'");

        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        configCompress(takePhoto);
        takePhoto.onPickFromCapture(imageUri);
    }

    private void openSysCameraWithCrop(CropOptions cropOptions) {
        if (takePhoto == null || cropOptions == null) showError("'takePhoto is null'");

        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        configCompress(takePhoto);

        takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (takePhoto != null)
            takePhoto.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (takePhoto != null)
            takePhoto.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (null != callBack)
            PermissionManager.handlePermissionsResult(activity, type, invokeParam, callBack);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (takePhoto != null)
            takePhoto.onActivityResult(requestCode, resultCode, data);
    }
}
