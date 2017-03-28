package com.xkj.trade.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xkj.trade.IO.okhttp.ChatWebSocket;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean.BeanUserLoginData;
import com.xkj.trade.bean_notification.NotificationSignUp;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.mvp.main_trade.activity.v.MainTradeContentActivity;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.NetUtil;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.ToashUtil;
import com.xkj.trade.utils.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import static android.util.Log.i;

/**
 * Created by huangsc on 2017-03-28.
 * TODO:
 */

public class LoginFragment extends BaseFragment implements UserLoginActivityInterface,View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view=inflater.inflate(R.layout.fragment_login,container,false);
    }

    private EditText etUserName;
    private EditText etUserPassWord;
    private Button bEnter;
    private Button bSignup;
    private UserLoginPresenter mUserLoginPresenter;
    public LoadingDialog mLoadingDialog;
    private String TAG= SystemUtil.getTAG(this);
    private android.os.Handler Handler=new Handler(){

    };
    @Override
    public void toMainActivity() {
        ACache.get(context).put(RequestConstant.ACCOUNT,etUserName.getText().toString());
        ACache.get(context).put(RequestConstant.LOGIN_PASSWORD,etUserPassWord.getText().toString());
        ChatWebSocket.getChartWebSocket();
        startActivity(new Intent(context, MainTradeContentActivity.class));
    }

    @Override
    public void showFaidPromt(UserLoginModelImpl.ResultEnum resultEnum) {
        hintLoading();
        ToashUtil.showShort(context,"登入有问题");
    }



    @Override
    public Context getContext() {
        return context;
    }


    public static int[] scrren;

    @Override
    public void initData() {
        mUserLoginPresenter=new UserLoginPresenter(this,Handler,getContext());
        ACache.get(context).put(RequestConstant.API_ID,"crm1");
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
                break;
            case R.id.b_sign_up:
                EventBus.getDefault().post(new NotificationSignUp());
                break;
        }
    }

    /**
     * 检测网络状态
     */
    private boolean checkNetStatus() {
        return NetUtil.isConnected(context)?true:false;
    }

    @Override
    public void initView() {

        etUserName=(EditText)view.findViewById(R.id.et_login_name);
        etUserPassWord=(EditText)view.findViewById(R.id.et_login_password);
        bEnter=(Button)view.findViewById(R.id.b_login_button);
        bSignup=(Button)view.findViewById(R.id.b_sign_up);
        bEnter.setOnClickListener(this);
        bSignup.setOnClickListener(this);
        etUserName.setText("10001");
        etUserPassWord.setText("123456a");
        mLoadingDialog=new LoadingDialog(context,"请稍等");

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
            mLoadingDialog=new LoadingDialog(context,"请稍等");
        }
        mLoadingDialog.show();
    }
}
