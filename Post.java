package com.swarup.kayhan.voice;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Kayhan on 12/25/2014.
 */
public class Post  extends Fragment implements View.OnClickListener{

    int postId;
    String userId;
    String postTitle;
    String postText;

    public Post() {
    }

    @SuppressLint("ValidFragment")
    public Post(String userId, String postTitle, String postText) {
        this.postId = postId;
        this.userId = userId;
        this.postTitle = postTitle;
        this.postText = postText;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }
    View view;
    TextView titleTextView;
    EditText bodyEditText,titleEditText;
    Button postButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.post_layout,container,false);
        titleTextView = (TextView)view.findViewById(R.id.title_postLayout);
        titleEditText = (EditText)view.findViewById(R.id.postTitle_editText_postLayout);
        bodyEditText = (EditText)view.findViewById(R.id.postText_editText_postLayout);
        postButton = (Button)view.findViewById(R.id.post_button_postLayout);
//        titleTextView.setText("Title: "+getPostTitle());
        bodyEditText.setText(getPostText());
        if(postTitle==null&&postText==null){
            bodyEditText.setBackgroundColor(Color.WHITE);
            postButton.setVisibility(View.VISIBLE);
            postButton.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.post_button_postLayout&&titleEditText.getText().toString().length()>0){
            MySqlHelper db = new MySqlHelper(getActivity(),null,null,0);
            Post post = new Post(Login.user.getUserId(),titleEditText.getText().toString(),bodyEditText.getText().toString());
            db.addPost(post);
        }
    }
}
