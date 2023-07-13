package com.gangbean.stockservice.batch;

import com.gangbean.stockservice.domain.Stock;
import com.gangbean.stockservice.domain.StockRandomPrice;
import com.gangbean.stockservice.domain.TradeReservation;
import com.gangbean.stockservice.repository.StockRepository;
import com.gangbean.stockservice.repository.TradeReservationRepository;
import com.gangbean.stockservice.util.BatchExecutionTime;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

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

    private final TradeReservationRepository tradeReservationRepository;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory, JobLauncher jobLauncher, StockRepository stockRepository, ApplicationContext context, TradeReservationRepository tradeReservationRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.jobLauncher = jobLauncher;
        this.stockRepository = stockRepository;
        this.context = context;
        this.tradeReservationRepository = tradeReservationRepository;
    }

    @Bean
    public Job stockUpdate(@Qualifier("stockUpdateStep1") Step step) {
        return jobBuilderFactory.get("stockUpdate")
                .start(step)
                .build();
    }

    @Bean
    public Step stockUpdateStep1(ItemReader<Stock> reader, ItemProcessor<Stock, Stock> processor, ItemWriter<Stock> writer) {
        return stepBuilderFactory.get("stockUpdateStep1")
                .<Stock, Stock>chunk(10)
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

    @Bean
    public Job executeReservedPayment(@Qualifier("executePayment") Step step) {
        return jobBuilderFactory.get("executeReservedPayment")
                .start(step)
                .build();
    }

    @Bean
    public Step executePayment(ItemReader<TradeReservation> reader
            , ItemProcessor<TradeReservation, TradeReservation> processor
            , ItemWriter<TradeReservation> writer) {
        return stepBuilderFactory.get("executePayment")
                .<TradeReservation, TradeReservation>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public JpaCursorItemReader<TradeReservation> reservationReader() {
        LocalDateTime reservation = BatchExecutionTime.nextExecutionTime("Reservation");
        return new JpaCursorItemReaderBuilder<TradeReservation>()
                .name("reservationReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT r FROM TradeReservation r WHERE r.tradeAt = :tradeAt")
                .parameterValues(Map.of("tradeAt", reservation))
                .build();
    }

    @Bean
    public ItemProcessor<TradeReservation, TradeReservation> executeTrade() {
        return reservation -> {
            reservation.executeTrade();
            return reservation;
        };
    }

    @Bean
    public ItemWriter<TradeReservation> reservationWriter() {
        return tradeReservationRepository::saveAll;
    }

    @Scheduled(initialDelay = 30 * 1000, fixedDelay = 30 * 1000)
    public void runStockUpdateBatch() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        logger.info(">>>>>>>>>>>>> START Scheduled Stock Job <<<<<<<<<<<<<<<<<<");

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        Job stockUpdate = (Job) context.getBean("stockUpdate");
        JobExecution execution = jobLauncher.run(stockUpdate, jobParameters);
        BatchStatus status = execution.getStatus();
        logger.info(status.toString());
        logger.info(">>>>>>>>>>>>> FINISHED Scheduled Stock Job <<<<<<<<<<<<<<<<<<");
    }

    @Scheduled(initialDelay = 30 * 1000, fixedDelay = 30 * 1000)
    public void runExecuteReservedTradeBatch() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        logger.info(">>>>>>>>>>>>> START Scheduled Reservation Job <<<<<<<<<<<<<<<<<<");
        
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        Job stockUpdate = (Job) context.getBean("executeReservedPayment");
        JobExecution execution = jobLauncher.run(stockUpdate, jobParameters);
        BatchStatus status = execution.getStatus();
        logger.info(status.toString());
        
        LocalDateTime nextExecution = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS);
        BatchExecutionTime.write("Reservation", nextExecution);
        logger.info(">>>>>>>>>>>>> FINISHED Scheduled Reservation Job <<<<<<<<<<<<<<<<<<");
    }

}
