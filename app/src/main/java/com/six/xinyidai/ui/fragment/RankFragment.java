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
import com.six.xinyidai.ui.adapter.RankProductAdapter;
import com.six.xinyidai.util.BrowsingHistory;
import com.six.xinyidai.util.TinyDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import lib.kingja.switchbutton.SwitchMultiButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankFragment extends Fragment {


    @Bind(R.id.rank_switchmultibutton)
    SwitchMultiButton rankSwitchmultibutton;
    @Bind(R.id.recyler_View1)
    RecyclerView recylerView1;
    private RankProductAdapter adapter;

    private ArrayList<String>list;

    private ArrayList<String>list2;
    private ArrayList<String>list3;

    private  Product product;
    public RankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rank, container, false);


        ButterKnife.bind(this, view);

        getRankList();
        initSwitch();

        return view;
    }

    private void getRankList() {
        list=new ArrayList<>();
        list.add("212931");
        list.add("40592");
        list.add("5827");
        list.add("3282");
        list.add("75930");
        list.add("12135");
        list.add("36350");
        list.add("2123");
        list.add("10495");
        list.add("1741");
        list.add("4154");
        list.add("94018");
        list.add("8732");
        list.add("6340");
        list.add("84621");
        list.add("3910");
        list.add("87322");
        list.add("63430");
        list.add("13123");
        list.add("497");
        list.add("63410");

        list2=new ArrayList<>();
     for(int i=1;i<30;i++){
         int i1 = new Random().nextInt(100000) + 3847;
         list2.add(i1+"");
     }
        list3=new ArrayList<>();

        for(int i=1;i<33;i++){
         int i1 = new Random().nextInt(100000) + 1947;
         list3.add(i1+"");
     }
    }

    private void initSwitch() {

        try {
            product = (Product) new TinyDB(getActivity()).getObject("Product1", Product.class);
            adapter = new RankProductAdapter(product.getPrdList(),list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert rankSwitchmultibutton != null;
        rankSwitchmultibutton.setText(Arrays.asList("热门", "新品", "推荐")).setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                switch (position){
                    case 0:
                        try {
                            Product   product3 = (Product) new TinyDB(getActivity()).getObject("Product1", Product.class);
                            adapter.setList(list);
                            adapter.setNewData(product3.getPrdList());
                            adapter.notifyDataSetChanged();
                            product.setPrdList(product3.getPrdList());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            Product product1 = (Product) new TinyDB(getActivity()).getObject("Product2", Product.class);
                            adapter.setList(list2);
                            adapter.setNewData(product1.getPrdList());
                            adapter.notifyDataSetChanged();
                            product.setPrdList(product1.getPrdList());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            Product product2 = (Product) new TinyDB(getActivity()).getObject("Product3", Product.class);
                            adapter.setList(list3);
                            adapter.setNewData(product2.getPrdList());
                            adapter.notifyDataSetChanged();
                            product.setPrdList(product2.getPrdList());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                }

            }
        });
        setListener(product);
    }

    private void setListener(final Product product) {

        recylerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recylerView1.setAdapter(adapter);
        recylerView1.addOnItemTouchListener(new OnItemClickListener() {
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
