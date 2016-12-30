package trade.xkj.com.trade.Utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import trade.xkj.com.trade.R;

/**
 * Created by huangsc on 2016-12-29.
 * TODO:
 */

public class CustomSwitch extends FrameLayout{
    private Context context;
    private View view;
    private ImageButton ibSwitch;
    private View parant;
    Animation animation;
    private SelectedChangedListener listener;
    public CustomSwitch(Context context) {
        this(context,null);
    }

    public CustomSwitch(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomSwitch(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        parant=inflate(context, R.layout.custom_switch_button,this);
        view=this.findViewById(R.id.imageView_rang);
        ibSwitch=(ImageButton) this.findViewById(R.id.ib_switch);
        ibSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("hsc", "ACTION_DOWN: "+parant.isSelected());
                if(parant.isSelected()) {
                    view.startAnimation(animation=AnimationUtils.loadAnimation(context, R.anim.transtale_x_toleft));
                    parant.setSelected(false);
                }else{
                    view.startAnimation(animation=AnimationUtils.loadAnimation(context, R.anim.transtale_x_toright));
                    parant.setSelected(true);
                }
                animation.setFillAfter(true);
                if(listener!=null)
                    listener.SelectChange(parant.isSelected());
            }
        });
    }
    public interface SelectedChangedListener{
        void SelectChange(Boolean select);
    }
    public void setSelectedChangeListener(SelectedChangedListener listener){
        this.listener=listener;
    }
}
