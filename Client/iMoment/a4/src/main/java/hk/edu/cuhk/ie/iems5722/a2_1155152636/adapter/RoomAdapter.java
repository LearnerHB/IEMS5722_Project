package hk.edu.cuhk.ie.iems5722.a2_1155152636.adapter;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.R;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.Room;

import java.util.List;
public class RoomAdapter extends ArrayAdapter {
    private final int resourceId;

    public RoomAdapter(Context context, int textViewResourceId, List<Room> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room fruit = (Room) getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView roomName = (TextView) view.findViewById(R.id.room_name);//获取该布局内的文本视图
        roomName.setText(fruit.getName());//为文本视图设置文本内容
        return view;
    }
}
