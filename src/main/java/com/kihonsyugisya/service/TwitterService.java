package com.kihonsyugisya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kihonsyugisya.properties.TwitterProperties;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.signature.TwitterCredentials;

@Service
public class TwitterService {

    // Twitterの認証情報を保持するTwitterPropertiesを自動注入
    @Autowired
    private TwitterProperties twitterProperties;

    // 渡された内容をツイートするメソッド
    public void tweet(String contents) {

        // Twitterクライアントを初期化し、認証情報を設定
        TwitterClient twitterClient = new TwitterClient(TwitterCredentials.builder()
                .accessToken(twitterProperties.getAccessToken()) // アクセストークンの設定
                .accessTokenSecret(twitterProperties.getAccessTokenSecret()) // アクセストークンシークレットの設定
                .apiKey(twitterProperties.getApiKey()) // APIキーの設定
                .apiSecretKey(twitterProperties.getApiSecretKey()) // APIシークレットキーの設定
                .build());

        // 引数で渡された内容をツイートする
        try {
        	twitterClient.postTweet(contents);
		} catch (Exception e) {
			// TODO:メール送信
			System.out.println("tweet 失敗　:" + e);
		}
        
    }
}
