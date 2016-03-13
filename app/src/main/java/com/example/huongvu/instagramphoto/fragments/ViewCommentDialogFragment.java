package com.example.huongvu.instagramphoto.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.example.huongvu.instagramphoto.R;
import com.example.huongvu.instagramphoto.models.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HUONGVU on 3/13/2016.
 */
public class ViewCommentDialogFragment extends DialogFragment{

    ListView listView;
    Adapter commentAdapter;
    List<Comment> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_main, container, false);
        listView = (ListView)v.findViewById(R.id.lvPhotos);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListComments();
    }
    private void getListComments(){
        list = new ArrayList<Comment>();
        //listView.setAdapter(commentAdapter);
    }
}
