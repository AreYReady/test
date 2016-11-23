package trade.xkj.com.trade.Base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import org.greenrobot.eventbus.EventBus;

public  abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRegister();
        initData();
        initView();
        DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
        displayMetrics.scaledDensity=displayMetrics.density;
    }

    public abstract void initRegister();
    public abstract  void initData();
    public abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
