package com.example.bluejoe.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.weigan.loopview.LoopView;
public class MarkText extends AppCompatActivity {
    private static final String TAG = "MarkText";
    private TextView textView;
    SpannableString spanString;
    private ArrayList<View> aV = new ArrayList<View>();
    private ViewPager vpager_one;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter;
    private ArrayList<List<CardView>> mData = new ArrayList<List<CardView>>();
    private List<CardList> mCard = new ArrayList<>();
    private List<String> data = new ArrayList<>();
    private int cur_state = 0;
    private String ss = new String(),sss = new String();
    private String s = new String();

    private void addBackColorSpan(final TextView textView,final SpannableString spanString,final int st,final int e, int index) {

        BackgroundColorSpan span;
        if(index ==1) {
            span = new BackgroundColorSpan(Color.YELLOW);

        }
          else
                span = new BackgroundColorSpan(Color.GREEN);
        spanString.setSpan(span, st, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final ClickableSpan clickSpan2 = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {

                ds.setColor(Color.parseColor("#000000"));
                ds.setUnderlineText(false); //去掉下划线
            }

            @Override
            public void onClick(View widget) {
                if(cur_state ==0){
                Toast.makeText(MarkText.this, "取消成功" , Toast.LENGTH_SHORT).show();
                BackgroundColorSpan span= new BackgroundColorSpan(Color.TRANSPARENT);
                spanString.setSpan(span, st, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                spanString.removeSpan(this);
                textView.setText(spanString);}
                else if(cur_state==1){
                    ss = (String) s.subSequence(st,e);
                    cur_state=2;
                }
                else if(cur_state==2){
                    sss = (String) s.subSequence(st,e);
                    cur_state = 0;
                }
            }
        };
        spanString.setSpan(clickSpan2, st, e, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


        textView.setText(spanString);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflate = LayoutInflater.from(this);
        View view;
        view = inflate.inflate(R.layout.activity_mark_text,null);
        textView = view.findViewById(R.id.text_view);
        showText();
        setContentView(R.layout.activity_page);
        vpager_one = findViewById(R.id.vpager_one);
        aList = new ArrayList<>();
        //aList.add(view);
        Intent intent = getIntent();
        final ArrayList<CharSequence> string = intent.getCharSequenceArrayListExtra("string");
      //  for(int i = 0; i<string.size()-1; i++) {
        int i = 0;
            view = inflate.inflate(R.layout.activity_mark_relation, null);
            aV.add(view);
            textView = view.findViewById(R.id.sentence_text_view);
            showRelation(i);
            aList.add(view);
        //}


        mAdapter = new MyPagerAdapter(aList);
        vpager_one.setAdapter(mAdapter);
    }

    public void showText() {
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        Intent intent = getIntent();
        final ArrayList<CharSequence> string = intent.getCharSequenceArrayListExtra("string");
        textView.setText(string.get(string.size()-1));
        spanString = new SpannableString(string.get(string.size()-1));
    }

    public void showRelation(final int index) {
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        Intent intent = getIntent();
        final ArrayList<CharSequence> string = intent.getCharSequenceArrayListExtra("string");

        textView.setText(string.get(index));
        spanString = new SpannableString(string.get(index));

        LayoutInflater inflate = LayoutInflater.from(this);
        View view = inflate.inflate(R.layout.activity_mark_relation,null);

        final ListView listView = (ListView) aV.get(index).findViewById(R.id.list_view);
        //List<CardList> mmData = new LinkedList<CardList>();
        //mData.add(mmData);

        FloatingActionButton fabBtn = (FloatingActionButton) aV.get(index).findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_state = 1;
                CardList cL = new CardList("s","w","w");
                mCard.add(cL);
                CardAdapter adapter = new CardAdapter(MarkText.this,R.layout.card_item_choose,mCard);
                listView.setAdapter(adapter);
                Log.d(TAG, "onClick: " + ss);
                Log.d(TAG, "onClick: "+sss);
            }
        });
        final ActionMode.Callback2 textSelectionActionModeCallback;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            textSelectionActionModeCallback = new ActionMode.Callback2() {
                TextView curTextView = textView;
                SpannableString curSpanString = spanString;


                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

                    return true;//返回false则不会显示弹窗
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    MenuInflater menuInflater = actionMode.getMenuInflater();
                    menu.clear();
                    menuInflater.inflate(R.menu.selection_action_menu,menu);
                    return true;
                }

                @Override
                @TargetApi(Build.VERSION_CODES.M)
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    //根据item的ID处理点击事件
                    TextView tmpTextView;
                    View tmpView;
                    LayoutInflater tmpinflate = LayoutInflater.from(MarkText.this);

                    s = ""+textView.getText();
                    switch (menuItem.getItemId()){
                        case R.id.human:
                            int selectionStart = curTextView.getSelectionStart();
                            int selectionEnd = curTextView.getSelectionEnd();



                            addBackColorSpan(curTextView,curSpanString, selectionStart,selectionEnd, 1);
                            Toast.makeText(MarkText.this, "设置成功", Toast.LENGTH_SHORT).show();
                            actionMode.finish();//收起操作菜单
                            /*tmpView = tmpinflate.inflate(R.layout.relation_list_item,null);
                            tmpTextView = tmpView.findViewById(R.id.relation_name);
                            tmpTextView.append(curTextView.getText(),selectionStart,selectionEnd);*/
                            break;
                        case R.id.place:
                            selectionStart = curTextView.getSelectionStart();
                            selectionEnd = curTextView.getSelectionEnd();


                            addBackColorSpan(curTextView,curSpanString,selectionStart,selectionEnd, 2);
                            Toast.makeText(MarkText.this, "设置成功", Toast.LENGTH_SHORT).show();
                            actionMode.finish();
                            /*tmpView = tmpinflate.inflate(R.layout.relation_list_item,null);
                            tmpTextView = tmpView.findViewById(R.id.relation_name);
                            tmpTextView.append(curTextView.getText(),selectionStart,selectionEnd);*/
                            break;
                    }
                    return  true;//返回true则系统的"复制"、"搜索"之类的item将无效，只有自定义item有响应
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }

                @Override
                public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
                    //可选  用于改变弹出菜单的位置
                    super.onGetContentRect(mode, view, outRect);
                }
            };
            textView.setCustomSelectionActionModeCallback(textSelectionActionModeCallback);
        }
    }

    public boolean saveJSON(Mention mention) {
        Gson gson = new Gson();
        String json = gson.toJson(mention);
        // Write json to file
        File file = new File(Environment.getExternalStorageDirectory(), "test.txt");
        try {
//            FileOutputStream fos = new FileOutputStream(file);
            FileOutputStream fos = openFileOutput("data.json", Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public final class ViewHolder{
        public CardView card;
    }

    class CardList{
        String relation;
        String start1;
        String start2;
        CardList(String relation, String start1, String start2){
            this.relation = relation;
            this.start1 = start1;
            this.start2 = start2;
        }
        String getStart1()
        {
            return this.start1;
        }
        String getStart2()
        {
            return this.start2;
        }
    }

    class CardAdapter extends ArrayAdapter<CardList>{
        private int resourceId;

        CardAdapter(@NonNull Context context, int resource, @NonNull List<CardList> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            CardList cardList = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            TextView textView1 = view.findViewById(R.id.cardtext1);
//            TextView textView1 = view.findViewById(R.id.cardtext1);
//            TextView textView2 = view.findViewById(R.id.cardtext2);
            textView1.setText(cardList.getStart1());
//            textView2.setText(cardList.getStart2());
            return view;
        }

    }
}
