package com.isolsgroup.rxmvvmdemo.data.local;

import com.isolsgroup.rxmvvmdemo.model.CartEntry;
import com.isolsgroup.rxmvvmdemo.model.Product;

import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class LocalRealmDB {
    private final Realm realm;

    public LocalRealmDB(Realm realm) {
        this.realm = realm;
    }

    public RealmResults<Product> getProductByCategory(String category) {
        return realm.where(Product.class).equalTo("category", category).findAllAsync().sort("id", Sort.ASCENDING);
    }

    public void saveProducts(final List<Product> products) {
        realm.executeTransactionAsync(realm -> realm.copyToRealmOrUpdate(products));
    }

    public RealmResults<Product> getProductById(long id) {
        return realm.where(Product.class).
                equalTo("id", id).
                findAllAsync();
    }

    public RealmResults<CartEntry> getCartEntryByProduct(long id) {
        return realm
                .where(CartEntry.class)
                .equalTo("productId",  id)
                .findAllAsync();
    }

    public void addToCart(final long id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CartEntry cartEntry = new CartEntry();
                cartEntry.setProductId(id);
                realm.copyToRealmOrUpdate(cartEntry);
            }
        });
    }

    public long getCartCount() {
        return realm.where(CartEntry.class).count();
    }


    public void removeFromCart(final long id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<CartEntry> realmResults = realm.where(CartEntry.class)
                        .equalTo("productId", id).findAll();
                realmResults.deleteAllFromRealm();
            }
        });

    }

}
