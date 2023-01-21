package com.hasithat.springbootbatch.repo;

import com.hasithat.springbootbatch.entiry.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository  extends CrudRepository<Customer, Integer> {
}
