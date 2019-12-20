package me.csxiong.ipermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.lang.ref.WeakReference;

/**
 * @Desc : simple permission executor
 * @Author : csxiong - 2019-12-20
 */
public class IPermission {

    private final String PERMISSION_TAG = "delegate_permission_tag";

    private WeakReference<IPermissionDelegateFragment> mPermissionDelegate;

    public IPermission(FragmentActivity activity) {
        Preconditions.checkNotNull(activity);
        mPermissionDelegate = generatePermissionDelegateFragment(activity.getSupportFragmentManager());
    }

    public IPermission(Fragment fragment) {
        Preconditions.checkNotNull(fragment);
        mPermissionDelegate = generatePermissionDelegateFragment(fragment.getChildFragmentManager());
    }

    public static boolean isGanted(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    /**
     * create a delegate fragment
     *
     * @param fragmentManager
     * @return
     */
    private WeakReference<IPermissionDelegateFragment> generatePermissionDelegateFragment(
            FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return null;
        }
        Fragment fg = fragmentManager.findFragmentByTag(PERMISSION_TAG);
        boolean needCreate = fg == null;
        if (needCreate) {
            fg = new IPermissionDelegateFragment();
            fragmentManager.beginTransaction().add(fg, PERMISSION_TAG).commitNowAllowingStateLoss();
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
        if (mPermissionDelegate != null) {
            for (String permission : permissions) {
                mPermissionDelegate.get().preparePermission(permission);
            }
        }
        return this;
    }

    /**
     * excute permission
     * means receive result one time
     *
     * @param permissionResultCallBack
     */
    public void excute(PermissionResultCallBack permissionResultCallBack) {
        if (mPermissionDelegate != null) {
            mPermissionDelegate.get().setNeedRequestOneByOne(false);
            mPermissionDelegate.get().setPermissionResultCallBack(permissionResultCallBack);
            mPermissionDelegate.get().requestPermission();
        }
    }

    /**
     * excute permission request one by one
     * means receive result one by one
     *
     * @param permissionResultCallBack
     */
    public void excuteEach(PermissionResultCallBack permissionResultCallBack) {
        if (mPermissionDelegate != null) {
            mPermissionDelegate.get().setNeedRequestOneByOne(true);
            mPermissionDelegate.get().setPermissionResultCallBack(permissionResultCallBack);
            mPermissionDelegate.get().requestPermission();
        }
    }

    /**
     * 部分修改系统权限在6.0之后被拒绝 需要申请
     * https://stackoverflow.com/questions/32971846/settings-system-api-behaviour-in-android-api-level-23
     * https://www.jianshu.com/p/304600727c56
     *
     * @param context
     */
    public static void requestSettingsSystemPermission(Context context) {
        if (Build.VERSION.SDK_INT > 23 && !Settings.System.canWrite(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
