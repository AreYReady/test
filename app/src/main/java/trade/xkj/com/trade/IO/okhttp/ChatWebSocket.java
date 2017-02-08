package trade.xkj.com.trade.IO.okhttp;

import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import trade.xkj.com.trade.base.MyApplication;
import trade.xkj.com.trade.bean.BeanUserLoginDataSocket;
import trade.xkj.com.trade.bean.RealTimeDataList;
import trade.xkj.com.trade.bean.ResponseEvent;
import trade.xkj.com.trade.constant.MessageType;
import trade.xkj.com.trade.constant.RequestConstant;
import trade.xkj.com.trade.constant.UrlConstant;
import trade.xkj.com.trade.utils.ACache;
import trade.xkj.com.trade.utils.AesEncryptionUtil;
import trade.xkj.com.trade.utils.SystemUtil;

/**
 * Created by huangsc on 2017-01-23.
 * TODO:webSocket工具类
 */

public class ChatWebSocket  extends WebSocketListener {
    private WebSocket mWebSocket =null;
    private JSONObject mJSONObject =null;
    private String TAG= SystemUtil.getTAG(this);
    private static ChatWebSocket mChatWebSocket=null;


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.i(TAG, "onOpen: "+response.toString());
        mWebSocket =webSocket;
        BeanUserLoginDataSocket beanUserLoginData = new BeanUserLoginDataSocket(Integer.valueOf(ACache.get(MyApplication.getInstance().getApplicationContext()).getAsString(RequestConstant.LOGIN_NAME)), AesEncryptionUtil.encrypt(ACache.get(MyApplication.getInstance().getApplicationContext()).getAsString(RequestConstant.LOGIN_PASSWORD)), 9988);
        String s = new Gson().toJson(beanUserLoginData, BeanUserLoginDataSocket.class);
        Log.i(TAG, "connectSocket: "+s);
        sendMessage(s);
    }

    @Override
    public void onMessage(WebSocket webSocket, String resultMessage) {
        ResponseEvent responseEvent = new Gson().fromJson(resultMessage, ResponseEvent.class);
        if (responseEvent != null) {
            switch (responseEvent.getMsg_type()) {
                case MessageType.TYPE_BINARY_LOGIN_RESULT://登录结果信息
                    Log.i(TAG, "handleResult: 登入" + resultMessage);
                    break;
                case MessageType.TYPE_BINARY_REAL_TIME_LIST://发送实时数据
                    RealTimeDataList realTimeDataList = new Gson().fromJson(resultMessage, RealTimeDataList.class);
                    Log.i(TAG, "handleResult:发送实时数据=  " + resultMessage);
                    EventBus.getDefault().post(realTimeDataList);
                    break;
            }
        }

    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.i(TAG, "onMessage: "+bytes.toString());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.i(TAG, "onClosing: "+reason);
        webSocket.close(1000, null);
        mChatWebSocket=null;

    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.i(TAG, "onFailure: ");
        t.printStackTrace();
    }

    /**
     * 初始化WebSocket服务器
     */
    private void run() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(UrlConstant.WS_URL).build();
        client.newWebSocket(request, this);
        client.dispatcher().executorService().shutdown();
    }

    /**
     *
     * @param s
     * @return
     */
    public boolean sendMessage(String s){
        return mWebSocket.send(s);
    }

    public void closeWebSocket(){
        mChatWebSocket=null;
        mWebSocket.close(1000,"主动关闭");
        Log.e("close","关闭成功");
    }

    /**
     * 获取全局的ChatWebSocket类
     * @return ChatWebSocket
     */
    public static ChatWebSocket getChartWebSocket(){
        if(mChatWebSocket==null) {
            mChatWebSocket =new ChatWebSocket();
            mChatWebSocket.run();
        }
        return mChatWebSocket;
    }
}
