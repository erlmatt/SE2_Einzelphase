package com.example.se2_einzelphase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    private EditText input;
    private final static String host = "se2-submission.aau.at";
    private final static int port = 20080;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    //input = (EditText) findViewById(R.id.input);
    //TextView output = (TextView) findViewById(R.id.output);

    private int alternierendeQuersumme(String matrikelnummer){
        int alternierendeQuersumme = 0;
        for(int i = 0; i < matrikelnummer.length(); i++){
            int ziffer = Integer.parseInt(String.valueOf(matrikelnummer.charAt(i)));
            if(i % 2 == 0){
                alternierendeQuersumme += ziffer;
            }else {
                alternierendeQuersumme -= ziffer;
            }

        }
        return alternierendeQuersumme;
    };

    private boolean istGerade(int zahl){
        if (zahl % 2 == 0) return true;
        return false;
    }



}