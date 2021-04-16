package hk.edu.cuhk.ie.iems5722.group14.entity;

import android.app.Application;

import hk.edu.cuhk.ie.iems5722.a2_1155152636.user.ModuleMoA4User;

public class UserInfo extends Application{

    private int id;
    private String nickName;

    public void setId(int id) {
        this.id = id;
        // setting module id
        try {
            ModuleMoA4User.getInstance().setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        // setting module name
        try {
            ModuleMoA4User.getInstance().setName(nickName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }
}
