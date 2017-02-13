package com.xkj.trade.mvp.master;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-27.
 * TODO:
 */

public class FragmentMasterPosition extends BaseFragment {
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_master_position, null);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_master_position);
        mRecyclerView.setAdapter(new MyAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyHolder holder = new MyHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_master_position, parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }

        class MyHolder extends RecyclerView.ViewHolder {

            public MyHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
