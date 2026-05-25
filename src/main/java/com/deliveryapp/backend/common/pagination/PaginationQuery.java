package com.deliveryapp.backend.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationQuery {

    private int page;
    private int size;
    private String sortBy;
    private String direction;

}
