package com.bibizhaoji.bibiji.support;

public class BackSupport {

    private long mLastBackTime;
    private static final long BACK_INTERNAL_TIME = 2000L;

    public boolean canBeBack() {
        boolean result;
        result = !(mLastBackTime == 0 || System.currentTimeMillis() - mLastBackTime > BACK_INTERNAL_TIME);
        mLastBackTime = System.currentTimeMillis();
        return result;
    }
}
