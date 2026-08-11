package com.zqw.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zqw.crm.entity.SysRole;
import com.zqw.crm.vo.AssignPermissionsReq;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    List<Long> getRolePermissionIds(Long roleId);

    void assignPermissions(AssignPermissionsReq req);
}
