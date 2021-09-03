package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HelloActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        Bundle extras = getIntent().getExtras();
        String json  = extras.getString("json");
        String jsonUser = extras.getString("jsonUser");
        System.out.println("Posts: " + json);

        TextView tv = (TextView) findViewById(R.id.posts);
        TextView tvUser = (TextView) findViewById(R.id.user);

        try {
            JSONObject jsonObject = new JSONObject(jsonUser);
            String name = jsonObject.getString("name");
            String userid = jsonObject.getString("id");

            tvUser.setText("Hello " + "\n" +name + "\n" + userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        tv.setText(json);

        ArrayList<Post> user_posts = new ArrayList<Post>();

        //JsonObject jsonObject = new JsonObject(json);



    }
}