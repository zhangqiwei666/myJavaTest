package com.zqw.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zqw.crm.entity.CrmOpportunity;

public interface CrmOpportunityService extends IService<CrmOpportunity> {

    void updateStage(Long opportunityId, String newStage);
}
