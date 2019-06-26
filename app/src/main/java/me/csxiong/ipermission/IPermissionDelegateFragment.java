package me.csxiong.ipermission;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : a delegate for permission request and packing after received
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/6/26 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class IPermissionDelegateFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 200;

    private ArrayList<String> requestPermissions = new ArrayList<>();

    private ArrayList<String> allPermissions = new ArrayList<>();

    private PermissionResultCallBack permissionResultCallBack;

    public void setPermissionResultCallBack(PermissionResultCallBack permissionResultCallBack) {
        this.permissionResultCallBack = permissionResultCallBack;
    }

    public IPermissionDelegateFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * prepare permission
     *
     * @param permission the target permission
     */
    protected void preparePermission(String permission) {
        Preconditions.checkNotNull(permission);
        //exclude samle permission
        if (allPermissions.contains(permission)) {
            //all permission array exist same to request permission array
            return;
        }
        allPermissions.add(permission);

        //select the permission denied
        if (!isGanted(permission)) {
            requestPermissions.add(permission);
            return;
        }

        //select the permission revoked
        if (!isRevokedByPolicy(permission)) {
            requestPermissions.add(permission);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void requestPermission() {
        FragmentActivity activity = getActivity();
        Preconditions.checkNotNull(activity);
        requestPermissions(requestPermissions.toArray(new String[requestPermissions.size()]), PERMISSION_REQUEST_CODE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isGanted(String permission) {
        FragmentActivity activity = getActivity();
        Preconditions.checkNotNull(activity);
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isRevokedByPolicy(String permission) {
        FragmentActivity activity = getActivity();
        Preconditions.checkNotNull(activity);
        return activity.getPackageManager().isPermissionRevokedByPolicy(permission, activity.getPackageName());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //receive permission result after policy selected
        //to be safe
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissionResultCallBack != null) {
                ArrayList<PermissionResult> permissionResults = new ArrayList<>();

                //1.remove deal permission and remove it on all permissions
                for (int i = 0; i < permissions.length; i++) {
                    String requestedPermission = permissions[i];
                    int grantedCode = grantResults[i];
                    //remove all add Permission list
                    allPermissions.remove(requestedPermission);
                    permissionResults.add(new PermissionResult(requestedPermission, grantedCode == PackageManager.PERMISSION_GRANTED));
                }

                //2.rest permissions is successful at first
                for (String successPermission : allPermissions) {
                    permissionResults.add(new PermissionResult(successPermission, true));
                }

                permissionResultCallBack.onPermissionResult(permissionResults);
                //to be safe,clear the call back,using once;
                permissionResultCallBack = null;
                clear();
            }
        }
    }

    public void clear() {
        allPermissions.clear();
        requestPermissions.clear();
    }
}
