package hk.edu.cuhk.ie.iems5722.group14.entity;

public class Circle {

    String nickName;
    String content;
    int post_id;
    String is_like;
    int likes;

    public Circle(String nickName, String content, int post_id, String is_like, int likes) {
        this.nickName = nickName;
        this.content = content;
        this.post_id = post_id;
        this.is_like = is_like;
        this.likes = likes;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    // String portrait;

}
