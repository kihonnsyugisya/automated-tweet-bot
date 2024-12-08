package com.kihonsyugisya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.kihonsyugisya.dto.EmailRequestDto; 

@Service 
public class EmailService {

    @Autowired // JavaMailSenderをSpringコンテナから注入
    private JavaMailSender mailSender; 
    
    // プロパティからメールの宛先を注入
    @Value("${email.recipient}")
    private String defaultRecipient;
    
    // プロパティからプロジェクト名を注入
    @Value("${spring.application.name}")
    private String projectName;

    public void sendEmail(EmailRequestDto emailRequestDto) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        
        // 受信者、件名、本文を設定
        // 宛先が指定されていない場合はデフォルトを使用
        message.setTo(emailRequestDto.getTo() != null ? emailRequestDto.getTo() : defaultRecipient); 
        message.setSubject(emailRequestDto.getSubject() != null ? emailRequestDto.getSubject() :projectName + ": エラー通知"); 
        message.setText(emailRequestDto.getBody()); 

        // mailSenderを使ってメールを送信
        mailSender.send(message);
    }
}
