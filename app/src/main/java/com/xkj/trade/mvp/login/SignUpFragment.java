package com.xkj.trade.mvp.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;

/**
 * Created by huangsc on 2017-03-28.
 * TODO:
 */

public class SignUpFragment extends BaseFragment implements View.OnClickListener {
    Button mBack;
    Button mSignUp;
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
}
