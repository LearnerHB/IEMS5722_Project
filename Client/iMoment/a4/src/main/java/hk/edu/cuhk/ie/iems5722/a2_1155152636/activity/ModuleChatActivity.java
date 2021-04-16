package hk.edu.cuhk.ie.iems5722.a2_1155152636.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.R;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.adapter.ChatListAdapter;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.bean.ChatMessage;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.bean.ChatRoom;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.bean.GetMessage;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.network.GsonUtils;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.network.HttpHtil;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.network.ResponseCallback;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.user.ModuleMoA4User;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.view.PhilListView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ModuleChatActivity extends AppCompatActivity implements PhilListView.OnPullToRefreshListener {

    ChatRoom chatRoom;

    private PhilListView phlistview;
    List<ChatMessage> chatMessageList;
    ChatListAdapter chatListAdapter;

    int PAGE = 1;//页数
    private EditText inputText;

    Call<String> getMessage;
    Call<String> sendMessage;


    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_module);

        //获取聊天数据
        chatRoom = (ChatRoom) getIntent().getSerializableExtra("detail");
        if (chatRoom == null) { // 没有数据
            Toast.makeText(this, "数据有误", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            setTitle(chatRoom.getName());
        }
        // initialize toolbar
        Toolbar toolbar = findViewById(R.id.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // initialize list view
        phlistview = findViewById(R.id.phlistview);
        chatMessageList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(this, chatMessageList);
        phlistview.setAdapter(chatListAdapter);
        phlistview.setOnPullToRefreshListener(this);
        // initialize data
        getMessageList(chatRoom.getId());
        // edit
        inputText = (EditText) findViewById(R.id.input_text);
        inputText.setMaxHeight(200);//最大程度200
        // send button
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(inputText.getText().toString())) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
                    try {
                        sendMesageInfo(inputText.getText().toString());
                        inputText.setText("");
                    } catch (Exception e) {
                        if ("获取用户信息失败".equals(e.getMessage())) {
                            Toast.makeText(ModuleChatActivity.this,"获取用户信息失败",Toast.LENGTH_SHORT).show();
                        }
                        e.printStackTrace();
                    }
                }
            }
        });
        // initialize socket
        initSocket();
    }

    private void initSocket() {
        try {
            socket = IO.socket("http://18.191.234.51:8001/");
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            socket.on(Socket.EVENT_CONNECT, onConnectSuccess);
            socket.on("notification", onNotification);
            socket.connect();
        } catch (URISyntaxException e) {
            Log.e("socket", "initSocket error: ", e);
        }
    }

    @Override
    protected void onStart() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", ModuleMoA4User.getInstance().getName());
            jsonObject.put("room", chatRoom.getId());
            socket.emit("join", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if ("获取用户信息失败".equals(e.getMessage())) {
                Toast.makeText(this,"获取用户信息失败",Toast.LENGTH_SHORT).show();
            }
            e.printStackTrace();
        }
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.add_item) { // 刷新
            chatMessageList.clear();
            PAGE = 1;
            getMessageList(chatRoom.getId());
//                Toast.makeText(this, "You click Add", Toast.LENGTH_SHORT).show();
            //            case R.id.remove_item:
//                Toast.makeText(this, "You click Remove", Toast.LENGTH_SHORT).show();
//                break;
        } else if (itemId == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void addMsg(String message) {
        try {
            addMsg(ModuleMoA4User.getInstance().getName(),message);
        } catch (Exception e) {
            addMsg("未知发信人",message);
            e.printStackTrace();
        }
    }
    private void addMsg(String userName ,String message) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
        String time = formatter.format(new Date());

        ChatMessage sendMessage = new ChatMessage();
        sendMessage.setMessage(message);    // set content
        sendMessage.setChatroom_id(chatRoom.getId());    // set room id
        sendMessage.setName(userName);    // set user name
        sendMessage.setMessage_time(time);    // set time

        // main thread
        new Handler(Looper.getMainLooper()).post(() -> {
            chatMessageList.add(sendMessage);
            chatListAdapter.notifyDataSetChanged();
            phlistview.setSelection(chatMessageList.size() - 1);
        });
    }


    /**
     * 获取聊天信息
     *
     * @param chatroom_id
     */
    private void getMessageList(String chatroom_id) {
        getMessage = HttpHtil.getApiService().getMessage(chatroom_id, PAGE);
        getMessage.enqueue(new ResponseCallback<String>() {
            @Override
            protected void fail(Call<String> call, Throwable t) {
                Log.e("ChatActivity", "fail: ", t);
                Toast.makeText(ModuleChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void success(Call<String> call, Response<String> response) {
                Log.e("ChatActivity", "success:  response -> " + response.body());
                if (response.body() != null) {
                    GetMessage message = GsonUtils.getInstance().fromJson(response.body(), GetMessage.class);
                    if ("OK".equals(message.getStatus())) {
                        if (message.getData().getMessages() != null) {
                            Collections.reverse(message.getData().getMessages());
                            for (ChatMessage chatMessage : message.getData().getMessages()) {

                            }
                            chatMessageList.addAll(0, message.getData().getMessages());
                            chatListAdapter.notifyDataSetChanged();
                            phlistview.setSelection(message.getData().getMessages().size());
                        } else {
                            fail(call, new Exception("没有消息"));
                        }
                    } else if ("{\"message\":\"Page number exceeds total pages\",\"status\":\"ERROR\"}".equals(response.body().trim())) {
                        fail(call, new Exception("没有更多记录"));
                    } else {
                        fail(call, new Exception("数据返回有误"));
                    }
                } else {
                    fail(call, new Exception("获取数据失败"));
                }
            }
        });
    }

    @Override
    protected void onStop() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", ModuleMoA4User.getInstance().getName());
            jsonObject.put("room", chatRoom.getId());
            socket.emit("leave", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if ("获取用户信息失败".equals(e.getMessage())) {
                Toast.makeText(this,"获取用户信息失败",Toast.LENGTH_SHORT).show();
            }
            e.printStackTrace();
        }
        super.onStop();
    }

    /**
     * 发送消息
     */
    private void sendMesageInfo(String message) throws Exception {
        sendMessage = HttpHtil.getApiService().sendMessage(chatRoom.getId(), ModuleMoA4User.getInstance().getId(), ModuleMoA4User.getInstance().getName(), message);
        sendMessage.enqueue(new ResponseCallback<String>() {
            @Override
            protected void fail(Call<String> call, Throwable t) {
                Log.e("ChatActivity", "fail: ", t);
                inputText.setText(message);
                inputText.setSelection(message.length() - 1);
                Toast.makeText(ModuleChatActivity.this, "发送失败，请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void success(Call<String> call, Response<String> response) {
                Log.d("ChatActivity", "sendMesageInfo :  response -> " + response.body());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        String status = jsonObject.getString("status");
                        if ("OK".equals(status)) {
                            addMsg(message);
                        } else {
                            fail(call, new Exception("发送失败"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        fail(call, e);
                    }
                } else {
                    fail(call, new Exception("发送失败"));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (getMessage != null && getMessage.isCanceled()) {
            getMessage.cancel();
        }
        if (sendMessage != null && sendMessage.isCanceled()) {
            sendMessage.cancel();
        }
        socket.disconnect();
        socket.off();
        super.onDestroy();
    }

    @Override
    public void onBottom() {   // 加载不处理
//        phlistview.onRefresh(false);
    }

    @Override
    public void onTop() {
        phlistview.onRefresh(true);
        PAGE++; //  换页
        getMessageList(chatRoom.getId());
        phlistview.onRefresh(false);
    }


    private Emitter.Listener onConnectSuccess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("socket", "call:  onConnectSuccess  ---- ");
            for (int i = 0; i < args.length; i++) {
                Log.e("socket", "call:  args[" + i + "] -> " + args[i]);
            }
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("socket", "call:  onConnectError  ---- ");
            for (int i = 0; i < args.length; i++) {
                Log.e("socket", "call:  args[" + i + "] -> " + args[i]);
            }
        }
    };

    private Emitter.Listener onNotification = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            Log.e("socket", "call:  onNotification  ---- ");
            for (int i = 0; i < args.length; i++) {
                Log.e("socket", "call:  args[" + i + "] -> " + args[i]);
            }
            try {
                JSONObject json = new JSONObject(args[0].toString());
                String userName = json.getString("username");
                String message = json.getString("message");
                String chatroom_id = json.getString("chatroom_id");
                if(ModuleMoA4User.getInstance().getName().equals(userName)){
                    return; // 本人发的消息
                }

                Intent intent = new Intent(ModuleChatActivity.this, ModuleChatActivity.class);
                ChatRoom intentRoom = new ChatRoom();
                intentRoom.setId(chatroom_id);  // set id
                intentRoom.setName("chatroom" + chatroom_id);  // set name
                intent.putExtra("detail", intentRoom);//传递聊天室数据
                PendingIntent pendingIntent = PendingIntent.getActivity(ModuleChatActivity.this, 0, intent, 0);

                NotificationManagerCompat manager = NotificationManagerCompat.from(ModuleChatActivity.this);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel chan1 = new NotificationChannel("static",
                            "消息通知", NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(chan1);
                }
                Notification notification = new NotificationCompat.Builder(ModuleChatActivity.this, "static")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("chatroom " + chatroom_id)
                        .setContentText(userName + " : " + message)
//                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .build();
                manager.notify(new Random(new Date().getTime()).nextInt(99), notification);

                if (chatRoom.getId().equals(chatroom_id)){
                    // 是本房间的，加上消息，否则，仅通知
                    addMsg(userName,message);
                }

            } catch (Exception e) {
                Log.e("socket", "call: ", e);
            }
        }
    };

}