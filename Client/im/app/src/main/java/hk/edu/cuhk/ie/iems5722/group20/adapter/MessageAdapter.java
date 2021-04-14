package hk.edu.cuhk.ie.iems5722.group20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.im.R;
import hk.edu.cuhk.ie.iems5722.group20.entity.Message;
import hk.edu.cuhk.ie.iems5722.group20.entity.UserInfo;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private final int resourceId;

    private UserInfo userInfo;

    private String MYID;


    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        resourceId = resource;
        userInfo = (UserInfo) context.getApplicationContext();
        MYID = String.valueOf(userInfo.getId());
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message = (Message)getItem(position);

        // 获得消息 判断是自己的消息或者其他人的消息


        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        LinearLayout leftLayout = view.findViewById(R.id.left_layout);
        LinearLayout rightLayout = view.findViewById(R.id.right_layout);

        if(!message.getUserId().equals(MYID)){
            leftLayout.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.GONE);

//            TextView user = (TextView) view.findViewById(R.id.UserName);
            TextView messageContent = (TextView) view.findViewById(R.id.chatContent);
            TextView time = (TextView)view.findViewById(R.id.time);

//            user.setText(message.getName());
            messageContent.setText(message.getContent());

            System.out.println("========"+message.getTime());

            time.setText(message.getTime());

        }else{
            // 自己的消息
            leftLayout.setVisibility(View.GONE);
            rightLayout.setVisibility(View.VISIBLE);

//            TextView user = (TextView) view.findViewById(R.id.right_UserName);
            TextView messageContent = (TextView) view.findViewById(R.id.right_chatContent);
            TextView time = (TextView)view.findViewById(R.id.right_time);

//            user.setText(message.getName());
            messageContent.setText(message.getContent());
            time.setText(message.getTime());

        }



        return view;

    }
}
