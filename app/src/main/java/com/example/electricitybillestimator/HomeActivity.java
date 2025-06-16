package com.example.electricitybillestimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button btnGoToMain, btnGoToAbout, btnGoToHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnGoToMain = findViewById(R.id.btnGoToMain);
        btnGoToAbout = findViewById(R.id.btnGoToAbout);
        btnGoToHistory = findViewById(R.id.btnGoToHistory);

        btnGoToMain.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        btnGoToAbout.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        btnGoToHistory.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
    }
}
