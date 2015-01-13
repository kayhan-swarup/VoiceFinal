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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kayhan on 12/25/2014.
 */

public class User  extends Fragment implements View.OnClickListener{
    String userId;
    String userName;
    String email;
    String DOB;
    String password;
    String firstName,lastName;
    String sex;


    public User(){}

    @SuppressLint("ValidFragment")
    public User(String userName){
        this.userName = userName;
    }

    @SuppressLint("ValidFragment")
    public User(String userId,String password,String email,String firstName,String lastName,String sex){
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.firstName= firstName;
        this.lastName = lastName;
        this.sex = sex;
    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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
    Button searchButton,followingButton,follwerButton;
    EditText search;
    boolean myPage=true;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_layout,container,false);

        userNameTextView = (TextView)view.findViewById(R.id.name_userLayout);
        passwordTextView = (TextView)view.findViewById(R.id.password_userLayout);
        emailTextView= (TextView)view.findViewById(R.id.email_userLayout);
        followingButton = (Button)view.findViewById(R.id.following_button_userLayout);
        searchButton = (Button)view.findViewById(R.id.search_button);
        follwerButton = (Button)view.findViewById(R.id.follwer_button_userLayout);
        search = (EditText)view.findViewById(R.id.search);
        userNameTextView.setText("Name: "+getFirstName()+" "+getLastName());
        passwordTextView.setText("Sex: "+getSex());
        emailTextView.setText("Email: "+getEmail());
        followingButton.setOnClickListener(this);
        follwerButton.setOnClickListener(this);

        final MySqlHelper db =  new MySqlHelper(getActivity(),null,null,0);
        postList = db.getAllPosts(getUserId());
        values  = new String[postList.size()];

        searchButton.setOnClickListener(this);


        int i=0;
        while(i<postList.size()){
            values[i]=postList.get(i++).getPostTitle();
        }
        listView = (ListView) view.findViewById(R.id.list_userLayout);
        adapter  = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ItemClickedListener());
        List<String> list = db.getAllFollowers(userId);
        Toast.makeText(getActivity(),list.size()+"followers",Toast.LENGTH_SHORT).show();
        db.close();
        return view;
    }
    List<Post> listOthers;
    public void setUser(String userId,String email){
        this.userId=userId;this.email=email;
        userNameTextView.setText("Name: "+userId);
//        passwordTextView.setText("Password: "+getPassword());
        emailTextView.setText("Email: "+email);
        if(Login.user.getUserId().equals(getUserId())){
            myPage=true;
            followingButton.setText("FOLLOWINGS");
            follwerButton.setText("FOLLOWERS");
            values = new String[postList.size()];
            int i=0;
            while(i<postList.size()){
                values[i]=postList.get(i++).getPostTitle();
            }
            listView.clearDisappearingChildren();
//            listView = (ListView)view.findViewById(R.id.list_userLayout);
//            adapter  = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, values);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new ItemClickedListener());

        }
        else{
            myPage=false;
            followingButton.setText("FOLLOW");
            follwerButton.setText("BACK");

            int i=0;
            MySqlHelper db = new MySqlHelper(getActivity(),null,null, 0);
            if(postOther==null)
                return;
            listOthers = db.getAllPosts(postOther.getUserId());
            values = new String[listOthers.size()];
            while(i<listOthers.size()){
                values[i]=listOthers.get(i++).getPostTitle();
            }
            listView.clearDisappearingChildren();
//            adapter  = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, values);
//            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new ItemClickedListener());
        }
        super.onCreate(getArguments());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    ListDialog searchList;
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.search_button){
            if(search.getText().toString().length()>0){
                MySqlHelper db1 = new MySqlHelper(getActivity(),null,null,0);
                List <User> userList = db1.getAllUsers();
                ArrayList<String>list = new ArrayList<String>();
                int i=0;
                String searchStr=search.getText().toString();
                while(i<userList.size()){
                    if(userList.get(i).getUserId().equals(search.getText().toString())||userList.get(i).getEmail().equals(searchStr)||
                            userList.get(i).getFirstName().equals(searchStr)||userList.get(i).getLastName().equals(searchStr)){
//                        setUser(userList.get(i).getUserId(),userList.get(i).getEmail());
                        list.add(userList.get(i).getUserId());
//                        Toast.makeText(getActivity(),"ENTERED",Toast.LENGTH_SHORT);
//                        UserDialog dialog = new UserDialog(userList.get(i));
//                        dialog.show(getActivity().getSupportFragmentManager(),"");

                        }i++;
                    }
                String[] values = new String[list.size()];
                i=0;
                while(i<values.length)
                    values[i]=list.get(i++);
                new ListDialog("Search Result",values).show(getActivity().getSupportFragmentManager(),"");
                db1.close();
                }

            }

        else if(v.getId()==R.id.following_button_userLayout){
            MySqlHelper db = new MySqlHelper(getActivity(),null,null,0);
           List<String> list=db.getAllFollowings(getUserId());
            int i=0;
            String values[]=new String[list.size()];
            while (i<list.size())
                values[i]=list.get(i++);

            new ListDialog("My Followings",values).show(getActivity().getSupportFragmentManager(),"");
            db.close();
        }
        else if(v.getId()==R.id.follwer_button_userLayout){
            MySqlHelper db = new MySqlHelper(getActivity(),null,null,0);

                List<String> list = db.getAllFollowers(getUserId());
                int i=0;
                values=new String[list.size()];
                while(i<list.size()){
                    values [i] = list.get(i++);
                }
                new ListDialog("My Followers" +
                        " List",values).show(getActivity().getSupportFragmentManager(),"");

            db.close();
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
