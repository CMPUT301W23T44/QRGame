package com.example.qrgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SearchUserDetailAdapater extends ArrayAdapter {
    public SearchUserDetailAdapater(@NonNull Context context, ArrayList<String> Namelist) {
        super(context,0, Namelist);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView==null){
            view= LayoutInflater.from(super.getContext()).inflate(R.layout.searchqrdetail_content,parent,false);
        }
        else{
            view=convertView;
        }

        String item = (String) getItem(position);

        TextView qrname = view.findViewById(R.id.textView2);

        qrname.setText(item);

        return view;
    }
}
