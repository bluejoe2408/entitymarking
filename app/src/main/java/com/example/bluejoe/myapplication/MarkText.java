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
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarkText extends AppCompatActivity {
    private static final String TAG = "MarkText";
    public static final int TEXT_SIZE_MIN = 40;
    public static final int TEXT_SIZE_MAX = 70;
    public static final int TEXT_SIZE_STEP = 3;
    private TextView textView;
    SpannableString spanString;
    private ArrayList<View> aV = new ArrayList<View>();
    private ViewPager vpager_one;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter;
    private ArrayList<List<CardView>> mData = new ArrayList<List<CardView>>();
    private List<CardList> mCard = new ArrayList<>();
    private int cur_state = 0;
    private String ss = new String(),sss = new String();
    private String s = new String();
    String[] colorList={"#FBFFA3", "#CABFAB", "#E97A7A", "#EAFFD0", "#F9F9F9"};

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
            Button text_smaller = view.findViewById(R.id.text_smaller);
            Button text_larger = view.findViewById(R.id.text_larger);
            showRelation(i);
            aList.add(view);

            text_smaller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    float textSize = textView.getTextSize();
                    Log.d(TAG, "onClick: " + textSize);
                    if (textSize > TEXT_SIZE_MIN) {
                        textSize -= TEXT_SIZE_STEP;
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        Toast.makeText(MarkText.this, "减小字号(´・ω・｀)", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            text_larger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    float textSize = textView.getTextSize();
                    Log.d(TAG, "onClick: " + textSize);
                    if (textSize < TEXT_SIZE_MAX) {
                        textSize += TEXT_SIZE_STEP;
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        Toast.makeText(MarkText.this, "增大字号(´・ω・｀)", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CardList cardList = mCard.get(i);
                Toast.makeText(MarkText.this, "别戳我！", Toast.LENGTH_SHORT).show();
            }
        });
        final CardAdapter adapter = new CardAdapter(MarkText.this,R.layout.card_item_choose,mCard);
        listView.setAdapter(adapter);

        FloatingActionButton fabBtn = (FloatingActionButton) aV.get(index).findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_state = 1;
                Random random = new Random();
                CardList cL = new CardList("s","w","w", colorList[random.nextInt(5)]);
                mCard.add(cL);
                adapter.notifyDataSetChanged();
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
                            tmpTextView.append(curTextView.getString(),selectionStart,selectionEnd);*/
                            break;
                        case R.id.place:
                            selectionStart = curTextView.getSelectionStart();
                            selectionEnd = curTextView.getSelectionEnd();


                            addBackColorSpan(curTextView,curSpanString,selectionStart,selectionEnd, 2);
                            Toast.makeText(MarkText.this, "设置成功", Toast.LENGTH_SHORT).show();
                            actionMode.finish();
                            /*tmpView = tmpinflate.inflate(R.layout.relation_list_item,null);
                            tmpTextView = tmpView.findViewById(R.id.relation_name);
                            tmpTextView.append(curTextView.getString(),selectionStart,selectionEnd);*/
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

    /**
     * 将Mention类保存为JSON
     * @param mention Mention类
     * @return 布尔值，1表示保存成功
     */
    public static boolean saveJSON(Mention mention) {
        Gson gson = new Gson();
        String json = gson.toJson(mention);
        String articleId = mention.getArticleId();
        // Write JSON to file
        File dict = new File(Environment.getExternalStorageDirectory(), "entity_marking");
        File file = new File(Environment.getExternalStorageDirectory(), "entity_marking/" + articleId + ".json");
        try {
            if (!dict.exists()) {
                if (!dict.mkdir()) {
                    return false;
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(json.getBytes());
            fos.close();
            Log.d(TAG, "saveJSON: success");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "saveJSON: failed");
            return false;
        }
        return true;
    }

    /**
     * 从JSON文件中解析Mention类
     * @param file File类对象
     * @return Mention类对象
     */
    public static Mention loadJSON(File file) {
        Gson gson = new Gson();
        BufferedReader reader = null;
        Mention mention = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String string;
            string = reader.readLine();
            mention = gson.fromJson(string, Mention.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mention;
    }

    /**
     * 检查该文本是否已经过标注
     * @param string String类型文本内容
     * @return 布尔值，1表示已经过标注
     */
    public static boolean checkJSON(String string){
        String articleId = Mention.getMD5(string);
        File file = new File(Environment.getExternalStorageDirectory(), "entity_marking/" + articleId + ".json");
        return file.exists();
    }

    class CardList{
        String relation;
        String firstEntity;
        String secondEntity;
        String background;

        CardList(String relation, String firstEntity, String secondEntity, String background) {
            this.relation = relation;
            this.firstEntity = firstEntity;
            this.secondEntity = secondEntity;
            this.background = background;
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
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.firstEntity = view.findViewById(R.id.first_entity);
                viewHolder.secondEntity = view.findViewById(R.id.second_entity);
                viewHolder.cardView = view.findViewById(R.id.card_view);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            if (cardList != null) {
                viewHolder.firstEntity.setText(cardList.firstEntity);
                viewHolder.secondEntity.setText(cardList.secondEntity);
                viewHolder.cardView.setCardBackgroundColor(Color.parseColor(cardList.background));
            }
            return view;
        }

        class ViewHolder {
            TextView firstEntity;
            TextView secondEntity;
            CardView cardView;
        }
    }
}
