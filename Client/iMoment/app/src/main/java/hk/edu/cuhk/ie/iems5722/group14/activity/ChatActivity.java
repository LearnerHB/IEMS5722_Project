package hk.edu.cuhk.ie.iems5722.group14.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.im.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import hk.edu.cuhk.ie.iems5722.group14.adapter.MessageAdapter;
import hk.edu.cuhk.ie.iems5722.group14.asynctask.HttpGetTask;
import hk.edu.cuhk.ie.iems5722.group14.asynctask.PostMessageTask;
import hk.edu.cuhk.ie.iems5722.group14.entity.Message;
import hk.edu.cuhk.ie.iems5722.group14.entity.UserInfo;

public class ChatActivity extends AppCompatActivity {

    static LinkedList<Message> messageList = new LinkedList<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    static int pageNum =1;
    static int totalPage;
    private UserInfo userInfo;
    static String target_user_id;
    static String target_user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 获取ChatRoomId
        Intent intent = getIntent();
        target_user_id = intent.getStringExtra("target_user_id");
        target_user_name = intent.getStringExtra("target_user_name");
        toolbar.setTitle(target_user_name);

        //
        messageList.clear();
        pageNum = 1;


         userInfo = (UserInfo) getApplicationContext();



        // ListView
        final MessageAdapter messageAdapter = new MessageAdapter(ChatActivity.this, R.layout.chat_layout,messageList);
        final ListView listView = findViewById(R.id.listview);
        listView.setAdapter(messageAdapter);

        //ListView 上滑加载数据
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_IDLE:
                        if(view.getLastVisiblePosition()==view.getCount()-1){
                        }else if(pageNum<totalPage && view.getFirstVisiblePosition()==0){
                            System.out.println("Loading...");
                            pageNum = pageNum+1;
                            int position = messageList.size()-1;
                            updateList(target_user_id,pageNum);
                            messageAdapter.notifyDataSetChanged();
                            System.out.println("position is:" + position);
                            listView.setSelection(0);
                        }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        // end

        //获取第一页的数据
        updateList(target_user_id,pageNum);

        int position = messageList.size()-1;
        System.out.println("position is:" + position);

        // 将焦点设置在第一条消息
        listView.setSelection(position);


        // refresh button

        ImageView refreshButton = findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click");

                messageList.clear();
                //
                pageNum = 1;

                updateList(target_user_id,pageNum);
                messageAdapter.notifyDataSetChanged();
                System.out.println("Data has been updated..");
                // I want to change the focus after data updated, but it does not work, always go to the newest item.
                listView.setSelection(0);
            }
        });

        //send Message
        Button imageView = findViewById(R.id.send);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText)findViewById(R.id.textView);

                String message = editText.getText().toString().trim();
//                String name = userInfo.nickName;
                String send_user = String.valueOf(userInfo.getId());
                String receive_user = target_user_id;

                String URLString = "http://18.191.232.230/api/project/messages";
                String[] parameters = new String[]{URLString,send_user,receive_user,message};

                if (!("".equals(message))){

                    PostMessageTask postMessageTask = new PostMessageTask();
                    try {
                        String s = postMessageTask.execute(parameters).get();
                        JSONObject jsonObject = new JSONObject(s);
                        String status = jsonObject.getString("status");
                        System.out.println(status);

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Date now = new Date();
                    Message newMessage = new Message(send_user,dateFormat.format(now),message,String.valueOf(userInfo.getId()));
                    messageList.add(newMessage);

                    editText.setText("");
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    messageAdapter.notifyDataSetChanged();
                    listView.setSelection(messageAdapter.getCount()-1);
                }

            }
        });
    }

    private void updateList(String chatRoomId, int pageNum){

        // 获取数据
        String url = "http://18.191.232.230/api/project/messages?curr_user="+userInfo.getId()+"&target_user="+target_user_id;

        HttpGetTask getChatRoomMessageTask = new HttpGetTask();

        try {
            String s = getChatRoomMessageTask.execute(url).get();
            System.out.println(s);

            JSONObject jsonObject = new JSONObject(s);
            String status = jsonObject.getString("status");
            if("ok".equals(status)){

                JSONArray array = jsonObject.getJSONArray("data");

//                JSONArray array = data.getJSONArray("messages");
//                totalPage = data.getInt("total_pages");
//                System.out.println("total page is: "+totalPage);
                for(int i=0;i<array.length();i++){
                    String receiveUser = array.getJSONObject(i).getString("receive_user");
                    String sendUser = array.getJSONObject(i).getString("send_user");
                    String msg = array.getJSONObject(i).getString("message");

                    String time = array.getJSONObject(i).getString("message_time");
//                    String userID = array.getJSONObject(i).getString("user_id");
                    Message message = new Message(sendUser,time,msg,sendUser);
                    messageList.addFirst(message);
                }

            }else {
                System.out.println("Get data error");
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
