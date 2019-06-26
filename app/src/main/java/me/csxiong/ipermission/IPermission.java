package me.csxiong.ipermission;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.lang.ref.WeakReference;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : simple permission tools
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/6/26 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class IPermission {

    private final String PERMISSION_TAG = "delegate_permission_tag";

    public WeakReference<IPermissionDelegateFragment> mPermissionDelegate;

    public IPermission(FragmentActivity activity) {
        Preconditions.checkNotNull(activity);
        mPermissionDelegate = generatePermissionDelegateFragment(activity.getSupportFragmentManager());
    }

    public IPermission(Fragment fragment) {
        Preconditions.checkNotNull(fragment);
        mPermissionDelegate = generatePermissionDelegateFragment(fragment.getChildFragmentManager());
    }

    /**
     * create a delegate fragment
     *
     * @param fragmentManager
     * @return
     */
    private WeakReference<IPermissionDelegateFragment> generatePermissionDelegateFragment(FragmentManager fragmentManager) {
        Fragment fg = fragmentManager.findFragmentByTag(PERMISSION_TAG);
        boolean needCreate = fg == null;
        if (needCreate) {
            fg = new IPermissionDelegateFragment();
            fragmentManager.beginTransaction()
                    .add(fg, PERMISSION_TAG)
                    .commitNow();
        }
        return new WeakReference<>((IPermissionDelegateFragment) fg);
    }

    /**
     * build permission content
     *
     * @param permissions
     * @return
     */
    public IPermission request(String... permissions) {
        for (String permission : permissions) {
            mPermissionDelegate.get().preparePermission(permission);
        }
        return this;
    }

    /**
     * excute permission
     * means receive result one times
     *
     * @param permissionResultCallBack
     */
    public void excute(PermissionResultCallBack permissionResultCallBack) {
        mPermissionDelegate.get().setNeedRequestOneByOne(false);
        Preconditions.checkNotNull(permissionResultCallBack);
        mPermissionDelegate.get().setPermissionResultCallBack(permissionResultCallBack);
        mPermissionDelegate.get().requestPermission();
    }

    /**
     * excute permission request one by one
     * means receive result one by one
     *
     * @param permissionResultCallBack
     */
    public void excuteOneByOne(PermissionResultCallBack permissionResultCallBack) {
        mPermissionDelegate.get().setNeedRequestOneByOne(true);
        mPermissionDelegate.get().setPermissionResultCallBack(permissionResultCallBack);
        mPermissionDelegate.get().requestPermission();
    }

}
