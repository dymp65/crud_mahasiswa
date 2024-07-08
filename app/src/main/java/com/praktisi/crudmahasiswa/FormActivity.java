package com.praktisi.crudmahasiswa;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName, editAddress;
    Spinner spinnerGender;
    Button btnSave;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        myDb = new DatabaseHelper(this);

        editName = findViewById(R.id.editTextName);
        spinnerGender = findViewById(R.id.spinnerGender);
        editAddress = findViewById(R.id.editTextAddress);
        btnSave = findViewById(R.id.buttonSave);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");

        if (id != null) {
            loadData();
            btnSave.setText("Update");
        } else {
            btnSave.setText("Save");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id != null) {
                    updateData();
                } else {
                    saveData();
                }
            }
        });
    }

    private void loadData() {
        Cursor res = myDb.getAllData();
        if (res.moveToFirst()) {
            do {
                if (res.getString(0).equals(id)) {
                    editName.setText(res.getString(1));
                    if (res.getString(2).equals("Male")) {
                        spinnerGender.setSelection(0);
                    } else {
                        spinnerGender.setSelection(1);
                    }
                    editAddress.setText(res.getString(3));
                    break;
                }
            } while (res.moveToNext());
        }
    }

    private void saveData() {
        boolean isInserted = myDb.insertData(editName.getText().toString(), spinnerGender.getSelectedItem().toString(), editAddress.getText().toString());
        if (isInserted)
            Toast.makeText(FormActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(FormActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
        finish();
    }

    private void updateData() {
        boolean isUpdate = myDb.updateData(id, editName.getText().toString(), spinnerGender.getSelectedItem().toString(), editAddress.getText().toString());
        if (isUpdate)
            Toast.makeText(FormActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(FormActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
        finish();
    }
}

