package com.zqw.crm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zqw.crm.entity.SysRole;
import com.zqw.crm.mapper.SysRoleMapper;
import com.zqw.crm.service.SysRoleService;
import com.zqw.crm.vo.AssignPermissionsReq;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        return baseMapper.selectPermissionIdsByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(AssignPermissionsReq req) {
        baseMapper.deleteRolePermissions(req.getRoleId());
        if (req.getPermissionIds() != null && !req.getPermissionIds().isEmpty()) {
            for (Long permId : req.getPermissionIds()) {
                baseMapper.insertRolePermission(req.getRoleId(), permId);
            }
        }
    }
}
