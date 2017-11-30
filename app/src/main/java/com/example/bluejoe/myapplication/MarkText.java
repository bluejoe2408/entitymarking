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
        //textView.setMovementMethod(new ScrollingMovementMethod());
//        InputStream inputStream = getResources().openRawResource(R.raw.t0);
//        InputStream inputStream = null;
//        try {
//            inputStream = getAssets().open("t0.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        File file = new File("a.txt");
//        InputStream inputStream = new FileInputStream(file);
//        String string =
//                "海外网11月1日电 日本第195次特别国会于当地时间11月1日召开，日本自民党总裁安倍晋三在当日下午举行的众参两院全体大会首相提名选举中被选为第98任首相，全体阁僚和党高层留任。1日晚间，安倍第4次内阁将正式启动，这是继1952年吉田茂担任首相以来，日本时隔65年将再次出现“第4次内阁”。由于修宪势力获得了远远超过修宪动议所需众院额定议席数的三分之二以上，包含宪法第9条在内的修宪问题将成为焦点。\n" +
//                        "\n" +
//                        "据日本时事通讯社报道，在1日下午举行的日本众参两院首相提名选举中，日本自民党总裁安倍晋三再次当选为日本第98任首相。第4次安倍内阁启动后党内四大要职将全部留任，全体阁僚也将继续任职。自民党的大岛理森再任众议院议长、立宪民主党的赤松广隆当选为副议长，自民党的古屋圭司当选为议院运营委员长。\n" +
//                        "\n" +
//                        "8月3日启动的第3届安倍政府第3次改组内阁已于1日上午全员辞职。安倍晋三在众参两院全体大会上被指名为首相后，将与公明党党首山口那津男在首相官邸举行会谈，就维持联立政权等进行会谈。之后官房长官菅义伟将公布阁僚名单，晚些时候皇宫将举行首相任命仪式和阁僚认证仪式。1日晚，首相官邸将召开记者见面会，安倍将说明新内阁的基本姿态以及国内外诸多课题的应对方针。记者见面会之后，安倍拟在今晚的首次内阁会议上指示编制2017年度补充预算案，旨在充实育儿支援措施以推进其招牌政策“育人革命”。\n" +
//                        "\n" +
//                        "围绕着特别国会会期问题，在1日上午的众院各派协议会上，朝野政党已达成一致，将特别国会会期延长至12月9日，时间长达39天。日本首相安倍晋三将在特别国会期间发表施政信念演讲，并接受各党质询。在野党计划在特别国会期间继续就森友学园与加计学园问题追究安倍的责任。此前，日本执政党将会期拟定为8天，即11月1日至8日，而在野党要求将会期延长至12月上旬，充分保证审议时间。\n" +
//                        "\n" +
//                        "对于选举之后的日本，国际社会最为关注的问题无疑是修宪前景。在新一届的日本国会中，自民、希望、维新各党对包括《宪法》第九条在内的修宪积极态度，公明党态度谨慎，立宪民主党则“反对修改第九条”，共产、社民两党也持守护第九条的立场。安倍今年5月发表修宪主张，称希望在宪法第9条中增加自卫队的内容，并给出了“2020年施行新宪法”的明确时间表。随着“修宪势力”可能在此次众议院选战后壮大，安倍领导的自民党正在考虑加速此进程。修改战后日本的“和平宪法”条款，即宣布日本“放弃战争”的宪法第9条，为日本将自卫队最终升级为军队、重获战争权利扫清道路。安倍欲使日本重获国家交战权和集体防卫权，复归“正常国家”地位。一旦失去“和平宪法”的束缚日本会走向何方，会对东亚局势造成什么样的冲击，中韩等国对此担忧加剧。\n" +
//                        "\n" +
//                        "据此前报道，31日，日本首相安倍晋三（自民党总裁）在该党高层会议上正式宣布，11月1日第四届安倍内阁启动后党内四大要职将全部留任。安倍在会上呼吁团结称，“不辜负国民的托付，一项一项切实兑现选举承诺十分重要。”四大要职以外的党高层除了议员退职和众院选举落选者外都将继续任用。关于修宪，自民党把最快于明年由国会提议纳入考虑。以在《宪法》第九条中写明自卫队存在为主的该党修宪草案拟最快在明年初提交给例行国会，以便朝野各党展开讨论。（编译/海外网 巩浩）";
//        String string = getString(inputStream);
        Intent intent = getIntent();
        final ArrayList<CharSequence> string = intent.getCharSequenceArrayListExtra("string");
        textView.setText(string.get(string.size()-1));
        spanString = new SpannableString(string.get(string.size()-1));

        /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params.setMargins(10,10,10,10);
        textView.setLayoutParams(params);*/



    }

    public void showRelation(final int index) {
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        //textView.setMovementMethod(new ScrollingMovementMethod());
//        InputStream inputStream = getResources().openRawResource(R.raw.t0);
//        InputStream inputStream = null;
//        try {
//            inputStream = getAssets().open("t0.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        File file = new File("a.txt");
//        InputStream inputStream = new FileInputStream(file);
//        String string =
//                "海外网11月1日电 日本第195次特别国会于当地时间11月1日召开，日本自民党总裁安倍晋三在当日下午举行的众参两院全体大会首相提名选举中被选为第98任首相，全体阁僚和党高层留任。1日晚间，安倍第4次内阁将正式启动，这是继1952年吉田茂担任首相以来，日本时隔65年将再次出现“第4次内阁”。由于修宪势力获得了远远超过修宪动议所需众院额定议席数的三分之二以上，包含宪法第9条在内的修宪问题将成为焦点。\n" +
//                        "\n" +
//                        "据日本时事通讯社报道，在1日下午举行的日本众参两院首相提名选举中，日本自民党总裁安倍晋三再次当选为日本第98任首相。第4次安倍内阁启动后党内四大要职将全部留任，全体阁僚也将继续任职。自民党的大岛理森再任众议院议长、立宪民主党的赤松广隆当选为副议长，自民党的古屋圭司当选为议院运营委员长。\n" +
//                        "\n" +
//                        "8月3日启动的第3届安倍政府第3次改组内阁已于1日上午全员辞职。安倍晋三在众参两院全体大会上被指名为首相后，将与公明党党首山口那津男在首相官邸举行会谈，就维持联立政权等进行会谈。之后官房长官菅义伟将公布阁僚名单，晚些时候皇宫将举行首相任命仪式和阁僚认证仪式。1日晚，首相官邸将召开记者见面会，安倍将说明新内阁的基本姿态以及国内外诸多课题的应对方针。记者见面会之后，安倍拟在今晚的首次内阁会议上指示编制2017年度补充预算案，旨在充实育儿支援措施以推进其招牌政策“育人革命”。\n" +
//                        "\n" +
//                        "围绕着特别国会会期问题，在1日上午的众院各派协议会上，朝野政党已达成一致，将特别国会会期延长至12月9日，时间长达39天。日本首相安倍晋三将在特别国会期间发表施政信念演讲，并接受各党质询。在野党计划在特别国会期间继续就森友学园与加计学园问题追究安倍的责任。此前，日本执政党将会期拟定为8天，即11月1日至8日，而在野党要求将会期延长至12月上旬，充分保证审议时间。\n" +
//                        "\n" +
//                        "对于选举之后的日本，国际社会最为关注的问题无疑是修宪前景。在新一届的日本国会中，自民、希望、维新各党对包括《宪法》第九条在内的修宪积极态度，公明党态度谨慎，立宪民主党则“反对修改第九条”，共产、社民两党也持守护第九条的立场。安倍今年5月发表修宪主张，称希望在宪法第9条中增加自卫队的内容，并给出了“2020年施行新宪法”的明确时间表。随着“修宪势力”可能在此次众议院选战后壮大，安倍领导的自民党正在考虑加速此进程。修改战后日本的“和平宪法”条款，即宣布日本“放弃战争”的宪法第9条，为日本将自卫队最终升级为军队、重获战争权利扫清道路。安倍欲使日本重获国家交战权和集体防卫权，复归“正常国家”地位。一旦失去“和平宪法”的束缚日本会走向何方，会对东亚局势造成什么样的冲击，中韩等国对此担忧加剧。\n" +
//                        "\n" +
//                        "据此前报道，31日，日本首相安倍晋三（自民党总裁）在该党高层会议上正式宣布，11月1日第四届安倍内阁启动后党内四大要职将全部留任。安倍在会上呼吁团结称，“不辜负国民的托付，一项一项切实兑现选举承诺十分重要。”四大要职以外的党高层除了议员退职和众院选举落选者外都将继续任用。关于修宪，自民党把最快于明年由国会提议纳入考虑。以在《宪法》第九条中写明自卫队存在为主的该党修宪草案拟最快在明年初提交给例行国会，以便朝野各党展开讨论。（编译/海外网 巩浩）";
//        String string = getString(inputStream);
        Intent intent = getIntent();
        final ArrayList<CharSequence> string = intent.getCharSequenceArrayListExtra("string");

        textView.setText(string.get(index));
        spanString = new SpannableString(string.get(index));

        LayoutInflater inflate = LayoutInflater.from(this);
        View view;
        view = inflate.inflate(R.layout.activity_mark_relation,null);

        final ListView listView = (ListView) aV.get(index).findViewById(R.id.list_view);
        //List<CardList> mmData = new LinkedList<CardList>();
        //mData.add(mmData);
        CardAdapter adapter = new CardAdapter(MarkText.this,R.layout.card_item_choose,mCard);


        listView.setAdapter(adapter);

        FloatingActionButton fabBtn = (FloatingActionButton) aV.get(index).findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_state = 1;
                CardList cL = new CardList("s","w","w");
//                LayoutInflater inflate = LayoutInflater.from(MarkText.this);
//                View view;
//                view = inflate.inflate(R.layout.card_item_choose,null);
//                CardListView  cV = view.findViewById(R.id.card_choose);
                mCard.add(cL);
                Log.d(TAG, "onClick: " + ss);
                Log.d(TAG, "onClick: "+sss);
            }
        });

        /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params.setMargins(10,10,10,10);
        textView.setLayoutParams(params);*/
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
        CardList(String relation,String start1,String start2){
            this.relation = relation;
            this.start1 = start1;
            this.start2 = start2;
        }
        String getStart1()
        {
            return  start1;
        }
        String getStart2()
        {
            return start2;
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
            TextView textView2 = view.findViewById(R.id.cardtext2);
            textView1.setText(cardList.getStart1());
            textView2.setText(cardList.getStart2());
            return view;
        }

    }
}
