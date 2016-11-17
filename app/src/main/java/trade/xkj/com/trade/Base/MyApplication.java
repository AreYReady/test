package trade.xkj.com.trade.Base;

import android.app.Application;

/**
 * Created by admin on 2016-11-17.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    public static MyApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }
}
