package com.example.game2d;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlayerScoreListAdapter extends ArrayAdapter<PlayerScore> {
    private static String TAG = "PlayerScoreListAdapter";

    private Context mContext;
    int mResource;

    public PlayerScoreListAdapter(Context context, int resource, ArrayList<PlayerScore> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,ViewGroup parent) {

        //get player information
        String player = getItem(position).getPlayer();
        String score = getItem(position).getScore();

        //create player object with information
        PlayerScore playerScore = new PlayerScore(player, score);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvPlayer = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvScore = (TextView) convertView.findViewById(R.id.textView2);

        tvPlayer.setText(player);
        tvScore.setText(score);

        return convertView;

    }
}















