package com.swarup.kayhan.voice;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Kayhan on 1/3/2015.
 */


public class Login extends Fragment {
    View view;
    MySqlHelper dbHelper;

    public static User user;
    EditText userId,password,userName,userNameLogin,passwordLogin,firstName,lastName,email;
    RadioButton male,female;
    Button signUp,login;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login,container,false);
        userId = (EditText)view.findViewById(R.id.userId_signup);
        password= (EditText)view.findViewById(R.id.password_signup);
        firstName = (EditText)view.findViewById(R.id.firstName_signup);
        lastName = (EditText)view.findViewById(R.id.lastName_signup);
        email = (EditText)view.findViewById(R.id.email_signup);
        male = (RadioButton)view.findViewById(R.id.male_signUp);
        female = (RadioButton)view.findViewById(R.id.female_signUp);

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
                if(!userId.getText().equals("")&&!password.equals("")&&email.getText().toString().length()>0&&firstName.getText().length()>0){
                    user = new User();
                    user.setUserId(userId.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.setFirstName(firstName.getText().toString());
                    user.setLastName(lastName.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setSex(female.isChecked()?"FEMALE":"MALE");
                    if(dbHelper!=null)
                        dbHelper.addUser(user);
                }
                dbHelper.close();
            }else if(v.getId()==R.id.login_button){

                dbHelper = new MySqlHelper(getActivity(),null,null,0);
                List<User> list = dbHelper.getAllUsers();
                Toast.makeText(getActivity(),"List Count: "+list.size(),Toast.LENGTH_LONG).show();
                if(list.size()>0){

                    int i=0;
                    while(i<list.size()){
                        user = list.get(i++);
                        if(user.getUserId().equals(userNameLogin.getText().toString())&&user.getPassword().equals(passwordLogin.getText().toString())){
                            Toast.makeText(getActivity(),"SUCCESS!",Toast.LENGTH_LONG).show();

                            dbHelper.addFollow(user.getUserId(),user.getUserId());
                            startActivity(new Intent(getActivity(), ProfileActivity.class));
                            dbHelper.close();
                            break;
                        }
                    }
            }
//                user = dbHelper.getUser(0);


            }
        }
    }
}
