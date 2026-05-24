package com.hardik.problemsservice.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int currentPage) 
{

}