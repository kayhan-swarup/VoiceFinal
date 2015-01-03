package com.swarup.kayhan.voice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kayhan on 12/25/2014.
 */

public class User  extends Fragment{
    String userId;
    String userName;
    String email;
    String DOB;
    String password;
    public User(){}

    @SuppressLint("ValidFragment")
    public User(String userName){
        this.userName = userName;
    }

    @SuppressLint("ValidFragment")
    public User(String userId,String password,String email,String fullName){
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.userName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    View view;

    TextView userNameTextView,passwordTextView,emailTextView;
    ListView listView;
    ArrayAdapter<String> adapter;
    List <Post> postList;
    String[] values = {""};
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_layout,container,false);

        userNameTextView = (TextView)view.findViewById(R.id.name_userLayout);
        passwordTextView = (TextView)view.findViewById(R.id.password_userLayout);
        emailTextView= (TextView)view.findViewById(R.id.email_userLayout);
        userNameTextView.setText("Name: "+getUserName());
        passwordTextView.setText("Password: "+getPassword());
        emailTextView.setText("Email: "+getEmail());

        MySqlHelper db =  new MySqlHelper(getActivity(),null,null,0);
        postList = db.getAllPosts(getUserId());
        values  = new String[postList.size()];
        int i=0;
        while(i<postList.size()){
            values[i]=postList.get(i++).getPostTitle();
        }
        listView = (ListView) view.findViewById(R.id.list_userLayout);
        adapter  = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ItemClickedListener());
        return view;
    }
    class ItemClickedListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Post post = postList.get(position);
            PostDialog postDialog = new PostDialog(post.getPostTitle(),post.getPostText(),post.getUserId());

            postDialog.show(getActivity().getSupportFragmentManager(),post.getPostTitle());



        }
    }
}
