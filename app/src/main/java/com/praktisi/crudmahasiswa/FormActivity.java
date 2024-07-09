package com.praktisi.crudmahasiswa;

import android.content.Intent;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;

public class FormActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName, editAddress;
    Spinner spinnerGender;
    Button btnSave, btnDelete;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Form Mahasiswa");

        myDb = new DatabaseHelper(this);

        editName = findViewById(R.id.editTextName);
        spinnerGender = findViewById(R.id.spinnerGender);
        editAddress = findViewById(R.id.editTextAddress);
        btnSave = findViewById(R.id.buttonSave);
        btnDelete = findViewById(R.id.buttonDelete);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");

        if (id != null) {
            loadData();
            btnSave.setText("Perbaharui");
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnSave.setText("Simpan");
            btnDelete.setVisibility(View.GONE);
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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete();
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
        if (isInserted) {
            Toast.makeText(FormActivity.this, "Data Berhasil Disimpan", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
        }
        else{
            Toast.makeText(FormActivity.this, "Penyimpanan Data Gagal", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
        }

        finish();
    }

    private void updateData() {
        boolean isUpdate = myDb.updateData(id, editName.getText().toString(), spinnerGender.getSelectedItem().toString(), editAddress.getText().toString());
        if (isUpdate){
            Toast.makeText(FormActivity.this, "Data Berhasil Diupdated", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
        }
        else{
            Toast.makeText(FormActivity.this, "Update Data Gagal", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
        }

        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Data")
                .setMessage("Apakah Yakin Untuk Hapus Data Ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData();
                    }
                })
                .setNegativeButton("Tidak", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteData() {
        Integer deletedRows = myDb.deleteData(id);
        if (deletedRows > 0) {
            Toast.makeText(FormActivity.this, "Data Berhasil Dihapus", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
        } else {
            Toast.makeText(FormActivity.this, "Hapus Data Gagal", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}

