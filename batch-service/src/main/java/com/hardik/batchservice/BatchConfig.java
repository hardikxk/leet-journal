package com.hardik.batchservice;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Bean
    FlatFileItemReader<Problem> flatFileItemReader(@Value("/first-1407-leetcode-questions.csv") Resource problems_1407) {
        return new FlatFileItemReaderBuilder<Problem>()
                .resource(problems_1407)
                .name("problems-1407-reader")
                .delimited().delimiter(",")
                .names("id,title,acceptance,difficulty".split(","))
                .fieldSetMapper(fieldSet -> new Problem(
                        fieldSet.readInt("id"),
                        fieldSet.readString("title"),
                        fieldSet.readString("acceptance"),
                        fieldSet.readString("difficulty")
                ))
                .linesToSkip(1402)
                .build();
    }

    @Bean
    Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, FlatFileItemReader<Problem> flatFileItemReader, JdbcBatchItemWriter<Problem> jdbcBatchItemWriter) {
        return new StepBuilder("step1", jobRepository)
                .<Problem, Problem>chunk(10)
                .transactionManager(platformTransactionManager)
                .reader(flatFileItemReader)
                .writer(jdbcBatchItemWriter)
                .build();
    }

    @Bean
    JdbcBatchItemWriter<Problem> jdbcBatchItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Problem>()
                .dataSource(dataSource)
                .sql("""
                INSERT INTO PROBLEM(ID, TITLE, ACCEPTANCE, DIFFICULTY)
                VALUES (?, ?, ?, ?)                                                                                                                    \s
                ON CONFLICT (ID) DO UPDATE SET
                    TITLE = EXCLUDED.TITLE,                                                                                                                   \s
                    ACCEPTANCE = EXCLUDED.ACCEPTANCE,
                    DIFFICULTY = EXCLUDED.DIFFICULTY
                WHERE PROBLEM.TITLE != EXCLUDED.TITLE
                    OR PROBLEM.ACCEPTANCE != EXCLUDED.ACCEPTANCE
                    OR PROBLEM.DIFFICULTY != EXCLUDED.DIFFICULTY
                """)
                .assertUpdates(false)
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setInt(1, item.id());
                    ps.setString(2, item.title());
                    ps.setString(3, item.acceptance());
                    ps.setString(4, item.difficulty());
                })
                .build();
    }

    @Bean
    Job job(JobRepository jobRepository, Step step1) {
        return new JobBuilder("problemEtlJob", jobRepository)
                .start(step1)
                .incrementer(new RunIdIncrementer())
                .build();
    }
}

record Problem(int id, String title, String acceptance, String difficulty){}