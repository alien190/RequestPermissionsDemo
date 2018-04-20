package com.example.ivanovnv.requestpermissionsdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int WRITE_PERMISSION_RC = 123;
    private EditText mInput;
    private Button mWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInput = findViewById(R.id.et_input);
        mWrite = findViewById(R.id.btn_write);

        mWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToWrite = mInput.getText().toString();
                writeToFileIfNotEmpty(textToWrite);

            }
        });
    }

    private void writeToFileIfNotEmpty(String textToWrite) {
        if(TextUtils.isEmpty(textToWrite)) {
            Toast.makeText(this, "text is empty", Toast.LENGTH_SHORT).show();
        } else {
            writeToFileWithPermissionIfNeeded(textToWrite);
        }
    }

    private void writeToFileWithPermissionIfNeeded(String textToWrite) {
        if(isWritePermissionGranted()) {
            writeToFile(textToWrite);
        } else {
           requestWritePermission();
        }
    }

    private void requestWritePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setMessage("Без разрешения невозможно записать текст в файл")
                    .setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_RC);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_RC);
        }
    }

    private void writeToFile(String textToWrite) {
        Toast.makeText(this, textToWrite + "is written to file", Toast.LENGTH_SHORT).show();
    }

    private boolean isWritePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode != WRITE_PERMISSION_RC) return;
        if (grantResults.length !=1 ) return;

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            String textToWrite = mInput.getText().toString();
            writeToFile(textToWrite);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Вы можете дать разрешение в настройка устройства")
                    .setPositiveButton("Понятно", null)
                    .show();
        }

    }
}
