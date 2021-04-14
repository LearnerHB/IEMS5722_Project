package hk.edu.cuhk.ie.iems5722.group20.entity;

public class Message {
    private String name;
    private String time;
    private String content;
    private String userId;

    public Message(String name, String time, String content, String userId) {
        this.name = name;
        this.time = time;
        this.content = content;
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
