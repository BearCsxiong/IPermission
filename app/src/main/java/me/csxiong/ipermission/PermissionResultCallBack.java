package me.csxiong.ipermission;

import android.support.annotation.UiThread;

import java.util.List;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : permission result call back
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/6/26 created by csxiong
 * |--------------------------------------------------------------------------------
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
     * @param requestList the list of permissions
     */
    @UiThread
    void onPreRequest(List<String> requestList);
}
