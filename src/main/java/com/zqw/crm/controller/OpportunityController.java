package com.zqw.crm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqw.crm.common.PageResult;
import com.zqw.crm.common.Result;
import com.zqw.crm.entity.CrmOpportunity;
import com.zqw.crm.security.SecurityUser;
import com.zqw.crm.service.CrmOpportunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "07. 销售商机接口")
@RestController
@RequestMapping("/api/crm/opportunity")
public class OpportunityController {

    private final CrmOpportunityService opportunityService;

    public OpportunityController(CrmOpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @Operation(summary = "商机分页列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('crm:opportunity:query')")
    public Result<PageResult<CrmOpportunity>> getPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        var page = opportunityService.page(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<CrmOpportunity>().orderByDesc(CrmOpportunity::getCreateTime));
        return Result.success(new PageResult<>(page.getRecords(), page.getTotal()));
    }

    @Operation(summary = "新增销售商机")
    @PostMapping
    @PreAuthorize("hasAuthority('crm:opportunity:query')")
    public Result<Boolean> createOpportunity(@RequestBody CrmOpportunity opportunity,
                                             @AuthenticationPrincipal SecurityUser loginUser) {
        if (loginUser != null && loginUser.getSysUser() != null) {
            opportunity.setOwnerId(loginUser.getSysUser().getId());
        }
        opportunityService.save(opportunity);
        return Result.success(true);
    }

    @Operation(summary = "推进商机阶段")
    @PutMapping("/{id}/stage")
    @PreAuthorize("hasAuthority('crm:opportunity:query')")
    public Result<Boolean> updateStage(@PathVariable Long id, @RequestParam String stage) {
        opportunityService.updateStage(id, stage);
        return Result.success(true);
    }
}
