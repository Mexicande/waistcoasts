package com.six.xinyidai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.six.xinyidai.R;
import com.six.xinyidai.bean.Product;
import com.six.xinyidai.ui.adapter.ProductAdapter;
import com.six.xinyidai.util.TinyDB;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecommendProductActivity extends AppCompatActivity {

    @Bind(R.id.title_name)
    TextView titleName;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.recommend_recylerview)
    RecyclerView recommendRecylerview;
    private ProductAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_product);
        ButterKnife.bind(this);
        setRecycler();

    }

    private void setRecycler() {
        ivBack.setVisibility(View.VISIBLE);
        int extra = getIntent().getIntExtra("Product", 0);
        switch (extra) {
            case 1:
                titleName.setText("推荐列表");
                setProduct((Product) new TinyDB(this).getObject("HotProduct", Product.class));
                break;
            case 2:
                titleName.setText("新品列表");
                setProduct((Product) new TinyDB(this).getObject("NewProduct", Product.class));
                break;
            case 3:
                titleName.setText("热门列表");
                setProduct((Product) new TinyDB(this).getObject("RecommendProduct", Product.class));
                break;
            default:
                break;
        }

    }

    private void setProduct(final Product product) {

        adapter = new ProductAdapter(product.getPrdList());
        recommendRecylerview.setLayoutManager(new LinearLayoutManager(this));
        recommendRecylerview.setAdapter(adapter);
        recommendRecylerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", product.getPrdList().get(position));
                startActivity(new Intent(RecommendProductActivity.this, ProductActivity.class).putExtras(bundle));
            }
        });
    }



    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
