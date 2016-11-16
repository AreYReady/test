package trade.xkj.com.trade.mvp.login;

import android.os.Bundle;
import android.widget.EditText;

import trade.xkj.com.trade.BaseActivity;
import trade.xkj.com.trade.R;

public class LoginActivity extends BaseActivity {

    private EditText etUserName;
    private EditText etUserPassWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        etUserName=(EditText)findViewById(R.id.et_login_name);
        etUserPassWord=(EditText)findViewById(R.id.et_login_password);

    }
}
