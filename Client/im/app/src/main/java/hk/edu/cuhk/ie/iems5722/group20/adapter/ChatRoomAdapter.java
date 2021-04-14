package hk.edu.cuhk.ie.iems5722.group20.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.im.R;

import java.util.List;

import hk.edu.cuhk.ie.iems5722.group20.entity.ChatRoom;


public class ChatRoomAdapter extends ArrayAdapter<ChatRoom> {

    private final int resourceId;
    private AppCompatActivity mActivity;


    public ChatRoomAdapter(AppCompatActivity context, int resource, List<ChatRoom> objects) {
        super(context, resource, objects);
        objects.add(0, null);
        mActivity = context;
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatRoom chatRoom = (ChatRoom) getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView messageContent = (TextView) view.findViewById(R.id.chatRoom);

        messageContent.setText(chatRoom.getChatroom());

        return view;

    }
}