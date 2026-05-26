package com.deliveryapp.backend.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationQuery {

    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String direction = "asc";

}
