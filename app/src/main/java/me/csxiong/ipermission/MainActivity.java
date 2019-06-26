package me.csxiong.ipermission;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : sampler permission testing A
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/6/26 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class MainActivity extends FragmentActivity {

    TextView mTvWrite;

    TextView mTvCamera;

    TextView mTvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvWrite = findViewById(R.id.tv_write);
        mTvCamera = findViewById(R.id.tv_camera);
        mTvPhone = findViewById(R.id.tv_phone);

        new IPermission(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(Manifest.permission.CAMERA)
                .request(Manifest.permission.CALL_PHONE)
                .excute(new PermissionResultCallBack() {
                    @Override
                    public void onPermissionResult(List<PermissionResult> results) {
                        for (PermissionResult permissionResult : results) {
                            if (permissionResult.getPermission().equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                mTvWrite.setText((permissionResult.isSuccess() ? "完成!" : "失败!") + "读写权限获取");
                            } else if (permissionResult.getPermission().equals(Manifest.permission.CAMERA)) {
                                mTvCamera.setText((permissionResult.isSuccess() ? "完成!" : "失败!") + "相机权限获取");
                            } else if (permissionResult.getPermission().equals(Manifest.permission.CALL_PHONE)) {
                                mTvPhone.setText((permissionResult.isSuccess() ? "完成!" : "失败!") + "电话权限获取");
                            }
                        }
                    }
                });
    }

    public void show(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
