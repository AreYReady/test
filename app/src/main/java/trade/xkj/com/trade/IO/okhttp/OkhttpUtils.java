package trade.xkj.com.trade.IO.okhttp;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocketListener;

/**
 * Created by huangsc on 2017-01-23.
 * TODO:okhttp工具类
 */

public class OkhttpUtils {
    private static final OkHttpClient.Builder builder = new OkHttpClient.Builder();
    private static final OkHttpClient mOkHttpClient =builder.connectTimeout(30, TimeUnit.SECONDS).build();

    static{
//        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    }

    /**
     * 该不会开启异步线程。
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException{
        return mOkHttpClient.newCall(request).execute();
    }
    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
        }
    public static void enqueue(Request request, WebSocketListener responseListener) {
        mOkHttpClient.newWebSocket(request,responseListener);
    }
    public static void enqueue(String url,Map map, Callback responseCallback) {
        mOkHttpClient.newCall(new Request.Builder().url(url).post(getRequestPost(map)).build()).enqueue(responseCallback);

    }
    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     * @param request
     */
    public static void enqueue(Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }

        });
    }
    public static String getStringFromServer(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 组装body
     * @param map
     * @return
     */
    public static RequestBody getRequestPost(Map<String ,String> map){
        FormBody.Builder builder = new FormBody.Builder();
        if(map!=null){
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for(Map.Entry<String ,String> item:entries){
                builder.add(item.getKey(),item.getValue());
            }
        }
        return builder.build();
    }

}
