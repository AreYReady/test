package trade.xkj.com.trade.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import trade.xkj.com.trade.Base.BaseActivity;
import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.ToashUtil;
import trade.xkj.com.trade.bean.BeanUserLoginData;
import trade.xkj.com.trade.mvp.MainActivity;

public class UserLoginActivity extends BaseActivity implements UserLoginActivityInterface,View.OnClickListener{

    private EditText etUserName;
    private EditText etUserPassWord;
    private Button bEnter;
    private UserLoginPresenter mUserLoginPresenter;
    @Override
    public void cleanPassword() {
        etUserPassWord.setText("");
    }

    @Override
    public void cleanUserName() {
        etUserName.setText("");
    }

    @Override
    public void toMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showFaidPromt() {
        ToashUtil.showShort(this,"登入有问题");
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initRegister() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        mUserLoginPresenter=new UserLoginPresenter(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_login_button:
                mUserLoginPresenter.login(new BeanUserLoginData(Integer.valueOf(etUserName.getText().toString()),etUserPassWord.getText().toString()));
        }
    }


    @Override
    public void initView() {
        etUserName=(EditText)findViewById(R.id.et_login_name);
        etUserPassWord=(EditText)findViewById(R.id.et_login_password);
        bEnter=(Button)findViewById(R.id.b_login_button);
        bEnter.setOnClickListener(this);
    }

}
