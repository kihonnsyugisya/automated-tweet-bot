package com.kihonsyugisya.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kihonsyugisya.entity.RakutenApiParametersEntity;

@Mapper
public interface RakutenApiParametersMapper {
    
    // 最新のrakuten_api_parametersテーブルからレコードを1件取得
    @Select("SELECT id, age, sex FROM rakuten_api_parameters ORDER BY id DESC LIMIT 1")
    RakutenApiParametersEntity findLatestRecord();

    // 新しいrakuten_api_parametersレコードを挿入
    @Insert("INSERT INTO rakuten_api_parameters (age, sex) VALUES (#{age}, #{sex})")
    void insertRakutenApiParameter(RakutenApiParametersEntity rakutenApiParameter);
    
    // アフィリエイトURLをpast_affiliate_urlsテーブルに挿入（重複時は無視）
    @Insert("INSERT INTO past_affiliate_urls (affiliate_url) VALUES (#{affiliateUrl}) ON CONFLICT (affiliate_url) DO NOTHING")
    void insertAffiliateUrl(String affiliateUrl);

    // 指定されたアフィリエイトURLがpast_affiliate_urlsテーブルに存在するかチェック
    @Select("SELECT COUNT(*) > 0 FROM past_affiliate_urls WHERE affiliate_url = #{affiliateUrl}")
    boolean existsAffiliateUrl(String affiliateUrl);
}
