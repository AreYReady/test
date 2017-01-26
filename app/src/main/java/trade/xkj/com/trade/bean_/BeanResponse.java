package trade.xkj.com.trade.bean_;

import org.json.JSONStringer;

/**
 * Created by huangsc on 2017-01-24.
 * TODO:返回实体类
 */

public class BeanResponse {
    int status;
    String msg;
    JSONStringer data;
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONStringer getData() {
        return data;
    }

    public void setData(JSONStringer data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "status ="+status+",msg = "+msg+", data="+data;
    }
}
