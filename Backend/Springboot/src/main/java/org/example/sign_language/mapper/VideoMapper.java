package org.example.sign_language.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.sign_language.entity.Video;

import java.util.List;

@Mapper
public interface VideoMapper {
    @Select("select * from video where title like concat('%',#{title},'%')")
    List<Video> selectVideo(String title);
}
