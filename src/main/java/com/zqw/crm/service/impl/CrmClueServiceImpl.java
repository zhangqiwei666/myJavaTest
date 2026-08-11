package com.zqw.crm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zqw.crm.common.BizException;
import com.zqw.crm.entity.CrmClue;
import com.zqw.crm.entity.CrmCustomer;
import com.zqw.crm.mapper.CrmClueMapper;
import com.zqw.crm.service.CrmClueService;
import com.zqw.crm.service.CrmCustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrmClueServiceImpl extends ServiceImpl<CrmClueMapper, CrmClue> implements CrmClueService {

    private final CrmCustomerService customerService;

    public CrmClueServiceImpl(CrmCustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void convertToCustomer(Long clueId, Long currentUserId) {
        CrmClue clue = this.getById(clueId);
        if (clue == null) {
            throw new BizException("销售线索不存在");
        }
        if ("已转化".equals(clue.getStatus())) {
            throw new BizException("该线索已转化，请勿重复转化");
        }

        CrmCustomer customer = new CrmCustomer();
        customer.setName(clue.getName());
        customer.setPhone(clue.getPhone());
        customer.setCompany(clue.getCompany());
        customer.setLevel("普通客户");
        customer.setStatus("跟进中");
        customer.setOwnerId(clue.getOwnerId() != null ? clue.getOwnerId() : currentUserId);
        customer.setCreatorId(currentUserId);
        customer.setRemark("由销售线索自动转化生成 (线索来源: " + clue.getSource() + ")");
        customerService.save(customer);

        clue.setStatus("已转化");
        this.updateById(clue);
    }
}
