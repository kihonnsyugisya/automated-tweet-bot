package com.kihonsyugisya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.kihonsyugisya.entity.OpenAiApiParametersEntity;
import com.kihonsyugisya.entity.RakutenApiParametersEntity;
import com.kihonsyugisya.form.OpenAiForm;
import com.kihonsyugisya.form.RakutenParameterForm;
import com.kihonsyugisya.service.BatchService;

@Controller
public class BatchController {
	
	@Autowired
	private BatchService batchService;

    @GetMapping("/")
    public String showForm(Model model) {
    	
    	RakutenApiParametersEntity rakutenApiParametersEntity = batchService.getRakutenParameter();
    	RakutenParameterForm rakutenParameterForm = new RakutenParameterForm();
    	rakutenParameterForm.setAge(rakutenApiParametersEntity.getAge());
    	rakutenParameterForm.setSex(rakutenApiParametersEntity.getSex());
    	
    	OpenAiApiParametersEntity openAiApiParametersEntity = batchService.getOpenAiParameter();
    	OpenAiForm openAiForm = new OpenAiForm();
    	openAiForm.setModel(openAiApiParametersEntity.getModel());
    	openAiForm.setPrompt(openAiApiParametersEntity.getPrompt());
    	
        model.addAttribute("rakutenFromDto", rakutenParameterForm);
        model.addAttribute("openAiFormDto", openAiForm);
        return "Top";
    }

    @PostMapping("/rakutenParameterRegist")
    public String submitForm(@ModelAttribute RakutenParameterForm rakutenFromDto, Model model) {
        
        // サービスを呼び出す
    	batchService.setRakutenParameter(rakutenFromDto);

        // 成功メッセージを設定
        model.addAttribute("message", "送信が成功しました!");

        return "result"; // 結果を表示する別のテンプレートを返す
    }
    
    @PostMapping("/openAiParameterRegist")
    public String submitForm(@ModelAttribute OpenAiForm openAiForm, Model model) {
        // フォームから送信されたデータの処理
        
        // ここでサービスを呼び出す
    	batchService.setOpenAiParameter(openAiForm);

        // 成功メッセージを設定
        model.addAttribute("message", "送信が成功しました!");

        return "result"; // 結果を表示する別のテンプレートを返す
    }
}
