package me.csxiong.ipermission;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : hold permission result
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/6/26 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class PermissionResult {

    private String permission;

    private boolean isSuccess;

    public PermissionResult(String permission, boolean isSuccess) {
        this.permission = permission;
        this.isSuccess = isSuccess;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString() {
        return "PermissionResult{" +
                "permission='" + permission + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
