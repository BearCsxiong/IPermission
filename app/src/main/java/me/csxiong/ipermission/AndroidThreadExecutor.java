package me.csxiong.ipermission;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : simple thread ex tools
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/6/26 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class AndroidThreadExecutor {

    private Handler mUIHandler;

    private ThreadPoolExecutor mBackgroundExecutor;

    private BlockingQueue<Runnable> mBackgroundTaskQueue;

    private static AndroidThreadExecutor instance;

    private AndroidThreadExecutor() {
        mUIHandler = new Handler(Looper.getMainLooper());
        mBackgroundTaskQueue = new LinkedBlockingDeque<>();
        mBackgroundExecutor = new ThreadPoolExecutor(0, 20, 60, TimeUnit.SECONDS, mBackgroundTaskQueue);
    }

    public static AndroidThreadExecutor getInstance() {
        synchronized (AndroidThreadExecutor.class) {
            if (instance == null) {
                instance = new AndroidThreadExecutor();
            }
            return instance;
        }
    }

    /**
     * return true means put messageQueue successful
     *
     * @param runnable
     * @return
     */
    public boolean runOnUiThread(Runnable runnable) {
        return mUIHandler.post(runnable);
    }

    /**
     * runnable excuted on background
     *
     * @param runnable
     */
    public void runOnBackground(Runnable runnable) {
        mBackgroundExecutor.execute(runnable);
    }


}
