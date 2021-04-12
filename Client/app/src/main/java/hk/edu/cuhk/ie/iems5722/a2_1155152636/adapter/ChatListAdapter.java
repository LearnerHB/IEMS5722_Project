package hk.edu.cuhk.ie.iems5722.a2_1155152636.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.R;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.bean.ChatMessage;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.user.User;

import java.util.List;

/**
 * 聊天消息列表
 */
public class ChatListAdapter extends BaseAdapter {
    private Context context;
    private List<ChatMessage> chatMessageList;

    public ChatListAdapter(Context context, List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.msg_item, null);
            holder.left_layout = convertView.findViewById(R.id.left_layout);
            holder.right_layout = convertView.findViewById(R.id.right_layout);
            holder.left_msg = convertView.findViewById(R.id.left_msg);
            holder.left_date = convertView.findViewById(R.id.left_date);
            holder.right_msg = convertView.findViewById(R.id.right_msg);
            holder.right_date = convertView.findViewById(R.id.right_date);
            holder.left_user = convertView.findViewById(R.id.left_user);
            holder.right_user = convertView.findViewById(R.id.right_user);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (User.name.equals(chatMessageList.get(position).getName())) {
            holder.right_layout.setVisibility(View.VISIBLE);
            holder.left_layout.setVisibility(View.GONE);
            holder.right_msg.setText(chatMessageList.get(position).getMessage());
            holder.right_date.setText(chatMessageList.get(position).getMessage_time());
            holder.right_user.setText("User: " + chatMessageList.get(position).getName());
        } else {
            holder.left_layout.setVisibility(View.VISIBLE);
            holder.right_layout.setVisibility(View.GONE);
            if (TextUtils.isEmpty(chatMessageList.get(position).getMessage())) {

            }
            holder.left_msg.setText(chatMessageList.get(position).getMessage());
            holder.left_date.setText(chatMessageList.get(position).getMessage_time());
            holder.left_user.setText("User: " + chatMessageList.get(position).getName());
        }

        return convertView;
    }

    class ViewHolder {
        LinearLayout left_layout, right_layout;
        TextView left_msg, left_date, right_msg, right_date, left_user, right_user;
    }
}
