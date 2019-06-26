package me.csxiong.ipermission;

import java.util.List;

/**
 * easy change the strategy to hold result
 */
public abstract class EnsureAllPermissionCallBack implements PermissionResultCallBack {

    @Override
    public void onPermissionResult(List<PermissionResult> results) {
        for (PermissionResult permissionResult : results) {
            if (!permissionResult.isSuccess()) {
                onAllPermissionEnable(false);
                return;
            }
        }
        onAllPermissionEnable(true);
    }

    public abstract void onAllPermissionEnable(boolean isEnable);
}
