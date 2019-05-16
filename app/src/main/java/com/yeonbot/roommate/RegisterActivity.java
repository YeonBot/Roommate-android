package com.yeonbot.roommate;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yeonbot.roommate.HttpURLConnection.RequestHttpURLConnection;

import org.json.JSONException;

public class RegisterActivity extends AppCompatActivity {

    private EditText idText;
    private EditText passwordText;
    private EditText emailText;
    private RadioGroup genderGroup;
    private Button registerButton;

    //가입 하기 버튼 클릭시 url & parameter
    private String url = "http://10.0.2.2:8080/member/register";
    private ContentValues values = null;
    //초기 값.
    private String gender = "여성";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        emailText = (EditText) findViewById(R.id.emailText);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(registerClickListener);

        //성별 라디오 그롭 선택 listener
        genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        genderGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
    }

    //url로 함께 보낼 parameter 를 key:value 에 맞추어준다.
    private void setContentValues() {
        values = new ContentValues();
        values.put("memberId",idText.getText().toString());
        values.put("memberPassword",passwordText.getText().toString());
        values.put("memberEmail",emailText.getText().toString());
        values.put("memberGender",gender);
    }

    //남, 여 라디오 버튼 클릭시 이벤트
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (i == R.id.genderWoman) {
                gender = "여성";
            } else if (i == R.id.genderMan) {
                gender = "남성";
            }
        }
    };

    //가입하기 버튼 클릭시 이벤트
    Button.OnClickListener registerClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {

            //파라미터 values 값 초기화
            setContentValues();

            Post post = new Post(url, values);
            post.execute();
        }
    };


    //비동기 처리 클래스 (비동기 : 해야할 일을 위임하고 기다리는 처리 방식)
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
            Toast.makeText(RegisterActivity.this,s,Toast.LENGTH_SHORT).show();
        }
    }


}
