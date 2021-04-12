package hk.edu.cuhk.ie.iems5722.a2_1155152636.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hk.edu.cuhk.ie.iems5722.a2_1155152636.activity.ChatActivity;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.R;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.bean.ChatRoom;

/**
 * 聊天室适配器 显示聊天室数据/装载接口数据
 */
public class ChatAdapter extends BaseAdapter {

    private Context context;
    private List<ChatRoom> chatRoomList;

    public ChatAdapter(Context context,List<ChatRoom> chatRoomList){
        this.chatRoomList = chatRoomList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return chatRoomList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatRoomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chatroom,null);
            holder.tv_chat_room = convertView.findViewById(R.id.tv_chat_room);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_chat_room.setText(chatRoomList.get(position).getName());
        holder.tv_chat_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("detail",chatRoomList.get(position));//传递聊天室数据
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView tv_chat_room;
    }
}
