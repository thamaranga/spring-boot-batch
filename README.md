This is a sample application which demonstrates how to use spring batch.

Here we are reading customer data from a csv file (C:\Users\Lenovo\Desktop\MOCK_DATA.csv)
and importing them into a table called customer.
(You can find the csv file inside this project folder also.)

In spring batch we are several components like below.

1. ItemReader - read data from a source (csv file)
2. ItemWriter - write data into destination (db table)
3. ItemProcess- logic we need to execute on items
4. Step       - Combination of ItemReader,ItemWriter and ItemProcess  
5. Job        - The task we need to perform (We can create a job using a 
                single or multiple steps.)
6. JobLauncher- This initiates the job
7. fault tolerance can be done through skip policy
8. skip listner is for listning  about failure records 

batch_job_execution , batch_job_instance, batch_step_execution are the
most important tables related to batch job tables.




