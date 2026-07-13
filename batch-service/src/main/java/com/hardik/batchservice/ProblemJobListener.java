package com.hardik.batchservice;

import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

@Component
public class ProblemJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("=================================");
        System.out.println("Starting Problem ETL Job");
        System.out.println("=================================");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("=================================");
        System.out.println("Job Status: " + jobExecution.getStatus());

        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            System.out.println("Records Read: " + stepExecution.getReadCount());
            System.out.println("Records Written: " + stepExecution.getWriteCount());
            System.out.println("Records Skipped: " + stepExecution.getSkipCount());
            if (!stepExecution.getFailureExceptions().isEmpty()) {
                System.out.println("Failures: " + stepExecution.getFailureExceptions());
            }
        }

        System.out.println("=================================");
    }
}
