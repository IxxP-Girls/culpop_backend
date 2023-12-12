package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper

public interface AdminMapper {
    void insertAdmin(Admin admin);
    Admin selectEmail(String email);
}
