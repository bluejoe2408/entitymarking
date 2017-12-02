package com.example.bluejoe.myapplication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.weigan.loopview.LoopView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Random;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class MarkText extends AppCompatActivity {
    public ArrayList<Mention> mentionArray = new ArrayList<>();
    public ArrayList<Mention> importMention = new ArrayList<>();
    private static final String TAG = "MarkText";
    public static final int TEXT_SIZE_MIN = 40;
    public static final int TEXT_SIZE_MAX = 70;
    public static final int TEXT_SIZE_STEP = 3;
    public static final int UPDATE_UI = 1;
    public static final int UPDATE_ADAPTER = 2;
    private TextView textView;
    SpannableString spanString;
    private ArrayList<View> aV = new ArrayList<>();

    private List<ArrayList<CardList>> mCard = new ArrayList<>();
    private ArrayList<View> tmpCard = new ArrayList<>();
    private int cur_state = 0;
    private String ss, sss;
    private int st1, st2;
    private String s;
    private CardAdapter adapter;
    String[] colorList = {"#FBFFA3", "#CABFAB", "#FEE5B1", "#EAFFD0", "#F9F9F9"};
    private ArrayList<String> list = new ArrayList<>();
    private List<Button> btnSmallerList = new ArrayList<>();
    private List<Button> btnLargerList = new ArrayList<>();
    private List<TextView> textViewList = new ArrayList<>();
    private View view;
    private LayoutInflater inflate;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter = null;
    MyViewPager vpager_one;

//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case UPDATE_UI:
//                    textView.setTextColor(Color.BLACK);
//                    textView.setBackgroundColor(Color.WHITE);
//                    textView.setTextSize(18);
//                    textView.setMovementMethod(LinkMovementMethod.getInstance());
//                    textView.setText(msg.getData().getString("text"));
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case UPDATE_ADAPTER:
//                    mAdapter.notifyDataSetChanged();
//                default:
//                    break;
//            }
//        }
//    };

    private void addBackColorSpan(final TextView textView, final SpannableString spanString, final int st, final int e, final int index, final int ii) {

        BackgroundColorSpan span;
        if (index == R.id.human) {
            span = new BackgroundColorSpan(Color.YELLOW);
            mentionArray.get(ii).addEntity(s.substring(st, e), "human", st);
        } else if (index == R.id.place) {
            span = new BackgroundColorSpan(Color.GREEN);
            mentionArray.get(ii).addEntity(s.substring(st, e), "place", st);
        } else {
            span = new BackgroundColorSpan(Color.WHITE);
            Toast.makeText(this, "Unknown", Toast.LENGTH_SHORT).show();
        }
        spanString.setSpan(span, st, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final ClickableSpan clickSpan2 = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {

                ds.setColor(Color.parseColor("#000000"));
                ds.setUnderlineText(false); //去掉下划线
            }

            @Override
            public void onClick(View widget) {
                if (cur_state == 0) {
                    Toast.makeText(MarkText.this, "取消成功", Toast.LENGTH_SHORT).show();
                    BackgroundColorSpan span = new BackgroundColorSpan(Color.TRANSPARENT);
                    
                    spanString.setSpan(span, st, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanString.removeSpan(this);
                    textView.setText(spanString);

                    mentionArray.get(vpager_one.mCurrentPage).removeEntity(st);

                    saveJSON(mentionArray.get(vpager_one.mCurrentPage));

                    List<CardList> cardList = mCard.get(vpager_one.mCurrentPage);
                    for (int i = 0; i < cardList.size(); i++) {
                        if (cardList.get(i).getStt1() == st || cardList.get(i).getStt2() == st) {
                            cardList.remove(i);
                        }
                    }
                    ListView listView = aV.get(vpager_one.mCurrentPage).findViewById(R.id.list_view);
                    adapter = new CardAdapter(MarkText.this, R.layout.card_item, mCard.get(vpager_one.mCurrentPage));
                    listView.setAdapter(adapter);
                    listView.setSelection(mCard.size() - 1);
                } else if (cur_state == 1) {
                    st1 = st;
                    ss = (String) s.subSequence(st, e);
                    Log.d(TAG, "onClick: ss" + ss);

//                    Log.d(TAG, "onClick: mv"+adapter.mview);
                    TextView tmpc = tmpCard.get(0).findViewById(R.id.cardtext1);
                    Log.d(TAG, "onClick: tmpcard" + tmpCard.size());
                    tmpc.setText(ss);
                    tmpc.setTextColor(Color.parseColor("#0D625E"));
                    mCard.get(ii).get(mCard.get(ii).size() - 1).firstEntity = ss;
                    cur_state = 2;
                } else if (cur_state == 2) {
                    st2 = st;
                    sss = (String) s.subSequence(st, e);
                    Log.d(TAG, "onClick: sss" + sss);
                    TextView tmpc = tmpCard.get(0).findViewById(R.id.cardtext2);
                    tmpc.setText(sss);
                    tmpc.setTextColor(Color.parseColor("#0D625E"));
                    mCard.get(ii).get(mCard.get(ii).size() - 1).secondEntity = sss;
                    cur_state = 3;
                }
            }
        };
        spanString.setSpan(clickSpan2, st, e, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        Log.d(TAG, "addBackColorSpan: s" + span);
        textView.setText(spanString);
    }


    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add("夫妻");
        list.add("邻国");
        inflate = LayoutInflater.from(this);
        view = inflate.inflate(R.layout.activity_mark_text, null);
        textView = view.findViewById(R.id.text_view);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        switch (type) {
            case "text":
                final ArrayList<CharSequence> string = intent.getCharSequenceArrayListExtra("string");
                showText();
                setContentView(R.layout.activity_page);
                vpager_one = findViewById(R.id.vpager_one);
                aList = new ArrayList<>();
                aList.add(view);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }).start();
                int num = intent.getIntExtra("num", 0);
                for (int i = 0; i < num; i++) {

                    importMention.add((Mention) intent.getSerializableExtra("mention" + i));
                }
                for (int i = 0; i < string.size() - 1; i++) {
                    mentionArray.add(new Mention(string.get(string.size() - 1).toString(), i, (String) string.get(i)));
                    doALotThings(string.get(i).toString(), i);
//            Bundle bundle = new Bundle();
//            bundle.putString("text", string.get(i).toString());
//            Message message = new Message();
//            message.what = UPDATE_UI;
//            message.setData(bundle);
//            handler.sendMessage(message);
                    Log.d(TAG, "run: " + i + " finished.");
                }
                Log.d(TAG, "onCreate: num" + num);

                for (int i = 0; i < num; i++) {
                    Mention mention = importMention.get(i);
                    int sentenceID = mention.getSentenceId();
                    TextView textView = textViewList.get(sentenceID);
                    SpannableString span = (SpannableString) textView.getText();
                    for (int j = 0; j < mention.getEntityMentions().size(); j++) {
                        s = mention.getSentenceText();
                        addBackColorSpan(textView, span,
                                mention.getEntityMentions().get(j).getStartIndex(),
                                mention.getEntityMentions().get(j).getStartIndex() + mention.getEntityMentions().get(j).getEntity().length(),
                                (Objects.equals(mention.getEntityMentions().get(j).getType(), "human")) ? R.id.human : R.id.place,
                                sentenceID);
                    }
                    for (int j = 0; j < mention.getRelationMentions().size(); j++) {
                        Random random = new Random();
                        CardList cL = new CardList(mention.getRelationMentions().get(j).getRelation(),
                                mention.getSentenceText().substring(mention.getRelationMentions().get(j).getFirstEntityIndex(),
                                        mention.getRelationMentions().get(j).getFirstEntityIndex() +
                                                mention.getEntityByIndex(mention.getRelationMentions().get(j).getFirstEntityIndex()).length()),
                                mention.getSentenceText().substring(mention.getRelationMentions().get(j).getSecondEntityIndex(),
                                        mention.getRelationMentions().get(j).getSecondEntityIndex() +
                                                mention.getEntityByIndex(mention.getRelationMentions().get(j).getSecondEntityIndex()).length()),
                                colorList[random.nextInt(5)], R.layout.card_item,
                                mention.getRelationMentions().get(j).getFirstEntityIndex(),
                                mention.getRelationMentions().get(j).getSecondEntityIndex());
                        ListView listView = aV.get(sentenceID).findViewById(R.id.list_view);
                        mCard.get(sentenceID).add(cL);
                        mentionArray.get(sentenceID).addRelation(mention.getRelationMentions().get(j).getFirstEntityIndex(),
                                mention.getRelationMentions().get(j).getSecondEntityIndex(), mention.getRelationMentions().get(j).getRelation());
                        tmpCard.clear();
                        adapter = new CardAdapter(MarkText.this, R.layout.card_item, mCard.get(sentenceID));
                        Log.d(TAG, "onClick: tmpcard" + tmpCard.size());
                        listView.setAdapter(adapter);
                        listView.setSelection(mCard.size() - 1);
                    }
                }
                mAdapter = new MyPagerAdapter(aList);
                vpager_one.setAdapter(mAdapter);
                break;
            /*case "json":
                setContentView(R.layout.activity_page);
                vpager_one = findViewById(R.id.vpager_one);
                aList = new ArrayList<>();
                aList.add(view);
                int num = intent.getIntExtra("num", 0);
                for (int i = 0; i < num; i++) {

                    importMention.add((Mention)intent.getSerializableExtra("mention" + i));
                    //doALotThings(importMention.get(i).getSentenceText(), i);
                }
                mAdapter = new MyPagerAdapter(aList);
                vpager_one.setAdapter(mAdapter);
                break;*/
        }
    }

    @SuppressLint("InflateParams")
    private void doALotThings(String string, int i) {
        view = inflate.inflate(R.layout.activity_mark_relation, null);
        aV.add(view);
        textView = view.findViewById(R.id.sentence_text_view);
        textViewList.add((TextView) view.findViewById(R.id.sentence_text_view));
        btnSmallerList.add((Button) view.findViewById(R.id.text_smaller));
        btnLargerList.add((Button) view.findViewById(R.id.text_larger));

        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(string);

        showRelation(i);
        aList.add(view);

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        btnSmallerList.get(i).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float textSize = textViewList.get(textViewList.size() - 1).getTextSize();
                if (textSize > TEXT_SIZE_MIN) {
                    textSize -= TEXT_SIZE_STEP;
                    for (int i = 0; i < textViewList.size(); i++) {
                        textViewList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                    Toast.makeText(MarkText.this, "减小字号(´・ω・｀)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLargerList.get(i).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float textSize = textViewList.get(textViewList.size() - 1).getTextSize();
                if (textSize < TEXT_SIZE_MAX) {
                    textSize += TEXT_SIZE_STEP;
                    for (int i = 0; i < textViewList.size(); i++) {
                        textViewList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                    Toast.makeText(MarkText.this, "增大字号(´・ω・｀)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showText() {
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        Intent intent = getIntent();
        final ArrayList<CharSequence> string = intent.getCharSequenceArrayListExtra("string");
        textView.setText(string.get(string.size() - 1));
        spanString = new SpannableString(string.get(string.size() - 1));
    }

    public void showRelation(final int index) {
        Intent intent = getIntent();
        final ArrayList<CharSequence> string = intent.getCharSequenceArrayListExtra("string");
        final ListView listView = aV.get(index).findViewById(R.id.list_view);
        final FloatingActionButton fabBtn = aV.get(index).findViewById(R.id.fabBtn);
        spanString = new SpannableString(string.get(index));

        mCard.add(new ArrayList<CardList>());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (cur_state == 3) {
                    LoopView loopView = tmpCard.get(0).findViewById(R.id.loopView);
                    String previous_background = mCard.get(index).get(mCard.get(index).size() - 1).getBackground();
                    CardList cL = new CardList(list.get(loopView.getSelectedItem()), ss, sss, previous_background, R.layout.card_item, st1, st2);
                    mCard.get(index).remove(mCard.get(index).size() - 1);
                    mCard.get(index).add(cL);

                    mentionArray.get(index).addRelation(st1, st2, list.get(loopView.getSelectedItem()));

                    adapter = new CardAdapter(MarkText.this, R.layout.card_item, mCard.get(index));
                    listView.setAdapter(adapter);
                    listView.setSelection(mCard.size() - 1);

                    saveJSON(mentionArray.get(index));

                    // Show faBtn
                    fabBtn.setVisibility(View.VISIBLE);

                    cur_state = 0;
                } else {
                    Toast.makeText(MarkText.this, "别戳我！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (cur_state == 0) {
                    mentionArray.get(index).removeRelation(mCard.get(index).get(i).stt1, mCard.get(index).get(i).stt2);
                    mCard.get(index).remove(i);
                    saveJSON(mentionArray.get(index));
                    tmpCard.clear();
                    adapter = new CardAdapter(MarkText.this, R.layout.card_item, mCard.get(index));
                    Log.d(TAG, "onClick: tmpcard" + tmpCard.size());
                    listView.setAdapter(adapter);
                    listView.setSelection(mCard.size() - 1);
                }
                return true;
            }

        });

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cur_state == 0) {
                    cur_state = 1;
                    Random random = new Random();
                    CardList cL = new CardList("s", "未指定", "未指定", colorList[random.nextInt(5)], R.layout.card_item_choose, 0, 0);
                    mCard.get(index).add(cL);
                    tmpCard.clear();
                    adapter = new CardAdapter(MarkText.this, R.layout.card_item_choose, mCard.get(index));
                    Log.d(TAG, "onClick: tmpcard" + tmpCard.size());
                    listView.setAdapter(adapter);
                    listView.setSelection(mCard.size() - 1);
                    fabBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        final ActionMode.Callback2 textSelectionActionModeCallback;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
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
                    menuInflater.inflate(R.menu.selection_action_menu, menu);
                    return true;
                }

                @Override
                @TargetApi(Build.VERSION_CODES.M)
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    //根据item的ID处理点击事件
                    s = "" + curTextView.getText();
                    int selectionStart = curTextView.getSelectionStart();
                    int selectionEnd = curTextView.getSelectionEnd();
                    addBackColorSpan(curTextView, curSpanString, selectionStart, selectionEnd, menuItem.getItemId(), index);
                    Toast.makeText(MarkText.this, "设置成功", Toast.LENGTH_SHORT).show();
                    actionMode.finish();
                    saveJSON(mentionArray.get(vpager_one.mCurrentPage));
                    return true;//返回true则系统的"复制"、"搜索"之类的item将无效，只有自定义item有响应
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
     * 如果mention类为空则删除JSON
     *
     * @param mention Mention类
     */
    public void saveJSON(Mention mention) {
        Gson gson = new Gson();
        String json = gson.toJson(mention);
        String articleId = mention.getArticleId();
        int sentenceId = mention.getSentenceId();
        // Write JSON to file
        File dict = new File(Environment.getExternalStorageDirectory(), "entity_marking");
        File file = new File(Environment.getExternalStorageDirectory(), "entity_marking/"
                + articleId + "_" + sentenceId + ".json");
        try {
            if (!dict.exists()) {
                if (!dict.mkdir()) {
                    Toast.makeText(this, "创建目录失败，将无法保存标注结果！", Toast.LENGTH_SHORT).show();
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            if (mention.getEntityMentions().size() == 0 && mention.getRelationMentions().size() == 0) {
                if (file.exists()) {
                    if (!file.delete()) {
                        Toast.makeText(this, "删除空的标注结果失败", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                fos.write(json.getBytes());
                Log.d(TAG, "JSON" + json);
                Log.d(TAG, "saveJSON: success");
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "saveJSON: failed");
            Toast.makeText(this, "保存标注结果失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 从JSON文件中解析Mention类
     *
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
     *
     * @param string String类型文本内容
     * @return 标注句数，0表示未标注
     */
    public static int checkJSON(String string) {
        String articleId = Mention.getMD5(string);
        int i = 0;
        Log.d(TAG, "checkJSON: " + articleId);
        Log.d(TAG, "checkJSON: " + "entity_marking/" + articleId + "_" + i + ".json");
        File file = new File(Environment.getExternalStorageDirectory(), "entity_marking/" + articleId + "_" + i + ".json");
        while (file.exists()) {
            i++;
            file = new File(Environment.getExternalStorageDirectory(), "entity_marking/" + articleId + "_" + i + ".json");
        }
        return i;
    }

    class CardList {
        String relation;
        String firstEntity;
        String secondEntity;
        String background;
        int index;
        int stt1;
        int stt2;

        CardList(String relation, String firstEntity, String secondEntity, String background, int index, int st1, int st2) {
            this.relation = relation;
            this.firstEntity = firstEntity;
            this.secondEntity = secondEntity;
            this.background = background;
            this.index = index;
            this.stt1 = st1;
            this.stt2 = st2;
        }

        String getBackground() {
            return background;
        }

        public int getStt1() {
            return stt1;
        }

        public int getStt2() {
            return stt2;
        }
    }

    class CardAdapter extends ArrayAdapter<CardList> {
        private int resourceId;
        View mview;

        CardAdapter(@NonNull Context context, int resource, @NonNull List<CardList> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            CardList cardList = getItem(position);
            Log.d(TAG, "getView: pos" + position);
            View view;
            ViewHolder viewHolder = new ViewHolder();
            ViewHolder1 viewHolder1 = new ViewHolder1();
            if (convertView == null) {

                assert cardList != null;
                view = LayoutInflater.from(getContext()).inflate(cardList.index, parent, false);
                if (cardList.index == R.layout.card_item) {
                    viewHolder.firstEntity = view.findViewById(R.id.cardtext1);
                    viewHolder.secondEntity = view.findViewById(R.id.cardtext2);
                    viewHolder.cardView = view.findViewById(R.id.card_view);
                    viewHolder.relation = view.findViewById(R.id.cardrelation);
                    view.setTag(viewHolder);
                } else {
                    viewHolder1.firstEntity = view.findViewById(R.id.cardtext1);
                    viewHolder1.secondEntity = view.findViewById(R.id.cardtext2);
                    viewHolder1.cardView = view.findViewById(R.id.card_view);
                    viewHolder1.loopView = view.findViewById(R.id.loopView);
                    view.setTag(viewHolder1);
                }
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            if (cardList != null) {
                if (cardList.index == R.layout.card_item) {
                    viewHolder.firstEntity.setText(cardList.firstEntity);
                    viewHolder.secondEntity.setText(cardList.secondEntity);
                    viewHolder.cardView.setCardBackgroundColor(Color.parseColor(cardList.background));
                    viewHolder.relation.setText(cardList.relation);
                } else {
                    viewHolder1.firstEntity.setText(cardList.firstEntity);
                    viewHolder1.secondEntity.setText(cardList.secondEntity);
                    viewHolder1.cardView.setCardBackgroundColor(Color.parseColor(cardList.background));

                    viewHolder1.loopView.setItems(list);
//                    viewHolder1.loopView.setNotLoop();//设置是否循环播放
                    viewHolder1.loopView.setItemsVisibleCount(3);
                    viewHolder1.loopView.setInitPosition(0);//设置初始位置
                    viewHolder1.loopView.setTextSize(20);
                    viewHolder1.loopView.setCenterTextColor(Color.RED);
                    viewHolder1.loopView.setDividerColor(Color.RED);
                }
            }
            mview = view;
            tmpCard.add(mview);
            Log.d(TAG, "getView: mview" + mview);
            return view;
        }

        class ViewHolder {
            TextView firstEntity;
            TextView secondEntity;
            CardView cardView;
            TextView relation;
        }

        class ViewHolder1 {
            TextView firstEntity;
            TextView secondEntity;
            CardView cardView;
            LoopView loopView;
        }
    }
}
