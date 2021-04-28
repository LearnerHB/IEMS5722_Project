package hk.edu.cuhk.ie.iems5722.group14.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.im.R;
import hk.edu.cuhk.ie.iems5722.group14.asynctask.HttpGetTask;
import hk.edu.cuhk.ie.iems5722.group14.entity.NewContacter;
import hk.edu.cuhk.ie.iems5722.group14.entity.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewContacterAdapter extends ArrayAdapter<NewContacter> {

    private int resourceId;
    UserInfo userInfo;


    public NewContacterAdapter(Context context, int resource, List<NewContacter> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        NewContacter newContacter = getItem(position);
        View view = LayoutInflater.from (getContext ()).inflate (resourceId, parent, false);
        final TextView nickName = view.findViewById(R.id.new_friend_name);
        nickName.setText(newContacter.getNickname());
        Button accept = view.findViewById(R.id.accept);
        Button refuse = view.findViewById(R.id.refuse);
        userInfo = (UserInfo) getContext().getApplicationContext();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                String url = "http://18.222.103.240/api/project/accept_or_refuse?operation="+1+
                        "&request_name='"+nickName.getText()+"'&receive_id="+userInfo.getId();
                responseToRequest(url,nickName);
            }
        });
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://18.222.103.240/api/project/accept_or_refuse?operation="+0+
                        "&request_name='"+nickName.getText()+"'&receive_id="+userInfo.getId();
                responseToRequest(url,nickName);
            }
        });
        return view;
    }

    private void responseToRequest(String url,TextView nickName){
        HttpGetTask httpGetTask = new HttpGetTask();
        try {
            String s = httpGetTask.execute(url).get();

            JSONObject jsonObject = new JSONObject(s);
            String status = jsonObject.getString("status");
            if("ok".equals(status)){

                System.out.println("Success");

            }else {
                System.out.println("Failure");
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