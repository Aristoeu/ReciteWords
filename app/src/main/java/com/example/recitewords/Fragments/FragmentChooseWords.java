package com.example.recitewords.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.recitewords.R;
import com.example.recitewords.Activities.WordsActivity;
import com.example.recitewords.Trees.TreeInfo;
import com.example.recitewords.Trees.TreeNode;
import com.example.recitewords.Trees.TreeNodeAdapter;
import com.example.recitewords.Trees.TreePatent;

import java.util.ArrayList;

public class FragmentChooseWords extends BaseFragment {
    private TextView textView;
    private ListView lv_tree;
    private TreeNodeAdapter adapter;
    private TreeNode parent ;
    private ArrayList<TreePatent> patentList = new ArrayList<>();
    private ArrayList<TreeInfo> mList = new ArrayList<>();

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_choose_words, null);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_words, null);
        textView = view.findViewById(R.id.myWords);
        lv_tree = view.findViewById(R.id.lv_tree);
        initDatan();
        adapter = new TreeNodeAdapter(getContext(), parent);
        lv_tree.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WordsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initDatan() {
        //组
        TreePatent patent1 = new TreePatent(1, 0, "小学英语");//id, parentId, name
        TreePatent patent2 = new TreePatent(2, 0, "初中英语");
        TreePatent patent3 = new TreePatent(3, 0, "高中英语");
        TreePatent patent4 = new TreePatent(4, 0, "大学英语");
        TreePatent patent5 = new TreePatent(5, 0, "研究生英语");
        TreePatent patent6 = new TreePatent(6, 1, "一年级");
        TreePatent patent7 = new TreePatent(7, 2, "中考");
        TreePatent patent8 = new TreePatent(8, 3, "高考");
        TreePatent patent9 = new TreePatent(9, 6, "入学英语");
        patentList.add(patent1);
        patentList.add(patent2);
        patentList.add(patent3);
        patentList.add(patent4);
        patentList.add(patent5);
        patentList.add(patent6);
        patentList.add(patent7);
        patentList.add(patent8);
        patentList.add(patent9);

        //组对应节点
        TreeInfo info1 = new TreeInfo(0, 1, "二年级");//id, groupId, name
        TreeInfo info2 = new TreeInfo(1, 1, "三年级");
        TreeInfo info3 = new TreeInfo(2, 1, "四年级");
        TreeInfo info4 = new TreeInfo(3, 2, "初二");
        TreeInfo info5 = new TreeInfo(4, 2, "初三");
        TreeInfo info6 = new TreeInfo(5, 3, "上册");
        TreeInfo info7 = new TreeInfo(6, 4, "下册");
        TreeInfo info8 = new TreeInfo(7, 4, "上册");
        TreeInfo info9 = new TreeInfo(8, 5, "第一单元");
        TreeInfo info10 = new TreeInfo(9, 5, "第二单元");
        TreeInfo info11 = new TreeInfo(10, 6, "托福");
        TreeInfo info12 = new TreeInfo(11, 6, "托福");
        TreeInfo info13 = new TreeInfo(12, 7, "中考冲刺");
        TreeInfo info14 = new TreeInfo(13, 8, "托福");
        TreeInfo info15 = new TreeInfo(14, 9, "托福");
        mList.add(info1);
        mList.add(info2);
        mList.add(info3);
        mList.add(info4);
        mList.add(info5);
        mList.add(info6);
        mList.add(info7);
        mList.add(info8);
        mList.add(info9);
        mList.add(info10);
        mList.add(info11);
        mList.add(info12);
        mList.add(info13);
        mList.add(info14);
        mList.add(info15);
        parent = new TreeNode(null, "我的词库", false);
        initTreeRoot(parent, 0);
        lv_tree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.OnClickListener(position, false, false, "");
            }
        });
    }

    //添加分支节点
    private void initTreeRoot(TreeNode parent, int parentId){
        for (int i = 0; i < patentList.size(); i++ ) {
            TreePatent treeParent = patentList.get(i);
            if (parentId == treeParent.getParentId()){
                TreeNode treeNode = new TreeNode(parent, treeParent.getName(), false);
                initTreeRoot(treeNode, treeParent.getId());
                parent.addChildNode(treeNode);
            }
        }
        initTreeChild(parent, parentId);
    }

    //添加子叶节点
    private void initTreeChild(TreeNode parent, int groupId) {
        for (int i = 0; i < mList.size(); i++) {
            TreeInfo treeInfo = mList.get(i);
            if (groupId == treeInfo.getGroupId()){
                TreeNode treeNode = new TreeNode(parent, treeInfo.getName(), true);
                parent.addChildNode(treeNode);
            }
        }
    }

}