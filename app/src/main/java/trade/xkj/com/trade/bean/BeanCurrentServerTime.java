package trade.xkj.com.trade.bean;

import android.util.Log;

import trade.xkj.com.trade.utils.SystemUtil;

/**
 * Created by huangsc on 2016-11-11.
 */

public class BeanCurrentServerTime {
    static long serverTime;
    static long nativeTime;
    long currentNativeTime;
    public static BeanCurrentServerTime instance;

    public static synchronized BeanCurrentServerTime getInstance(long currentServerTime) {
        if (instance == null) {
            instance = new BeanCurrentServerTime(currentServerTime);
        }else{
            serverTime=currentServerTime;
            nativeTime=System.currentTimeMillis();
        }
        return instance;
    }


    private BeanCurrentServerTime(long currentServerTime) {
        this.nativeTime = System.currentTimeMillis();
        serverTime=currentServerTime;
    }

    /**
     * 计算服务器当前的时间
     *
     * @return
     */
    public long getCurrentServerTime() {
        currentNativeTime = System.currentTimeMillis();
        Log.i(SystemUtil.getTAG(this), "currentNativeTime: " + currentNativeTime);
        return serverTime + (currentNativeTime - nativeTime);
    }

    public long getOldServerTime() {
        return serverTime;
    }

    public long getDiffTime() {
        return serverTime - nativeTime;
    }


}
