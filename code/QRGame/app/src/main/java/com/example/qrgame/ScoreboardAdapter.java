package com.example.qrgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ScoreboardAdapter extends ArrayAdapter {

    public ScoreboardAdapter(Context context, ArrayList<ScoreRank> ScList) {
        super(context, 0,ScList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;

        if (convertView==null){
            view= LayoutInflater.from(super.getContext()).inflate(R.layout.scoreboard_content,parent,false);
        }
        else{
            view=convertView;
        }
        ScoreRank item=(ScoreRank) getItem(position);

        TextView score=view.findViewById(R.id.scoreboard_ct_score);
        TextView name=view.findViewById(R.id.scoreboard_ct_name);
        TextView rank = view.findViewById(R.id.scoreboard_ct_rank);


        score.setText(item.GetScoreString());
        name.setText(item.GetName());
        rank.setText(item.GetRankString());

        return view;

    }
}
