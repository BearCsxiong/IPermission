package me.csxiong.ipermission;

public class Preconditions {

    public static void checkNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException("check no null!");
        }
    }
}
