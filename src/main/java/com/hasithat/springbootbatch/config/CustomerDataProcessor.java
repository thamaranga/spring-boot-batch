package com.hasithat.springbootbatch.config;

import com.hasithat.springbootbatch.entiry.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerDataProcessor implements ItemProcessor<Customer, Customer> {

    /*If you have any data to process for customer objects you can do it inside below
    * method*/
    @Override
    public Customer process(Customer customer) throws Exception {
        int age=Integer.parseInt(customer.getAge());
        System.out.println("processing item ##############");
        if(age>35){
            return customer;
        }
        return null;
    }
}
