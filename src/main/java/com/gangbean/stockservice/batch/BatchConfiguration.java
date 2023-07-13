package com.gangbean.stockservice.batch;

import com.gangbean.stockservice.domain.Stock;
import com.gangbean.stockservice.domain.StockRandomPrice;
import com.gangbean.stockservice.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;

@EnableBatchProcessing
@EnableScheduling
@Configuration
public class BatchConfiguration {

    private final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final JobLauncher jobLauncher;

    private final StockRepository stockRepository;

    private final ApplicationContext context;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory, JobLauncher jobLauncher, StockRepository stockRepository, ApplicationContext context) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.jobLauncher = jobLauncher;
        this.stockRepository = stockRepository;
        this.context = context;
    }

    @Bean
    public Job stockUpdate(Step step) {
        return jobBuilderFactory.get("stockUpdate")
                .start(step)
                .build();
    }

    @Bean
    public Step stockUpdateStep1(ItemReader<Stock> reader, ItemProcessor<Stock, Stock> processor, ItemWriter<Stock> writer) {
        return stepBuilderFactory.get("stockUpdateStep1")
                .<Stock, Stock>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public JpaCursorItemReader<Stock> jpaCursorItemReader() {
        return new JpaCursorItemReaderBuilder<Stock>()
                .name("jpaCursorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT s FROM Stock s")
                .build();
    }

    @Bean
    public ItemProcessor<Stock, Stock> stockPriceUpdate() {
        return stock -> {
            stock.updatePrice(new StockRandomPrice(), LocalDateTime.now());
            return stock;
        };
    }

    @Bean
    public ItemWriter<Stock> jpaCursorItemWriter() {
        return stockRepository::saveAll;
    }

    @Scheduled(initialDelay = 30 * 1000, fixedDelay = 30 * 1000)
    public void runJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        logger.info(">>>>>>>>>>>>> START Scheduled Job <<<<<<<<<<<<<<<<<<");

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        Job stockUpdate = (Job) context.getBean("stockUpdate");
        JobExecution execution = jobLauncher.run(stockUpdate, jobParameters);
        BatchStatus status = execution.getStatus();
        logger.info(status.toString());
        logger.info(">>>>>>>>>>>>> FINISHED Scheduled Job <<<<<<<<<<<<<<<<<<");
    }

}
