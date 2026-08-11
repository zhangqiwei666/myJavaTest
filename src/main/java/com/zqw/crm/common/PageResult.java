package com.zqw.crm.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private List<T> list;
    private Long total;

    public PageResult() {
    }

    public PageResult(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public List<T> getList() { return list; }
    public void setList(List<T> list) { this.list = list; }

    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
}
