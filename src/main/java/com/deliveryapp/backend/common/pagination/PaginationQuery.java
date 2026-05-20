package com.deliveryapp.backend.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationQuery {

    private int page;
    private int size;

}
