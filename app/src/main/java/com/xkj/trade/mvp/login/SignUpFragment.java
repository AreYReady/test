package com.xkj.trade.mvp.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean.BeanCurrentServerTime;
import com.xkj.trade.bean_.BeanServerTimeForHttp;
import com.xkj.trade.bean_.BeanSignUpInfo;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.DateUtils;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.xkj.trade.constant.UrlConstant.URL_MT4_REG;
import static com.xkj.trade.constant.UrlConstant.URL_SERVICE_TIME;

/**
 * Created by huangsc on 2017-03-28.
 * TODO:注册
 */

public class SignUpFragment extends BaseFragment implements View.OnClickListener {
    Button mBack;
    Button mSignUp;
    EditText mEmail;
    EditText mLoginName;
    EditText mTelephone;
    String regEx = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
    String regPh ="^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
    TextView mEmailPrompt;
    TextView mLoginNamePrompt;
    TextView mPhonePrompt;
    RadioGroup mLever;
    String mLeverNumble="200";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view=inflater.inflate(R.layout.fragment_sign_up,container,false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mBack=(Button)view.findViewById(R.id.b_sign_up_back);
        mSignUp=(Button)view.findViewById(R.id.b_sign_up_enter);
        mBack.setOnClickListener(this);
        mSignUp.setOnClickListener(this);
        mEmail=(EditText)view.findViewById(R.id.et_sign_up_email);
        mLever=(RadioGroup)view.findViewById(R.id.rg_lever);
        mLever.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_lever50:
                        mLeverNumble="50";
                        break;
                    case R.id.rb_lever100:
                        mLeverNumble="100";
                        break;
                    case R.id.rb_lever200:
                        mLeverNumble="200";
                        break;
                    case R.id.rb_lever300:
                        mLeverNumble="300";
                        break;
                    case R.id.rb_lever400:
                        mLeverNumble="400";
                        break;
                }
            }
        });
        mTelephone=(EditText)view.findViewById(R.id.et_sign_up_telephone);
        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                }else{
                    Pattern pattern = Pattern.compile(regEx);
                    Matcher matcher = pattern.matcher(mEmail.getText().toString());
                    if(!matcher.matches()){
                        Log.i(TAG, "afterTextChanged: 1");
                        mEmailPrompt.setVisibility(View.VISIBLE);
                    }else{
                        Log.i(TAG, "afterTextChanged: 2");
                        mEmailPrompt.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        mTelephone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){}
                else {
                    if(!Pattern.compile(regPh).matcher(mTelephone.getText().toString()).matches()){
                        Log.i(TAG, "afterTextChanged: 1");
                        mPhonePrompt.setVisibility(View.VISIBLE);
                    }else{
                        Log.i(TAG, "afterTextChanged: 2");
                        mPhonePrompt.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        mLoginName=(EditText)view.findViewById(R.id.et_sign_up_name);
        mLoginName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(mLoginName.getText().toString()==null){
                        mLoginNamePrompt.setVisibility(View.VISIBLE);
                    }else{
                        mLoginNamePrompt.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        mLoginNamePrompt=(TextView)view.findViewById(R.id.tv_sign_up_name_prompt);
        mEmailPrompt=(TextView) view.findViewById(R.id.tv_sign_up_email_prompt);
        mPhonePrompt=(TextView) view.findViewById(R.id.tv_sign_up_telephone_prompt);
        mPhonePrompt.setText("你输入的电话号码不符合格式，请重新输入");
        mEmailPrompt.setText("你输入的邮箱不符合格式，请重新输入");
        mLoginNamePrompt.setText("昵称不能为空");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_sign_up_back:
                Log.i(TAG, "onClick: ");
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitNow();
                break;
            case R.id.b_sign_up_enter:
                requestSignUp();
                break;
        }
    }
    private void requestSignUp(){
        //获取时间后，登入
        okhttp3.Request request=new okhttp3.Request.Builder().url(URL_SERVICE_TIME).post(new FormBody.Builder().build()).build();
        OkhttpUtils.enqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + call.request() + e.getMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String s;
                BeanServerTimeForHttp beanServerTimeForHttp = new Gson().fromJson(s = response.body().string(), BeanServerTimeForHttp.class);
                Log.i(TAG, "onResponse:时间 " + s);
                BeanCurrentServerTime.getInstance(DateUtils.getOrderStartTime(beanServerTimeForHttp.getData(), "yyyyMMddHHmmss"));
                Map<String, String> map = new TreeMap<>();
                map.put(RequestConstant.NAME, AesEncryptionUtil.stringBase64toString(mLoginName.getText().toString()));
                map.put(RequestConstant.PHONE, AesEncryptionUtil.stringBase64toString(mTelephone.getText().toString()));
                map.put(RequestConstant.EMAIL, AesEncryptionUtil.stringBase64toString(mEmail.getText().toString()));
                map.put(RequestConstant.LEVERAGE, AesEncryptionUtil.stringBase64toString(mLeverNumble));
                OkhttpUtils.enqueue(URL_MT4_REG, map, new MyCallBack() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final BeanSignUpInfo beanSignUpInfo=new Gson().fromJson(response.body().string(), BeanSignUpInfo.class);
                        Log.i(TAG, "onResponse: 注册" +new Gson().toJson(beanSignUpInfo));
                        if(beanSignUpInfo.getStatus()==1){
                            BeanSignUpInfo.DataBean.Mt4Bean mt4 = beanSignUpInfo.getData().getMt4();
                            title="注册成功";
                            showSucc("你的用户名：" + mt4.getName() +
                                    "\n" + "你的账号：" + mt4.getLogin()
                                    + "\n" + "你的密码：" + mt4.getPassword(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeThis();
                                }
                            });
                        }else{
                            title="注册失败";
                            showFail(String.format(getString(R.string.action_fail),beanSignUpInfo.getTips()!=null?beanSignUpInfo.getTips():beanSignUpInfo.getMsg()));
                        }
                    }
                });
            }
        });
    }
    private void removeThis(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
