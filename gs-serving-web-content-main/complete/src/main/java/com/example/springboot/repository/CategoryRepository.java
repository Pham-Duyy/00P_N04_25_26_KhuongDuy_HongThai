package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.Category;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository {
    private List<Category> categories = new ArrayList<>();

    public List<Category> findAll() {
        return categories;
    }

    public void save(Category category) {
        categories.add(category);
    }

    public Category findById(Long id) {
        for (Category c : categories) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public boolean update(Long id, Category newCategory) {
        Category category = findById(id);
        if (category != null) {
            category.setName(newCategory.getName());
            category.setDescription(newCategory.getDescription());
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        Category category = findById(id);
        if (category != null) {
            categories.remove(category);
            return true;
        }
        return false;
    }
}
