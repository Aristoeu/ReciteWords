package com.example.recitewords.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.recitewords.Data.MyList;
import com.example.recitewords.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsActivity extends AppCompatActivity {
    public ListView listView;
    private List<Boolean> mChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        init();
    }

    private void init() {
        listView = findViewById(R.id.StdInfo);
        ImageView back = findViewById(R.id.back);
        TextView confirm = findViewById(R.id.confirm);
        MyAdapter adapter = new MyAdapter(MyList.ListToChoose);
        listView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<mChecked.size();i++){
                    if(mChecked.get(i)){
                        Log.d("<<<>>>",i+"");
                        MyList.ListToReview.add(MyList.ListToChoose.get(i));
                    }
                finish();
            }}
        });
    }

    public class MyAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> list;
        private Map<Integer,Boolean> map=new HashMap<>();
        private int position;

        public MyAdapter(ArrayList<String> list) {
            this.context = getApplicationContext();
            this.list = list;
            mChecked = new ArrayList<Boolean>();
            for(int i=0;i<list.size();i++){
                mChecked.add(false);
            }

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            position = i;
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                //引入布局
                view = View.inflate(context, R.layout.item_layout, null);
                //实例化对象
                holder.word = view.findViewById(R.id.chosen_word);
                holder.mCheckBox =view.findViewById(R.id.tv_upper);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //给控件赋值
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked==true){
                        map.put(position,true);
                    }else {
                        map.remove(position);
                    }
                }
            });
            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox)v;
                    mChecked.set(i, cb.isChecked());
                }
            });
            holder.word.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WordsActivity.this,DetailActivity.class);
                    intent.putExtra("index",i);
                    startActivity(intent);
                }
            });

            if(map!=null&&map.containsKey(position)){
                holder.mCheckBox.setChecked(true);
            }else {
                holder.mCheckBox.setChecked(false);
            }
            holder.word.setText(list.get(i));
            holder.mCheckBox.setChecked(mChecked.get(position));
            return view;
        }

        class ViewHolder {
            TextView word;
            CheckBox mCheckBox;
        }

    }

}
