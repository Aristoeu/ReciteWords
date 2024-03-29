package com.example.recitewords.Trees;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.recitewords.Activities.WordsActivity;
import com.example.recitewords.R;

import java.util.ArrayList;
import java.util.List;


public class TreeNodeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private TreeNode root;

    private ArrayList<TreeNode> mListNode = new ArrayList<>();//所有的节点
    private ArrayList<TreeNode> mListTree = new ArrayList<>();//要展现的节点

    public TreeNodeAdapter(Context context, TreeNode treeNode){
        mContext = context;
        root = treeNode;
        mInflater = LayoutInflater.from(mContext);
        mListNode.clear();
        setTreeNode(root);
        setTreeNodeToShow(false, false, "");
    }

    /***
     *添加所有节点到mListNode列表中
     * @param node
     */
    public void setTreeNode(TreeNode node){
        mListNode.add(node);
        if (node.isLeaf())return;
        List<TreeNode> child = node.getChildList();
        if (child == null)return;
        for (int i = 0; i < child.size(); i++) {
            setTreeNode(child.get(i));
        }
    }

    private void setTreeNodeToShow(boolean isCheck, boolean state, String name){
        mListTree.clear();
        treeNodeToShow(this.root, isCheck, state, name);
    }

    /**
     * 装配所有展开的节点数据显示出来
     * @param node
     * @param isCheck
     * @param state
     * @param name
     */
    private void treeNodeToShow(TreeNode node, boolean isCheck, boolean state, String name) {
        mListTree.add(node);
        if (node.isExpanded()
                && !node.isLeaf()
                && node.getChildList() != null){
            List<TreeNode> children = node.getChildList();
            for (int i = 0; i < children.size(); i++) {
                if (state && node.getName().equals(name)){
                    node.setCheck(isCheck);
                    children.get(i).setCheck(isCheck);
                    changceCheck(children.get(i), state);
                }
                treeNodeToShow(children.get(i), isCheck, state, name);
            }
        }
    }

    /**
     * 改变子节点状态
     * @param node
     * @param state
     */
    public void changceCheck(TreeNode node, boolean state){
        if (state){
            List<TreeNode> child = node.getChildList();
            if (child != null){
                for (int j = 0; j < child.size(); j++) {
                    child.get(j).setCheck(node.isCheck());
                    changceCheck(child.get(j), state);
                }
            }
        }
    }

    /**
     * 改变展开/收起状态
     * @param node
     * @param isCheck
     * @param state
     */
    public void changceExpandedTree(TreeNode node, boolean isCheck, boolean state){
        for (int i = 0; i < mListNode.size(); i++) {
            if (node.getName().equals(mListNode.get(i).getName())){
                if (state){
                    mListNode.get(i).setCheck(isCheck);
                }else {
                    boolean flag = mListNode.get(i).isExpanded();
                    mListNode.get(i).setExpanded(!flag);
                }
            }
        }
    }

    /**
     * 点击item
     * @param isCheck
     * @param state  true位点击ChechBox， false为点击item
     * @param name
     */
    public void OnClickListener(int position, boolean isCheck, boolean state, String name){
        TreeNode node = mListTree.get(position);
        if (node.isLeaf()){
            if (state){
                for (int i = 0; i < mListNode.size(); i++) {
                    if (node.getName().equals(mListNode.get(i).getName())){
                        mListNode.get(i).setCheck(isCheck);
                    }
                }
            }else{
                Intent intent = new Intent(mContext, WordsActivity.class);
                mContext.startActivity(intent);}
        }else {
            changceExpandedTree(node, isCheck, state);
            setTreeNodeToShow(isCheck, state, name);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mListTree.size();
    }

    @Override
    public Object getItem(int position) {
        return mListTree.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.tree_item, null);
            holder.tv_expanded = convertView.findViewById(R.id.tv_expanded);
            holder.tv = convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TreeNode node = mListTree.get(position);

        if (!node.isLeaf()){
            if (node.isExpanded()){
                holder.tv_expanded.setVisibility(View.VISIBLE);
                holder.tv_expanded.setText("○");
            }else {
                holder.tv_expanded.setVisibility(View.VISIBLE);
                holder.tv_expanded.setText("○");
            }
        }else {
            holder.tv_expanded.setVisibility(View.GONE);
        }
        holder.tv.setText(node.getName());


        convertView.setPadding(node.getLevel()*50, 0, 0, 0);//根据节点树级排布节点位置
        return convertView;
    }

    class ViewHolder{
        private TextView tv_expanded;
        private TextView tv;
    }

}
