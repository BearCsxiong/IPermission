package me.csxiong.ipermission;

import android.support.annotation.UiThread;

import java.util.List;

/**
 * @Desc : permission result call back
 * @Author : csxiong - 2019-12-20
 */
public interface PermissionResultCallBack {

    /**
     * call back after permission request
     *
     * @param results
     */
    void onPermissionResult(List<PermissionResult> results);

    /**
     * call back for pre request permission
     *
     * @param requestList the list of permissions
     */
    @UiThread
    void onPreRequest(List<String> requestList);
}
