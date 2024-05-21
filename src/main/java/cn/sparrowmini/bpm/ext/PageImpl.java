package cn.sparrowmini.bpm.ext;

import java.io.Serializable;
import java.util.List;

public class PageImpl<T> implements Serializable {
    private long pageIndex;
    private long pageSize;
    private long offset;
    private long totalPages;
    private long totalElements;
    private List<T> content;

    public PageImpl() {
    }

    public PageImpl(long totalElements, List<T> content, long pageIndex, long pageSize) {
        this.totalElements = totalElements;
        this.content = content;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public long getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getOffset() {
        return this.pageIndex * this.pageSize;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
