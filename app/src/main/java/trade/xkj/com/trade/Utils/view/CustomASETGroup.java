package trade.xkj.com.trade.Utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.SystemUtil;

/**
 * Created by huangsc on 2016-12-13.
 * TODO:
 */

public class CustomASETGroup extends FrameLayout{
    private Context context;
    private ImageView mImageView;
    private TextView mTVDesc;
    private TextView mTVDescPrompt;
    private AddSubEditText mASET;
    private String TAG= SystemUtil.getTAG(this);
    private String descString;
    private Boolean isVisibity;
    public CustomASETGroup(Context context) {
        this(context,null);
    }

    public CustomASETGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomASETGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomGroup);

        descString = ta.getString(R.styleable.CustomGroup_desc_text);
        isVisibity=ta.getBoolean(R.styleable.CustomGroup_iamge_visibility,true);
        ta.recycle();
        init();
    }

    private void init() {
        View inflate = LayoutInflater.from(context).inflate(R.layout.v_aset_group, null);
        addView(inflate);
        mImageView=(ImageView)findViewById(R.id.iv_state);
        mTVDesc=(TextView)findViewById(R.id.tv_desc);
        mTVDescPrompt=(TextView)findViewById(R.id.tv_desc_prompt);
        mASET=(AddSubEditText)findViewById(R.id.aset_aset);
        if(descString!=null){
            mTVDesc.setText(descString);
        }
        if(!isVisibity){
            mImageView.setVisibility(INVISIBLE);
        }
        mASET.setAmountChangeListener(new AddSubEditText.AmountChangeListener() {
            @Override
            public void amountChange(int count) {
            }
        });
        inflate.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    Log.i(TAG, "onTouch: ");
                  onTouchShow();
                }
                return true;
            }
        });

    }
    public void onTouchShow(){
        if (mImageView.isSelected()) {
            mImageView.setSelected(false);
            mTVDescPrompt.setVisibility(INVISIBLE);
            mASET.setNumbelTextInvisible();
        } else {
            mImageView.setSelected(true);
            mTVDescPrompt.setVisibility(VISIBLE);
            mASET.setNumbelTextVisible();
        }
    }

    public AddSubEditText getmASET() {
        return mASET;
    }

    public void setmASET(AddSubEditText mASET) {
        this.mASET = mASET;
    }

    public ImageView getmImageView() {
        return mImageView;
    }

    public void setmImageView(ImageView mImageView) {
        this.mImageView = mImageView;
    }



    public void setmTVDesc(String mTVDescString) {
        this.mTVDesc.setText(mTVDescString);
    }



    public void setmTVDescPrompt(TextView mTVDescPrompt) {
        this.mTVDescPrompt = mTVDescPrompt;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(MotionEvent.ACTION_DOWN==ev.getAction()&&!mImageView.isSelected()){
            Log.i(TAG, "onInterceptTouchEvent: ");
            onTouchShow();
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
