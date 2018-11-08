package com.jfilter.filter;

import com.jfilter.model.ModelBase;

import java.util.List;

public class SearchResultData<T extends ModelBase> {

    private Long total;

    private List<T> resultData;

    public SearchResultData(Long total, List<T> resultData) {
        super();
        this.total = total;
        this.resultData = resultData;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getResultData() {
        return resultData;
    }

    public void setResultData(List<T> resultData) {
        this.resultData = resultData;
    }

}
