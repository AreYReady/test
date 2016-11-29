package trade.xkj.com.trade.mvp.main_trade.v;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import trade.xkj.com.trade.Base.BaseFragment;
import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.GalleryAdapter;
import trade.xkj.com.trade.Utils.View.HistoryTradeView;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPre;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPreImpl;

/**
 * Created by admin on 2016-11-22.
 */

public class MainTradeContentFrag extends BaseFragment implements MainTradeContentLFragListener {
    private View view;
    private MainTradeContentPre mMainTradeContentPre;
    private HorizontalScrollView mHScrollView;
    private HistoryTradeView mHistoryTradeView;
    private LinearLayout ll;
    private Context context;
    private View imView;
    private int h;
    private int w;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<Integer> mDatas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_trade_context, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHScrollView = (HorizontalScrollView) view.findViewById(R.id.hsv_trade);
        imView = view.findViewById(R.id.imageVi);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        context = this.getActivity();
        ViewTreeObserver vto = mHScrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                h = mHScrollView.getHeight();
                w = mHScrollView.getWidth();
                Log.i(TAG, "Height=" + h); // 得到正确结果
                mHistoryTradeView = new HistoryTradeView(context);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(w * 5, h);
                mHistoryTradeView.setLayoutParams(layoutParams);
                ll.addView(mHistoryTradeView);
                mHScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GalleryAdapter(context, mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mMainTradeContentPre = new MainTradeContentPreImpl(this, mHandler);
        mMainTradeContentPre.loading();
        mDatas = new ArrayList<Integer>(Arrays.asList(R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher));
    }
    HistoryDataList data;
    @Override
    public void freshView(final HistoryDataList data) {
        Log.i(TAG, "freshView: ");
        this.data = data;

        initScrollView();
    }

    private void initScrollView() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHScrollView.scrollTo(w * 5, 0);
                mHistoryTradeView.setHistoryData(data);
            }
        }, 1000);
    }
}
