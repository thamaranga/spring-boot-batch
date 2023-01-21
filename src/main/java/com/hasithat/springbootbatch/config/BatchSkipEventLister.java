package com.hasithat.springbootbatch.config;

import com.hasithat.springbootbatch.entiry.Customer;
import org.springframework.batch.core.SkipListener;

/*This is a listner class for skip items.
In real time scenario we can insert data into a db table about skipping records
using below three methods.
*/

public class BatchSkipEventLister implements SkipListener<Customer, Number> {
    @Override
    public void onSkipInRead(Throwable throwable) {
        System.out.println("Skipped while reading "+throwable.getMessage());
    }

    @Override
    public void onSkipInWrite(Number number, Throwable throwable) {
        System.out.println("Skipped while writing "+throwable.getMessage());
    }

    @Override
    public void onSkipInProcess(Customer customer, Throwable throwable) {
        System.out.println("Skipped while processing "+throwable.getMessage());
    }
}
