package me.csxiong.ipermission;

/**
 * @Desc : some pre-conditions check helper
 * @Author : csxiong - 2019-12-20
 */
public class Preconditions {

    public static void checkNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException("check no null!");
        }
    }
}
