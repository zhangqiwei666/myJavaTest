package com.zqw.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zqw.crm.entity.SysPermission;

import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {

    List<SysPermission> getPermissionTree();
}
