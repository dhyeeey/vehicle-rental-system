package org.intech.vehiclerental.dto.paginationdto;

import com.blazebit.persistence.PagedList;
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