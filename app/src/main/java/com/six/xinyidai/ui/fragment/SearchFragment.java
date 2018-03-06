package com.six.xinyidai.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.six.xinyidai.R;
import com.six.xinyidai.bean.Product;
import com.six.xinyidai.ui.activity.HtmlActivity;
import com.six.xinyidai.ui.adapter.SearchProductAdapter;
import com.six.xinyidai.util.BrowsingHistory;
import com.six.xinyidai.util.TinyDB;
import com.six.xinyidai.util.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import lib.kingja.switchbutton.SwitchMultiButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    private static final String[] CHANNELS = new String[]{"小额贷款", "大额贷款"};
    @Bind(R.id.switchmultibutton)
    SwitchMultiButton switchmultibutton;
    @Bind(R.id.recyler_View)
    RecyclerView recylerView;
    private SearchProductAdapter adapter;

    private ArrayList<String> list;

    private ArrayList<String>list2;

    private  Product product;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        getSearchList();
        initSwitch();
        return view;
    }

    private void getSearchList() {

        list=new ArrayList<>();
        for(int i=1;i<40;i++){
            int i1 = new Random().nextInt(104000) + 4247;
            list.add(i1+"");
        }
        list2=new ArrayList<>();

        for(int i=1;i<44;i++){
            int i1 = new Random().nextInt(103000) + 1647;
            list2.add(i1+"");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initSwitch() {
        try {
            product = (Product) new TinyDB(getActivity()).getObject("Product1", Product.class);
            adapter = new SearchProductAdapter(product.getPrdList(),list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert switchmultibutton != null;
        switchmultibutton.setText(Arrays.asList("大额贷款", "急速小贷")).setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                try {
                    if(position==0){
                        Product  product1 = (Product) new TinyDB(getActivity()).getObject("Product1", Product.class);
                        if(product!=null){
                            adapter.setNewData(product1.getPrdList());
                            adapter.notifyDataSetChanged();
                            product.setPrdList(product1.getPrdList());
                        }else {
                            ToastUtils.showToast(getActivity(),"加载失败......");
                        }

                    }else {
                        Product  product2 = (Product) new TinyDB(getActivity()).getObject("Product2", Product.class);
                        adapter.setList(list2);
                        adapter.setNewData(product2.getPrdList());
                        adapter.notifyDataSetChanged();
                        product.setPrdList(product2.getPrdList());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setListener(product);
    }


    private void setListener(final Product product) {

        recylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recylerView.setAdapter(adapter);
        recylerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("product", product.getPrdList().get(position));
//                startActivity(new Intent(getActivity(),ProductActivity.class).putExtras(bundle));
                startActivity(new Intent(getActivity(), HtmlActivity.class).putExtra("url",product.getPrdList().get(position).getLink()));
                new BrowsingHistory().execute(product.getPrdList().get(position).getUid());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
