package com.example.game2d;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.game2d.GameView.screenRatioX;
import static com.example.game2d.GameView.screenRatioY;

public class Bullet {
    int x, y, width, height;
    Bitmap butllet;

    Bullet(Resources res){

        butllet = BitmapFactory.decodeResource(res, R.drawable.bullet);

            width = butllet.getWidth();
            height = butllet.getHeight();

            width /= 4;
            height /=4;

            width = (int) (width * screenRatioX);
            height = (int) (height * screenRatioY);

            butllet = Bitmap.createScaledBitmap(butllet, width, height, false);
    }
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
