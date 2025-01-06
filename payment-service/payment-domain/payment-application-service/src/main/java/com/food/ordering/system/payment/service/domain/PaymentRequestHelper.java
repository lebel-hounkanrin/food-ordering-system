package com.food.ordering.system.payment.service.domain;

import com.food.ordering.payment.service.domain.PaymentDomainService;
import com.food.ordering.payment.service.domain.entity.CreditEntry;
import com.food.ordering.payment.service.domain.entity.CreditHistory;
import com.food.ordering.payment.service.domain.entity.Payment;
import com.food.ordering.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.domain.valueObject.CustomerId;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepo;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepo creditEntryRepo;
    private final CreditHistoryRepository creditHistoryRepository;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                PaymentRepository paymentRepository,
                                CreditEntryRepo creditEntryRepo,
                                CreditHistoryRepository creditHistoryRepository) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepo = creditEntryRepo;
        this.creditHistoryRepository = creditHistoryRepository;
    }


    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received payment completed event for order id {}", paymentRequest.getOrderId());
        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistoryList = getCreditHistories(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(
                payment, creditEntry, creditHistoryList, failureMessages
        );
        paymentRepository.save(payment);
        if(failureMessages.isEmpty()) {
            creditEntryRepo.save(creditEntry);
            creditHistoryRepository.save(creditHistoryList.get(creditHistoryList.size() - 1));
        }
        return paymentEvent;
    }

    private List<CreditHistory> getCreditHistories(CustomerId customerId) {
        Optional<List<CreditHistory>> optionalCreditHistories = creditHistoryRepository.findByCustomerId(customerId);
        if (optionalCreditHistories.isEmpty()) {
            log.error("No credit history found for order id {}", customerId.getValue());
            throw new PaymentApplicationServiceException("No credit history found for order id " + customerId.getValue());
        }
        return optionalCreditHistories.get();
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntryOptional = creditEntryRepo.findByCustomerId(customerId);
        if(creditEntryOptional.isEmpty()) {
            log.error("No credit entry found for customer id {}", customerId.getValue());
            throw new PaymentApplicationServiceException("No credit entry found for customer id " + customerId.getValue());
        }
        return creditEntryOptional.get();
    }
}
