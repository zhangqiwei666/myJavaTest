package com.zqw.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zqw.crm.entity.SysPermission;
import com.zqw.crm.mapper.SysPermissionMapper;
import com.zqw.crm.service.SysPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Override
    public List<SysPermission> getPermissionTree() {
        return this.list(new LambdaQueryWrapper<SysPermission>().orderByAsc(SysPermission::getSort));
    }
}
