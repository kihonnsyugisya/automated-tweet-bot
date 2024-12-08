package com.kihonsyugisya.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobRunner implements CommandLineRunner {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job myJob; // 定義したジョブをインジェクト

    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()) // タイムスタンプをパラメータに追加
                .toJobParameters();
        jobLauncher.run(myJob, params);
    }
}

