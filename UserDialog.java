package com.swarup.kayhan.voice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Kayhan on 1/4/2015.
 */
@SuppressLint("ValidFragment")
public class UserDialog extends DialogFragment implements View.OnClickListener{
    User user;
    @SuppressLint("ValidFragment")
    public UserDialog(User user){
        this.user = user;

    }
    View view;

    TextView sexTextView,emailTextView;
    ListView listView;
    ArrayAdapter<String> adapter;
    List<Post> postList;
    String[] values = {""};
    Button followButton,backButton;
    boolean myPage=true;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_dialog,container,false);
        getDialog().setTitle(user.getUserName());
//        userNameTextView = (TextView)view.findViewById(R.id.name_userDialog);
//        passwordTextView = (TextView)view.findViewById(R.id.password_userDialog);
        sexTextView = (TextView)view.findViewById(R.id.sexTextView_userDialog);

        emailTextView= (TextView)view.findViewById(R.id.email_userDialog);
        followButton = (Button)view.findViewById(R.id.follow_button_userDialog);
        backButton = (Button)view.findViewById(R.id.back_button_userDialog);
//        userNameTextView.setText(user.getFirstName()+" "+user.getLastName());
//        passwordTextView.setText("Password: "+user.getPassword());
        emailTextView.setText("Email: "+user.getEmail());
        sexTextView.setText("Sex: "+user.getSex());
        getDialog().setTitle(user.getFirstName()+" "+user.getLastName());

        followButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        final MySqlHelper db =  new MySqlHelper(getActivity(),null,null,0);
        postList = db.getAllPosts(user.getUserId());
        values  = new String[postList.size()];

//        searchButton.setOnClickListener(this);


        int i=0;
        while(i<postList.size()){
            values[i]=postList.get(i++).getPostTitle();
        }
        listView = (ListView) view.findViewById(R.id.list_userDialog);
        adapter  = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ItemClickedListener());
        List<String> list = db.getAllFollowers(user.userId);
        Toast.makeText(getActivity(), list.size() + "followers", Toast.LENGTH_SHORT).show();
        db.close();
        return view;    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.follow_button_userDialog:
                MySqlHelper db = new MySqlHelper(getActivity(),null,null,0);
                db.addFollow(this.user.getUserId(),Login.user.getUserId());
                db.close();
                break;
            case R.id.back_button_userDialog:
                dismiss();
                break;
            default:
                break;
        }
    }
    Post postOther;
    class ItemClickedListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            postOther = postList.get(position);
            PostDialog postDialog = new PostDialog(postOther.getPostId(),postOther.getPostTitle(),postOther.getPostText(),postOther.getUserId());

            MySqlHelper db = new MySqlHelper(getActivity(),null,null,0);

            postDialog.show(getActivity().getSupportFragmentManager(),postOther.getPostTitle());
            db.addView(Login.user.getUserId(),postOther.postId);

            db.close();

        }
    }
}
