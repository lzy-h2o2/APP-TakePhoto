package com.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zndroid.takephoto.CallBack;
import com.zndroid.takephoto.TakePhotoAPI;

import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.PermissionManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private TakePhotoAPI api;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.im_show);

        api = new TakePhotoAPI();
        api.getDefault(this, new CallBack() {
            @Override
            public void takeSuccess(final TResult result) {
                Toast.makeText(MainActivity.this, "成功选择了" + result.getImages().size() + "张", Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InputStream inputStream = null;
                        try {
                            inputStream = new FileInputStream(result.getImage().getCompressPath());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imageView.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void takeFail(TResult result, String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void takeCancel() {
                Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_LONG).show();
            }

            @Override
            public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
                return api.invoke(invokeParam, MainActivity.this);
            }
        });
        api.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        api.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        api.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void take_photo(View view) {
        if (api != null)
            api.openCamera();
    }

    public void open_gallery(View view) {
        if (api != null)
            api.openGallery();
    }

    public void open_gallery_mux(View view) {
        if (api != null)
            api.openGallery(3);
    }

    public void take_photo_with_crop(View view) {
        if (api != null) {
            CropOptions cropOptions = new CropOptions.Builder()
                    .setWithOwnCrop(true)
                    .create();
            api.openCameraWithCrop(cropOptions);
        }
    }
}
