package me.csxiong.ipermission;

import android.Manifest;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new IPermission(this)
//                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .excute(new PermissionResultCallBack() {
//                    @Override
//                    public void onPermissionResult(List<PermissionResult> results) {
//                        for (PermissionResult permissionResult : results) {
//                            if (permissionResult.getPermission().equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                                Toast.makeText(getApplicationContext(), (permissionResult.isSuccess() ? "success" : "fail") + " by reuqest" + permissionResult.getPermission(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                });

        new IPermission(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(Manifest.permission.CAMERA)
                .request(Manifest.permission.CALL_PHONE)
                .excute(new EnsureAllPermissionCallBack() {
                    @Override
                    public void onAllPermissionEnable(boolean isEnable) {
                        if (isEnable) {
                            Toast.makeText(getApplicationContext(), "thank you", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "we need all permission", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
