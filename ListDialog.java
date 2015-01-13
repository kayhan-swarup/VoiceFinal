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
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Kayhan on 1/4/2015.
 */
@SuppressLint("ValidFragment")
public class ListDialog extends DialogFragment {
    String title;
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] values=null;

    @SuppressLint("ValidFragment")
    public ListDialog(String title,String[] values){
        this.title = title;
        this.values = values;
    }

    View view;
    TextView infoTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_dialog,container,false);
        listView = (ListView)view.findViewById(R.id.list_dialog);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,values);
        listView.setAdapter(adapter);
            listView.setOnItemClickListener(new MySearchListener());
        getDialog().setTitle(title);
        return view;
    }
    class MySearchListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MySqlHelper db = new MySqlHelper(getActivity(),null,null,0);
            User user = db.getUser(values[position]);
            new UserDialog(user).show(getActivity().getSupportFragmentManager(),"");
            getDialog().dismiss();
        }
    }
}
