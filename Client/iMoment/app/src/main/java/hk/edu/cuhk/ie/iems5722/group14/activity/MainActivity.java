package hk.edu.cuhk.ie.iems5722.group14.activity;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.im.R;
import hk.edu.cuhk.ie.iems5722.group14.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private List<View> lsViews = new ArrayList<>();
    private MyPagerAdapter pagerAdapter;


    //底部三个按钮
    private RadioButton rbChat;
    private RadioButton rbFind;
    private RadioButton rbMe;
    private RadioButton rbContact;



    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.main_vp);
        radioGroup = (RadioGroup) findViewById(R.id.tab_menu);

        rbChat = (RadioButton) findViewById(R.id.rbChat);
        rbFind = (RadioButton) findViewById(R.id.rbFind);
        rbMe = (RadioButton) findViewById(R.id.rbMe);
        rbContact = (RadioButton) findViewById(R.id.rbContact);
        rbChat.setChecked(true);

        lsViews.add(getLayoutInflater().inflate(R.layout.page_chats, null, false));
        lsViews.add(getLayoutInflater().inflate(R.layout.page_contact, null, false));
        lsViews.add(getLayoutInflater().inflate(R.layout.page_find, null, false));
        lsViews.add(getLayoutInflater().inflate(R.layout.page_me, null, false));

        pagerAdapter = new MyPagerAdapter(lsViews, this);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0://Chats
                        rbChat.setChecked(true);
                        break;
                    case 1://Chats
                        rbContact.setChecked(true);
                        break;
                    case 2://Contacts
                        rbFind.setChecked(true);
                        break;
                    case 3://Discover
                        rbMe.setChecked(true);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbChat:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rbContact:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.rbFind:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.rbMe:
                        viewPager.setCurrentItem(3);
                        break;
                }
            }
        });

    }

}
