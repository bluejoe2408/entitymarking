package com.example.bluejoe.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView title = findViewById(R.id.title);
        final Button button_start = findViewById(R.id.button_start);
        final Typeface ventouse = Typeface.createFromAsset(getAssets(), "fonts/ventouse.ttf");
        final Animation translateAnimation = AnimationUtils.loadAnimation(this, R.anim.translate);
        final Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha);

        title.setTypeface(ventouse);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_start.startAnimation(translateAnimation);
                button_start.startAnimation(alphaAnimation);
                Intent intent = new Intent(MainActivity.this, ChooseText.class);
                startActivity(intent);
            }
        });
    }
}