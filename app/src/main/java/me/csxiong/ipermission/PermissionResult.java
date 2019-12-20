package me.csxiong.ipermission;

/**
 * @Desc : the decription of permission result
 * @Author : csxiong - 2019-12-20
 */
public class PermissionResult {

    /**
     * target
     */
    private String permission;

    /**
     * success or fail
     */
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
