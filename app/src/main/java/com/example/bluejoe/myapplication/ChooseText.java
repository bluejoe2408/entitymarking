package com.example.bluejoe.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private List<TextList> textList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_text);
        initTextList();
        String string;
        Button button_choose = (Button) findViewById(R.id.button_choose);
        TextListAdapter adapter = new TextListAdapter(ChooseText.this,
                R.layout.text_list_item, textList);
        ListView listView = (ListView) findViewById(R.id.text_list);
        listView.setAdapter(adapter);
        button_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Choose a txt file
                Intent intent = new Intent(ChooseText.this, MarkText.class);
                startActivity(intent);
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
