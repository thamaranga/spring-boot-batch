package com.hasithat.springbootbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootBatchApplication {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	//This Get endpoint is used for calling  data importing batch
	@GetMapping("/import_data")
	public ResponseEntity<?> importData(){
		JobParameters jobParameters= new JobParametersBuilder()
				.addLong("startAt", System.currentTimeMillis()).toJobParameters();
		try {
			//Passing jobParameters object is optional.
			jobLauncher.run(job, jobParameters);
		}catch(Exception ex){
			System.out.println("ex "+ex.getMessage());
		}
		return ResponseEntity.ok("JOB EXECUTED SUCCESSFULLY !!!");
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBatchApplication.class, args);
	}

}
