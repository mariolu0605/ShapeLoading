package com.mario.shapeloading;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.mario.library.drawable.DouYinLoadingDrawable;
import com.mario.library.drawable.ShapeLoadingDrawable;
import com.mario.library.drawable.ThreeBallsLoadingDrawable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.iv_shape_loading);

//        ShapeLoadingDrawable shapeLoadingDrawable = new ShapeLoadingDrawable();
//        imageView.setImageDrawable(shapeLoadingDrawable);
//        shapeLoadingDrawable.start();

//        ThreeBallsLoadingDrawable threeBallsLoadingDrawable = new ThreeBallsLoadingDrawable();
//        imageView.setImageDrawable(threeBallsLoadingDrawable);
//        threeBallsLoadingDrawable.start();

        DouYinLoadingDrawable douYinLoadingDrawable = new DouYinLoadingDrawable();
        imageView.setImageDrawable(douYinLoadingDrawable);
        douYinLoadingDrawable.start();
    }
}
