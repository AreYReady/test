package com.xkj.trade.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xkj.trade.IO.okhttp.ChatWebSocket;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseActivity;
import com.xkj.trade.bean.BeanUserLoginData;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.mvp.main_trade.activity.v.MainTradeContentActivity;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.NetUtil;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.ToashUtil;
import com.xkj.trade.utils.view.LoadingDialog;

import static android.util.Log.i;


public class UserLoginActivity extends BaseActivity implements UserLoginActivityInterface,View.OnClickListener{

    private EditText etUserName;
    private EditText etUserPassWord;
    private Button bEnter;
    private UserLoginPresenter mUserLoginPresenter;
    public LoadingDialog mLoadingDialog;
    private String TAG= SystemUtil.getTAG(this);
    private Handler Handler=new Handler(){

    };
    @Override
    public void toMainActivity() {
        ACache.get(this).put(RequestConstant.ACCOUNT,etUserName.getText().toString());
        ACache.get(this).put(RequestConstant.LOGIN_PASSWORD,etUserPassWord.getText().toString());
        ChatWebSocket.getChartWebSocket();
        startActivity(new Intent(this, MainTradeContentActivity.class));
        finish();
    }

    @Override
    public void showFaidPromt(UserLoginModelImpl.ResultEnum resultEnum) {
        hintLoading();
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
    public void initData() {
        mUserLoginPresenter=new UserLoginPresenter(this,Handler,getContext());
        scrren = SystemUtil.getScrren(this);
        ACache.get(this).put(RequestConstant.API_ID,"crm1");
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_login_button:

                if(!checkNetStatus()){
                 ToashUtil.show(getContext(),"网咯不可用", Toast.LENGTH_SHORT);
                    return ;
                }
                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    mUserLoginPresenter.login(new BeanUserLoginData(etUserName.getText().toString(), etUserPassWord.getText().toString()));
                    }
                }).start();
        }
    }

    /**
     * 检测网络状态
     */
    private boolean checkNetStatus() {
        return NetUtil.isConnected(this)?true:false;
    }

    @Override
    public void initView() {

        etUserName=(EditText)findViewById(R.id.et_login_name);
        etUserPassWord=(EditText)findViewById(R.id.et_login_password);
        bEnter=(Button)findViewById(R.id.b_login_button);
        bEnter.setOnClickListener(this);
        etUserName.setText("10001");
        etUserPassWord.setText("123456a");
        mLoadingDialog=new LoadingDialog(this,"请稍等");
        SystemUtil.setTranslucentForImage(this);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void hintLoading() {
        if(mLoadingDialog!=null){
            mLoadingDialog.close();
        }

    }

    @Override
    public void showLoading() {
        i(TAG, "showLoading: 开始加载");
        if(mLoadingDialog==null){
            mLoadingDialog=new LoadingDialog(this,"请稍等");
        }
        mLoadingDialog.show();
    }
}
