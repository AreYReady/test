package com.xkj.trade.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.xkj.trade.R;

/**
 * Created by huangsc on 2017-03-06.
 * TODO:dialog的工具类
 */

public  class DialogUtils {
    public AlertDialog.Builder alertDialog;

    public  DialogUtils(Context context, String title, String msg,
                                                   String positiveName, DialogInterface.OnClickListener positiveListener,
                                                   String negativeName, DialogInterface.OnClickListener negativeListener) {

    }
    public  DialogUtils(Context context, String title, String msg,
                        String positiveName, DialogInterface.OnClickListener positiveListener,
                        String negativeName) {
        this(context,title,msg,positiveName,positiveListener,negativeName,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    public  DialogUtils(Context context, String title, String msg,
                        DialogInterface.OnClickListener positiveListener) {
        alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton("确定", positiveListener);
        alertDialog.show();
    }
    public  DialogUtils(Context context,String msg) {
        alertDialog = new AlertDialog.Builder(context).setMessage(msg);
        alertDialog.show();
    }
    public  DialogUtils(Context context,String title,String msg) {
        alertDialog = new AlertDialog.Builder(context,R.style.AlertDialog_Fail).setTitle(title).setMessage(msg);
        alertDialog.show();
    }
}
