package com.nvd.applocker;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LanguageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);


        RelativeLayout vn = findViewById(R.id.vietnam);
        LinearLayout chosevn = findViewById(R.id.choseVietnam);

        RelativeLayout en = findViewById(R.id.english);
        LinearLayout choseen = findViewById(R.id.choseEnglish);

        ImageView next = findViewById(R.id.ivNext);
        ImageView tick = findViewById(R.id.ivTick);

        TextView textViewen = findViewById(R.id.texten);
        TextView textViewvn = findViewById(R.id.textvn);

        if (Settings.canDrawOverlays(this)){
            next.setVisibility(View.GONE);
            tick.setVisibility(View.VISIBLE);
        }


        LanguageManager lang = new LanguageManager(this);
        String check = lang.getLang();
        if (check.equals("vi")){
            textViewvn.setVisibility(View.VISIBLE);
            textViewen.setVisibility(View.INVISIBLE);
        }
        if (check.equals("en")){
            textViewvn.setVisibility(View.INVISIBLE);
            textViewen.setVisibility(View.VISIBLE);
        }


        vn.setOnClickListener(view ->
                {
                    lang.updateResource("vi");
                    chosevn.setVisibility(View.VISIBLE);
                    choseen.setVisibility(View.INVISIBLE);
                    textViewvn.setVisibility(View.VISIBLE);
                    textViewen.setVisibility(View.INVISIBLE);
                }
                );
        en.setOnClickListener(view ->
                {
                    lang.updateResource("en");
                    choseen.setVisibility(View.VISIBLE);
                    chosevn.setVisibility(View.INVISIBLE);
                    textViewvn.setVisibility(View.INVISIBLE);
                    textViewen.setVisibility(View.VISIBLE);
                }
        );
        next.setOnClickListener(view ->{
            Intent intent = new Intent(LanguageActivity.this,AppintroAcivity.class);
            startActivity(intent);
        });
        tick.setOnClickListener(view ->{
            Intent intent = new Intent(LanguageActivity.this,MainActivity.class);
            startActivity(intent);
        });
    }


}
