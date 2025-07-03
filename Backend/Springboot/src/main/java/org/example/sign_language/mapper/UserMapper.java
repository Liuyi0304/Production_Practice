package org.example.sign_language.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.sign_language.entity.User;

import java.util.List;
@Mapper
public interface UserMapper {
    int insert(User user);

    @Delete("delete from account where id = #{id}")
    int deleteById(Integer id);

    int updateById(User user);

    @Select("select * from account where id = #{id}")
    User selectById(Integer id);

    List<User> selectAll(User user);

    @Select("select * from account where username = #{username}")
    User selectByUsername(String username);
}
