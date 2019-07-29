package com.isolsgroup.rxmvvmdemo.ui.productdetail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.isolsgroup.rxmvvmdemo.R;
import com.isolsgroup.rxmvvmdemo.data.repos.CartRepository;
import com.isolsgroup.rxmvvmdemo.data.repos.CatalogRepository;
import com.isolsgroup.rxmvvmdemo.databinding.ActivityProductDetailBinding;
import com.isolsgroup.rxmvvmdemo.di.ActivityComponent;
import com.isolsgroup.rxmvvmdemo.util.ToastUtil;

import javax.inject.Inject;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String KEY_PRODUCT_ID = "productId";

    @Inject
    CatalogRepository catalogRepository;
    @Inject
    CartRepository cartRepository;
    @Inject
    ToastUtil toastUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComponent.component(this).inject(this);

        long productId = getIntent().getLongExtra(KEY_PRODUCT_ID, 0);

        ProductDetailsViewModel viewModel = new ProductDetailsViewModel(catalogRepository, cartRepository, productId, toastUtil);
        ActivityProductDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        binding.setVm(viewModel);
        viewModel.loadProduct();

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.collapsibleToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
