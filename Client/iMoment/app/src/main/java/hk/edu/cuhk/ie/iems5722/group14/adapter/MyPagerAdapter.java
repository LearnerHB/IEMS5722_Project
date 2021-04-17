package hk.edu.cuhk.ie.iems5722.group14.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import com.example.im.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hk.edu.cuhk.ie.iems5722.a2_1155152636.activity.MainFragment;
import hk.edu.cuhk.ie.iems5722.group14.activity.ChatActivity;
import hk.edu.cuhk.ie.iems5722.group14.activity.CircleActivity;
import hk.edu.cuhk.ie.iems5722.group14.activity.LoginActivity;
import hk.edu.cuhk.ie.iems5722.group14.asynctask.AddFriendRequest;
import hk.edu.cuhk.ie.iems5722.group14.asynctask.HttpGetTask;
import hk.edu.cuhk.ie.iems5722.group14.entity.ChatRoom;
import hk.edu.cuhk.ie.iems5722.group14.entity.NewContacter;
import hk.edu.cuhk.ie.iems5722.group14.entity.UserInfo;

public class MyPagerAdapter extends PagerAdapter {

    private AppCompatActivity mActivity;
    private List<String> contacters = new ArrayList<>();
    private List<NewContacter> newContacters = new ArrayList<>();
    private static UserInfo userInfo;
    private static ArrayList<ChatRoom> chatRoomList = new ArrayList<>();


    List<View> lsViews;//存储ViewPager需要的View

    //构造方法，用来传递View列表
    public MyPagerAdapter(List<View> lsViews, AppCompatActivity mActivity) {
        this.lsViews = lsViews;
        this.mActivity = mActivity;
        userInfo = (UserInfo) mActivity.getApplicationContext();
    }

    //更新视图数据
    public void Update(List<View> lsViews) {
        this.lsViews = lsViews;
    }

    //获得视图数量
    @Override
    public int getCount() {
        return lsViews.size();
    }

