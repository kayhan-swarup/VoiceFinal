package com.swarup.kayhan.voice;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Kayhan on 1/3/2015.
 */


public class Login extends Fragment {
    View view;
    MySqlHelper dbHelper;

    User user;
    EditText userId,password,userName,userNameLogin,passwordLogin;

    Button signUp,login;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login,container,false);
        userId = (EditText)view.findViewById(R.id.userId_signup);
        password= (EditText)view.findViewById(R.id.password_signup);
        userName = (EditText)view.findViewById(R.id.fullname);

        signUp = (Button)view.findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(new MyListener());
        login = (Button)view.findViewById(R.id.login_button);
        login.setOnClickListener(new MyListener());
        userNameLogin = (EditText)view.findViewById(R.id.userid_login);
        passwordLogin = (EditText)view.findViewById(R.id.password_login);
        return view;
    }
    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.sign_up_button){
                Toast.makeText(getActivity(), "DONE!", Toast.LENGTH_LONG).show();
                dbHelper = new MySqlHelper(getActivity(),null,null,0);
                if(!userId.getText().equals("")&&!password.equals("")){
                    user = new User();
                    user.setUserId(userId.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.setUserName(userName.getText().toString());
                    user.setEmail("");
                    if(dbHelper!=null)
                        dbHelper.addUser(user);
                }
            }else if(v.getId()==R.id.login_button){
                dbHelper = new MySqlHelper(getActivity(),null,null,0);
                List<User> list = dbHelper.getAllBooks();
                Toast.makeText(getActivity(),"List Count: "+list.size()+"UserName: "+list.get(0).userName+""+list.get(0).password,Toast.LENGTH_LONG).show();
                while(!list.isEmpty()){

                    User user= list.remove(0);
                    Toast.makeText(getActivity(),user.userName+" "+user.password,Toast.LENGTH_LONG).show();
                    if(user.userName.equals(userNameLogin.getText().toString())&&user.password.equals(passwordLogin.getText().toString())){
                        Toast.makeText(getActivity(),"DONE!",Toast.LENGTH_LONG).show();
                    }else Toast.makeText(getActivity(),"NOPE!",Toast.LENGTH_LONG).show();
                }Toast.makeText(getActivity(),"NOPE!",Toast.LENGTH_LONG).show();

//                user = dbHelper.getUser(0);

            }
        }
    }
}
