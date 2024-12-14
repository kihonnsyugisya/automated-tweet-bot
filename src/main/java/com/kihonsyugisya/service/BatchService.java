package com.kihonsyugisya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kihonsyugisya.entity.OpenAiApiParametersEntity;
import com.kihonsyugisya.entity.RakutenApiParametersEntity;
import com.kihonsyugisya.form.OpenAiForm;
import com.kihonsyugisya.form.RakutenParameterForm;
import com.kihonsyugisya.repository.OpenAiPromptMapper;
import com.kihonsyugisya.repository.RakutenApiParametersMapper;

@Service
public class BatchService {
	
	@Autowired
	private OpenAiPromptMapper openAiPromptMapper;
	
	@Autowired
	private RakutenApiParametersMapper rakutenApiParametersMapper;
	
	public OpenAiApiParametersEntity getOpenAiParameter() {
        // DBから最大番号のIDを取得
        long maxPromptId = openAiPromptMapper.getMaxPromptId();
                
		return openAiPromptMapper.getLatestPrompt(maxPromptId);		
	}
	
	public RakutenApiParametersEntity getRakutenParameter() {
		return rakutenApiParametersMapper.findLatestRecord();
	}
	
	public void setRakutenParameter(RakutenParameterForm rakutenParameterForm) {
		RakutenApiParametersEntity entity = new RakutenApiParametersEntity();
		entity.setAge(rakutenParameterForm.getAge());
		entity.setSex(rakutenParameterForm.getSex());
		rakutenApiParametersMapper.insertRakutenApiParameter(entity);
	}
	
	public void setOpenAiParameter(OpenAiForm openAiForm) {
		OpenAiApiParametersEntity entity = new OpenAiApiParametersEntity();
		entity.setModel(openAiForm.getModel());
		entity.setPrompt(openAiForm.getPrompt());
		openAiPromptMapper.insertPrompt(entity);
		
	}
	
    // アフィリエイトURLを登録するメソッド
    public void registerAffiliateUrl(String affiliateUrl) {
        rakutenApiParametersMapper.insertAffiliateUrl(affiliateUrl);
    }

    // アフィリエイトURLの存在を確認するメソッド
    public boolean isAffiliateUrlExists(String affiliateUrl) {
        return rakutenApiParametersMapper.existsAffiliateUrl(affiliateUrl);
    }
}
