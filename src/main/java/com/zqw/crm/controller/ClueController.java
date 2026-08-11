package com.zqw.crm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqw.crm.common.PageResult;
import com.zqw.crm.common.Result;
import com.zqw.crm.entity.CrmClue;
import com.zqw.crm.security.SecurityUser;
import com.zqw.crm.service.CrmClueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "06. 销售线索接口")
@RestController
@RequestMapping("/api/crm/clue")
@RequiredArgsConstructor
public class ClueController {

    private final CrmClueService clueService;

    public ClueController(CrmClueService clueService) {
        this.clueService = clueService;
    }

    @Operation(summary = "线索分页列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('crm:clue:query')")
    public Result<PageResult<CrmClue>> getPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        var page = clueService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<CrmClue>().orderByDesc(CrmClue::getCreateTime));
        return Result.success(new PageResult<>(page.getRecords(), page.getTotal()));
    }

    @Operation(summary = "新增销售线索")
    @PostMapping
    @PreAuthorize("hasAuthority('crm:clue:query')")
    public Result<Boolean> createClue(@RequestBody CrmClue clue, @AuthenticationPrincipal SecurityUser loginUser) {
        if (loginUser != null && loginUser.getSysUser() != null) {
            clue.setOwnerId(loginUser.getSysUser().getId());
        }
        clueService.save(clue);
        return Result.success(true);
    }

    @Operation(summary = "线索转化成客户档案")
    @PostMapping("/{id}/convert")
    @PreAuthorize("hasAuthority('crm:customer:add')")
    public Result<Boolean> convertToCustomer(@PathVariable Long id, @AuthenticationPrincipal SecurityUser loginUser) {
        Long userId = (loginUser != null && loginUser.getSysUser() != null) ? loginUser.getSysUser().getId() : 1L;
        clueService.convertToCustomer(id, userId);
        return Result.success(true);
    }
}
