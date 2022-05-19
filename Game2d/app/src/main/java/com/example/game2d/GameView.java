package com.example.game2d;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private Background background1, background2;
    private int screenX;
    private int screenY;
    private int score = 0;
    // public de class khac nhan duoc screenRatio
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Flight flight;
    private List<Bullet> bullets;
    private Bird[] birds;
    private Random random;
    private SharedPreferences prefs;
    private GameActivity activity;
    //Tieng game
    private SoundPool soundPool;
    private int sound;
    private String player;


    public GameView(GameActivity activity, int screenX, int screenY, String player) {
        super(activity);

        this.activity = activity;
        this.player = player;

        //Score ban dau
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        //constructor cho soundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        flight = new Flight(this, screenY, getResources());

        bullets = new ArrayList<>();

        background2.x = screenX;

        paint = new Paint();
        //set text va mau` cho color
        paint.setTextSize(100);
        paint.setColor(Color.BLACK);

        //1 luc co 4 con chim
        birds = new Bird[2];
        for (int i = 0; i < 2; i++) {
            Bird bird = new Bird(getResources());
            birds[i] = bird;
        }

        //constructer cho random
        random = new Random();
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        // ma de update khi muon may bay bay len hoac ha xuong
        if (flight.isGoingUp)
            flight.y -= 30 * screenRatioY;
        else
            flight.y += 30 * screenRatioY;

        //dung de ngan may bay bay ra khoi man hinh
        if (flight.y < 0) {
            flight.y = 0;
        }

        if (flight.y > screenY - flight.height)
            flight.y = screenY - flight.height;

        //trash list dung sau khi remove bullet vuot qua screen
        List<Bullet> trash = new ArrayList<>();
        for (Bullet bullet : bullets) {

            if (bullet.x > screenX) {
                trash.add(bullet);

            }
            bullet.x += 50 * screenRatioX;

            //khi dan ban vao chim thi se chet
            for (Bird bird : birds) {

                if (Rect.intersects(bird.getCollisionShape(), bullet.getCollisionShape())) {

                    score++;
                    bird.x = -500;
                    bullet.x = screenX + 500;
                    bird.wasShot = true;

                }

            }
        }
        for (Bullet bullet : trash) {
            bullets.remove(bullet);
        }

        for (Bird bird : birds) {
            bird.x -= bird.speed;

            if (bird.x + bird.width < 0) {

                if (!bird.wasShot) {
                    isGameOver = true;
                    return;
                }

                int bound = (int) (30 * screenRatioX);
                bird.speed = random.nextInt(bound);

                if (bird.speed < 10 * screenRatioX) {
                    bird.speed = (int) (10 * screenRatioX);
                }
                bird.x = screenX;
                bird.y = random.nextInt(screenY - bird.height);

                bird.wasShot = false;
            }

            //
            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {
                isGameOver = true;
                return;
            }
        }
    }

    // draw hinh anh len tren thiet bi
    private void draw() {
        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Bird bird : birds) {
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);
            }

//            score

            canvas.drawText( player + " " + score + "", screenX / 3f, 164, paint);
            //game over
            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);

                // SCore cho game over
                saveIfHighScore();
                updateIfGreater();
                waitBeforeExiting();
                return;
            }

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Bullet bullet : bullets) {
                canvas.drawBitmap(bullet.butllet, bullet.x, bullet.y, paint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    //Exit
    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    //function luu lai diem cao nhat
    private void saveIfHighScore() {
        if (prefs.getInt("highscore", 0) < score) {

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // On touch event dùng để điều khiển máy bay bay lên hoặc bay xuống
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                if (event.getX() < screenX / 2)
                    flight.toShoot++;
                break;
        }

        return true;
    }

    public void newBullet() {

        // phat am thanh khi ban
        if (!prefs.getBoolean("isMute", false)) {
            soundPool.play(sound, 1, 1, 0, 0, 1);
        }

        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);
        bullets.add(bullet);
    }

    public void updateIfGreater() {
        //                checkPlayer();
        final String highestScore = String.valueOf(score);
        final String finalPlayer = player;
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
                data[0] = finalPlayer;
                data[1] = highestScore;

                PutData putData = new PutData("http://192.168.1.7:8080/doan/DoAnAnroid/update.php", "POST", field, data);
                putData.startPut();
                putData.onComplete();
                //End Write and Read data with URL
            }
        });
    }


}
