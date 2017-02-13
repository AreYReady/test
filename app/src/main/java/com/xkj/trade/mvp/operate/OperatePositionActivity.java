package com.xkj.trade.mvp.operate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.xkj.trade.R;
import com.xkj.trade.base.OperateBaseActivity;
import com.xkj.trade.utils.ToashUtil;

import immortalz.me.library.TransitionsHeleper;
import immortalz.me.library.bean.InfoBean;
import immortalz.me.library.method.ColorShowMethod;


/**
 * Created by huangsc on 2016-12-10.
 * TODO:对界面上的各种按钮键操作的显示activity
 */

public class OperatePositionActivity extends OperateBaseActivity {
    public static final String OPERATEACTION="operate_action";
    private OperateAction operateAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_operate_add);
        operateAction=(OperateAction) getIntent().getExtras().get(OPERATEACTION);
//        SystemUtil.setColor(this,R.color.colorPrimaryDark);
//        SystemUtil.setTranslucent(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (operateAction) {
            case ADD:
                TransitionsHeleper.getInstance().setShowMethod(new ColorShowMethod(R.color.color_tertiary_dark, R.color.color_tertiary_dark) {
                    @Override
                    public void loadCopyView(InfoBean bean, ImageView copyView) {
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(
                                ObjectAnimator.ofFloat(copyView,"rotation",0,180),
                                ObjectAnimator.ofFloat(copyView, "scaleX", 1, 0),
                                ObjectAnimator.ofFloat(copyView, "scaleY", 1, 0)
                        );
                        set.setInterpolator(new AccelerateInterpolator());
                        set.setDuration(duration / 4 * 5).start();
                    }
                }
                ).show(this,null);
                fragmentTransaction.add(R.id.fl_operate_content, new AddPositionFragment());
                break;
            case ClOSE_POSITION:
                fragmentTransaction.add(R.id.fl_operate_content, new ClosePositionFrament());
                break;
            case EDIT_POSITION:
                fragmentTransaction.add(R.id.fl_operate_content, new EditPositionFrament());
                break;
            case UNLINK:
                fragmentTransaction.add(R.id.fl_operate_content, new UnlinkFrament());
                break;
            case DELETE_PENDING_POSITION:
                fragmentTransaction.add(R.id.fl_operate_content, new DeletePositionFrament());
                break;
            case EDIT_PENDING_POSITION:
                fragmentTransaction.add(R.id.fl_operate_content, new EditPendingPositionFrament());
                break;
            default:
                fragmentTransaction.add(R.id.fl_operate_content, new AddPositionFragment());
                ToashUtil.showShort(this,"哥哥还没做,随便给你个内容");
        }
        fragmentTransaction.commit();
    }

    @Override
    public void initEvent() {

    }

    public enum OperateAction {
        ADD,
        DELETE_PENDING_POSITION,
        EDIT_PENDING_POSITION,
        EDIT_POSITION,
        UNLINK,
        ClOSE_POSITION
    }
}