    //用来判断当前视图是否需要缓存
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //翻页的时候移除之前的视图
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(lsViews.get(position));
    }

    //翻页的时候加载新的视图
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // init circles
        switch (position) {
            case 0:
                FragmentManager fm = mActivity.getSupportFragmentManager();
                Fragment fragment = fm.findFragmentById(R.id.layout);
                if (fragment == null) {
                    fragment = MainFragment.newInstance();
                    fm.beginTransaction()
                            .add(R.id.layout, fragment)
                            .commit();
                }
                View view_0 = lsViews.get(0);
                final ChatRoomAdapter chatRoomAdapter = new ChatRoomAdapter(mActivity, R.layout.chatroom_list, chatRoomList);
                ListView listView = view_0.findViewById(R.id.chatroom_listview);
                listView.setAdapter(chatRoomAdapter);
                String url = "http://18.191.232.230/api/project/rooms?user_id=" + userInfo.getId();
                HttpGetTask httpGetTask = new HttpGetTask();
                try {
                    String s = httpGetTask.execute(url).get();
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")) {
                        //continue
                        JSONArray array = jsonObject.getJSONArray("data");
                        chatRoomList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            int id = array.getJSONObject(i).getInt("user_id");
                            String nickname = array.getJSONObject(i).getString("nickname");
                            ChatRoom chatRoom = new ChatRoom(id, nickname);
                            chatRoomList.add(chatRoom);
                        }
                    } else {
                        System.out.println("Error");
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 设置跳转到对应的ChatRoom
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ChatRoom chatRoom = (ChatRoom) parent.getItemAtPosition(position);

                        Intent i = new Intent(mActivity, ChatActivity.class);
                        i.putExtra("target_user_id", String.valueOf(chatRoom.getId()));
                        i.putExtra("target_user_name", chatRoom.getChatroom());
                        mActivity.startActivity(i);
                    }
                });


            case 1:

                View view_contact = lsViews.get(1);
                init_contact(view_contact);


                break;
            //
            case 2:
                View view = lsViews.get(2);

                Button button = view.findViewById(R.id.friends);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mActivity, CircleActivity.class);
                        mActivity.startActivity(i);
                    }
                });
                break;
            case 3:
                View view1 = lsViews.get(3);
                TextView userName = view1.findViewById(R.id.nickname);
                ImageView portrait = view1.findViewById(R.id.protrait);
                portrait.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mActivity, "Modify photo", Toast.LENGTH_SHORT).show();
                    }
                });
                userInfo = (UserInfo) mActivity.getApplicationContext();
                userName.setText(userInfo.getNickName());
                Button logout = view1.findViewById(R.id.logout);
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mActivity, LoginActivity.class);
                        mActivity.startActivity(i);
                    }
                });

            default:
                break;


        }
        container.addView(lsViews.get(position));

        return lsViews.get(position);
    }

    private void init_contact(View view_contact) {

        // 初始化 新增列表


        NewContacterAdapter newContacterAdapter = new NewContacterAdapter(mActivity, R.layout.item_new_friend_notification, newContacters);
        ListView listView = view_contact.findViewById(R.id.new_contact_list);
        listView.setAdapter(newContacterAdapter);

        // 添加好友

        ImageView add_friend = view_contact.findViewById(R.id.add_friend);
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText added_user = new EditText(mActivity);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Enter the nickname").setIcon(android.R.drawable.ic_dialog_info).setView(added_user)
                        .setNegativeButton("Cancel", null);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String resultMessage = "Request failed";

                        String receive_user = added_user.getText().toString();
                        if (!"".equals(receive_user)) {
                            resultMessage = sendFriendRequest(receive_user);
                        }

                        AlertDialog.Builder response = new AlertDialog.Builder(mActivity);
                        response.setTitle(resultMessage).setNegativeButton("OK", null);
                        response.show();
//                        System.out.println(added_user.getText().toString());
                        // 获得要添加的好友昵称。

                    }
                });
                builder.show();
            }
        });


        // 好友请求列表
        userInfo = (UserInfo) mActivity.getApplicationContext();

        String url = "http://18.191.232.230/api/project/get_friend_request?user_id=" + userInfo.getId();
        HttpGetTask getFriendsRequest = new HttpGetTask();

        try {
            String s = getFriendsRequest.execute(url).get();

            JSONObject jsonObject = new JSONObject(s);
            String status = jsonObject.getString("status");
            if ("ok".equals(status)) {

                JSONArray data = jsonObject.getJSONArray("data");
                newContacters.clear();
                for (int i = 0; i < data.length(); i++) {
                    newContacters.add(new NewContacter(data.getJSONObject(i).getString("nickname")));
                }

            } else {
                System.out.println("Get data error");
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // 初始化联系人列表

        url = "http://18.191.232.230/api/project/get_friends?user_id=" + userInfo.getId();
        HttpGetTask getFriends = new HttpGetTask();
        try {
            String s = getFriends.execute(url).get();

            JSONObject jsonObject = new JSONObject(s);
            String status = jsonObject.getString("status");
            if ("ok".equals(status)) {

                JSONArray data = jsonObject.getJSONArray("data");
                contacters.clear();
                for (int i = 0; i < data.length(); i++) {
                    contacters.add(data.getJSONObject(i).getString("nickname"));
                }

            } else {
                System.out.println("Get data error");
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ListView listView2 = view_contact.findViewById(R.id.contacter);//在视图中找到ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, contacters);//新建并配置ArrayAapeter
        listView2.setAdapter(adapter);

    }

//    private void openAlbum(){
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//        intent.setType("image/*");
//        context.startActivityFor;
//    }

    private String sendFriendRequest(String receive_nickname) {
        String URLString = "http://18.191.232.230/api/project/add_friend";
        String[] parameters = new String[]{URLString, String.valueOf(userInfo.getId()), receive_nickname};
        String result = "";
        AddFriendRequest newFriend = new AddFriendRequest();
        try {
            String s = newFriend.execute(parameters).get();
            JSONObject jsonObject = new JSONObject(s);
            String status = jsonObject.getString("status");
            switch (status) {
                case "0":
                    result = "No such user";
                    break;
                case "1":
                    result = "Request sent";
                    break;
                case "2":
                    result = "Already your friend";
                    break;
                default:
                    break;
            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;


    }


}
