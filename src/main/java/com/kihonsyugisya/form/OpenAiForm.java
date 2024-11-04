package com.kihonsyugisya.form;

import lombok.Data;

@Data
public class OpenAiForm {
    private String prompt;   // プロンプト
    private String model;    // モデル
}
