package trade.xkj.com.trade.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import trade.xkj.com.trade.base.BaseActivity;
import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.AesEncryptionUtil;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.Utils.ToashUtil;
import trade.xkj.com.trade.Utils.view.LoadingDialog;
import trade.xkj.com.trade.bean.BeanUserLoginData;
import trade.xkj.com.trade.mvp.main_trade.v.MainTradeContentActivity;

public class UserLoginActivity extends BaseActivity implements UserLoginActivityInterface,View.OnClickListener{

    private EditText etUserName;
    private EditText etUserPassWord;
    private Button bEnter;
    private UserLoginPresenter mUserLoginPresenter;
//    private Dialog mLoadingDialog;
    public LoadingDialog mLoadingDialog;
    private String TAG= SystemUtil.getTAG(this);
    @Override
    public void toMainActivity() {
        startActivity(new Intent(this, MainTradeContentActivity.class));
        finish();
    }

    @Override
    public void showFaidPromt(UserLoginModelImpl.ResultEnum resultEnum) {
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

    public static int[] scrren;
    @Override
    public void initRegister() {
    }
    @Override
    public void initData() {
        mUserLoginPresenter=new UserLoginPresenter(this);
       scrren = SystemUtil.getScrren(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_login_button:
                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    mUserLoginPresenter.login(new BeanUserLoginData(Integer.valueOf(etUserName.getText().toString()), AesEncryptionUtil.encrypt(etUserPassWord.getText().toString())));
                    }
                }).start();
        }
    }


    @Override
    public void initView() {
        etUserName=(EditText)findViewById(R.id.et_login_name);
        etUserPassWord=(EditText)findViewById(R.id.et_login_password);
        bEnter=(Button)findViewById(R.id.b_login_button);
        bEnter.setOnClickListener(this);
        etUserName.setText("83047938");
        etUserPassWord.setText("abcd8888");
        mLoadingDialog=new LoadingDialog(this,"请稍等");
        SystemUtil.setTranslucentForImage(this);
    }
    @Override
    public void hintLoading() {
        if(mLoadingDialog!=null){
            mLoadingDialog.close();
        }

    }

    @Override
    public void showLoading() {
        Log.i(TAG, "showLoading: 开始加载");
        if(mLoadingDialog==null){
            mLoadingDialog=new LoadingDialog(this,"请稍等");
        }
        mLoadingDialog.show();

    }
}
