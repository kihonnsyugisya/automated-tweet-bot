package com.kihonsyugisya.service;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.kihonsyugisya.dto.OpenAiRequestDto;
import com.kihonsyugisya.dto.OpenAiResponseDto;
import com.kihonsyugisya.entity.OpenAiApiParametersEntity;
import com.kihonsyugisya.properties.OpenAiProperties;

@SpringBootTest // Spring Bootのテストコンテキストを使用
class OpenAiServiceTest {

    @MockBean // SpringのMockBeanを使ってモックを注入
    private OpenAiProperties openAiProperties;

    @Autowired // Springが管理するOpenAiServiceを注入
    private OpenAiService openAiService;

    /**
     * OpenAI APIを正常に呼び出した場合のテストです。
     * <p>
     * リクエストに対してOpenAI APIから正常なレスポンスが返されることを期待します。
     * </p>
     */
    @Test
    void testCallOpenAiApi_Success() {
        // Arrange
        OpenAiApiParametersEntity latestPrompt = new OpenAiApiParametersEntity();
        latestPrompt.setModel("gpt-4o");
        latestPrompt.setPrompt("Hello!");
              
        when(openAiProperties.getApiUrl()).thenReturn("https://api.openai.com/v1/chat/completions");
        when(openAiProperties.getApiKey()).thenReturn("");

        OpenAiRequestDto requestDto = new OpenAiRequestDto();
        // Messageリストを作成し、Messageオブジェクトを追加
        List<OpenAiRequestDto.Message> messages = new ArrayList<>();
        messages.add(new OpenAiRequestDto.Message("user", "How are you?"));
        
        // リストをrequestDtoにセット
        requestDto.setMessages(messages);
        

		OpenAiResponseDto responseDto = new OpenAiResponseDto();

		// choicesリストがnullであれば初期化
		if (responseDto.getChoices() == null) {
			responseDto.setChoices(new ArrayList<>()); // リストを初期化
		}

		// Choiceオブジェクトを作成して追加
		OpenAiResponseDto.Choice choice = new OpenAiResponseDto.Choice();
		choice.setMessage(new OpenAiResponseDto.Choice.Message("assistant", "Thanks"));
		responseDto.getChoices().add(choice);

//		when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(OpenAiResponseDto.class)))
//	    .thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));
		
        // Act
        String result = openAiService.callOpenAiApi(requestDto);
        
        System.out.println(result + " result");

        // Assert
//        assertEquals("Thanks", result);
    }

}
