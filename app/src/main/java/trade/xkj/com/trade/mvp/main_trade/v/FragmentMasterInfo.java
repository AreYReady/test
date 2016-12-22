package trade.xkj.com.trade.mvp.main_trade.v;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-22.
 * TODO:详细介绍单个高手的信息
 */

public class FragmentMasterInfo extends BaseFragment {
    private BackInterface mBackInterface;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_master_info,null);
        if(!(getActivity() instanceof BackInterface)){
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }else{
            this.mBackInterface = (BackInterface)getActivity();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBackInterface.setSelectedFragment(this);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    public Boolean onBackPressed() {
        if(getFragmentManager()!=null) {
            getFragmentManager().popBackStackImmediate();
            return true;
        }
       return super.onBackPressed();
    }
}
