package com.isolsgroup.rxmvvmdemo.ui.productdetail;

import androidx.databinding.ObservableField;

import com.isolsgroup.rxmvvmdemo.data.repos.CartRepository;
import com.isolsgroup.rxmvvmdemo.data.repos.CatalogRepository;
import com.isolsgroup.rxmvvmdemo.model.CartEntry;
import com.isolsgroup.rxmvvmdemo.model.Product;
import com.isolsgroup.rxmvvmdemo.util.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ProductDetailsViewModel {
    private final CatalogRepository repository;
    private final CartRepository cartRepository;
    private final ToastUtil toastUtil;

    private final ObservableField<Product> product;
    private final ObservableField<Boolean> isLoading;
    private final ObservableField<Boolean> showError;
    private final ObservableField<Boolean> inCart;

    private final RealmResults<Product> realProduct;
    private final RealmResults<CartEntry> realCart;
    private final long productId;

    public ProductDetailsViewModel(CatalogRepository repository, CartRepository cartRepository, long productId, ToastUtil toastUtil) {
        this.repository = repository;
        this.cartRepository = cartRepository;
        this.productId = productId;
        this.product = new ObservableField<>();
        this.showError = new ObservableField<>(false);
        this.isLoading = new ObservableField<>(false);
        this.inCart = new ObservableField<>(false);
        this.realProduct = repository.getProductId(productId);
        this.realCart = cartRepository.getCartEntry(productId);
        this.toastUtil=toastUtil;

        realProduct.addChangeListener(productChangeListener);
        realCart.addChangeListener(cartChangeListener);
    }

    public ObservableField<Product> getProduct() {
        return product;
    }

    public ObservableField<Boolean> getInCart() {
        return inCart;
    }

    public String getPrice() {
        return product.get() == null ? "" : "\u20B9 " + product.get().getPrice();
    }

    public String getName(){
        return product.get().getName();
    }

    public ObservableField<Boolean> getIsLoading() {
        return isLoading;
    }
    public ObservableField<Boolean> getShowError() {
        return showError;
    }

    public void loadProduct() {
        if (isLoading.get()) {
            return;
        }
        showError.set(false);
        isLoading.set(true);
        repository.fetchAndPersistProduct(productId).subscribe(new Observer<Product>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Product freshProduct) {
            }

            @Override
            public void onError(@NonNull Throwable e) {
                isLoading.set(false);
                toastUtil.showLongMessage("Unable to sync with server!");
                showError.set(true);
            }

            @Override
            public void onComplete() {
                isLoading.set(false);
            }
        });
    }

    public void onAddToCartClick() {
        if (!inCart.get()) {
            cartRepository.addToCart(productId);
            toastUtil.showShortMessage("Added to cart!");
        } else {
            cartRepository.removeFromCart(productId);
            toastUtil.showShortMessage("Removed from cart!");
        }
    }

    private RealmChangeListener<RealmResults<Product>> productChangeListener = new RealmChangeListener<RealmResults<Product>>() {
        @Override
        public void onChange(RealmResults<Product> results) {
            product.set(realProduct.get(0));
        }
    };

    private RealmChangeListener<RealmResults<CartEntry>> cartChangeListener = new RealmChangeListener<RealmResults<CartEntry>>() {
        @Override
        public void onChange(RealmResults<CartEntry> cartEntries) {
            inCart.set(realCart.size() != 0);
        }
    };
}
