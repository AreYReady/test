package trade.xkj.com.trade.Utils.sslsocket;

import android.util.Log;

import java.nio.ByteBuffer;

import trade.xkj.com.trade.Utils.SocketUtil;
import trade.xkj.com.trade.Utils.SystemUtil;

/**
 * @author xjunda
 * @date 2016-09-01
 */
public class SSLEncodeImp implements Encoder<String> {
    @Override
    public void encode(String value, ByteBuffer buffer) throws EncoderException {
        Log.i(SystemUtil.getTAG(this.getClass()), "encode: Encoding request: " + value );
//        Log.i("123", "encode: buffer: " +buffer.remaining() + " strLengh" + value.length());
        synchronized (this){
            buffer.clear();
            buffer.put(SocketUtil.writePureByte(value));
        }
    }
}

