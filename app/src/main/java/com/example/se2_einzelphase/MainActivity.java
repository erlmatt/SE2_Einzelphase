package com.example.se2_einzelphase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private EditText input;
    private final static String host = "se2-submission.aau.at";

    private final static int port = 20080;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v)
            {
                try {
                    TextView output = findViewById(R.id.output);
                    EditText input = findViewById(R.id.input);
                    output.setText(istGerade(alternierendeQuersumme(input.getText().toString())));
                    TextView textResult = findViewById(R.id.outputTwo);

                    getNetworkCall()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<String>() {
                                String result = "";
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                }

                                @Override
                                public void onNext(@NonNull String s) {
                                    result += s;
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    e.printStackTrace();
                                    textResult.setText(e.getMessage());
                                }

                                @Override
                                public void onComplete() {
                                    textResult.setText(result);
                                }
                            });
                } catch (Exception ex) {
                    TextView textResult = findViewById(R.id.output);
                    textResult.setText(ex.getMessage());
                }

            }
        });



    }

    public Observable<String> getNetworkCall() {
        return Observable.create(emitter -> {
            try {
                Socket socket = new Socket(host, port);
                String matriculationNumber = ((EditText) findViewById(R.id.input)).getText().toString();

                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write(matriculationNumber + "\n");
                out.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line).append("\n");
                }

                emitter.onNext(responseBuilder.toString());

                out.close();
                reader.close();
                socket.close();
                emitter.onComplete();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                emitter.onError(e);
            }
        });
    }


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

    private String istGerade(int zahl) {
        String result;
        if (zahl % 2 == 0) {
            result = "Die alternierende Quersumme ist gerade";
        } else {
            result = "Die alternierende Quersumme ist ungerade";
        }
        return result;
    }

    @Override
    public boolean navigateUpTo(Intent upIntent) {
        return super.navigateUpTo(upIntent);
    }
}