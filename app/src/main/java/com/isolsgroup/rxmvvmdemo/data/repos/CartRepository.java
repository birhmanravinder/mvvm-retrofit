package com.isolsgroup.rxmvvmdemo.data.repos;

import com.isolsgroup.rxmvvmdemo.data.local.LocalRealmDB;
import com.isolsgroup.rxmvvmdemo.model.CartEntry;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.RealmResults;

@Singleton
public class CartRepository{
    private LocalRealmDB db;

    @Inject
    public CartRepository(LocalRealmDB db) {
        this.db=db;
    }

    public void addToCart(long productId) {
        db.addToCart(productId);
    }

    public RealmResults<CartEntry> getCartEntry(long productId) {
        return db.getCartEntryByProduct(productId);
    }

    public void removeFromCart(long productId) {
        db.removeFromCart(productId);
    }

    public long getCartCount() {
        return db.getCartCount();
    }

}
