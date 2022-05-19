package com.example.game2d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListHighScoreActivity extends AppCompatActivity {

    private static String TAG = "ListHighScoreActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_high_score);
        Log.d(TAG, "onCreate: Started");

        findViewById(R.id.returnMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentScore = new Intent(ListHighScoreActivity.this, MainActivity.class);
                startActivity(intentScore);
                finish();
            }
        });

        //Lay data
        String[] field = new String[1];
        field[0] = "username";

        //Creating array for data
        String[] data = new String[1];
        data[0] = "0";

        PutData putData = new PutData("http://192.168.1.7:8080/doan/DoAnAnroid/getdata.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                ArrayList<PlayerScore> playerScoreArrayList = new ArrayList<>();
                try {
                    JSONArray arr = new JSONArray(result);
                    int i ;
                    for ( i = 0; i < arr.length(); i++ ){
                        PlayerScore playerScore = new PlayerScore(arr.getJSONObject(i).getString("player"), arr.getJSONObject(i).getString("score"));
                        playerScoreArrayList.add(playerScore);
                    }

                    ListView mListView = (ListView) findViewById(R.id.listView);

                    PlayerScoreListAdapter adapter = new PlayerScoreListAdapter(this, R.layout.adapter_view_layout, playerScoreArrayList);
                    mListView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
