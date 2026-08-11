package com.zqw.crm.controller;

import com.zqw.crm.common.Result;
import com.zqw.crm.entity.SysPermission;
import com.zqw.crm.service.SysPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "04. 权限/菜单接口")
@RestController
@RequestMapping("/api/system/permission")
public class PermissionController {

    private final SysPermissionService sysPermissionService;

    public PermissionController(SysPermissionService sysPermissionService) {
        this.sysPermissionService = sysPermissionService;
    }

    @Operation(summary = "获取全部权限菜单树/列表")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('sys:role:query')")
    public Result<List<SysPermission>> getTree() {
        return Result.success(sysPermissionService.getPermissionTree());
    }
}
