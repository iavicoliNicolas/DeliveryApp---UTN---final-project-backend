package com.deliveryapp.backend.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationResult<T> {

    private List<T> content;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
}
