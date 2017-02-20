package com.xkj.trade.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xkj.trade.IO.okhttp.ChatWebSocket;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseActivity;
import com.xkj.trade.bean.BeanUserLoginData;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.mvp.main_trade.activity.v.MainTradeContentActivity;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.ToashUtil;
import com.xkj.trade.utils.view.LoadingDialog;

import static android.util.Log.i;


public class UserLoginActivity extends BaseActivity implements UserLoginActivityInterface,View.OnClickListener{

    private EditText etUserName;
    private EditText etUserPassWord;
    private Button bEnter;
    private UserLoginPresenter mUserLoginPresenter;
//    private Dialog mLoadingDialog;
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
                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    mUserLoginPresenter.login(new BeanUserLoginData(etUserName.getText().toString(), etUserPassWord.getText().toString()));
                    }
                }).start();
        }
//        sendDataOfVolley();
    }
    private void sendDataOfVolley() {
//        final String s= DateUtils.getShowTime(System.currentTimeMillis()-10000);
//        i(TAG, "sendDataOfVolley: time"+ s);
//        SingleVolleyRequestQueue queue=SingleVolleyRequestQueue.getInstance(MyApplication.getInstance().getApplicationContext());
//        StringRequest requetTime=new StringRequest(URL_SERVICE_TIME, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                i(TAG, "onResponse: "+response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        queue.addToRequestQueue(requetTime);
//        String name = AesEncryptionUtil.stringBase64toString(etUserName.getText().toString());
//        String pass = AesEncryptionUtil.stringBase64toString(etUserPassWord.getText().toString());
//        final TreeMap<String,String> map=new TreeMap<>();
//        map.put("account",name);
//        map.put("apiid","crm1");
//        map.put("apitime",s);
//        map.put("password",pass);
//        String md5;
//        md5=AesEncryptionUtil.getMD5(AesEncryptionUtil.getUrl(URL_LOGIN,map).concat("v66YKULHFld2JElhm"));
//        i(TAG, "sendDataOfVolley: md5解析前 "+AesEncryptionUtil.getUrl(URL_LOGIN,map).concat("v66YKULHFld2JElhm"));
//        map.put("apisign",md5);
////        String url=AesEncryptionUtil.getUrl(URL_LOGIN,map);
//        StringRequest request=new StringRequest(Request.Method.POST,URL_LOGIN, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                i(TAG, "onResponse: "+response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                i(TAG, "onErrorResponse: "+error.toString());
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return map;
//            }
//        };
//        i(TAG, "sendDataOfVolley: "+request.getUrl());
////        queue.addToRequestQueue(requetTime);
//        queue.addToRequestQueue(request);
    }

    @Override
    public void initView() {
//        Log.i(TAG, "initView: 1xdp="+getResources().getDimension(R.dimen.x1));
//        Log.i(TAG, "initView: 1ydp="+getResources().getDimension(R.dimen.y1));
//        WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        int screenWidth = screenWidth = display.getWidth();
//        int screenHeight = screenHeight = display.getHeight();
//        Log.i(TAG, "initView: screenWidth="+screenWidth);
//        Log.i(TAG, "initView: screenHeight="+screenHeight);
        etUserName=(EditText)findViewById(R.id.et_login_name);
        etUserPassWord=(EditText)findViewById(R.id.et_login_password);
        bEnter=(Button)findViewById(R.id.b_login_button);
        bEnter.setOnClickListener(this);
//        etUserName.setText("83047938");
        etUserName.setText("10001");
        etUserPassWord.setText("123456a");
//        etUserPassWord.setText("abcd8888");

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
