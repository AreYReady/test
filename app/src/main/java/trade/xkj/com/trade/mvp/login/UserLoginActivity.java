package trade.xkj.com.trade.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;
import java.util.TreeMap;

import trade.xkj.com.trade.IO.Volley.SingleVolleyRequestQueue;
import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.AesEncryptionUtil;
import trade.xkj.com.trade.Utils.DateUtils;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.Utils.ToashUtil;
import trade.xkj.com.trade.Utils.view.LoadingDialog;
import trade.xkj.com.trade.base.BaseActivity;
import trade.xkj.com.trade.base.MyApplication;
import trade.xkj.com.trade.bean.BeanUserLoginData;
import trade.xkj.com.trade.constant.ServerIP;
import trade.xkj.com.trade.mvp.main_trade.v.MainTradeContentActivity;

import static android.util.Log.i;

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
//        sendDataOfVolley();
    }
    private void sendDataOfVolley() {
        final String s= DateUtils.getShowTime(System.currentTimeMillis()-10000);
        i(TAG, "sendDataOfVolley: time"+ s);
        SingleVolleyRequestQueue queue=SingleVolleyRequestQueue.getInstance(MyApplication.getInstance().getApplicationContext());
        StringRequest requetTime=new StringRequest(ServerIP.URL_SERVICE_TIME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        String name = AesEncryptionUtil.stringBase64toString(etUserName.getText().toString());
        String pass = AesEncryptionUtil.stringBase64toString(etUserPassWord.getText().toString());
        final TreeMap<String,String> map=new TreeMap<>();
        map.put("account",name);
        map.put("apiid","crm1");
        map.put("apitime",s);
        map.put("password",pass);
        String md5;
        md5=AesEncryptionUtil.getMD5(AesEncryptionUtil.getUrl(ServerIP.URL_LOGIN,map).concat("v66YKULHFld2JElhm"));
        Log.i(TAG, "sendDataOfVolley: md5解析前 "+AesEncryptionUtil.getUrl(ServerIP.URL_LOGIN,map).concat("v66YKULHFld2JElhm"));
        map.put("apisign",md5);
        String url=AesEncryptionUtil.getUrl(ServerIP.URL_LOGIN,map);
        StringRequest request=new StringRequest(Request.Method.POST,ServerIP.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                i(TAG, "onResponse: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                i(TAG, "onErrorResponse: "+error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        Log.i(TAG, "sendDataOfVolley: "+request.getUrl());
//        queue.addToRequestQueue(requetTime);
        queue.addToRequestQueue(request);
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
        etUserName.setText("83047938");
//        etUserName.setText("10001");
//        etUserPassWord.setText("123456a");
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
