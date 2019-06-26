package me.csxiong.ipermission;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Arrays;

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

    private ArrayList<String> successPermissions = new ArrayList<>();

    private PermissionResultCallBack permissionResultCallBack;

    private boolean isNeedRequestOneByOne = false;

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

    protected void setNeedRequestOneByOne(boolean isNeedRequestOneByOne) {
        this.isNeedRequestOneByOne = isNeedRequestOneByOne;
    }

    /**
     * prepare permission
     *
     * @param permission the target permission
     */
    protected void preparePermission(String permission) {
        Preconditions.checkNotNull(permission);
        //exclude samle permission
        if (successPermissions.contains(permission) || requestPermissions.contains(permission)) {
            //all permission array exist same to request permission array
            return;
        }
        //select the permission denied
        if (!isGanted(permission)) {
            requestPermissions.add(permission);
            return;
        }

        //select the permission revoked
        if (!isRevokedByPolicy(permission)) {
            requestPermissions.add(permission);
            return;
        }

        //success permission
        successPermissions.add(permission);
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void requestPermission() {
        if (requestPermissions.isEmpty()) {
            return;
        }
        if (isNeedRequestOneByOne) {
            //bugfix: oneByOne mode we need send granted permission result before we request another
            excuteSuccessPermission();
            requestPermission(requestPermissions.remove(0));
        } else {
            requestPermission(requestPermissions.toArray(new String[requestPermissions.size()]));
            requestPermissions.clear();
        }
    }

    private void requestPermission(String... permissions) {
        FragmentActivity activity = getActivity();
        Preconditions.checkNotNull(activity);
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
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
                    permissionResults.add(new PermissionResult(requestedPermission, grantedCode == PackageManager.PERMISSION_GRANTED));
                }

                //2.diff strategy diff next
                if (isNeedRequestOneByOne) {
                    permissionResultCallBack.onPermissionResult(permissionResults);
                    if (hasNext()) {
                        requestPermission();
                        return;
                    }
                } else {
                    //3.rest permissions is successful at first
                    for (String successPermission : successPermissions) {
                        permissionResults.add(new PermissionResult(successPermission, true));
                    }
                    permissionResultCallBack.onPermissionResult(permissionResults);
                }
                clear();
            }
        }
    }

    /**
     * send success permission
     */
    private void excuteSuccessPermission() {
        if (permissionResultCallBack != null) {
            ArrayList<PermissionResult> permissionResults = new ArrayList<>();
            for (String permission : successPermissions) {
                if (isNeedRequestOneByOne) {
                    permissionResults.clear();
                    permissionResults.add(new PermissionResult(permission, true));
                    permissionResultCallBack.onPermissionResult(permissionResults);
                } else {
                    permissionResults.add(new PermissionResult(permission, true));
                }
            }

            if (!isNeedRequestOneByOne) {
                permissionResultCallBack.onPermissionResult(permissionResults);
            }
            successPermissions.clear();
        }
    }

    private boolean hasNext() {
        return !requestPermissions.isEmpty();
    }

    public void clear() {
        //to be safe,clear the call back,using once;
        permissionResultCallBack = null;
        successPermissions.clear();
        requestPermissions.clear();
    }
}
