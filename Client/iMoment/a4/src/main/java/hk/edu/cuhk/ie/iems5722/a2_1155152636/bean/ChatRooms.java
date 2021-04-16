package hk.edu.cuhk.ie.iems5722.a2_1155152636.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 聊天室实体类
 */
public class ChatRooms implements Serializable {


    /**
     * data : [{"name":"generalRoom","id":1},{"name":"Room IEMS5722","id":2}]
     * status : OK
     */
    private List<ChatRoom> data;
    private String status;

    public void setData(List<ChatRoom> data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ChatRoom> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ChatRooms{" +
                "data=" + data +
                ", status='" + status + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

}
