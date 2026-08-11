package com.zqw.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zqw.crm.common.PageResult;
import com.zqw.crm.entity.SysUser;
import com.zqw.crm.vo.AssignRolesReq;
import com.zqw.crm.vo.LoginReq;
import com.zqw.crm.vo.LoginResp;

public interface SysUserService extends IService<SysUser> {

    LoginResp login(LoginReq loginReq);

    PageResult<SysUser> getUserPage(Integer pageNum, Integer pageSize, String username);

    void createUser(SysUser sysUser);

    void updateUser(SysUser sysUser);

    void assignRoles(AssignRolesReq req);
}
