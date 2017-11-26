package com.example.bluejoe.myapplication;
import android.widget.TextView;
import android.os.Bundle;
import android.app.Activity;
import java.io.*;
import android.graphics.Color;


public class MainActivity extends Activity {


    private TextView textView_showText;

    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
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
    public void showFuWaText() {
        textView_showText.setTextColor(Color.BLACK);
        textView_showText.setBackgroundColor(Color.WHITE);
        textView_showText.setTextSize(15);
        File file = new File("a.txt");
        InputStream inputStream = new FileInputStream(file);
        String string_fuwaText =
                "         “福娃”是五个拟人化的娃娃，他们的原型和头饰蕴含着与海洋、森林、火、大地和天空的联系，" +
                        "应用了中国传统艺术的表现方式，展现了灿烂的中国文化的博大精深。" +
                        "北京奥运会吉祥物的每个娃娃都代表着一个美好的祝愿：" +
                        "贝贝象征繁荣、晶晶象征欢乐、欢欢象征激情、迎迎象征健康、妮妮象征好运。" +
                        "五个福娃分别叫“贝贝”、“晶晶”、“欢欢”、“迎迎”、“妮妮”。各取它们名字中的一个字有次序的组成了谐音“北京欢迎你”。";
        textView_showText.setText(string_fuwaText);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView_showText = (TextView)this.findViewById(R.id.textview_fuwaText);

        showFuWaText();                                         //显示描述福娃的文字
    }


}