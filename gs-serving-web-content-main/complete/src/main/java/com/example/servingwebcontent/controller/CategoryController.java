package com.example.servingwebcontent.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.servingwebcontent.model.Category;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    // Danh sách lưu trữ các category (giả lập database)
    private List<Category> categories = new ArrayList<>();

    // Lấy tất cả category
    @GetMapping
    public List<Category> getAllCategories() {
        return categories;
    }

    // Thêm mới category
    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        categories.add(category);
        return category;
    }

    // Sửa category theo id
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category newCategory) {
        Category category = findCategoryById(id);
        if (category != null) {
            category.setName(newCategory.getName());
            category.setDescription(newCategory.getDescription());
            return category;
        }
        // Không tìm thấy category
        return null;
    }

    // Xóa category theo id
    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Long id) {
        Category category = findCategoryById(id);
        if (category != null) {
            categories.remove(category);
            return "Xóa thành công";
        }
        return "Không tìm thấy category";
    }

    // Hàm tiện ích tìm category theo id
    private Category findCategoryById(Long id) {
        for (Category c : categories) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }
}
