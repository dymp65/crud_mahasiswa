package com.praktisi.crudmahasiswa;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    FloatingActionButton fabAddNew;
    ListView listViewNames;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItem;
    ArrayList<String> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CRUD App");

        myDb = new DatabaseHelper(this);
        listItem = new ArrayList<>();
        ids = new ArrayList<>();

        fabAddNew = findViewById(R.id.fabAddNew);
        listViewNames = findViewById(R.id.listViewNames);

        viewAllData();

        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });

        listViewNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                intent.putExtra("ID", ids.get(position));
                startActivity(intent);
            }
        });
    }

    private void viewAllData() {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(MainActivity.this, "No data found", Toast.LENGTH_LONG).show();
            return;
        }

        while (res.moveToNext()) {
            ids.add(res.getString(0));
            listItem.add(res.getString(1));
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        listViewNames.setAdapter(adapter);
    }
}
