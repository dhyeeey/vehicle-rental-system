package org.intech.vehiclerental.dto.paginationdto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResponse<T> {

    private List<T> content;
    private Integer currentPage;
    private Long totalItems;
    private Integer totalPages;
    private Integer pageSize;
    private Boolean isLast;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.currentPage = page.getNumber();
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.pageSize = page.getSize();
        this.isLast = page.isLast();
    }
}