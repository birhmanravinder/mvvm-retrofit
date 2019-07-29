package com.isolsgroup.rxmvvmdemo.ui.productlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.isolsgroup.rxmvvmdemo.R;
import com.isolsgroup.rxmvvmdemo.databinding.ItemProductBinding;
import com.isolsgroup.rxmvvmdemo.model.Product;
import com.isolsgroup.rxmvvmdemo.util.ActivityRouter;

import io.realm.RealmResults;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ItemViewHolder> {
    private ActivityRouter router;
    private final RealmResults<Product> dataList;

    public ProductListAdapter(ActivityRouter router, RealmResults<Product> dataList) {
        this.router=router;
        this.dataList = dataList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_product, parent, false);
        return new ItemViewHolder(binding, new ProductItemViewModel());
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ProductItemViewModel viewModel;
        private final ItemProductBinding binding;

        public ItemViewHolder(ItemProductBinding binding, ProductItemViewModel viewModel) {
            super(binding.getRoot());
            this.viewModel = viewModel;
            this.binding = binding;
        }

        public void bind(Product product) {
            viewModel.setProduct(product);
            binding.setVm(viewModel);
            binding.executePendingBindings();

            binding.btnProduct.setOnClickListener(v -> {

                if(getAdapterPosition()==RecyclerView.NO_POSITION)return;
                Product product1=dataList.get(getAdapterPosition());
                router.startProductDetailActivity(product1.getId());
            });
        }
    }

}
