package me.csxiong.ipermission;

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
}
