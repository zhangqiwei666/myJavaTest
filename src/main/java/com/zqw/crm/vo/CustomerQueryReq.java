package com.zqw.crm.vo;

import lombok.Data;

@Data
public class CustomerQueryReq {

    private String name;
    private String phone;
    private String level;
    private String status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }

    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
