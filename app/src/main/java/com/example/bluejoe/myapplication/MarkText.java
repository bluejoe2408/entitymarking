package com.example.bluejoe.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MarkText extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_text);

        textView = (TextView)this.findViewById(R.id.text_view);

        showText();
    }

    public void showText() {
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setMovementMethod(new ScrollingMovementMethod());
        InputStream inputStream = getResources().openRawResource(R.raw.t0);
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
        String string = getString(inputStream);
        textView.setText(string);
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