package trade.xkj.com.trade.bean;

/**
 * Created by admin on 2016-11-16.
 */

public class BeanUserLoginData {
    /**
     * msg_type : 10
     * login : 1000
     * password : 54B7589A1B85E7324F00483EB7F6A2F1
     * port : 9988
     */

    private int msg_type;
    private int login;
    private String password;
    private int port;
    public BeanUserLoginData(int login, String password_hash) {
        this.msg_type = 10;
        this.port =9994;
        this.login = login;
        this.password = password_hash;
    }
    public BeanUserLoginData(int login, String password_hash,int port) {
        this.msg_type = 10;
        this.port =port;
        this.login = login;
        this.password = password_hash;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "{" +
                "msg_type=" + msg_type +
                ", login=" + login +
                ", password='" + password + '\'' +
                ", port=" + port +
                '}';
    }
}
