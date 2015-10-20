package com.dreamspace.uucampusseller.ui.fragment.Goods;

import android.os.Bundle;
import android.view.View;


import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.adapter.Goods.OnSaleListAdapter;
import com.dreamspace.uucampusseller.adapter.Goods.PullOffListAdapter;
import com.dreamspace.uucampusseller.ui.GoodsFragment;
import com.dreamspace.uucampusseller.ui.base.BaseLazyFragment;
import com.dreamspace.uucampusseller.widget.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Lx on 2015/9/23.
 */
public class OnSalePullOffFragment extends BaseLazyFragment {
    @Bind(R.id.my_goods_sale_pulloff_lv)
    LoadMoreListView listView;

    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle == null? "null":bundle.getString(GoodsFragment.MANAGEMENT_TYPE);
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        if(type.equals(getResources().getString(R.string.on_sale))){
            initOnsaleViews();
        }else if(type.equals(getResources().getString(R.string.already_pull_off))){
            initPulloffViews();
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_my_goods_onsale_pulloff;
    }

    private void initOnsaleViews(){
        List<String> list = new ArrayList<>();
        for(int i = 0;i < 15;i++){
            list.add(i+"");
        }
        OnSaleListAdapter adapter = new OnSaleListAdapter(mContext,list,OnSaleListAdapter.ViewHolder.class);
        listView.setAdapter(adapter);
    }

    private void initPulloffViews(){
        List<String> list = new ArrayList<>();
        for(int i = 0;i < 15;i++){
            list.add(i+"");
        }
        PullOffListAdapter adapter = new PullOffListAdapter(mContext,list,PullOffListAdapter.ViewHolder.class);
        listView.setAdapter(adapter);
    }
}
