package trade.xkj.com.trade.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import trade.xkj.com.trade.Utils.SystemUtil;

/**
 * Created by admin on 2016-11-23.
 * TODO:所有的fragment都需要继承的父类
 */

public  abstract class BaseFragment extends Fragment {
    protected View view;
    protected Context context;
    protected Handler mHandler;
    protected final String TAG= SystemUtil.getTAG(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view.setClickable(true);
        initData();
        initView();
    }

    protected abstract void initView();

    protected abstract void initData();
    public Boolean onBackPressed(){
        return false;
    }
    @Subscribe(sticky = true)
    public void getHander(Handler handler){
        Log.i(TAG, "getHandler: ");
        mHandler=handler;
    }
    public interface BackInterface {
          void setSelectedFragment(BaseFragment selectedFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
