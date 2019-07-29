package com.isolsgroup.rxmvvmdemo.ui.main;

import androidx.lifecycle.ViewModel;

import com.isolsgroup.rxmvvmdemo.model.Category;

public class CategoryItemViewModel extends ViewModel {

    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
