package com.kihonsyugisya.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.kihonsyugisya.batch.tasklet.FetchProductInfoTasklet;
import com.kihonsyugisya.batch.tasklet.GenerateTweetTasklet;
import com.kihonsyugisya.batch.tasklet.PostTweetTasklet;

/**
 * Spring Batchの設定クラス。
 * 複数のステップ（タスク）を定義し、それらを1つのジョブとしてまとめて実行する。
 * 各ステップには異なるタスクレットが割り当てられ、順に処理が行われる。
 */
@Configuration
class BatchConfiguration {

    /**
     * ジョブ全体を定義するBean。
     * 各ステップを順番に実行するジョブを作成する。
     *
     * @param jobRepository Spring Batchのジョブ管理を担当するリポジトリ
     * @param fetchProductInfoStep 商品情報を取得するステップ
     * @param generateTweetStep ツイート生成ステップ
     * @param postTweetStep ツイート投稿ステップ
     * @return 定義されたジョブ
     */
    @Bean
    Job myJob(JobRepository jobRepository, Step fetchProductInfoStep, Step generateTweetStep, Step postTweetStep) {
        return new JobBuilder("myJob", jobRepository)
                .start(fetchProductInfoStep) // 最初のステップを開始
                .next(generateTweetStep) // 次のステップに進む
                .next(postTweetStep) // 最後のステップに進む
                .build();
    }

    /**
     * 商品情報を取得するステップを定義するBean。
     * FetchProductInfoTaskletを実行する。
     *
     * @param jobRepository ジョブ管理用リポジトリ
     * @param transactionManager トランザクション管理
     * @return 定義されたステップ
     */
    @Bean
    Step fetchProductInfoStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("fetchProductInfoStep", jobRepository)
                .tasklet(fetchProductInfoTaskletBean(), transactionManager) // タスクレットとトランザクションを設定
                .build();
    }

    /**
     * ツイート生成ステップを定義するBean。
     * GenerateTweetTaskletを実行する。
     *
     * @param jobRepository ジョブ管理用リポジトリ
     * @param transactionManager トランザクション管理
     * @return 定義されたステップ
     */
    @Bean
    Step generateTweetStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("generateTweetStep", jobRepository)
                .tasklet(generateTweetTaskletBean(), transactionManager) // タスクレットとトランザクションを設定
                .build();
    }

    /**
     * ツイート投稿ステップを定義するBean。
     * PostTweetTaskletを実行する。
     *
     * @param jobRepository ジョブ管理用リポジトリ
     * @param transactionManager トランザクション管理
     * @return 定義されたステップ
     */
    @Bean
    Step postTweetStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("postTweetStep", jobRepository)
                .tasklet(postTweetTaskletBean(), transactionManager) // タスクレットとトランザクションを設定
                .build();
    }

    /**
     * 商品情報取得用タスクレットを定義するBean。
     * @return FetchProductInfoTaskletのインスタンス
     */
    @Bean
    Tasklet fetchProductInfoTaskletBean() {
        return new FetchProductInfoTasklet();
    }

    /**
     * ツイート生成用タスクレットを定義するBean。
     * @return GenerateTweetTaskletのインスタンス
     */
    @Bean
    Tasklet generateTweetTaskletBean() {
        return new GenerateTweetTasklet();
    }

    /**
     * ツイート投稿用タスクレットを定義するBean。
     * @return PostTweetTaskletのインスタンス
     */
    @Bean
    Tasklet postTweetTaskletBean() {
        return new PostTweetTasklet();
    }
}
