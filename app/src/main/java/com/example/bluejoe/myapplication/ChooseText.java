package com.example.bluejoe.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ChooseText extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 2;
    private static final String TAG = "ChooseText";
    private List<TextList> textList = new ArrayList<>();
    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_text);
        initTextList();
        Button button_choose = (Button) findViewById(R.id.button_choose);
        TextListAdapter adapter = new TextListAdapter(ChooseText.this,
                R.layout.text_list_item, textList);
        ListView listView = (ListView) findViewById(R.id.text_list);
        listView.setAdapter(adapter);
        button_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Choose a txt file
                if (ContextCompat.checkSelfPermission(ChooseText.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChooseText.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    Log.d(TAG, "onClick: 000");
                } else {
                    openFileManager();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextList textItem = textList.get(position);
                InputStream inputStream = null;
                try {
                    inputStream = getAssets().open(textItem.getFilename());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String string = getString(inputStream);
                Intent intent = new Intent(ChooseText.this, MarkText.class);
                intent.putExtra("string", string);
                startActivity(intent);
            }
        });
    }

    private void openFileManager() {
        Log.d(TAG, "openFileManager: 222");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
            Log.d(TAG, "openFileManager: 333");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: 444");
        String path = null;
        switch (requestCode) {
            case FILE_SELECT_CODE:
                Log.d(TAG, "onActivityResult: 555");
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    assert uri != null;
                    if ("content".equalsIgnoreCase(uri.getScheme())) {
                        String[] projection = { "_data" };
                        Cursor cursor = getContentResolver().query(uri, projection,null, null, null);
                        assert cursor != null;
                        int column_index = cursor.getColumnIndexOrThrow("_data");
                        if (cursor.moveToFirst()) {
                            path =  cursor.getString(column_index);
                            cursor.close();
                        }
                    } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                        path = uri.getPath();
                    }
                }
                break;
            default:
                break;
        }
        // Read string from path
        string = path;
        Intent intent = new Intent(ChooseText.this, MarkText.class);
        intent.putExtra("string", string);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: 111");
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFileManager();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void initTextList() {
        TextList sample1 = new TextList("Sample Text 1", "t0.txt");
        textList.add(sample1);
        TextList sample2 = new TextList("Sample Text 2", "t1.txt");
        textList.add(sample2);
        TextList sample3 = new TextList("Sample Text 3", "t2.txt");
        textList.add(sample3);
        TextList sample4 = new TextList("Sample Text 4", "t3.txt");
        textList.add(sample4);
        TextList sample5 = new TextList("Sample Text 5", "t4.txt");
        textList.add(sample5);
    }

    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        assert inputStreamReader != null;
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
