package hk.edu.cuhk.ie.iems5722.group14.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.im.R;
import hk.edu.cuhk.ie.iems5722.group14.adapter.CircleAdapter;
import hk.edu.cuhk.ie.iems5722.group14.asynctask.HttpGetTask;
import hk.edu.cuhk.ie.iems5722.group14.asynctask.NewPostRequest;
import hk.edu.cuhk.ie.iems5722.group14.entity.Circle;
import hk.edu.cuhk.ie.iems5722.group14.entity.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CircleActivity extends AppCompatActivity {

    private ArrayList<Circle> circleMessageList = new ArrayList<>();
    public UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userInfo = (UserInfo) getApplicationContext();

        // 获取朋友圈信息
        String url = "http://18.191.232.230/api/project/posts?user_id="+userInfo.getId();
        HttpGetTask getFriendsRequest = new HttpGetTask();

        try {
            String s = getFriendsRequest.execute(url).get();



            JSONObject jsonObject = new JSONObject(s);
            String status = jsonObject.getString("status");
            if("ok".equals(status)){

                JSONArray data = jsonObject.getJSONArray("data");
                circleMessageList.clear();
                for(int i=0;i<data.length();i++){
                    circleMessageList.add(new Circle(data.getJSONObject(i).getString("nickname"),
                            data.getJSONObject(i).getString("post_content"),
                            data.getJSONObject(i).getInt("post_id"),
                            data.getJSONObject(i).getString("is_like"),
                            data.getJSONObject(i).getInt("likes")));
                }

            }else {
                System.out.println("获取数据错误");
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 新推文
        ImageView new_posts = findViewById(R.id.new_posts);
        new_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText post_content = new EditText(CircleActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(CircleActivity.this);
                builder.setTitle("内容").setIcon(android.R.drawable.ic_dialog_info).setView(post_content)
                        .setNegativeButton("Cancel", null);
                builder.setPositiveButton("POST", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String resultMessage = "请求失败";

                        String postContent = post_content.getText().toString();
                        if(!"".equals(postContent)){
                            resultMessage = postRequest(postContent);
                        }

                        AlertDialog.Builder response = new AlertDialog.Builder(CircleActivity.this);
                        response.setTitle(resultMessage).setNegativeButton("OK",null);
                        if(!"".equals(resultMessage)){
                            response.show();
                        }


                    }
                });
                builder.show();

            }
        });

        CircleAdapter circleAdapter = new CircleAdapter(CircleActivity.this,R.layout.item_circle,circleMessageList);
        ListView listView = findViewById(R.id.circle);
        listView.setAdapter(circleAdapter);


    }

    private String postRequest(String postContent){
        String URLString = "http://18.191.232.230/api/project/posts";
        String[] parameters = new String[]{URLString,String.valueOf(userInfo.getId()),postContent};
        String result="";

        NewPostRequest newFriend = new NewPostRequest();
        try {
            String s = newFriend.execute(parameters).get();
            JSONObject jsonObject = new JSONObject(s);
            String status = jsonObject.getString("status");
            if (!"ok".equals(status)){
                result = "网络错误，请稍后再试";
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
