package com.zqw.crm.controller;

import com.zqw.crm.common.Result;
import com.zqw.crm.entity.SysRole;
import com.zqw.crm.service.SysRoleService;
import com.zqw.crm.vo.AssignPermissionsReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "03. 角色管理接口")
@RestController
@RequestMapping("/api/system/role")
public class RoleController {

    private final SysRoleService sysRoleService;

    public RoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @Operation(summary = "角色列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:query')")
    public Result<List<SysRole>> getList() {
        return Result.success(sysRoleService.list());
    }

    @Operation(summary = "新增角色")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:assign')")
    public Result<Boolean> createRole(@RequestBody SysRole role) {
        sysRoleService.save(role);
        return Result.success(true);
    }

    @Operation(summary = "修改角色")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:role:assign')")
    public Result<Boolean> updateRole(@RequestBody SysRole role) {
        sysRoleService.updateById(role);
        return Result.success(true);
    }

    @Operation(summary = "获取角色拥有的权限ID列表")
    @GetMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('sys:role:query')")
    public Result<List<Long>> getRolePermissions(@PathVariable Long roleId) {
        return Result.success(sysRoleService.getRolePermissionIds(roleId));
    }

    @Operation(summary = "给角色分配菜单与权限")
    @PostMapping("/assign-permissions")
    @PreAuthorize("hasAuthority('sys:role:assign')")
    public Result<Boolean> assignPermissions(@Valid @RequestBody AssignPermissionsReq req) {
        sysRoleService.assignPermissions(req);
        return Result.success(true);
    }
}
