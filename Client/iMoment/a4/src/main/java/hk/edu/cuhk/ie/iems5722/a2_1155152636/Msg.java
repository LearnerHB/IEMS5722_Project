package hk.edu.cuhk.ie.iems5722.a2_1155152636;

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;

    private String content;
    private int type;
    private String time;

    public Msg(String content, int type, String time) {
        this.content = " \t " + content + "\t" + "\t" + "["+ time+ "]";
        this.type = type;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public String getTime() {
        return time;
    }
}