package trade.xkj.com.trade.bean;

/**
 * @author xjunda
 * @date 2016-07-23
 * 相应包
 * @link UserLoginActivity # onLoginResult
 */
public class ResponseEvent{

    /**
     * msg_type : 11
     * result_code : 0
     */

    private int msg_type;
    private int result_code;

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

}
