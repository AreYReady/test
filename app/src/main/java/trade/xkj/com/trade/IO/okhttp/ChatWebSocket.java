package trade.xkj.com.trade.IO.okhttp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by huangsc on 2017-01-23.
 * TODO:webSocket工具类
 */

public class ChatWebSocket  extends WebSocketListener {
    private WebSocket mWebSocket =null;
    private JSONObject mJSONObject =null;

    private static ChatWebSocket mChatWebSocket=null;


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        mWebSocket =webSocket;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.e("MESSAGE>>>>>>>",text);
        try {
            mJSONObject = new JSONObject(text);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        System.out.println("MESSAGE: " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        mChatWebSocket=null;
        Log.e("Close:",code+reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    /**
     * 初始化WebSocket服务器
     */
    private void run() {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0,  TimeUnit.MILLISECONDS).build();
        Request request = new Request.Builder().url("ws://10.254.20.222:9502").build();
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
