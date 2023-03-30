package com.example.qrgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentAdapter extends ArrayAdapter {


    public CommentAdapter(@NonNull Context context, ArrayList<Comment> comments) {
        super(context, 0,comments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;

        if (convertView==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_content,parent,false);
        }
        else{
            view=convertView;
        }



        TextView point =view.findViewById(R.id.inventory_qr_point);
        TextView name=view.findViewById(R.id.inventory_qr_name);

        Comment item=(Comment)getItem(position);

        point.setText("comment:"+item.getComment());
        name.setText("name:"+item.getName());



        return view;
    }
}
