package com.fpt.haotpv.SuperDuperDrive.mapper;

import com.fpt.haotpv.SuperDuperDrive.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    // check duplicate username
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS(username, salt, password, firstname, lastname) " +
            "VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    Integer insert(User user);
}
