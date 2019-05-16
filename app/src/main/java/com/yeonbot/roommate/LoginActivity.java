package com.yeonbot.roommate;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yeonbot.roommate.HttpURLConnection.RequestHttpURLConnection;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;
    private EditText idText;
    private EditText passwordText;

    private String url = "http://10.0.2.2:8080/member/login";
    private ContentValues values = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        //로그인 버튼 리스너
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ditText 공백 확인
                if (!checkEditText()) {
                    return;
                }
                //content값(id,password) 채우기
                setContentValues();

                Post post = new Post(url,values);
                post.execute();
            }
        });

        //회원 가입 버튼 리스너
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }

    private boolean checkEditText() {
        String id = idText.getText().toString().trim();
        String password = idText.getText().toString().trim();
        if (id.equals("")) {
            Toast.makeText(LoginActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.equals("")) {
            Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setContentValues() {
        values = new ContentValues();
        values.put("memberId",idText.getText().toString());
        values.put("password",passwordText.getText().toString());
    }

    private class Post extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public Post(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = null; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            try {
                result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null){
                Toast.makeText(LoginActivity.this,"execption 발생",Toast.LENGTH_SHORT).show();
            }else {
                Log.d("TAG",s.toString());
                //로그인 페이지 이동.
            }
        }
    }

}
