package com.example.recitewords.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import com.example.recitewords.Fragments.BottomBar;
import com.example.recitewords.Fragments.FragmentReview;
import com.example.recitewords.Fragments.FragmentChooseWords;
import com.example.recitewords.Fragments.FragmentCalculate;
import com.example.recitewords.Fragments.FragmentSettings;
import com.example.recitewords.Data.MyList;
import com.example.recitewords.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "<<<>>>";

    public static int i =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReadText(R.raw.ielts,MyList.ListToReview);
        ReadText(R.raw.list_to_choose,MyList.ListToChoose);
        Toast.makeText(this,MyList.ListToReview.size()+"",Toast.LENGTH_LONG).show();
        BottomBar bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setContainer(R.id.fl_container)
                .setTitleBeforeAndAfterColor("#999999", "#008577")
                .addItem(FragmentReview.class,
                        "复习",
                        R.drawable.tab_review_unselected,
                        R.drawable.tab_review_selected)
                .addItem(FragmentChooseWords.class,
                        "选词",
                        R.drawable.tab_book_unselected,
                        R.drawable.tab_book_selected)
                .addItem(FragmentCalculate.class,
                        "统计",
                        R.drawable.tab_graph_unselected,
                        R.drawable.tab_graph_selected)
                .addItem(FragmentSettings.class,
                        "设置",
                        R.drawable.tab_settings_unselected,
                        R.drawable.tab_settings_selected)
                .build();
    }
    public void ReadText(int i,ArrayList<String> list) {
        Resources resources = this.getResources();
        InputStream StdInfo = null;
        try {
            StdInfo = resources.openRawResource(i);
            if (StdInfo.available() == 0)
                return;
            if (StdInfo != null) {
                //用utf-8读取文件
                Scanner input = new Scanner(StdInfo, "utf-8");
                while (input.hasNext()) {
                    //将读取出来的数据文件
                    ArrayList<String> SubInfo = new ArrayList<String>();
                    String word = input.next();
                    SubInfo.add(word);
                    list.add(word);
                }
            }
        } catch (IOException e) {
            Toast.makeText(this, "not exist!", Toast.LENGTH_LONG);
        }
    }


}
