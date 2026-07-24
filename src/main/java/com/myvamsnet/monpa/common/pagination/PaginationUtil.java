package com.myvamsnet.monpa.common.pagination;

import com.myvamsnet.monpa.dto.common.PagedResponse;
import org.springframework.data.domain.Page;

public final class PaginationUtil {

    private PaginationUtil() {
    }

    public static <T> PagedResponse<T> from(Page<T> page) {

        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();

    }

}
