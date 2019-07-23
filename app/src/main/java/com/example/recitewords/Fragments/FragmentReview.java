package com.example.recitewords.Fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recitewords.Data.MyList;
import com.example.recitewords.R;
import com.example.recitewords.Utils.OnWordListener;
import com.example.recitewords.Utils.PostWord;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public class FragmentReview extends BaseFragment {
    private ImageView pronounce;
    private Button button1,button2,button3;
    private TextView word,pronunciation,translation,floatView,sentences;
    private String Word,Translation,Pronunciation,MusicUrl="0",Sentences;
    private static final int COMPLETED = 0;
    private boolean isNotFinished = true;
    private int index = 0;
    private Handler handler = new Handler(){
        @Override
        public  void handleMessage(Message msg){
            if (msg.what==COMPLETED){
                word.setText(Word);
                translation.setText(Translation);
                pronunciation.setText("/"+Pronunciation+"/");
                sentences.setText(Sentences);
                sentences.setMovementMethod(ScrollingMovementMethod.getInstance());
            }
            if (msg.what==1){
                floatView.setText("你已经背完了所有的单词！");
                isNotFinished = false;
            }
        }
    };
    OnWordListener onWordListener = new OnWordListener() {
        @Override
        public void onSuccess(String result) {
            if (result.length()>100){
                try {

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlPullParser = factory.newPullParser();
                    xmlPullParser.setInput(new StringReader(result));
                    int eventType = xmlPullParser.getEventType();

                    String queryText = "";      //查询文本
                    String voiceText = "";      //发音信息
                    String voiceUrlText = "";   //发音地址信息
                    String meanText = "";       //基本释义信息
                    String exampleText = "";    //例句信息

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String nodeName = xmlPullParser.getName();
                        switch (eventType) {
                            //开始解析
                            case XmlPullParser.START_TAG: {
                                switch (nodeName) {
                                    case "key":
                                        queryText += xmlPullParser.nextText();
                                        break;
                                    case "ps":
                                        voiceText += xmlPullParser.nextText() + "|";
                                        break;
                                    case "pron":
                                        voiceUrlText += xmlPullParser.nextText() + "|";
                                        break;
                                    case "pos":
                                        meanText += xmlPullParser.nextText() + "  ";
                                        break;
                                    case "acceptation":
                                        meanText += xmlPullParser.nextText();
                                        break;
                                    case "orig":
                                        exampleText += xmlPullParser.nextText();
                                        exampleText = exampleText.substring(0,exampleText.length()-1);
                                        break;
                                    case "trans":
                                        exampleText += xmlPullParser.nextText();
                                        break;
                                    default:
                                        break;
                                }
                            }
                            default:
                                break;
                        }
                        eventType = xmlPullParser.next();
                    }

                    String[] voiceArray = voiceText.split("\\|");
                    String[] voiceUrlArray = voiceUrlText.split("\\|");

                    meanText = meanText.substring(0,meanText.length()-1);
                    exampleText = exampleText.substring(1,exampleText.length());

                    //创建SharedPreferences.Editor对象，指定文件名为
                    Word = queryText;
                    Pronunciation = "["+voiceArray[0]+"]";
                    MusicUrl = voiceUrlArray[0];
                    Translation = meanText;
                    Sentences = exampleText;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("<<<>>>", "解析过程中出错！！！");
                }
                Message msg = new Message();
                msg.what = COMPLETED;
                handler.sendMessage(msg);
            }
            else translation.setText("没查到翻译");
        }

        @Override
        public void onError() {

        }

        @Override
        public void onFailed() {

        }
    };
    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_review, null);
        return view;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, null);
        ImageView top = view.findViewById(R.id.rev_top);
        ImageView bot = view.findViewById(R.id.rev_bot);
        button1 = view.findViewById(R.id.btn_know);
        button2 = view.findViewById(R.id.btn_half_know);
        button3 = view.findViewById(R.id.btn_not_know);
        word = view.findViewById(R.id.word);
        pronunciation = view.findViewById(R.id.pronunciation);
        translation = view.findViewById(R.id.translation);
        floatView = view.findViewById(R.id.Float);
        pronounce = view.findViewById(R.id.pronounce);
        sentences = view.findViewById(R.id.sentences);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        final String wordToKnow;

        wordToKnow = MyList.ListToReview.get(index);
        final PostWord postWord = new PostWord();
        postWord.post_word(MyList.ListToReview.get(0),onWordListener);
        word.setText(wordToKnow);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == MyList.ListToReview.size()-1)
                {   Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);}
               else postWord.post_word(MyList.ListToReview.get(++index),onWordListener);
                floatView.setVisibility(View.VISIBLE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == MyList.ListToReview.size()-1)
                {   Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);}
               else{
                MyList.ListToReview.add(MyList.ListToReview.get(index));
                postWord.post_word(MyList.ListToReview.get(++index),onWordListener);}
                floatView.setVisibility(View.VISIBLE);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == MyList.ListToReview.size()-1)
                {   Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);}
              else {
                MyList.ListToReview.add(MyList.ListToReview.get(index));
                postWord.post_word(MyList.ListToReview.get(++index),onWordListener);}
                floatView.setVisibility(View.VISIBLE);
            }
        });
        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNotFinished)
                floatView.setVisibility(View.INVISIBLE);
            }
        });
        pronounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MusicUrl!="0"){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("<<<>>>",MusicUrl);
                                MediaPlayer mediaPlayer = new MediaPlayer();
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaPlayer.setDataSource(MusicUrl);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }
        });
    }

}
