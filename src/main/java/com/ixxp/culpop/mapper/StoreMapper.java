package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Store;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper {
    void insertStore(Store store);
}
