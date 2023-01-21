package com.hasithat.springbootbatch.config;

import com.hasithat.springbootbatch.entiry.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import com.hasithat.springbootbatch.repo.CustomerRepository;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
public class ApplicationBatchConfig {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    //ItemReader
    @Bean
    public FlatFileItemReader<Customer> itemReader(){
        FlatFileItemReader<Customer> itemReader= new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("C:\\Users\\Lenovo\\Desktop\\MOCK_DATA.csv"));
        //Skip first line of the csv file since they are header names
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(linemapper());
        //System.out.println("reading item **********");
        return itemReader;
    }

    //ItemProcessor
    @Bean
    public ItemProcessor<Customer, Customer>  customerItemProcessor(){
        return new CustomerDataProcessor();
    }


    private LineMapper<Customer> linemapper() {
        DefaultLineMapper<Customer> lineMapper=new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer= new DelimitedLineTokenizer();
        //csv file path
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        //Header column names of csv file
        lineTokenizer.setNames("id","firstName","lastName","email","gender","contactNo","country", "age");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper= new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;


    }

    //ItemWriter
    @Bean
    public RepositoryItemWriter<Customer> itemWriter(){
        RepositoryItemWriter<Customer> itemWriter= new RepositoryItemWriter<>();
        itemWriter.setRepository(customerRepository);
        itemWriter.setMethodName("save");
        //System.out.println("writing item #############");
        return itemWriter;
    }

    //Creating Step object
    /*
    * taskExecutor(taskExecutor()) is for supporting multi threading.
    *
    * chunk:Indicates the number of item to process before transaction committed.
    * In most common implementations chunk oriented processing is used in spring batch
    * One item is read with itemReader and it is send to itemProcessor
    * and this process goes on till the number of items read equals number of
    * commit interval.After that itemWriter process works and transaction is committed.
    *
    * */

    public Step customerStep(){
        System.out.println("********** STEP RUNNING ******************");
        return stepBuilderFactory.get("customerStep").<Customer, Customer>chunk(10)
                .reader(itemReader())
                .processor(customerItemProcessor())
                .writer(itemWriter())
               // .taskExecutor(taskExecutor())
                .faultTolerant()
                .listener(skipListener())
                /*Without creating customer skip policy we can set default
                * skip policy as below also*/
               // .skipLimit(100)
               // .skip(NumberFormatException.class)
                /*Below we are calling to the custom skip policy we created.*/
                .skipPolicy(skipPolicy())
                .build();
    }

    //Creating Job object
    @Bean
    public Job runJob(){
        return jobBuilderFactory.get("customerJob")
                .flow(customerStep())
                .end()
                .build();
    }

    /*This is  for multi threading implementation.
    Here we are creating 10 threads.
     */
    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor taskExecutor=new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }

    //Custom skip policy
    @Bean
    public SkipPolicy skipPolicy(){
        return new CustomSkipPolicy();
    }

    //Skip event listner
    @Bean
    public SkipListener skipListener(){
        return new BatchSkipEventLister();
    }


}
