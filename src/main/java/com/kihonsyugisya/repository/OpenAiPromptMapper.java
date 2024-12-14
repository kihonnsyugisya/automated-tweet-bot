package com.kihonsyugisya.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.kihonsyugisya.entity.OpenAiApiParametersEntity;

public interface OpenAiPromptMapper {

    @Select("SELECT MAX(id) FROM openai_api_parameters")
    long getMaxPromptId();

    @Select("SELECT * FROM openai_api_parameters WHERE id = #{promptId}")
    OpenAiApiParametersEntity getLatestPrompt(long promptId);
    
    @Insert("INSERT INTO openai_api_parameters (prompt, model) VALUES (#{prompt}, #{model})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPrompt(OpenAiApiParametersEntity openAiApiParametersEntity);
}
