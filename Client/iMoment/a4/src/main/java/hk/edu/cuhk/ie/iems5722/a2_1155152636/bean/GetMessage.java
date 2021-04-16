package hk.edu.cuhk.ie.iems5722.a2_1155152636.bean;

import java.util.List;

/**
 * 获取数据实体类
 */
public class GetMessage {


    /**
     * data : {"messages":[{"message_time":"2021-02-27 18:47:56","user_id":1155147507,"name":"Ren yubin","id":818,"chatroom_id":1,"message":"can you"},{"message_time":"2021-02-27 18:45:18","user_id":1155147507,"name":"Ren yubin","id":817,"chatroom_id":1,"message":"try it"},{"message_time":"2021-02-27 17:43:29","user_id":1155152375,"name":"Lyu Chenghao","id":814,"chatroom_id":1,"message":"Can you hear me"},{"message_time":"2021-02-27 16:07:02","user_id":1155147233,"name":"el","id":813,"chatroom_id":1,"message":"1"},{"message_time":"2021-02-27 15:53:24","user_id":1155152375,"name":"Lyu Chenghao","id":812,"chatroom_id":1,"message":"33333"}],"total_pages":104,"current_page":"1"}
     * status : OK
     */
    private DataEntity data;
    private String status;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GetMessage{" +
                "data=" + data +
                ", status='" + status + '\'' +
                '}';
    }

    public DataEntity getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public class DataEntity {
        /**
         * messages : [{"message_time":"2021-02-27 18:47:56","user_id":1155147507,"name":"Ren yubin","id":818,"chatroom_id":1,"message":"can you"},{"message_time":"2021-02-27 18:45:18","user_id":1155147507,"name":"Ren yubin","id":817,"chatroom_id":1,"message":"try it"},{"message_time":"2021-02-27 17:43:29","user_id":1155152375,"name":"Lyu Chenghao","id":814,"chatroom_id":1,"message":"Can you hear me"},{"message_time":"2021-02-27 16:07:02","user_id":1155147233,"name":"el","id":813,"chatroom_id":1,"message":"1"},{"message_time":"2021-02-27 15:53:24","user_id":1155152375,"name":"Lyu Chenghao","id":812,"chatroom_id":1,"message":"33333"}]
         * total_pages : 104
         * current_page : 1
         */
        private List<ChatMessage> messages;
        private int total_pages;
        private String current_page;

        public void setMessages(List<ChatMessage> messages) {
            this.messages = messages;
        }

        public void setTotal_pages(int total_pages) {
            this.total_pages = total_pages;
        }

        public void setCurrent_page(String current_page) {
            this.current_page = current_page;
        }

        public List<ChatMessage> getMessages() {
            return messages;
        }

        public int getTotal_pages() {
            return total_pages;
        }

        public String getCurrent_page() {
            return current_page;
        }

        @Override
        public String toString() {
            return "DataEntity{" +
                    "messages=" + messages +
                    ", total_pages=" + total_pages +
                    ", current_page='" + current_page + '\'' +
                    '}';
        }
    }

}
