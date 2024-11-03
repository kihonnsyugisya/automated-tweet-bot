package com.kihonsyugisya.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.kihonsyugisya.dto.EmailRequestDto;
import com.kihonsyugisya.logger.EmailLogger;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private EmailLogger emailLogger;

    // 400 Bad Request エラーをキャッチ
    @ExceptionHandler({ IllegalArgumentException.class, NullPointerException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleBadRequest(Exception ex) {
        EmailRequestDto emailRequestDto = new EmailRequestDto();
        emailRequestDto.setSubject("Bad Request: " + ex.getMessage());
        
        // エラー発生時にメール通知
        emailLogger.logAndEmail(emailRequestDto);
    }


    // その他の例外をキャッチして処理
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception ex) {
        EmailRequestDto emailRequestDto = new EmailRequestDto();
        emailRequestDto.setSubject("Internal Server Error: " + ex.getMessage());
        
        // エラー発生時にメール通知
        emailLogger.logAndEmail(emailRequestDto);
    }
}
