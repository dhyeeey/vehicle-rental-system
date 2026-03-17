package org.intech.vehiclerental.dto.paginationdto;

import com.blazebit.persistence.PagedList;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResponse<T> {

    private List<T> content;

    // Current page number (0-based if coming from Spring Page)
    // Example: 0 = first page, 1 = second page
    private Integer currentPage;

    // Total number of items available in the database (across all pages)
    // Example: 500 items total
    private Long totalItems;

    // Total number of pages based on totalItems and pageSize
    // Formula: totalPages = ceil(totalItems / pageSize)
    private Integer totalPages;

    // Number of items per page (page size / limit)
    // Example: pageSize = 10 → each page contains 10 items
    private Integer pageSize;

    // Indicates whether the current page is the last page
    // true → no more pages after this
    // false → more pages exist
    private Boolean isLast;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.currentPage = page.getNumber();
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.pageSize = page.getSize();
        this.isLast = page.isLast();
    }

    // Constructor for Blaze PagedList
    public PageResponse(PagedList<T> pagedList) {
        this.content = pagedList;
        this.currentPage = pagedList.getPage();
        this.totalItems = (long) pagedList.getTotalSize();
        this.totalPages = pagedList.getTotalPages();
        this.pageSize = pagedList.getMaxResults();
        this.isLast = pagedList.getTotalPages() == 0
                || pagedList.getPage() == pagedList.getTotalPages() - 1;
    }

}