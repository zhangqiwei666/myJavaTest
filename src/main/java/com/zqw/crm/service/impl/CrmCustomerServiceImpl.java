package com.zqw.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zqw.crm.common.BizException;
import com.zqw.crm.common.PageResult;
import com.zqw.crm.entity.CrmCustomer;
import com.zqw.crm.mapper.CrmCustomerMapper;
import com.zqw.crm.service.CrmCustomerService;
import com.zqw.crm.vo.CustomerQueryReq;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CrmCustomerServiceImpl extends ServiceImpl<CrmCustomerMapper, CrmCustomer> implements CrmCustomerService {

    @Override
    public PageResult<CrmCustomer> getCustomerPage(CustomerQueryReq req) {
        Page<CrmCustomer> page = new Page<>(req.getPageNum(), req.getPageSize());
        LambdaQueryWrapper<CrmCustomer> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(req.getName())) {
            wrapper.like(CrmCustomer::getName, req.getName()).or().like(CrmCustomer::getCompany, req.getName());
        }
        if (StringUtils.hasText(req.getPhone())) {
            wrapper.eq(CrmCustomer::getPhone, req.getPhone());
        }
        if (StringUtils.hasText(req.getLevel())) {
            wrapper.eq(CrmCustomer::getLevel, req.getLevel());
        }
        if (StringUtils.hasText(req.getStatus())) {
            wrapper.eq(CrmCustomer::getStatus, req.getStatus());
        }

        wrapper.orderByDesc(CrmCustomer::getCreateTime);

        Page<CrmCustomer> result = this.page(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    public void assignOwner(Long customerId, Long ownerId) {
        CrmCustomer customer = this.getById(customerId);
        if (customer == null) {
            throw new BizException("客户档案不存在");
        }
        customer.setOwnerId(ownerId);
        this.updateById(customer);
    }

    @Override
    public void transferToPublicPool(Long customerId) {
        CrmCustomer customer = this.getById(customerId);
        if (customer == null) {
            throw new BizException("客户档案不存在");
        }
        customer.setOwnerId(null); // 负责人为空代表公海池客户
        this.updateById(customer);
    }
}
