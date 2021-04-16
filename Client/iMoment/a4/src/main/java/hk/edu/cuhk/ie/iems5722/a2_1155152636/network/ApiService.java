package hk.edu.cuhk.ie.iems5722.a2_1155152636.network;


import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    /**
     * 获取聊天室列表接口
     *
     * @return
     */
    @GET("api/a3/get_chatrooms")
    Call<String>    getChatRooms();

    /**
     * 获取消息列表
     *
     * @param chatroom_id
     * @param page
     * @return
     */
    @GET("api/a3/get_messages")
    Call<String> getMessage(@Query("chatroom_id") String chatroom_id, @Query("page") int page);

    /**
     * 发送消息
     *
     * @param chatroom_id
     * @param user_id
     * @param name
     * @param message
     * @return
     */
    @FormUrlEncoded
    @POST("api/a3/send_message")
    Call<String> sendMessage(@Field("chatroom_id") String chatroom_id, @Field("user_id") String user_id, @Field("name") String name, @Field("message") String message);

    /**
     * 广播消息
     *
     * @param chatroom_id
     * @param message
     * @return
     */
    @FormUrlEncoded
    @POST("api/a4/broadcast_room")
    Call<String> broadcastRoom(@Field("chatroom_id") String chatroom_id, @Field("message") String message);

}