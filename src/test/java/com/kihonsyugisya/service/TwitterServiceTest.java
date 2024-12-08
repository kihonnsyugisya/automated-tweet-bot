package com.kihonsyugisya.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TwitterServiceTest {

    @Autowired
    private TwitterService twitterService; // 実際のサービスを使用


    @Test
    public void testTweet() {
        // テスト用のツイート内容
        String tweetContent = "Hello, Twitter!";
        
        // ツイートメソッドを呼び出す
        twitterService.tweet(tweetContent);
        
    }
}
