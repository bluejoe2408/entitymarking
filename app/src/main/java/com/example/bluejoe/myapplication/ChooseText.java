package com.example.bluejoe.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
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
                ArrayList<CharSequence> string = splitText(inputStream);
                Intent intent = new Intent(ChooseText.this, MarkText.class);
                intent.putExtra("type", "text");
                intent.putExtra("string", string);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < textList.size() - 1; i++) {
            textList.get(i).updateMentioned();
        }
    }

    public static ArrayList<CharSequence> splitText(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        ArrayList<CharSequence> string = new ArrayList<>();
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

    String getString(String filename) {
        InputStream inputStream;
        try {
            inputStream = getAssets().open(filename);
            return MainActivity.getString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "读取文本失败...";
        }
    }

    private void initTextList() {
        for (int i = 0; i < 10; i++) {
            TextList sample = new TextList("texts/t" + i + ".txt");
            textList.add(sample);
        }
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

        private String content;
        private String filename;
        private int mentioned;

        TextList(String filename) {
            this.content = getString(filename);
            this.filename = filename;
            this.mentioned = 0;
        }

        String getContent() {
            return content;
        }

        String getFilename() {
            return filename;
        }

        int getMentioned() {
            return mentioned;
        }

        void updateMentioned() {
            this.mentioned = MarkText.checkJSON(this.content);
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
                viewHolder.textMentioned = view.findViewById(R.id.text_mentioned);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            assert textList != null;
            viewHolder.textName.setText(textList.getContent());
            int mentioned = textList.getMentioned();
            if (mentioned > 0) {
                viewHolder.textMentioned.setText("已标注" + mentioned + "句");
                viewHolder.textMentioned.setTextColor(Color.parseColor("#88B990"));
            } else {
                viewHolder.textMentioned.setText(R.string.not_mentioned);
                viewHolder.textMentioned.setTextColor(Color.parseColor("#DC3737"));
            }
            return view;
        }

        class ViewHolder {
            TextView textName;
            TextView textMentioned;
        }
    }
}
