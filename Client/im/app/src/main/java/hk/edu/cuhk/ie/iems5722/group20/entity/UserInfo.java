package hk.edu.cuhk.ie.iems5722.group20.entity;

import android.app.Application;

public class UserInfo extends Application{

    private int id;
    private String nickName;

    public void setId(int id) {
        this.id = id;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }
}
