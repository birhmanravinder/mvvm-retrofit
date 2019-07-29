package com.isolsgroup.rxmvvmdemo.ui.main;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModel;

import com.isolsgroup.rxmvvmdemo.data.repos.CatalogRepository;
import com.isolsgroup.rxmvvmdemo.model.Category;

import javax.inject.Inject;

public class CategoryListViewModel extends ViewModel {
    public CatalogRepository repository;
    private ObservableList<Category> categories;

    @Inject
    public CategoryListViewModel(CatalogRepository repository){
        this.repository=repository;
        this.categories=new ObservableArrayList<>();
    }

    public void loadCategories() {
        categories.addAll(repository.getCategories());
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }
}
