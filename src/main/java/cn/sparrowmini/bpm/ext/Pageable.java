package cn.sparrowmini.bpm.ext;

import java.io.Serializable;

public class Pageable implements Serializable {
    private int pageIndex=0;
    private int pageSize=10;
    private String[] sort;

    public Pageable() {
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String[] getSort() {
        return sort;
    }

    public void setSort(String[] sort) {
        this.sort = sort;
    }

}
