package com.zqw.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zqw.crm.entity.CrmClue;

public interface CrmClueService extends IService<CrmClue> {

    void convertToCustomer(Long clueId, Long currentUserId);
}
