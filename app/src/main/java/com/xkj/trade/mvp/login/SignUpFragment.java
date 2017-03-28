package com.xkj.trade.mvp.login;

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

import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.constant.RequestConstant;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huangsc on 2017-03-28.
 * TODO:
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
                break;
        }
    }
    private void requestSignUp(){
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.NAME,mLoginName.getText().toString());
        map.put(RequestConstant.PHONE,mTelephone.getText().toString());
        map.put(RequestConstant.EMAIL,mEmail.getText().toString());
    }
}
