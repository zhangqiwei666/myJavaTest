package com.zqw.crm.controller;

import com.zqw.crm.common.Result;
import com.zqw.crm.security.SecurityUser;
import com.zqw.crm.service.SysUserService;
import com.zqw.crm.vo.LoginReq;
import com.zqw.crm.vo.LoginResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "01. 认证接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SysUserService sysUserService;

    public AuthController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginResp> login(@Valid @RequestBody LoginReq loginReq) {
        return Result.success(sysUserService.login(loginReq));
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/info")
    public Result<SecurityUser> getInfo(@AuthenticationPrincipal SecurityUser securityUser) {
        return Result.success(securityUser);
    }

    @Operation(summary = "用户退出登录")
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        return Result.success(true);
    }
}
