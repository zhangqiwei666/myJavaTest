package com.zqw.crm.controller;

import com.zqw.crm.common.PageResult;
import com.zqw.crm.common.Result;
import com.zqw.crm.entity.SysUser;
import com.zqw.crm.service.SysUserService;
import com.zqw.crm.vo.AssignRolesReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "02. 系统用户管理接口")
@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Operation(summary = "用户分页列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('sys:user:query')")
    public Result<PageResult<SysUser>> getPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(required = false) String username) {
        return Result.success(sysUserService.getUserPage(pageNum, pageSize, username));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add')")
    public Result<Boolean> createUser(@RequestBody SysUser sysUser) {
        sysUserService.createUser(sysUser);
        return Result.success(true);
    }

    @Operation(summary = "修改用户")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result<Boolean> updateUser(@RequestBody SysUser sysUser) {
        sysUserService.updateUser(sysUser);
        return Result.success(true);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result<Boolean> deleteUser(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.success(true);
    }

    @Operation(summary = "给用户分配角色")
    @PostMapping("/assign-roles")
    @PreAuthorize("hasAuthority('sys:role:assign')")
    public Result<Boolean> assignRoles(@Valid @RequestBody AssignRolesReq req) {
        sysUserService.assignRoles(req);
        return Result.success(true);
    }
}
