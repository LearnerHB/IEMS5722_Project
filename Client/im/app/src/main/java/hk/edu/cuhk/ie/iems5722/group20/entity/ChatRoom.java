package hk.edu.cuhk.ie.iems5722.group20.entity;

public class ChatRoom {
    private int id;
    private String chatroom;

    public int getId() {
        return id;
    }

    public String getChatroom() {
        return chatroom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChatroom(String chatroom) {
        this.chatroom = chatroom;
    }

    public ChatRoom(int id, String chatroom) {
        this.id = id;
        this.chatroom = chatroom;
    }
}
