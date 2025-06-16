package com.example.electricitybillestimator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    ListView listViewHistory;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewHistory = findViewById(R.id.listViewHistory);
        dbHelper = new DatabaseHelper(this);

        loadHistory();
    }

    private void loadHistory() {
        Cursor cursor = dbHelper.getAllBills(); // returns _id, month, final_cost

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No records found.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] fromColumns = {
                DatabaseHelper.COLUMN_MONTH,
                DatabaseHelper.COLUMN_FINAL_COST
        };

        int[] toViews = {
                android.R.id.text1,
                android.R.id.text2
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                fromColumns,
                toViews,
                0
        );

        adapter.setViewBinder((view, cur, columnIndex) -> {
            if (view.getId() == android.R.id.text2) {
                double finalCost = cur.getDouble(columnIndex);
                ((TextView) view).setText(String.format("Final Cost: RM %.2f", finalCost));
                return true;
            }
            return false;
        });

        listViewHistory.setAdapter(adapter);

        // Optional: tap to view details
        listViewHistory.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
            intent.putExtra("RECORD_ID", (int) id);
            startActivity(intent);
        });
    }
}
