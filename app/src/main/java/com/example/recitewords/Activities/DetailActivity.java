package com.example.recitewords.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recitewords.DetailWords.BaseFragment;
import com.example.recitewords.DetailWords.Fragment1;
import com.example.recitewords.DetailWords.Fragment2;
import com.example.recitewords.DetailWords.Fragment3;
import com.example.recitewords.DetailWords.Fragment4;
import com.example.recitewords.DetailWords.PagerSlideAdapter;
import com.example.recitewords.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.recitewords.Data.MyList.ListToChoose;

public class DetailActivity extends AppCompatActivity {

    public static int i;
    @BindView(R.id.page_0) TextView text0;
    @BindView(R.id.page_1) TextView text1;
    @BindView(R.id.page_2) TextView text2;
    @BindView(R.id.page_3) TextView text3;
    @BindView(R.id.main_tab_line) ImageView tab_line;
    @BindView(R.id.main_pager) ViewPager mViewPager;
    @BindView(R.id.back)ImageView back;
    @BindView(R.id.wordText)TextView word;
    @BindView(R.id.confirm)TextView Confirm;
    private int screenWidth;
    private List<BaseFragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        i=getIntent().getIntExtra("index",0);
        Toast.makeText(this,i+"",Toast.LENGTH_LONG).show();
        Log.d("<<<>>>",i+"");
        ButterKnife.bind(this);
        initData();
        initWidth();
        setListener();
    }

    private void initData() {
        mFragmentList.add(new Fragment1());
        mFragmentList.add(new Fragment2());
        mFragmentList.add(new Fragment3());
        mFragmentList.add(new Fragment4());
        PagerSlideAdapter adapter = new PagerSlideAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        text0.setTextColor(Color.WHITE);
        word.setText(ListToChoose.get(i));
    }

    private void setListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tab_line.getLayoutParams();
                lp.leftMargin = screenWidth/4*position + positionOffsetPixels/4;
                tab_line.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        word.setText(ListToChoose.get(i));
                        break;
                    case 1:
                        if ((DetailActivity.i+1)< ListToChoose.size()){
                        word.setText(ListToChoose.get(i+1));}
                        else word.setText("后面没单词了哦！");
                        break;
                    case 2:
                        if ((DetailActivity.i+2)< ListToChoose.size()){
                        word.setText(ListToChoose.get(i+2));}
                        else word.setText("后面没单词了哦！");
                        break;
                    case 3:
                        if ((DetailActivity.i+3)< ListToChoose.size()){
                        word.setText(ListToChoose.get(i+3));}
                        else word.setText("后面没单词了哦！");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tab_line.getLayoutParams();
        lp.width = screenWidth / 4;
        tab_line.setLayoutParams(lp);
    }

}
