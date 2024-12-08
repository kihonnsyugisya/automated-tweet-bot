package com.kihonsyugisya.entity;

import lombok.Data;

@Data
public class OpenAiApiParametersEntity {
    private Long id;         // プライマリキー
    private String prompt;   // プロンプト
    private String model;    // モデル
}
