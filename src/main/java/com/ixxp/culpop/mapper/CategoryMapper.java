package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    void insertCategory(Category category);
    Category selectCategory(String cateName);
}
