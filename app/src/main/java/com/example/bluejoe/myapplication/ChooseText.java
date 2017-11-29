package com.example.bluejoe.myapplication;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ChooseText extends AppCompatActivity {

    private static final String TAG = "ChooseText";
    private List<TextList> textList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_text);
        initTextList();
        writeDefaultFile();
        TextListAdapter adapter = new TextListAdapter(ChooseText.this,
                R.layout.text_list_item, textList);
        ListView listView = findViewById(R.id.text_list);
        listView.setAdapter(adapter);
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
                ArrayList<CharSequence> string = getString(inputStream);
                //Intent intent = new Intent(ChooseText.this, MarkText.class);
                Intent intent = new Intent(ChooseText.this, MarkText.class);
                intent.putExtra("string", string);
                startActivity(intent);
            }
        });
    }

    public static ArrayList<CharSequence> getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        ArrayList<CharSequence> string = new ArrayList<CharSequence>();
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
                if(!line.equals("")) string.add(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        string.add(sb.toString());
        return string;
    }

    private void initTextList() {
        TextList sample1 = new TextList("Sample Text 1", "texts/t0.txt");
        textList.add(sample1);
        TextList sample2 = new TextList("Sample Text 2", "texts/t1.txt");
        textList.add(sample2);
        TextList sample3 = new TextList("Sample Text 3", "texts/t2.txt");
        textList.add(sample3);
        TextList sample4 = new TextList("Sample Text 4", "texts/t3.txt");
        textList.add(sample4);
        TextList sample5 = new TextList("Sample Text 5", "texts/t4.txt");
        textList.add(sample5);
        textList.add(sample5);
        textList.add(sample5);
        textList.add(sample5);
        textList.add(sample5);
        textList.add(sample5);
        textList.add(sample5);
        textList.add(sample5);
        textList.add(sample5);
        textList.add(sample5);
        textList.add(sample5);
        textList.add(sample5);
    }

    public void writeDefaultFile() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "test.txt");
            FileOutputStream fos = new FileOutputStream(file);
            String info = "参考消息网11月2日报道 外媒称，在即将开始的亚洲之行期间，美国总统特朗普不会访问朝鲜与韩国之间的非军事区。";
            fos.write(info.getBytes());
            fos.close();
            Log.d(TAG, "writeDefaultFile: Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TextList {

        private String name;
        private String filename;

        TextList(String name, String filename) {
            this.name = name;
            this.filename = filename;
        }

        String getName() {
            return name;
        }

        String getFilename() {
            return filename;
        }
    }

    class TextListAdapter extends ArrayAdapter<TextList> {

        private int resourceId;

        TextListAdapter(@NonNull Context context, int resource, @NonNull List<TextList> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextList textList = getItem(position);
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textName = view.findViewById(R.id.text_name);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            assert textList != null;
            viewHolder.textName.setText(textList.getName());
            return view;
        }

        class ViewHolder {
            TextView textName;
        }
    }
}
