package com.example.electricitybillestimator;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText editTextUnits, editTextRebate;
    TextView textViewCharges, textViewFinalCost;
    Button buttonCalculate, buttonHistory, buttonAbout;

    DatabaseHelper dbHelper;

    String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        editTextUnits = findViewById(R.id.editTextUnits);
        editTextRebate = findViewById(R.id.editTextRebate);
        textViewCharges = findViewById(R.id.textViewCharges);
        textViewFinalCost = findViewById(R.id.textViewFinalCost);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonHistory = findViewById(R.id.buttonHistory);
        buttonAbout = findViewById(R.id.buttonAbout); // make sure this ID exists in your XML

        dbHelper = new DatabaseHelper(this);

        // Spinner setup
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        // Calculate Button
        buttonCalculate.setOnClickListener(v -> calculateBill());

        // History Button
        buttonHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // About Button
        buttonAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void calculateBill() {
        String month = spinnerMonth.getSelectedItem().toString();
        String unitStr = editTextUnits.getText().toString().trim();
        String rebateStr = editTextRebate.getText().toString().trim();

        if (unitStr.isEmpty() || rebateStr.isEmpty()) {
            Toast.makeText(this, "Please enter both units and rebate.", Toast.LENGTH_SHORT).show();
            return;
        }

        int units;
        double rebate;

        try {
            units = Integer.parseInt(unitStr);
            rebate = Double.parseDouble(rebateStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input format.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rebate < 0 || rebate > 5) {
            Toast.makeText(this, "Rebate must be between 0% and 5%.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalCharges = calculateTotalCharges(units);
        double finalCost = totalCharges - (totalCharges * rebate / 100.0);

        textViewCharges.setText(String.format("Total Charges: RM %.2f", totalCharges));
        textViewFinalCost.setText(String.format("Final Cost After Rebate: RM %.2f", finalCost));

        long result = dbHelper.insertBill(month, units, rebate, totalCharges, finalCost);
        if (result != -1) {
            Toast.makeText(this, "Record saved successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save data.", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateTotalCharges(int units) {
        double total = 0;
        if (units <= 200) {
            total = units * 0.218;
        } else if (units <= 300) {
            total = (200 * 0.218) + (units - 200) * 0.334;
        } else if (units <= 600) {
            total = (200 * 0.218) + (100 * 0.334) + (units - 300) * 0.516;
        } else {
            total = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + (units - 600) * 0.546;
        }
        return total;
    }
}
