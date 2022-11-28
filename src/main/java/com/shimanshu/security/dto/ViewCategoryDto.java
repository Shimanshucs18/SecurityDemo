package com.shimanshu.security.dto;

import com.shimanshu.security.entity.Category;
import lombok.Data;

import java.util.List;

@Data
public class ViewCategoryDto {
    private Category currentCategory;
    private List<Category> childCategories;
}
