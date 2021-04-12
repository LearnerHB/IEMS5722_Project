package hk.edu.cuhk.ie.iems5722.a2_1155152636.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.R;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.adapter.ChatAdapter;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.bean.ChatRoom;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.bean.ChatRooms;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.network.GsonUtils;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.network.HttpHtil;
import hk.edu.cuhk.ie.iems5722.a2_1155152636.network.ResponseCallback;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView roomListView;
    List<ChatRoom> roomList;

    Call<String> getRooms;

    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.back);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Just a icon.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        // list view
        roomListView = (ListView) findViewById(R.id.list_view);
        roomList = new ArrayList<>();
        chatAdapter = new ChatAdapter(MainActivity.this, roomList);
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
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void success(Call<String> call, Response<String> response) {
                Log.d("MainActivity", "response : " +  response.body());
                if (response.body() != null) {
                    ChatRooms chatRooms = GsonUtils.getInstance().fromJson(response.body(), ChatRooms.class);
                    if ("OK".equals(chatRooms.getStatus())) {
                        roomList.addAll(chatRooms.getData());
                        chatAdapter.notifyDataSetChanged();
                    }else {
                        fail(call, new Exception("数据返回有误"));
                    }
                } else {
                    fail(call, new Exception("获取数据失败"));
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onDestroy() {
        if (getRooms != null && getRooms.isCanceled()) {
            getRooms.cancel();
        }
        super.onDestroy();
    }
}