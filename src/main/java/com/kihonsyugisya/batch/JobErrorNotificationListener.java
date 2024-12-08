package com.kihonsyugisya.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kihonsyugisya.dto.EmailRequestDto;
import com.kihonsyugisya.logger.EmailLogger;

@Component
public class JobErrorNotificationListener implements JobExecutionListener {

    @Autowired
    private EmailLogger emailLogger;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // ジョブ開始前の処理（必要に応じて）
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().isUnsuccessful()) {
            String errorMessage = jobExecution.getAllFailureExceptions().toString();
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setSubject("Job Execution Error: " + errorMessage);
            emailLogger.logAndEmail(emailRequestDto);
        }
    }
}
