package com.zqw.crm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zqw.crm.common.BizException;
import com.zqw.crm.entity.CrmOpportunity;
import com.zqw.crm.mapper.CrmOpportunityMapper;
import com.zqw.crm.service.CrmOpportunityService;
import org.springframework.stereotype.Service;

@Service
public class CrmOpportunityServiceImpl extends ServiceImpl<CrmOpportunityMapper, CrmOpportunity> implements CrmOpportunityService {

    @Override
    public void updateStage(Long opportunityId, String newStage) {
        CrmOpportunity opportunity = this.getById(opportunityId);
        if (opportunity == null) {
            throw new BizException("销售商机不存在");
        }
        opportunity.setStage(newStage);
        this.updateById(opportunity);
    }
}
