package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void insertUser(User user);
    List<User> selectUserEmail(String email);
}
