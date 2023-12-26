package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper {
    void insertTag(Tag tag);
    void deleteTag(int tagId);
}
