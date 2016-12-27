package trade.xkj.com.trade.bean;

/**
 * Created by admin on 2016-11-16.
 */

public class BeanUserLoginDataSS {
    /**
     * msg_type : 10
     * login : 1000
     * password_hash : 54B7589A1B85E7324F00483EB7F6A2F1
     * port : 9988
     */

    private int msg_type;
    private String login;
    private String password_hash;
    private int port;
    public BeanUserLoginDataSS(String login, String password_hash) {
        this.msg_type = 10;
        this.port =9994;
        this.login = login;
        this.password_hash = password_hash;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }


    @Override
    public String toString() {
        return "{" +
                "msg_type=" + msg_type +
                ", login=" + login +
                ", password_hash='" + password_hash + '\'' +
                ", port=" + port +
                '}';
    }
}
