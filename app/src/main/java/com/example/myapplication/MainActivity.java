package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {



    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    public List<User> users;
    public List<Post> posts;


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public interface endPointInterface {
        // Request method and URL specified in the annotation

        @GET("users/")
        Call<List<User>> getUsers();

        @GET("posts/")
        Call<List<Post>> getPosts();


    }

    private void getPosts(){
        endPointInterface new_interface = retrofit.create((endPointInterface.class));
        Call<List<Post>> call = new_interface.getPosts();
        call.enqueue((new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                posts = response.body();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
            }
        }));
    }

    private void getUsers(){
        endPointInterface new_interface = retrofit.create(endPointInterface.class);
        Call<List<User>> call = new_interface.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                users = response.body();
                users.get(0).password = "abc";
                users.get(1).password = "123";
                users.get(2).password = "456";
                users.get(3).password = "789";
                users.get(4).password = "abc";
                users.get(5).password = "abc";
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    public boolean checkIfUserExists(String username){
        for (User user: users){
            if(username.equals(user.username)){
                return true;
            }
        }
        return false;
    }

    public void displayAllUsers(){
        for(User user: users){
            System.out.println("User: " + user.name);
            System.out.println("Username: " + user.username);
            System.out.println("Password: " + user.password);
        }
    }

    public boolean checkPassword(String username, String password){
        for(User user: users){
            if (username.equals(user.username)){
                if (password.equals(user.password)){
                    return true;
                }
            }
        }
        return false;
    }

    public User getUser(String username){
        for (User user: users){
            if(username.equals(user.username)){
                return user;
            }
        }
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText username = (EditText) findViewById(R.id.username);
        EditText userPassword = (EditText) findViewById(R.id.password);

        Button loginBtn = findViewById(R.id.loginBtn);

        MainActivity.this.getUsers();
        MainActivity.this.getPosts();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = username.getText().toString();
                String password = userPassword.getText().toString();

                // Make the check to see if the userName and password exist in the local Lists:
                if (checkIfUserExists(userName)) {
                    //if user exists, Check the password:
                    if (checkPassword(userName, password)) {
                        //if everything matches, retrieve the posts from the specific user, and move
                        // to the next activity:
                        int userId = getUser(userName).getId();


                        ArrayList<Post> user_posts = new ArrayList<Post>();

                        for (Post post : posts) {
                            if (Integer.parseInt(post.userId) == userId){
                                user_posts.add(post);
                            }
                        }


                        //once we have all the posts, we can display the posts into the next activity:
                        //Call the Next Activity with user_posts:

                        //convert object to json to hand off to the next activity:
                        Gson gson  = new Gson();
                        String jsonPosts  = gson.toJson(user_posts);
                        String jsonUser = gson.toJson(getUser(userName));



                        Intent intent = new Intent(MainActivity.this, HelloActivity.class);
                        intent.putExtra("json", jsonPosts);
                        intent.putExtra("jsonUser", jsonUser);
                        startActivity(intent);


                    } else {
                        //highlight the password field is incorrect:
                        Toast.makeText(MainActivity.this, "Password is Incorrect,", Toast.LENGTH_LONG).show();
                    }

                } else {
                    // highlight the username field, mark that the username is incorrect:
                    Toast.makeText(MainActivity.this, "Username is Incorrect.", Toast.LENGTH_LONG).show();

                }



            }
        });




    }
}