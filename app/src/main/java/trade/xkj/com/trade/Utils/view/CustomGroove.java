package trade.xkj.com.trade.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huangsc on 2017-01-05.
 * TODO:凹槽背景
 *
 */

public class CustomGroove extends View {
    private Context context;
    public CustomGroove(Context context) {
        this(context,null);
    }

    public CustomGroove(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomGroove(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    private void init() {

    }

}
