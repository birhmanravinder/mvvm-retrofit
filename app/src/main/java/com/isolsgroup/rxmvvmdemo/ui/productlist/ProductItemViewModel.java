package com.isolsgroup.rxmvvmdemo.ui.productlist;

import androidx.lifecycle.ViewModel;

import com.isolsgroup.rxmvvmdemo.model.Product;

public class ProductItemViewModel extends ViewModel {
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getPrice() {
        return "\u20B9 " + product.getPrice();
    }
}
