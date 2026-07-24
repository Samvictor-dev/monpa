package com.myvamsnet.monpa.service;

import com.myvamsnet.monpa.dto.common.PagedResponse;
import com.myvamsnet.monpa.dto.transaction.AdminTransactionFilter;
import com.myvamsnet.monpa.dto.transaction.TransactionHistoryResponse;
import com.myvamsnet.monpa.mapper.TransactionMapper;
import com.myvamsnet.monpa.model.Transaction;
import com.myvamsnet.monpa.repository.TransactionRepository;
import com.myvamsnet.monpa.specification.AdminTransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminTransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    public PagedResponse<TransactionHistoryResponse> getAllTransactions(

            AdminTransactionFilter filter,

            int page,

            int size

    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(Sort.Direction.DESC, "createdAt")
                );

        Page<Transaction> transactionPage =
                transactionRepository.findAll(
                        AdminTransactionSpecification.build(filter),
                        pageable
                );

        Page<TransactionHistoryResponse> responsePage =
                transactionPage.map(transactionMapper::toHistoryResponse);

        return PagedResponse.<TransactionHistoryResponse>builder()
                .content(responsePage.getContent())
                .page(responsePage.getNumber())
                .size(responsePage.getSize())
                .totalElements(responsePage.getTotalElements())
                .totalPages(responsePage.getTotalPages())
                .first(responsePage.isFirst())
                .last(responsePage.isLast())
                .hasNext(responsePage.hasNext())
                .hasPrevious(responsePage.hasPrevious())
                .build();

    }

}
