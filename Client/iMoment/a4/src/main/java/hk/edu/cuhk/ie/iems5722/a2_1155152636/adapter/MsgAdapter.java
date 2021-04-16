package hk.edu.cuhk.ie.iems5722.a2_1155152636.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.Msg;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.R;

import java.util.List;

public class MsgAdapter extends ArrayAdapter<Msg> {
    private int resourceId;

    public MsgAdapter(Context context,  int textViewResourceId, List<Msg> objects) {
        super(context,  textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Msg msg = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = (LinearLayout)view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (LinearLayout)view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = (TextView)view.findViewById(R.id.left_msg);
            viewHolder.leftDate = (TextView)view.findViewById(R.id.left_date);
            viewHolder.rightMsg = (TextView)view.findViewById(R.id.right_msg);
            viewHolder.rightDate = (TextView)view.findViewById(R.id.right_date);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        if(msg.getType() == Msg.TYPE_RECEIVED){
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getContent());
        }else if(msg.getType() == Msg.TYPE_SEND){
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.rightMsg.setText(msg.getContent());
        }
        return view;

    }

    class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        TextView leftDate;
        TextView rightDate;
    }
}