package com.isolsgroup.rxmvvmdemo.data.repos;

import com.isolsgroup.rxmvvmdemo.R;
import com.isolsgroup.rxmvvmdemo.data.local.LocalRealmDB;
import com.isolsgroup.rxmvvmdemo.data.remote.APIService;
import com.isolsgroup.rxmvvmdemo.data.remote.model.ProductListResponse;
import com.isolsgroup.rxmvvmdemo.data.remote.model.ProductResponse;
import com.isolsgroup.rxmvvmdemo.model.Category;
import com.isolsgroup.rxmvvmdemo.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;

@Singleton
public class CatalogRepository{
    private final LocalRealmDB db;
    private final APIService apiService;
    private static List<Category> categories;

    static {
        categories = new ArrayList<>();
        categories.add(new Category("Skincare", R.drawable.ic_skincare));
        categories.add(new Category("Tools", R.drawable.ic_tools));
        categories.add(new Category("Nails", R.drawable.ic_nail));
        categories.add(new Category("Makeup", R.drawable.ic_makeup));
        categories.add(new Category("Men", R.drawable.ic_men));
        categories.add(new Category("Bath & Body", R.drawable.ic_bath));
    }

    @Inject
    public CatalogRepository(LocalRealmDB db, APIService apiService) {
        this.db = db;
        this.apiService = apiService;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public RealmResults<Product> getProductsByCategory(String category) {
        return db.getProductByCategory(category);
    }

    public RealmResults<Product> getProductId(long id) {
        return db.getProductById(id);
    }

    public Observable<List<Product>> fetchAndPersistProducts(int page, String category) {
        return apiService.getProductList(page, category)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ProductListResponse, List<Product>>() {
                    @Override
                    public List<Product> apply(@NonNull ProductListResponse productListResponse) throws Exception {
                        return productListResponse.getProducts();
                    }
                })
                .doOnNext(new Consumer<List<Product>>() {
                    @Override
                    public void accept(@NonNull List<Product> products) throws Exception {
                        db.saveProducts(products);
                    }
                });
    }

    public Observable<Product> fetchAndPersistProduct(final long prodId) {
        return apiService.getProduct(prodId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ProductResponse, Product>() {
                    @Override
                    public Product apply(@NonNull ProductResponse productResponse) throws Exception {
                        return productResponse.getProduct();
                    }
                })
                .doOnNext(new Consumer<Product>() {
                    @Override
                    public void accept(@NonNull Product product) throws Exception {
                        db.saveProducts(Collections.singletonList(product));
                    }
                });

    }
}
