package trade.xkj.com.trade.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import trade.xkj.com.trade.constant.NetworkType;

/**
 * @author huangsc
 * @date 2016-11-17
 */
public class SystemUtil {
    /**
     * 当前activity是否在最前面
     *
     * @param context
     * @param activityName
     * @return
     */
    public static boolean isTopActivity(Context context, String activityName) {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains(activityName)) {
            isTop = true;
        }
        return isTop;
    }

    /**
     * 是否锁屏
     *
     * @param context
     * @return
     */
    public static boolean isScreenOn(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
        return !flag;
    }

    /**
     * 判断是不是有效的网络链接 <br/>
     * 3G，wifi，GPRS（歌词，头像）
     *
     * @param context
     * @return
     */
    public static boolean isAvalidNetSetting(Context context) {
        return !(NetworkType.UNKNOWN.endsWith(getNetworkType(context)));
    }

    /**
     * 　　* 获取设置的最大长度
     * 　　*
     * 　　* @return
     *
     */
    public static int getMaxLength(TextView tv) {
        int length = 0;
        try {
            InputFilter[] inputFilters = tv.getFilters();
            for (InputFilter filter : inputFilters) {
                Class<?> c = filter.getClass();
                if (c.getName().equals("android.text.InputFilter$LengthFilter")) {
                    Field[] f = c.getDeclaredFields();
                    for (Field field : f) {
                        if (field.getName().equals("mMax")) {
                            field.setAccessible(true);
                            length = (Integer) field.get(filter);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 获取网络类型
     *
     * @return 网络类型
     * @p)ram context
     */
    private static String getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            return NetworkType.UNKNOWN;
        }
        // if (!netInfo.isAvailable()) {
        // return NetworkType.UNKNOWN;
        // }
        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetworkType.WIFI;
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int netType = tm.getNetworkType();
        // 已知3G类型
        // NETWORK_TYPE_UMTS 3
        // NETWORK_TYPE_EVDO_0 5
        // NETWORK_TYPE_EVDO_A 6
        // NETWORK_TYPE_HSDPA 8
        // NETWORK_TYPE_HSUPA 9
        // NETWORK_TYPE_HSPA 10

        // NETWORK_TYPE_EVDO_B 12
        // NETWORK_TYPE_LTE 13
        // NETWORK_TYPE_EHRPD 14
        // NETWORK_TYPE_HSPAP 15
        // 已知2G类型
        // NETWORK_TYPE_GPRS 1
        // NETWORK_TYPE_EDGE 2
        // NETWORK_TYPE_CDMA 4
        // NETWORK_TYPE_1xRTT 7
        // NETWORK_TYPE_IDEN 11

        if (netType == TelephonyManager.NETWORK_TYPE_GPRS
                || netType == TelephonyManager.NETWORK_TYPE_EDGE
                || netType == TelephonyManager.NETWORK_TYPE_CDMA
                || netType == TelephonyManager.NETWORK_TYPE_1xRTT || netType == 11) {
            return NetworkType.NET_2G;
        }

        if (netType == TelephonyManager.NETWORK_TYPE_LTE) {
            return NetworkType.NET_4G;
        }

        return NetworkType.NET_3G;
    }

    public static String getTAG(Object object) {
        return "hsc : " + object.getClass().getSimpleName();
    }

    public static int[] getScrren(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        int[] i = new int[]{width, height};
        return i;
    }

    /**
     * dp转px
     *
     * @param context
     * @param
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics()));
    }

    public static float dp2pxFloat(Context context, float dpVal) {
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics()));
    }

    /**
     * sp转px
     *
     * @param context
     * @param
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics()));
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 判断是否是图片作为背景,是则状态栏透明
     *
     * @param activity
     */
    public static void setTranslucentForImage(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 设置状态栏颜色 * * @param activity 需要设置的activity * @param color 状态栏颜色值
     */
    public static void setColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 生成一个和状态栏大小相同的矩形条 * * @param activity 需要设置的activity * @param color 状态栏颜色值 * @return 状态栏矩形条
     */
    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }

    /**
     * 设置透明栏
     * @param activity
     */
    public static void setTranslucent(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
