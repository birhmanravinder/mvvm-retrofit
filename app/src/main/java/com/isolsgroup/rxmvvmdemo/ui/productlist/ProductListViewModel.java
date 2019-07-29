package com.isolsgroup.rxmvvmdemo.ui.productlist;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.isolsgroup.rxmvvmdemo.data.repos.CatalogRepository;
import com.isolsgroup.rxmvvmdemo.model.Product;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.realm.RealmResults;

public class ProductListViewModel extends ViewModel {
    private CatalogRepository catalogRepository;
    private RealmResults<Product> products;
    private String category;

    private boolean hasMore=true;
    private final ObservableField<Integer> page;
    private final ObservableField<Boolean> showError;
    private final ObservableField<Boolean> isLoading;

    @Inject
    public ProductListViewModel(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
        this.page=new ObservableField<>(1);
        this.showError = new ObservableField<>(false);
        this.isLoading = new ObservableField<>(false);
    }

    public void setCategory(String category){
        this.category=category;
        this.products = catalogRepository.getProductsByCategory(category);
    }

    public ObservableField<Boolean> getShowError() {
        return showError;
    }

    public ObservableField<Boolean> getIsLoading() {
        return isLoading;
    }

    public RealmResults<Product> getProducts() {
        return products;
    }

    public void loadForPage(final int requestedPage) {
        if (!isLoading.get()) {
            isLoading.set(true);
            showError.set(false);
            catalogRepository.fetchAndPersistProducts(requestedPage, category).subscribe(new Observer<List<Product>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull List<Product> products) {
                    hasMore = products.size() == 10;
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    isLoading.set(false);
                    if (products.size() == 0) {
                        showError.set(true);
                    }
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    isLoading.set(false);
                    page.set(requestedPage);
                }
            });
        }
    }

    public int getPage() {
        return page.get();
    }

    public boolean hasMore() {
        return hasMore;
    }

}
