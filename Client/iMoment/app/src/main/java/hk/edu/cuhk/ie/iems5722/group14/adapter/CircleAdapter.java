package hk.edu.cuhk.ie.iems5722.group14.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.im.R;
import hk.edu.cuhk.ie.iems5722.group14.asynctask.LikesPostRequest;
import hk.edu.cuhk.ie.iems5722.group14.entity.Circle;
import hk.edu.cuhk.ie.iems5722.group14.entity.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CircleAdapter extends ArrayAdapter<Circle> {

    private int resourceId;
    private UserInfo userInfo = (UserInfo) getContext().getApplicationContext();


    public CircleAdapter(Context context, int resource, List<Circle> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Circle circle = getItem(position);


        View view = LayoutInflater.from (getContext ()).inflate (resourceId, parent, false);


        final TextView nickName = (TextView) view.findViewById(R.id.nickname_circle);
        final TextView content = (TextView) view.findViewById(R.id.content_circle);
        final TextView postId = (TextView) view.findViewById(R.id.post_id);
        final ImageView dislike_flag = view.findViewById(R.id.dislike_flag);
        final ImageView like_flag = view.findViewById(R.id.like_flag);
        final TextView like_num = view.findViewById(R.id.like_num);

        if("1".equals(circle.getIs_like())){
            like_flag.setVisibility(View.VISIBLE);
            dislike_flag.setVisibility(View.GONE);
        }

        nickName.setText(circle.getNickName());
        content.setText(circle.getContent());
        postId.setText(String.valueOf(circle.getPost_id()));
        like_num.setText(String.valueOf(circle.getLikes()));

        System.out.println("-------"+circle.getIs_like());


        // 点击事件
        dislike_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String URLString = "http://18.191.232.230/api/project/like";
                postRequest(URLString, String.valueOf(postId.getText()));
                //
                dislike_flag.setVisibility(View.GONE);
                like_flag.setVisibility(View.VISIBLE);
                int tmp = Integer.valueOf( String.valueOf(like_num.getText()))+1;
                like_num.setText(String.valueOf(tmp));
            }
        });

        like_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URLString = "http://18.191.232.230/api/project/dislike";
                postRequest(URLString, String.valueOf(postId.getText()));
                like_flag.setVisibility(View.GONE);
                dislike_flag.setVisibility(View.VISIBLE);
                int tmp = Integer.valueOf( String.valueOf(like_num.getText()))-1;
                like_num.setText(String.valueOf(tmp));
            }
        });

        return view;

    }
    private String postRequest(String url ,String post_id){
        String[] parameters = new String[]{url,String.valueOf(userInfo.getId()),post_id};
        String result="";

        LikesPostRequest newFriend = new LikesPostRequest();
        try {
            String s = newFriend.execute(parameters).get();
            JSONObject jsonObject = new JSONObject(s);
            String status = jsonObject.getString("status");
            if (!"ok".equals(status)){
                result = "Network error, try again";
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
