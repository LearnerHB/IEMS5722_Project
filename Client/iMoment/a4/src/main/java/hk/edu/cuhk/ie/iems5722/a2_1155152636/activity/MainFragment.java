package hk.edu.cuhk.ie.iems5722.a2_1155152636.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import hk.edu.cuhk.ie.iems5722.a2_1155152636.R;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.adapter.ChatAdapter;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.bean.ChatRoom;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.bean.ChatRooms;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.network.GsonUtils;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.network.HttpHtil;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.network.ResponseCallback;
import retrofit2.Call;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private ListView roomListView;
    List<ChatRoom> roomList;

    Call<String> getRooms;

    ChatAdapter chatAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // list view
        roomListView = (ListView) getView().findViewById(R.id.list_view);
        roomList = new ArrayList<>();
        chatAdapter = new ChatAdapter(getContext(), roomList);
        roomListView.setAdapter(chatAdapter);
        getChatRoomList();
    }

    /**
     * 获取聊天室列表数据
     */
    private void getChatRoomList() {
        getRooms = HttpHtil.getApiService().getChatRooms();
        getRooms.enqueue(new ResponseCallback<String>() {
            @Override
            protected void fail(Call<String> call, Throwable t) {
                Log.e("MainActivity", "fail: ", t);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void success(Call<String> call, Response<String> response) {
                Log.d("MainActivity", "response : " + response.body());
                if (response.body() != null) {
                    ChatRooms chatRooms = GsonUtils.getInstance().fromJson(response.body(), ChatRooms.class);
                    if ("OK".equals(chatRooms.getStatus())) {
                        roomList.addAll(chatRooms.getData());
                        chatAdapter.notifyDataSetChanged();
                    } else {
                        fail(call, new Exception("数据返回有误"));
                    }
                } else {
                    fail(call, new Exception("获取数据失败"));
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getRooms != null && getRooms.isCanceled()) {
            getRooms.cancel();
        }
        super.onDestroy();
    }

}