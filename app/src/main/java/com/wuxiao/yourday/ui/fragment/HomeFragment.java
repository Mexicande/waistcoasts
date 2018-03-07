package com.wuxiao.yourday.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wuxiao.yourday.App;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.bean.ImagerBean;
import com.wuxiao.yourday.bean.Product;
import com.wuxiao.yourday.ui.activity.HomeActivity;
import com.wuxiao.yourday.ui.activity.HtmlActivity;
import com.wuxiao.yourday.ui.adapter.ProductAdapter;
import com.wuxiao.yourday.util.BrowsingHistory;
import com.wuxiao.yourday.util.Constants;
import com.wuxiao.yourday.util.TinyDB;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    @Bind(R.id.recylerview)
    RecyclerView recylerview;


    private BGABanner banner;

    private ProductAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        setRecycler();

        return view;
    }
    private void setRecycler() {
        try {
            setProduct((Product) new TinyDB(getContext()).getObject("Product", Product.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RelativeLayout layoutXiaoe;
    private RelativeLayout layoutDae;
    private ImageView ivGif;
    private TextView textPeople;
    private MarqueeView marqueeView;

    /**
     * RecyclerView   header
     *
     * @return
     */
    private View setView() {

        String[] phone = {"139", "188", "189", "185", "186", "134", "159", "158", "157", "189", "151", "152", "187", "130", "131", "132",
                "156", "155", "133", "153"};
        String[]number={"1000","900","4万","8万","2000","6000","10万","2000","1000","3000","3万","1000","5000","8000"};

        View view = getActivity().getLayoutInflater().inflate(R.layout.header_layout, null);
        layoutXiaoe = (RelativeLayout) view.findViewById(R.id.layout_xiaoe);
        layoutDae = (RelativeLayout) view.findViewById(R.id.layout_dae);
        marqueeView = (MarqueeView)view.findViewById(R.id.marquee_view);
        banner = (BGABanner) view.findViewById(R.id.banner_fresco_demo_content);
        ivGif = (ImageView) view.findViewById(R.id.iv_gif);

        Glide.with(this).load(R.mipmap.money).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivGif);

        textPeople = (TextView) view.findViewById(R.id.text_people);

        int nu;
        List<String> info = new ArrayList<>();
        for(int i=0;i<=11;i++){
            nu = new Random().nextInt(8999)+1000;
            String s=phone[i]+"****"+nu+"在"+ App.getProduct().getPrdList().get(new Random().nextInt(App.getProduct().getPrdList().size())).getName()+"成功借款"+number[i]+"元";
            info.add(s);
        }
        long millis = System.currentTimeMillis();

        long l = millis - 1502767343104l;

        textPeople.setText(String.valueOf(l));
        marqueeView.startWithList(info);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setListener();
        return view;

    }

    /**
     *
     * Product item
     *
     * @param product
     */
    private void setProduct(final Product product) {
        if (product != null) {
            adapter = new ProductAdapter(product.getPrdList());
            recylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            View footerView = setView();

            adapter.addHeaderView(footerView, 0);

            recylerview.setAdapter(adapter);

            recylerview.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("product", product.getPrdList().get(position));
//                    startActivity(new Intent(getActivity(), ProductActivity.class).putExtras(bundle));
                    startActivity(new Intent(getActivity(), HtmlActivity.class).putExtra("url",product.getPrdList().get(position).getLink()));
                    new BrowsingHistory().execute(product.getPrdList().get(position).getUid());
                }
            });
        }

    }


    private void setListener()  {

        try {
            ImagerBean bean = (ImagerBean) new TinyDB(getActivity()).getObject("ImagerBean", ImagerBean.class);
            setBunder(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 大额
        layoutDae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.viewPager1.setCurrentItem(2);
            }
        });
        layoutXiaoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.viewPager1.setCurrentItem(2);
            }
        });
    }

    /**
     * header 滚动
     *
     * @param bean
     */
    private void setBunder(ImagerBean bean) {
        if (bean != null) {
            final ArrayList<String> arr = new ArrayList<>();
            final List<ImagerBean.DaohangBean> daohang = bean.getDaohang();
            for (ImagerBean.DaohangBean s : daohang) {
                arr.add(Constants.Times.piURL + s.getAdvpath());
            }
            banner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
                @Override
                public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                    startActivity(new Intent(getContext(), HtmlActivity.class).putExtra("url", daohang.get(position).getLink()));
                }
            });
            banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                    Glide.with(getActivity())
                            .load(model)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .dontAnimate()
                            .into(itemView);
                }
            });
            banner.setData(arr, null);

        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
