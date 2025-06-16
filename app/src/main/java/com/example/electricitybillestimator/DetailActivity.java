package com.example.electricitybillestimator;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView textMonth, textUnits, textRebate, textTotalCharges, textFinalCost;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textMonth = findViewById(R.id.textMonth);
        textUnits = findViewById(R.id.textUnits);
        textRebate = findViewById(R.id.textRebate);
        textTotalCharges = findViewById(R.id.textTotalCharges);
        textFinalCost = findViewById(R.id.textFinalCost);

        dbHelper = new DatabaseHelper(this);

        int recordId = getIntent().getIntExtra("RECORD_ID", -1);
        if (recordId != -1) {
            loadDetail(recordId);
        } else {
            Toast.makeText(this, "Error loading details.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadDetail(int id) {
        Cursor cursor = dbHelper.getBillById(id);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Use DatabaseHelper constants for column names
                String month = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MONTH));
                int units = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UNITS));
                double rebate = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REBATE));
                double totalCharges = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TOTAL_CHARGES));
                double finalCost = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FINAL_COST));

                textMonth.setText("Month: " + month);
                textUnits.setText("Units Used: " + units);
                textRebate.setText("Rebate: " + rebate + "%");
                textTotalCharges.setText(String.format("Total Charges: RM %.2f", totalCharges));
                textFinalCost.setText(String.format("Final Cost: RM %.2f", finalCost));
            } else {
                Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show();
                finish();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
