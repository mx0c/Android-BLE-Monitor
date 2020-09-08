package com.huc.android_ble_monitor.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.LogsUtil;


public class ApplicationLogActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {
    private ListView mListView;
    private Spinner mTagSpinner;
    private Spinner mFilterSpinner;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final String TAG = "BLEM_LogActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.logging_activity);
        ActivityUtil.setToolbar(this, false);
        ActivityUtil.setToolbarTitle(this, "Application logs");

        mSwipeRefreshLayout = findViewById(R.id.LogSwipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView = findViewById(R.id.logListView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, LogsUtil.readLogs("All", "All")));

        mTagSpinner = findViewById(R.id.tagSpinner);
        mTagSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.TagArray, android.R.layout.simple_spinner_item));
        mTagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tag = parent.getItemAtPosition(position).toString();
                String filter = mFilterSpinner.getSelectedItem().toString();
                mListView.setAdapter(new ArrayAdapter<String>(ApplicationLogActivity.this, android.R.layout.simple_list_item_1, LogsUtil.readLogs(filter, tag)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mFilterSpinner = findViewById(R.id.filterSpinner);
        mFilterSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.FilterArray, android.R.layout.simple_spinner_item));
        mFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = parent.getItemAtPosition(position).toString();
                String tag = mTagSpinner.getSelectedItem().toString();
                mListView.setAdapter(new ArrayAdapter<String>(ApplicationLogActivity.this, android.R.layout.simple_list_item_1, LogsUtil.readLogs(filter, tag)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onRefresh() {
        String filter = mFilterSpinner.getSelectedItem().toString();
        String tag = mTagSpinner.getSelectedItem().toString();
        mListView.setAdapter(new ArrayAdapter<String>(ApplicationLogActivity.this, android.R.layout.simple_list_item_1, LogsUtil.readLogs(filter, tag)));
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
