package trade.xkj.com.trade.mvp.operate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-14.
 * TODO:
 */

public class EditPendingPositionFrament extends BaseFragment {
    private TextView tvAction;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_operate_edit_pending_position,null);
        return view;
    }

    @Override
    protected void initView() {
        tvAction=(TextView)view.findViewById(R.id.tv_enter_button);
        tvAction.setText(this.getString(R.string.update_position));

    }

    @Override
    protected void initData() {

    }
}
