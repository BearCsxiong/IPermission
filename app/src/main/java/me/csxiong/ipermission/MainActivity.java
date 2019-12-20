package me.csxiong.ipermission;

import android.Manifest;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionValues;
import android.widget.TextView;

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

//        new IPermission(this)
        new IPermission(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(Manifest.permission.CAMERA)
                .request(Manifest.permission.CALL_PHONE)
                .excuteEach(new PermissionResultCallBack() {
                    @Override
                    public void onPermissionResult(List<PermissionResult> results) {
                        for (PermissionResult permissionResult : results) {
                            if (permissionResult.getPermission().equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                mTvWrite.setText((permissionResult.isSuccess() ? "完成!" : "失败!") + "读写权限获取");
                            } else if (permissionResult.getPermission().equals(Manifest.permission.CAMERA)) {
                                mTvCamera.setText((permissionResult.isSuccess() ? "完成!" : "失败!") + "相机权限获取");
                            } else if (permissionResult.getPermission().equals(Manifest.permission.CALL_PHONE)) {
                                mTvPhone.setText((permissionResult.isSuccess() ? "完成!" : "失败!") + "电话权限获取");
//                                Preconditions.checkNotNull(null);
                            }
                        }
                    }
                });
    }


}
