package com.example.importagibis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        Button buttonSelectCsv = findViewById(R.id.button_select_csv);
        buttonSelectCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    openFileSelector();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "Request CODE:"+requestCode +" - GrantResults.legth: "+grantResults.length+ " - grantResults[0]: "+grantResults[0], Toast.LENGTH_SHORT).show();
       // if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFileSelector();
      //  } else {
       //     Toast.makeText(this, "Permissão de leitura de armazenamento negada", Toast.LENGTH_SHORT).show();
      //  }
    }

    private void openFileSelector() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()));
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                readCsvAndInsertToDb(uri);
            }
        }
    }

    private void readCsvAndInsertToDb(Uri uri) {
        try {
            System.out.println("URI: "+uri);
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(";");
                System.out.println("COLUNAS:" + columns.length);
                if (columns.length == 7) {
                    //int id = Integer.parseInt(columns[0].trim());
                    String titulo = columns[0].trim();
                    System.out.println("TITULO: "+titulo);
                    int numero = Integer.parseInt(columns[1].trim());
                    String serie = columns[2].trim();
                    String editora = columns[3].trim();
                    String imagem = columns[4].trim();
                    String ano = columns[5].trim();
                    int adquirido = Integer.parseInt(columns[6].trim());

                    ContentValues values = new ContentValues();
                    values.put("titulo",titulo);
                    values.put("numero",numero);
                    values.put("serie",serie);
                    values.put("editora",editora);
                    values.put("imagem",imagem);
                    values.put("ano",ano);
                    values.put("adquirido",adquirido);
                    db.insert("gibis",null,values);
                }
            }
            reader.close();
            Toast.makeText(this, "CSV importado com sucesso", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("MainActivity", "Erro ao ler o arquivo CSV", e);
            Toast.makeText(this, "Erro ao ler o arquivo CSV", Toast.LENGTH_SHORT).show();
        }
    }
}
