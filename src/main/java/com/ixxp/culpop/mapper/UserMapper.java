package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void insertUser(User user);
    List<User> selectUserEmail(String email);
    User selectEmail(String email);
    void updateProfile(int userId, String username);
    User getProfile(int userId);
}
