package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Store;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper {
    void insertStore(Store store);
    Store selectStore(int storeId);
    void updateStore(Store store);
    void deleteStore(int storeId);
}
