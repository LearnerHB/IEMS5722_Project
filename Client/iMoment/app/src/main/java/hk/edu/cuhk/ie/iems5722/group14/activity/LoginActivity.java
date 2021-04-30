package hk.edu.cuhk.ie.iems5722.group14.activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.example.im.R;
import hk.edu.cuhk.ie.iems5722.group14.asynctask.RegisterRequest;
import hk.edu.cuhk.ie.iems5722.group14.entity.UserInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private UserInfo userInfo;

    private CheckBox checkBox1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
        final EditText usernameEd = findViewById(R.id.username);
        final EditText passwordEd = findViewById(R.id.password);
        passwordEd.setTransformationMethod(PasswordTransformationMethod.getInstance());


        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //如果选中，显示密码
                    passwordEd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    passwordEd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(usernameEd.getText());
                String password = String.valueOf(passwordEd.getText());
                String URLString = "http://18.222.103.240/api/project/login";
                JSONObject resultMessage = userLogin(URLString,username,password);
                String status = "";
                String userId = "";
                try {
                    status = resultMessage.getString("status");
                    userId = resultMessage.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if("ok".equals(status)){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    userInfo = (UserInfo) getApplicationContext();
                    userInfo.setNickName(username);
                    userInfo.setId(Integer.valueOf(userId));
                    usernameEd.setText("");
                    passwordEd.setText("");
                    Toast.makeText(LoginActivity.this,"login successful",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this,"Incorrect username or password",Toast.LENGTH_SHORT).show();
                    // password error
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText username = new EditText(LoginActivity.this);
                final EditText password = new EditText(LoginActivity.this);
                final View dialogView = View.inflate(LoginActivity.this, R.layout.register_pop_out, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Register").setIcon(android.R.drawable.ic_dialog_info)
                        .setView(dialogView)
                        .setNegativeButton("Cancel", null);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        EditText editText = dialogView.findViewById(R.id.input_username);
                        EditText editText1 = dialogView.findViewById(R.id.input_password);
                        String username = editText.getText().toString();
                        String password = editText1.getText().toString();
                        String resultMessage = "";

                        if(!"".equals(username) && !"".equals(password)){
                            try {
                                String URLString = "http://18.222.103.240/api/project/register";
                                resultMessage = userRegister(URLString,username,password);
                                if("ok".equals(resultMessage)){
                                    Toast.makeText(LoginActivity.this,"Register successful",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(LoginActivity.this,"Server failure, try again",Toast.LENGTH_SHORT).show();
                                }

                            }catch (Exception e){

                            }

                        }else{
                            Toast.makeText(LoginActivity.this,"Confirm the account and password",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.show();
            }
        });


    }

    private String userRegister(String url,String userName,String password){

        String[] parameters = new String[]{url,userName,password};
        String result="";
        RegisterRequest registerRequest = new RegisterRequest();
        try {
            String s = registerRequest.execute(parameters).get();
            JSONObject jsonObject = new JSONObject(s);
            String status = jsonObject.getString("status");
            result = status;


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JSONObject userLogin(String url,String userName,String password){

        String[] parameters = new String[]{url,userName,password};
        String result="";
        RegisterRequest registerRequest = new RegisterRequest();
        JSONObject jsonObject = null;
        try {
            String s = registerRequest.execute(parameters).get();
            jsonObject = new JSONObject(s);
//            String status = jsonObject.getString("status");
//            result = status;


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


}
