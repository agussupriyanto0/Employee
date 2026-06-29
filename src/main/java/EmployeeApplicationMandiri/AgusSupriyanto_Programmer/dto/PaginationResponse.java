package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationResponse <T>{

    private int page;
    private int size;
    private long totalData;
    private int totalPages;
    private List<T> items;
}
