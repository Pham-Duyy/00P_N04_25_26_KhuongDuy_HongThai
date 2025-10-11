package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Category;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private List<Category> categories = new ArrayList<>();

    public List<Category> getAllCategories() {
        return categories;
    }

    public Category addCategory(Category category) {
        categories.add(category);
        return category;
    }

    public Category updateCategory(Long id, Category newCategory) {
        for (Category category : categories) {
            if (category.getId().equals(id)) {
                category.setName(newCategory.getName());
                category.setDescription(newCategory.getDescription());
                return category;
            }
        }
        return null;
    }

    public boolean deleteCategory(Long id) {
        return categories.removeIf(c -> c.getId().equals(id));
    }

    public Category findCategoryById(Long id) {
        for (Category c : categories) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }
}
