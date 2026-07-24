package com.myvamsnet.monpa.common.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageRequestFactory {

    private PageRequestFactory() {
    }

    public static Pageable defaultPage(
            int page,
            int size
    ) {

        return PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "createdAt"
                )
        );

    }

}
