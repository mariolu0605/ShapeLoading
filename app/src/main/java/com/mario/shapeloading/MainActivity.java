package com.mario.shapeloading;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.mario.library.drawable.ShapeLoadingDrawable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.iv_shape_loading);

        ShapeLoadingDrawable shapeLoadingDrawable = new ShapeLoadingDrawable();
        imageView.setImageDrawable(shapeLoadingDrawable);
        shapeLoadingDrawable.start();
    }
}
