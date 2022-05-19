package com.example.game2d;

import android.content.Intent;
import android.content.SharedPreferences;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class MainActivity extends AppCompatActivity {

    private boolean isMute;
    TextInputEditText textInputEditTextPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set full scene for the game
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        textInputEditTextPlayer = findViewById(R.id.player);

        //thong ke so diem
        findViewById(R.id.legendaryBoard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentScore = new Intent(MainActivity.this, ListHighScoreActivity.class);
                startActivity(intentScore);
                finish();
            }
        });

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPlayer();
            }
        });

        TextView highScoreTxt = findViewById(R.id.highScoreTxt);

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highScoreTxt.setText("HighScore: " + prefs.getInt("highscore", 0));

        isMute = prefs.getBoolean("isMute", false);

        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);

        if (isMute)
            volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
        else
            volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);

        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isMute = !isMute;
                if (isMute)
                    volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
                else
                    volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();
            }

        });

    }


    //viet player vao du lieu
    private void checkPlayer() {
        final String player;
        player = String.valueOf(textInputEditTextPlayer.getText());
        final String score = "0";
        if (!player.equals("")) {
            //Start ProgressBar first (Set visibility VISIBLE)
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[2];
                    field[0] = "player";
                    field[1] = "score";
                    //Creating array for data
                    String[] data = new String[2];
                    data[0] = player;
                    data[1] = score;
                    PutData putData = new PutData("http://192.168.1.7:8080/doan/DoAnAnroid/update.php", "POST", field, data);

                    putData.startPut();
                    putData.onComplete();
                    String result = putData.getResult();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra("getPlayer", player);
                    startActivity(intent);
                    finish();
                    //End Write and Read data with URL
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Enter player name!!!", Toast.LENGTH_SHORT).show();
        }
    }


}