package trade.xkj.com.trade.Base;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;

import trade.xkj.com.trade.Utils.SystemUtil;

/**
 * Created by admin on 2016-11-23.
 */

public  abstract class BaseFragment extends Fragment {
    protected View view;
    protected Handler mHandler;
    protected final String TAG= SystemUtil.getTAG(this);


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    protected abstract void initView();

    protected abstract void initData();
    @Subscribe(sticky = true)
    public void getHandler(Handler handler){
        Log.i(TAG, "getHandler: ");
        mHandler=handler;
    }
}
