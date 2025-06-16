package com.example.electricitybillestimator;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView github = findViewById(R.id.textGitHub);
        github.setText("https://github.com/aisyahhazee/Electricity_Bill_Estimator");
    }
}
