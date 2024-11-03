package com.kihonsyugisya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kihonsyugisya.dto.OpenAiRequestDto;
import com.kihonsyugisya.dto.OpenAiRequestDto.Message;
import com.kihonsyugisya.dto.OpenAiResponseDto;
import com.kihonsyugisya.entity.OpenAiApiParametersEntity;
import com.kihonsyugisya.properties.OpenAiProperties;
import com.kihonsyugisya.repository.OpenAiPromptMapper;

@Service
public class OpenAiService {
	
    @Autowired
    private  RestTemplate restTemplate;
    
    @Autowired
    private  OpenAiProperties openAiProperties;

    @Autowired
    private OpenAiPromptMapper openAiPromptMapper;

    /**
     * OpenAI APIを呼び出してレスポンスを取得します。
     *
     * @param requestDto OpenAI APIへのリクエストデータを含むDTO
     * @return OpenAI APIからのレスポンスの内容。正常に応答があった場合は、その内容を文字列として返します。
     * @throws RuntimeException OpenAI APIからの応答がない場合や、API呼び出し中にエラーが発生した場合
     */
    public String callOpenAiApi(OpenAiRequestDto requestDto) {
        String apiUrl = openAiProperties.getApiUrl();

        // DBから最新のプロンプトを取得
        OpenAiApiParametersEntity latestPromptEntity = openAiPromptMapper.getLatestPrompt();

        if (latestPromptEntity != null) {
        	requestDto.setModel(latestPromptEntity.getModel());
        	
            // 取得したプロンプトをリクエストDTOに追加
            requestDto.getMessages().addFirst(new Message("system", latestPromptEntity.getPrompt()));
        }

        // リクエストヘッダの作成
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiProperties.getApiKey());  // プロパティからAPIキーを取得
        headers.set("Content-Type", "application/json");

        // HttpEntityにヘッダとリクエストDTOをセット
        HttpEntity<OpenAiRequestDto> entity = new HttpEntity<>(requestDto, headers);
        
        System.out.println(entity.getBody());

        // リクエストを送信し、レスポンスを自動的にパース
        ResponseEntity<OpenAiResponseDto> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, OpenAiResponseDto.class);
        
        // レスポンスから末尾のcontentを取得
        OpenAiResponseDto apiResponse = response.getBody();
        if (apiResponse != null && !apiResponse.getChoices().isEmpty()) {
            int lastIndex = apiResponse.getChoices().size() - 1;
            return apiResponse.getChoices().get(lastIndex).getMessage().getContent();
        }

        
        // 応答がない場合はエラーを投げる
        throw new RuntimeException("No response from OpenAI API");
    }

}
