package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by An Kwi nam on 2016-09-03.
 */
public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText repassword;
    private Button signup;
    private Button cancel;

    String URL= "https://today-walks-lee-s-h.c9users.io/index.php";
    JSONParser jsonParser=new JSONParser();
    String Result;
    int successResult = -1;
    String messageResult = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        name = (EditText)findViewById(R.id.signup_nameinput);
        email = (EditText)findViewById(R.id.signup_emailinput);
        password = (EditText)findViewById(R.id.signup_pwinput);
        repassword = (EditText)findViewById(R.id.signup_repwinput);
        signup = (Button)findViewById(R.id.write_finish);
        cancel = (Button)findViewById(R.id.signup_btncancel);

        // 비밀번호 일치 검사
        repassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str_password = password.getText().toString();
                String str_repassword = repassword.getText().toString();

                if( str_password.equals(str_repassword) && !str_password.isEmpty() && !str_repassword.isEmpty()) {
                    password.setBackgroundColor(Color.GREEN);
                    repassword.setBackgroundColor(Color.GREEN);
                }
                else if( str_password.isEmpty() && !str_repassword.isEmpty() ) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 먼저 입력하시오", Toast.LENGTH_SHORT).show();
                    repassword.setText("");
                }
                else if(str_password.isEmpty() || str_repassword.isEmpty()){
                    password.setBackgroundColor(Color.DKGRAY);
                    repassword.setBackgroundColor(Color.DKGRAY);
                }
                else {
                    password.setBackgroundColor(Color.RED);
                    repassword.setBackgroundColor(Color.RED);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 이름 입력 확인
                if( name.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "이름 입력", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }
                // 이메일 입력 확인
                if( email.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "이메일 입력", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    return;
                }
                // 비밀번호 입력 확인
                if( password.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "비밀번호 입력", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }
                // 비밀번호 확인 입력 확인
                if( repassword.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "비밀번호 확인", Toast.LENGTH_SHORT).show();
                    repassword.requestFocus();
                    return;
                }
                // 비밀번호 일치 확인
                if( !password.getText().toString().equals(repassword.getText().toString()) ) {
                    Toast.makeText(SignupActivity.this, "비밀번호 불일치", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    repassword.setText("");
                    password.requestFocus();
                    return;
                }

                Intent result_email = new Intent();
                result_email.putExtra("EMAIL",email.getText().toString());

                AttemptLogin attemptLogin= new AttemptLogin();
                attemptLogin.execute(email.getText().toString(),password.getText().toString(),name.getText().toString());

                while (successResult < 0 ){
                    if(successResult >= 0)
                        break;
                }

                setResult(RESULT_OK, result_email);

                if(successResult == 1) {
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"회원가입 실패"+messageResult,Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private class AttemptLogin extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] args) {
            String name = args[2].toString();
//            String name="";
//            try{
//                namee = URLEncoder.encode(namee,"UTF-8");
//                name = new String(namee.getBytes("UTF-8"));
//            } catch(Exception e) {
//                e.printStackTrace();
//            }

            String password = args[1].toString();
            String email = args[0].toString();

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            if (name.length() > 0)
                params.add(new BasicNameValuePair("name", name));
            Log.e("Check",params.toString());
            JSONObject result = jsonParser.makeHttpRequest(URL, "POST", params);

            try {
                if (result != null) {
                    successResult = result.getInt("success");
                    messageResult = result.getString("message");
                } else {
                    successResult = 0;
                    messageResult = "json 생성오류";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                    Result = result.getString("success");
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}