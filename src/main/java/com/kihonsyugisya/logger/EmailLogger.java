package com.kihonsyugisya.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kihonsyugisya.dto.EmailRequestDto;
import com.kihonsyugisya.service.EmailService;

@Component
public class EmailLogger {
    private static final Logger logger = LoggerFactory.getLogger(EmailLogger.class);

    @Autowired
    private EmailService emailService;

    public void logAndEmail(EmailRequestDto emailRequestDto) {
        // 通常のログ出力
        logger.info(emailRequestDto.getBody());
        
        // メールで送信
        emailService.sendEmail(emailRequestDto);
    }
}
