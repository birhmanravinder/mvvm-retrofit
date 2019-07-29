package com.isolsgroup.rxmvvmdemo.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CartEntry extends RealmObject {
    @PrimaryKey
    private long productId;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CartEntry) {
            return ((CartEntry)obj).getProductId() == getProductId();
        }
        return false;
    }
}
