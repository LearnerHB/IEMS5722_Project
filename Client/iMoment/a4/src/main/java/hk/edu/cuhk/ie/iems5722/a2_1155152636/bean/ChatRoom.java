package hk.edu.cuhk.ie.iems5722.a2_1155152636.bean;

import java.io.Serializable;

/**
 * 聊天室数据
 */
public class ChatRoom  implements Serializable {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
