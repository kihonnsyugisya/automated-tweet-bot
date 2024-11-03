package com.kihonsyugisya.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kihonsyugisya.dto.EmailRequestDto;

@SpringBootTest // Springのテストコンテキストを有効にする
public class EmailServiceTest {

    @Autowired // JavaMailSenderをSpringコンテナから注入
    private EmailService emailService; // EmailServiceをテスト対象としてインジェクション

    @Test
    public void testSendEmail() {
        // テスト用のEmailRequestDtoを作成
        EmailRequestDto emailRequestDto = new EmailRequestDto();
        emailRequestDto.setTo(null);
        emailRequestDto.setSubject(null);
        emailRequestDto.setBody("宛先、件名をnullにした場合、プロパティのメアドが読み込まれること");

        // メール送信メソッドを呼び出す
        emailService.sendEmail(emailRequestDto);

    }
}
