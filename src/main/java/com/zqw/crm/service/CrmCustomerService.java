package com.zqw.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zqw.crm.common.PageResult;
import com.zqw.crm.entity.CrmCustomer;
import com.zqw.crm.vo.CustomerQueryReq;

public interface CrmCustomerService extends IService<CrmCustomer> {

    PageResult<CrmCustomer> getCustomerPage(CustomerQueryReq req);

    void assignOwner(Long customerId, Long ownerId);

    void transferToPublicPool(Long customerId);
}
