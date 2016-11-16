package trade.xkj.com.trade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public  abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }
    public abstract  void initData();
    public abstract void initView();
}
