package com.kihonsyugisya.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kihonsyugisya.entity.RakutenApiParametersEntity;

@Mapper
public interface RakutenApiParametersMapper {
    // 最新のレコードを1件だけ取得
    @Select("SELECT id, age, sex FROM rakuten_api_parameters ORDER BY id DESC LIMIT 1")
    RakutenApiParametersEntity findLatestRecord();

    // レコードを挿入
    @Insert("INSERT INTO rakuten_api_parameters (age, sex) VALUES (#{age}, #{sex})")
    void insertRakutenApiParameter(RakutenApiParametersEntity rakutenApiParameter);
}
