package com.vallim.payments.repository;

import com.vallim.payments.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {


}
