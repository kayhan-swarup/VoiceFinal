package com.swarup.kayhan.voice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Kayhan on 1/9/2015.
 */
public class RecentPosts extends Fragment {

    List<Post> posts;
    String[] values;
    ListView listView;
    public  RecentPosts(){




    }

    View view;
    ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recent_post,container,false);
        MySqlHelper db = new MySqlHelper(getActivity(),null,null,0);
        posts = db.getRecentPost(Login.user.getUserId());


        if(posts==null) return view;
            values = new String[posts.size()];
        for(int i=0;i<posts.size();i++)
            values[i]=posts.get(i).getPostTitle()+"  -  "+
                    db.getUser(posts.get(i).getUserId()).firstName+" "+
                    db.getUser(posts.get(i).getUserId()).lastName;
        listView = (ListView)view.findViewById(R.id.listView_recentPost);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ItemClickedListener());
        return view;
    }
    class ItemClickedListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            PostDialog postDialog = new PostDialog(posts.get(position).getPostId(),posts.get(position).getPostTitle(),posts.get(position).getPostText(),posts.get(position).getUserId());

            MySqlHelper db = new MySqlHelper(getActivity(),null,null,0);

            postDialog.show(getActivity().getSupportFragmentManager(),posts.get(position).getPostTitle());
            db.addView(Login.user.getUserId(),posts.get(position).postId);
            db.close();


        }
    }
}
