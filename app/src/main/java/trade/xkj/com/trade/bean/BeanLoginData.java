package trade.xkj.com.trade.bean;

/**
 * Created by admin on 2016-11-16.
 */

public class BeanLoginData {
    public String userName;
    public String userPassWord;
    public BeanLoginData(String userName,String userPassWord){
        this.userName=userName;
        this.userPassWord=userPassWord;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassWord() {
        return userPassWord;
    }

    public void setUserPassWord(String userPassWord) {
        this.userPassWord = userPassWord;
    }
}
