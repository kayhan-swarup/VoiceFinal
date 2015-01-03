package com.swarup.kayhan.voice;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Kayhan on 12/28/2014.
 */
public class MyAdapter extends BaseAdapter {

    public Context mContext;
    ArrayList<String> list;

    public MyAdapter(Context context, ArrayList<String> list){
        super();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
