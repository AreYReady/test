package com.xkj.trade.utils.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xkj.trade.R;


/**
 * Created by admin on 2016-11-22.
 * huangshunchao
 */

public class LoadingDialog {
//    LVCircularRing mLoadingView;
    Dialog mLoadingDialog;
    AnimationDrawable animationDrawable;
    private Context mContext;
    LinearLayout layout;

    public LoadingDialog(Context context, String msg) {
        mContext=context;
        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_login_view, null);
        // 获取整个布局
        layout = (LinearLayout) view.findViewById(R.id.dialog_view);
        // 页面中的LoadingView
//        mLoadingView = (LVCircularRing) view.findViewById(R.id.lv_circularring);
        // 页面中显示文本
//        TextView loadingText = (TextView) view.findViewById(R.id.loading_text);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_animation);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        // 显示文本
//        loadingText.setText(msg);
        // 创建自定义样式的Dialog
        mLoadingDialog = new Dialog(context, R.style.loading_dialog);
        // 设置返回键无效
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void show() {
        mLoadingDialog.show();
        animationDrawable.start();
    }

    public void close() {
        if (mLoadingDialog != null) {
            animationDrawable.stop();
            mLoadingDialog.dismiss();
        }
    }
}
