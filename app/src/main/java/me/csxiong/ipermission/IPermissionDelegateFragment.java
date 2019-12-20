package me.csxiong.ipermission;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

/**
 * @Desc : like an ViewModel usage
 * @Author : csxiong - 2019-12-20
 */
public class IPermissionDelegateFragment extends Fragment {

    /**
     * request code for permission
     */
    private static final int PERMISSION_REQUEST_CODE = 200;

    /**
     * arrays of permission
     */
    private ArrayList<String> requestPermissions = new ArrayList<>();

    /**
     * arrays of permission successful
     */
    private ArrayList<String> successPermissions = new ArrayList<>();

    /**
     * result callback
     */
    private PermissionResultCallBack permissionResultCallBack;

    /**
     * the strategy of request steps
     */
    private boolean isNeedRequestOneByOne = false;

    public void setPermissionResultCallBack(PermissionResultCallBack permissionResultCallBack) {
        this.permissionResultCallBack = permissionResultCallBack;
    }

    public IPermissionDelegateFragment() {
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
        // exclude samle permission
        if (successPermissions.contains(permission) || requestPermissions.contains(permission)) {
            // all permission array exist same to request permission array
            return;
        }
        // select the permission denied
        if (!isGanted(permission)) {
            requestPermissions.add(permission);
            return;
        }

        // select the permission revoked
        // if (!isRevokedByPolicy(permission)) {
        // requestPermissions.add(permission);
        // return;
        // }

        // success permission
        successPermissions.add(permission);
    }

    /**
     *  request
     */
    protected void requestPermission() {
        if (requestPermissions.isEmpty()) {
            if (!successPermissions.isEmpty()) {
                ArrayList<PermissionResult> results = new ArrayList<>();
                for (String permission : successPermissions) {
                    results.add(new PermissionResult(permission, true));
                }
                permissionResultCallBack.onPermissionResult(results);
            }
            return;
        }
        // update by csxiong , design for update UI -> enable show permission description
        permissionResultCallBack.onPreRequest(requestPermissions);
        if (isNeedRequestOneByOne) {
            // bugfix: oneByOne mode we need send granted permission result before we request another
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

    private boolean isGanted(String permission) {
        FragmentActivity activity = getActivity();
        Preconditions.checkNotNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // receive permission result after policy selected
        // to be safe
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissionResultCallBack != null) {
                ArrayList<PermissionResult> permissionResults = new ArrayList<>();

                // 1.remove deal permission and remove it on all permissions
                for (int i = 0; i < permissions.length; i++) {
                    String requestedPermission = permissions[i];
                    int grantedCode = grantResults[i];
                    permissionResults.add(
                            new PermissionResult(requestedPermission, grantedCode == PackageManager.PERMISSION_GRANTED));
                }

                // 2.diff strategy diff next
                if (isNeedRequestOneByOne) {
                    permissionResultCallBack.onPermissionResult(permissionResults);
                    if (hasNext()) {
                        requestPermission();
                        return;
                    }
                } else {
                    // 3.rest permissions is successful at first
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
        // to be safe,clear the call back,using once;
        permissionResultCallBack = null;
        successPermissions.clear();
        requestPermissions.clear();
    }
}
