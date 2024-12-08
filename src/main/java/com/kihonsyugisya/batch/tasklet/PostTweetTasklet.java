package com.kihonsyugisya.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kihonsyugisya.enums.ExecutionContextKeys;
import com.kihonsyugisya.service.TwitterService;

@Component
public class PostTweetTasklet implements Tasklet {

    @Autowired
    private TwitterService twitterService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // ExecutionContextから生成されたツイートを取得
        String generatedTweet = (String) chunkContext.getStepContext()
            .getStepExecution()
			.getJobExecution()
            .getExecutionContext()
            .get(ExecutionContextKeys.GENERATED_TWEET.getKey());

        // ツイートが生成されているか確認
        if (generatedTweet != null && !generatedTweet.isEmpty()) {
            // TwitterServiceを使ってツイートする
            twitterService.tweet(generatedTweet);
            System.out.println("ツイートしました: " + generatedTweet);
        } else {
            System.err.println("生成されたツイートがありません。");
        }

        return RepeatStatus.FINISHED;
    }
}

