package com.kihonsyugisya.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kihonsyugisya.service.EmailService;

@Component
public class EmailLogger {
    private static final Logger logger = LoggerFactory.getLogger(EmailLogger.class);

    @Autowired
    private EmailService emailService;

    public void logAndEmail(String message) {
        // 通常のログ出力
        logger.info(message);
        
        // メールで送信
        emailService.sendEmail("recipient@example.com", "ログ通知", message);
    }
}
