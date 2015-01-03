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
public class PostDialog extends DialogFragment {
    String title;
    String text;
    String userName;

    @SuppressLint("ValidFragment")
    public PostDialog(String title, String text, String userName) {
        this.title = title;
        this.text = text;
        this.userName = userName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_dialog,container,false);

//        ((TextView)view.findViewById(R.id.title_postDialog)).setText(title);
        getDialog().setTitle(title);
        ((TextView)view.findViewById(R.id.postText_editText_postDialog)).setText(text);
        ((Button)view.findViewById(R.id.ok_button_dialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
