package com.swarup.kayhan.voice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Kayhan on 1/4/2015.
 */
@SuppressLint("ValidFragment")
public class PostDialog extends DialogFragment implements View.OnClickListener{
    String title;
    String text;
    String userName;
    MySqlHelper dbHelper;
    int postId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @SuppressLint("ValidFragment")
    public PostDialog(int postId,String title, String text, String userName) {
        this.title = title;
        this.postId = postId;
        this.text = text;
        this.userName = userName;

    }


    TextView info;
    Button follow,back;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_dialog,container,false);

//        ((TextView)view.findViewById(R.id.title_postDialog)).setText(title);
        getDialog().setTitle(title);
        ((TextView)view.findViewById(R.id.postText_editText_postDialog)).setText(text);
        ((Button)view.findViewById(R.id.ok_button_dialog)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.like_button)).setOnClickListener(this);


        dbHelper = new MySqlHelper(getActivity(),null,null,0);
        if(dbHelper.isLiked(Login.user.getUserId(),postId)){
            ((Button)view.findViewById(R.id.like_button)).setEnabled(false);
        }
        int count = dbHelper.getLikeCount(postId);
        int viewCount = dbHelper.getViewCount(postId);
        info = (TextView)view.findViewById(R.id.like_textView);
        info.setText("Total Likes: "+count+"\nTotal Views: "+viewCount);
        dbHelper.close();
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ok_button_dialog){
            dismiss();
        }else if(v.getId()==R.id.like_button){

            dbHelper.addLike(Login.user.getUserId(),postId);
            ((Button)v).setEnabled(false);
            dbHelper.close();
        }
    }
}
