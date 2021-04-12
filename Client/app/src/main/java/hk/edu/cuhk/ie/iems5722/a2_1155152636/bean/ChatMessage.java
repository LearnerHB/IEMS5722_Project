package hk.edu.cuhk.ie.iems5722.a2_1155152636.bean;

public class ChatMessage {
    private String message_time;
    private int user_id;
    private String name;
    private int id;
    private String chatroom_id;
    private String message;

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChatroom_id() {
        return chatroom_id;
    }

    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "message_time='" + message_time + '\'' +
                ", user_id=" + user_id +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", chatroom_id=" + chatroom_id +
                ", message='" + message + '\'' +
                '}';
    }
}
