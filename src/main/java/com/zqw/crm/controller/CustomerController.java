package com.zqw.crm.controller;

import com.zqw.crm.common.PageResult;
import com.zqw.crm.common.Result;
import com.zqw.crm.entity.CrmCustomer;
import com.zqw.crm.security.SecurityUser;
import com.zqw.crm.service.CrmCustomerService;
import com.zqw.crm.vo.CustomerQueryReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "05. 客户管理接口 (CRUD)")
@RestController
@RequestMapping("/api/crm/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CrmCustomerService customerService;

    public CustomerController(CrmCustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "客户分页查询与条件过滤")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('crm:customer:query')")
    public Result<PageResult<CrmCustomer>> getCustomerPage(CustomerQueryReq req) {
        return Result.success(customerService.getCustomerPage(req));
    }

    @Operation(summary = "获取客户详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('crm:customer:query')")
    public Result<CrmCustomer> getById(@PathVariable Long id) {
        return Result.success(customerService.getById(id));
    }

    @Operation(summary = "新增客户档案")
    @PostMapping
    @PreAuthorize("hasAuthority('crm:customer:add')")
    public Result<Boolean> createCustomer(@RequestBody CrmCustomer customer,
                                         @AuthenticationPrincipal SecurityUser loginUser) {
        if (loginUser != null && loginUser.getSysUser() != null) {
            customer.setCreatorId(loginUser.getSysUser().getId());
            if (customer.getOwnerId() == null) {
                customer.setOwnerId(loginUser.getSysUser().getId());
            }
        }
        customerService.save(customer);
        return Result.success(true);
    }

    @Operation(summary = "更新客户档案")
    @PutMapping
    @PreAuthorize("hasAuthority('crm:customer:update')")
    public Result<Boolean> updateCustomer(@RequestBody CrmCustomer customer) {
        customerService.updateById(customer);
        return Result.success(true);
    }

    @Operation(summary = "删除客户档案")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('crm:customer:delete')")
    public Result<Boolean> deleteCustomer(@PathVariable Long id) {
        customerService.removeById(id);
        return Result.success(true);
    }

    @Operation(summary = "分配客户负责人")
    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAuthority('crm:customer:update')")
    public Result<Boolean> assignOwner(@PathVariable Long id, @RequestParam Long ownerId) {
        customerService.assignOwner(id, ownerId);
        return Result.success(true);
    }

    @Operation(summary = "投入客户到公海池")
    @PutMapping("/{id}/transfer-public")
    @PreAuthorize("hasAuthority('crm:customer:update')")
    public Result<Boolean> transferToPublicPool(@PathVariable Long id) {
        customerService.transferToPublicPool(id);
        return Result.success(true);
    }
}
